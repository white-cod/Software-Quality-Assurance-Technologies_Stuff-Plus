package ua.kpi.tzyapz.personal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
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

    // ---------- BL-30: working days between dates, both ends inclusive ----------

    // BUG-014: v1 loops with d.isBefore(end), so the last day is never counted.
    // Calendar: 2026-09-07 is Monday, 2026-09-12/13 are Saturday/Sunday.
    @ParameterizedTest(name = "BL-30: {0} – {1} → {2} роб. дн.")
    @CsvSource({
        "2026-09-07, 2026-09-11, 5",
        "2026-09-09, 2026-09-09, 1",
        "2026-09-11, 2026-09-14, 2",
        "2026-09-12, 2026-09-14, 1",
        "2026-09-07, 2026-09-18, 10"
    })
    void durationCountsWorkingDaysInclusive(LocalDate start, LocalDate end, int expectedDays) {
        assertEquals(expectedDays, calc.durationDays(start, end));
    }

    @Test
    @DisplayName("BL-30: лише субота й неділя — 0 робочих днів")
    void weekendOnlyHasNoWorkingDays() {
        assertEquals(0, calc.durationDays(LocalDate.of(2026, 9, 12), LocalDate.of(2026, 9, 13)));
    }

    @Test
    @DisplayName("BL-30: початок пізніше за кінець або відсутня дата — IllegalArgumentException")
    void invalidPeriodThrows() {
        LocalDate monday = LocalDate.of(2026, 9, 7);
        LocalDate friday = LocalDate.of(2026, 9, 11);
        assertAll("durationDays",
            () -> assertThrows(IllegalArgumentException.class, () -> calc.durationDays(friday, monday)),
            () -> assertThrows(IllegalArgumentException.class, () -> calc.durationDays(null, friday)),
            () -> assertThrows(IllegalArgumentException.class, () -> calc.durationDays(monday, null))
        );
    }

    // ---------- BL-40: canRequest = duration >= 1 && duration <= balance ----------

    @ParameterizedTest(name = "BL-40: canRequest(balance={0}, duration={1}) = {2}")
    @CsvSource({
        "5, 5, true",
        "5, 4, true",
        "5, 1, true",
        "5, 6, false",
        "5, 0, false",
        "0, 1, false"
    })
    void canRequestOnlyWithinBalance(int balance, int duration, boolean expected) {
        assertEquals(expected, calc.canRequest(balance, duration));
    }
}
