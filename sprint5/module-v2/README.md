# Модуль «Персонал+» v2 + регресійний набір спринта 4

* `src/main/java/ua/kpi/tzyapz/personal/` — модуль **v2 (2.0.0)** від розробників, без змін.
* `src/test/java/ua/kpi/tzyapz/personal/` — тести спринта 4 **без змін** (копія `sprint4/src/test/`, порівняно `diff -r`).

```bash
cd sprint5/module-v2
mvn clean test                         # Tests run: 79, Failures: 6, Errors: 0   (юніт)
mvn clean test -DexcludedGroups=none   # Tests run: 86, Failures: 6, Errors: 0   (+ 7 UI на demo)
```

Червоні: `LeaveCalculatorTest.canRequestOnlyWithinBalance` [1] — **BUG-019, регресія v2**; `PayrollCalculatorTest.hourlyRateIsRoundedHalfUp` [2], [3] і `overtimeUsesRoundedHourlyRate` [4] — BUG-016 (Reopened); `EmployeeValidatorTest.ageOnHireDateCountsBirthday` [3], [4] — BUG-018 (Reopened). Порівняння з v1 — `../regression-log.xlsx`, `../surefire-v1/`, `../surefire-v2/`.
