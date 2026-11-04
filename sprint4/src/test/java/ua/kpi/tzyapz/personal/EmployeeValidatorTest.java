package ua.kpi.tzyapz.personal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Юніт-тести правила BL-80 (SRS 1.1): email, РНОКПП, дата найму, вік на дату найму, повна перевірка. */
@DisplayName("EmployeeValidator — дані працівника (BL-80)")
class EmployeeValidatorTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 11, 1);

    private EmployeeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EmployeeValidator();
    }

    // ---------- email: local@domain.zone, zone of at least 2 Latin letters ----------

    @ParameterizedTest(name = "BL-80: email «{0}» коректний")
    @ValueSource(strings = {
        "ivan.petrenko@personal.plus",
        "qp_test+1@mail.example.com.ua",
        "o.kolomoiets%hr@my-company.ua",
        "qp.kand@example.ua"
    })
    void acceptsWellFormedEmail(String email) {
        assertTrue(validator.isValidEmail(email), email);
    }

    @ParameterizedTest(name = "BL-80: email «{0}» некоректний")
    @NullAndEmptySource
    @ValueSource(strings = {
        "qp.kand@example.c",
        "qp.kand@example.12",
        "qp.kand@",
        "@example.ua",
        "qp kand@example.ua",
        "qp.kand@@example.ua"
    })
    void rejectsMalformedEmail(String email) {
        assertFalse(validator.isValidEmail(email), String.valueOf(email));
    }

    // ---------- tax id: exactly 10 digits ----------

    @Test
    @DisplayName("BL-80: РНОКПП із 10 цифр коректний")
    void acceptsTenDigitTaxId() {
        assertTrue(validator.isValidTaxId("1234567890"));
    }

    // BUG-017: v1 checks length <= 10, so a 9-digit code is accepted.
    @ParameterizedTest(name = "BL-80: РНОКПП «{0}» некоректний")
    @NullAndEmptySource
    @ValueSource(strings = {"123456789", "12345678901", "12345A7890", "123456789 "})
    void rejectsTaxIdThatIsNotTenDigits(String taxId) {
        assertFalse(validator.isValidTaxId(taxId), String.valueOf(taxId));
    }

    // ---------- hire date: not later than today ----------

    @Test
    @DisplayName("BL-80: дата найму — сьогодні або раніше коректна, завтра — ні")
    void hireDateMustNotBeInFuture() {
        assertAll("isHireDateValid",
            () -> assertTrue(validator.isHireDateValid(TODAY, TODAY)),
            () -> assertTrue(validator.isHireDateValid(TODAY.minusDays(1), TODAY)),
            () -> assertFalse(validator.isHireDateValid(TODAY.plusDays(1), TODAY))
        );
    }

    // ---------- age on hire date: at least 16 full years, birthday counts ----------

    // BUG-018: v1 compares years only and ignores the day and month of birth.
    @ParameterizedTest(name = "BL-80: народження {0}, найм {1} → 16 повних років: {2}")
    @CsvSource({
        "2010-11-01, 2026-11-01, true",
        "2010-10-31, 2026-11-01, true",
        "2010-11-02, 2026-11-01, false",
        "2010-12-31, 2026-01-01, false",
        "2011-01-01, 2026-11-01, false"
    })
    void ageOnHireDateCountsBirthday(LocalDate birthDate, LocalDate hireDate, boolean expected) {
        assertEquals(expected, validator.isOldEnough(birthDate, hireDate));
    }

    // ---------- validate: list of error codes ----------

    @Test
    @DisplayName("BL-80: коректні дані — порожній список помилок")
    void validDataHasNoErrors() {
        List<String> errors = validator.validate("qp.ivan@example.ua", "1234567890",
                LocalDate.of(1995, 5, 15), LocalDate.of(2026, 10, 1), TODAY);
        assertTrue(errors.isEmpty(), errors.toString());
    }

    @Test
    @DisplayName("BL-80: усі поля некоректні — коди EMAIL, TAX_ID, HIRE_DATE, AGE")
    void allInvalidFieldsAreReported() {
        List<String> errors = validator.validate("qp.kand@example.c", "12345A7890",
                LocalDate.of(2015, 1, 10), LocalDate.of(2026, 12, 1), TODAY);
        assertIterableEquals(List.of("EMAIL", "TAX_ID", "HIRE_DATE", "AGE"), errors);
    }

    @Test
    @DisplayName("BL-80: відсутня дата — IllegalArgumentException")
    void missingDateThrows() {
        assertAll("missing dates",
            () -> assertThrows(IllegalArgumentException.class, () -> validator.isHireDateValid(null, TODAY)),
            () -> assertThrows(IllegalArgumentException.class, () -> validator.isOldEnough(null, TODAY)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> validator.validate("qp.ivan@example.ua", "1234567890", LocalDate.of(1995, 5, 15), null, TODAY))
        );
    }
}
