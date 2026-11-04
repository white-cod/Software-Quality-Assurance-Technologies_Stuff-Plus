package ua.kpi.tzyapz.personal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Юніт-тести правил BL-10 … BL-40 (SRS 1.1): нарахування, перенесення, тривалість, можливість заявки. */
@DisplayName("LeaveCalculator — відпустки (BL-10 … BL-40)")
class LeaveCalculatorTest {

    private LeaveCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new LeaveCalculator();
    }

    // ---------- BL-10: accrued = min(2 × monthsWorked, 24) ----------

    @ParameterizedTest(name = "BL-10: accrued({0}) = {1}")
    @CsvSource({"0, 0", "1, 2", "5, 10", "11, 22", "12, 24"})
    void accruesTwoDaysPerFullMonth(int monthsWorked, int expectedDays) {
        assertEquals(expectedDays, calc.accrued(monthsWorked));
    }

    // BUG-013: v1 does not cap the result at MAX_ACCRUAL_PER_YEAR (13 -> 26 instead of 24).
    @ParameterizedTest(name = "BL-10: річний максимум — accrued({0}) = 24")
    @CsvSource({"13, 24", "24, 24", "60, 24"})
    void accrualIsCappedAtYearlyMaximum(int monthsWorked, int expectedDays) {
        assertEquals(expectedDays, calc.accrued(monthsWorked));
    }

    @Test
    @DisplayName("BL-10: від'ємна кількість місяців — IllegalArgumentException")
    void negativeMonthsWorkedThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.accrued(-1));
    }

    // ---------- BL-20: carryOver = min(unused, 5) ----------

    @ParameterizedTest(name = "BL-20: carryOver({0}) = {1}")
    @CsvSource({"0, 0", "4, 4", "5, 5", "6, 5", "9, 5"})
    void carryOverIsCappedAtFiveDays(int unused, int expectedDays) {
        assertEquals(expectedDays, calc.carryOver(unused));
    }

    @Test
    @DisplayName("BL-20: від'ємний залишок — IllegalArgumentException")
    void negativeUnusedDaysThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.carryOver(-1));
    }
}
