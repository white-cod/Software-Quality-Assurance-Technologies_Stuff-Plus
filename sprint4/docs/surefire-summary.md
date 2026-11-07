# Протокол запуску — Спринт 4 «Персонал+» (модуль v1 + OrangeHRM demo)

> **Дата і час:** 01.11.2026, 17:08–17:09 · **Команда:** `mvn -B clean test -DexcludedGroups=none` (з каталогу `sprint4/`)  
> **Середовище:** JDK 17.0.20 (Temurin), Maven 3.9.11, Surefire 3.2.5, JUnit Jupiter 5.10.2, Selenium 4.21.0 (драйвер — Selenium Manager), Google Chrome 156.0.8078.74 headless, Windows 11 Pro 64-bit  
> **Об'єкт:** модуль `ua.kpi.tzyapz.personal` v1 (1.0.0) — без змін; UI — https://opensource-demo.orangehrmlive.com (OrangeHRM OS 5.9)  
> **Повні звіти:** `target/surefire-reports/` після запуску; текстові підсумки цього прогону — [`surefire-reports/`](surefire-reports/)

## 1. Підсумок Maven

```
[ERROR] Tests run: 86, Failures: 17, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
```

`BUILD FAILURE` очікуваний: «червоні» тести фіксують закладені дефекти модуля v1 і лишаються в наборі до v2 (методичка 4.9). Errors = 0 — жоден тест не впав через помилку самого тесту; Skipped = 0 — вимкнених тестів немає. UI-набір пройшов повністю (7 / 7) і повторно — 7 / 7 (стабільність). Попередження Selenium «Unable to find CDP implementation matching 156» на результат не впливає: CDP (DevTools Protocol) тести не використовують.

## 2. По тест-класах

«Тестів» — тест-методів (параметризований метод рахується як один, як у нормативі); «Запусків» — рядків звіту Surefire.

| Тест-клас | Рівень | Тестів | Запусків | Failures | Errors | Skipped | Час, с |
| :--- | :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| `EmployeeValidatorTest` | юніт | 9 | 28 | 3 | 0 | 0 | 0.19 |
| `ExampleTest` | юніт | 2 | 5 | 0 | 0 | 0 | 0.02 |
| `LeaveCalculatorTest` | юніт | 9 | 28 | 8 | 0 | 0 | 0.05 |
| `PayrollCalculatorTest` | юніт | 7 | 18 | 6 | 0 | 0 | 0.04 |
| `EmployeeTest` | UI | 2 | 2 | 0 | 0 | 0 | 14.74 |
| `LoginTest` | UI | 3 | 3 | 0 | 0 | 0 | 12.34 |
| `NavigationTest` | UI | 2 | 2 | 0 | 0 | 0 | 20.27 |

`ExampleTest` — зразок із пакета модуля (не входить до нормативу команди).

## 3. Failed і відповідні звіти про дефекти

| Тест (набір даних) | Повідомлення | Звіт |
| :--- | :--- | :-: |
| `EmployeeValidatorTest.ageOnHireDateCountsBirthday` [3] | expected: <false> but was: <true> | BUG-018 |
| `EmployeeValidatorTest.ageOnHireDateCountsBirthday` [4] | expected: <false> but was: <true> | BUG-018 |
| `EmployeeValidatorTest.rejectsTaxIdThatIsNotTenDigits` [3] | 123456789 → expected: <false> but was: <true> | BUG-017 |
| `LeaveCalculatorTest.accrualIsCappedAtYearlyMaximum` [1] | expected: <24> but was: <26> | BUG-013 |
| `LeaveCalculatorTest.accrualIsCappedAtYearlyMaximum` [2] | expected: <24> but was: <48> | BUG-013 |
| `LeaveCalculatorTest.accrualIsCappedAtYearlyMaximum` [3] | expected: <24> but was: <120> | BUG-013 |
| `LeaveCalculatorTest.durationCountsWorkingDaysInclusive` [1] | expected: <5> but was: <4> | BUG-014 |
| `LeaveCalculatorTest.durationCountsWorkingDaysInclusive` [2] | expected: <1> but was: <0> | BUG-014 |
| `LeaveCalculatorTest.durationCountsWorkingDaysInclusive` [3] | expected: <2> but was: <1> | BUG-014 |
| `LeaveCalculatorTest.durationCountsWorkingDaysInclusive` [4] | expected: <1> but was: <0> | BUG-014 |
| `LeaveCalculatorTest.durationCountsWorkingDaysInclusive` [5] | expected: <10> but was: <9> | BUG-014 |
| `PayrollCalculatorTest.netDeductsBothRatesFromGross` [1] | expected: <7700.00> but was: <7790.00> | BUG-015 |
| `PayrollCalculatorTest.netDeductsBothRatesFromGross` [2] | expected: <9506.17> but was: <9617.28> | BUG-015 |
| `PayrollCalculatorTest.netDeductsBothRatesFromGross` [3] | expected: <0.77> but was: <0.78> | BUG-015 |
| `PayrollCalculatorTest.overtimeUsesRoundedHourlyRate` [4] | expected: <446.43> but was: <446.40> | BUG-016 |
| `PayrollCalculatorTest.hourlyRateIsRoundedHalfUp` [2] | expected: <148.81> but was: <148.80> | BUG-016 |
| `PayrollCalculatorTest.hourlyRateIsRoundedHalfUp` [3] | expected: <0.60> but was: <0.59> | BUG-016 |

Усього 17 failed-запусків у 7 тест-методах → **6 звітів** (BUG-013 … BUG-018, мітка `module`). Кожен failed має звіт; один дефект — один звіт, навіть якщо його фіксують кілька запусків або методів (BUG-016: `hourlyRateIsRoundedHalfUp` і `overtimeUsesRoundedHourlyRate`).

## 4. Як відтворити

```bash
cd sprint4
mvn clean test                       # лише юніт-тести (UI виключено тегом ui)
mvn clean test -DexcludedGroups=none # юніт- + UI-тести (потрібні Chrome та інтернет)
```
