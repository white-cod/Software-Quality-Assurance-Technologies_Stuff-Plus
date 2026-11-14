package ua.kpi.tzyapz.personal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Юніт-тести правил BL-50 … BL-70 (SRS 1.1): сума «на руки», годинна ставка, понаднормові. */
@DisplayName("PayrollCalculator — нарахування (BL-50 … BL-70)")
class PayrollCalculatorTest {

    private PayrollCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new PayrollCalculator();
    }

    // ---------- BL-50: net = gross × (1 − 0.18 − 0.05), HALF_UP ----------

    // BUG-015: v1 takes the military levy from the amount after PIT (10 000.00 -> 7 790.00).
    @ParameterizedTest(name = "BL-50: net({0}) = {1}")
    @CsvSource({
        "10000.00, 7700.00",
        "12345.67, 9506.17",
        "1.00, 0.77",
        "0.01, 0.01"
    })
    void netDeductsBothRatesFromGross(String gross, String expectedNet) {
        assertEquals(new BigDecimal(expectedNet), calc.net(new BigDecimal(gross)));
    }

    @Test
    @DisplayName("BL-50: gross = 0,00 → 0,00")
    void netOfZeroIsZero() {
        assertEquals(new BigDecimal("0.00"), calc.net(new BigDecimal("0.00")));
    }

    @Test
    @DisplayName("BL-50: від'ємна або відсутня сума — IllegalArgumentException")
    void netRejectsNegativeOrMissingGross() {
        assertAll("net",
            () -> assertThrows(IllegalArgumentException.class, () -> calc.net(new BigDecimal("-0.01"))),
            () -> assertThrows(IllegalArgumentException.class, () -> calc.net(null))
        );
    }

    // ---------- BL-60: hourlyRate = monthly / 168, HALF_UP ----------

    // BUG-016: v1 rounds with RoundingMode.DOWN (25 000 -> 148.80 instead of 148.81).
    @ParameterizedTest(name = "BL-60: hourlyRate({0}) = {1}")
    @CsvSource({
        "10000, 59.52",
        "25000, 148.81",
        "100, 0.60",
        "16800, 100.00",
        "0, 0.00"
    })
    void hourlyRateIsRoundedHalfUp(String monthly, String expectedRate) {
        assertEquals(new BigDecimal(expectedRate), calc.hourlyRate(new BigDecimal(monthly)));
    }

    @Test
    @DisplayName("BL-60: від'ємний або відсутній оклад — IllegalArgumentException")
    void hourlyRateRejectsNegativeOrMissingMonthly() {
        assertAll("hourlyRate",
            () -> assertThrows(IllegalArgumentException.class, () -> calc.hourlyRate(new BigDecimal("-1"))),
            () -> assertThrows(IllegalArgumentException.class, () -> calc.hourlyRate(null))
        );
    }

    // ---------- BL-70: overtimePay = hourlyRate(monthly) × 1.5 × hours, HALF_UP ----------

    // The 25 000 row fails through the BL-60 rounding (BUG-016): 148.81 × 1.5 × 2 = 446.43.
    @ParameterizedTest(name = "BL-70: overtimePay({0}, {1} год) = {2}")
    @CsvSource({
        "16800, 2.5, 375.00",
        "10000, 1, 89.28",
        "10000, 0.5, 44.64",
        "25000, 2, 446.43",
        "16800, 0, 0.00"
    })
    void overtimeUsesRoundedHourlyRate(String monthly, String hours, String expectedPay) {
        assertEquals(new BigDecimal(expectedPay), calc.overtimePay(new BigDecimal(monthly), new BigDecimal(hours)));
    }

    @Test
    @DisplayName("BL-70: від'ємні або відсутні години чи оклад — IllegalArgumentException")
    void overtimeRejectsNegativeOrMissingArguments() {
        BigDecimal monthly = new BigDecimal("16800");
        assertAll("overtimePay",
            () -> assertThrows(IllegalArgumentException.class, () -> calc.overtimePay(monthly, new BigDecimal("-1"))),
            () -> assertThrows(IllegalArgumentException.class, () -> calc.overtimePay(monthly, null)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> calc.overtimePay(new BigDecimal("-1"), BigDecimal.ONE))
        );
    }
}
