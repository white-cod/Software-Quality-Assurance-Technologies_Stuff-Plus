# Матриця покриття — Спринт 4 «Персонал+»

> **Автор:** Шпилька Т. (ТА) · **Еталон очікуваних значень:** розділ BL SRS 1.1 ([`sprint2/SRS-1.1.md`](../../sprint2/SRS-1.1.md), розд. 5) — не поведінка коду  
> **Результат:** прогін 01.11.2026 ([`surefire-summary.md`](surefire-summary.md)) на модулі v1 · ✅ passed · ❌ failed (Bug ID)  
> **Code review:** за чек-листом 5.3 методички; позначка «✅ 5.3» — рецензент схвалив pull request класу, зауваження — у коментарях PR

## 1. Юніт-тести: BL-вимога → правило → тести

| BL | Правило (SRS 1.1) | Межі та класи значень | Тест-метод | Тип | Автор | Рецензент | Результат |
| :-: | :--- | :--- | :--- | :-: | :--- | :--- | :-: |
| BL-10 | accrued = 2 × monthsWorked | 0, 1, 5, 11, 12 (12 — остання точка до межі 24) | `LeaveCalculatorTest.accruesTwoDaysPerFullMonth` | параметр. | Шпилька Т. | Любочка В. ✅ 5.3 | ✅ |
| BL-10 | річний максимум 24 | 13 (межа + 1), 24, 60 | `LeaveCalculatorTest.accrualIsCappedAtYearlyMaximum` | параметр. | Шпилька Т. | Любочка В. ✅ 5.3 | ❌ BUG-013 |
| BL-10 | monthsWorked < 0 → виняток | −1 | `LeaveCalculatorTest.negativeMonthsWorkedThrows` | assertThrows | Шпилька Т. | Любочка В. ✅ 5.3 | ✅ |
| BL-20 | carryOver = min(unused, 5) | 0, 4, 5, 6, 9 (межа 5 з обох боків) | `LeaveCalculatorTest.carryOverIsCappedAtFiveDays` | параметр. | Шпилька Т. | Любочка В. ✅ 5.3 | ✅ |
| BL-20 | unused < 0 → виняток | −1 | `LeaveCalculatorTest.negativeUnusedDaysThrows` | assertThrows | Шпилька Т. | Любочка В. ✅ 5.3 | ✅ |
| BL-30 | робочі дні включно з обома датами | пн–пт (5), один день (1), пт–пн (2), сб–пн (1), два тижні (10) | `LeaveCalculatorTest.durationCountsWorkingDaysInclusive` | параметр. | Мініцька А. | Любочка В. ✅ 5.3 | ❌ BUG-014 |
| BL-30 | суботи й неділі не рахуються | сб–нд (0) | `LeaveCalculatorTest.weekendOnlyHasNoWorkingDays` | @Test | Мініцька А. | Любочка В. ✅ 5.3 | ✅ |
| BL-30 | start > end або дата відсутня → виняток | пт–пн навпаки; null у start; null в end | `LeaveCalculatorTest.invalidPeriodThrows` | assertAll + assertThrows | Мініцька А. | Любочка В. ✅ 5.3 | ✅ |
| BL-40 | true лише якщо 1 ≤ duration ≤ balance | (5,5), (5,4), (5,1), (5,6), (5,0), (0,1) — обидві межі | `LeaveCalculatorTest.canRequestOnlyWithinBalance` | параметр. | Мініцька А. | Любочка В. ✅ 5.3 | ✅ |
| BL-50 | net = gross × 0,77, HALF_UP | 10 000,00; 12 345,67 (приклади SRS); 1,00; 0,01 (округлення) | `PayrollCalculatorTest.netDeductsBothRatesFromGross` | параметр. | Коломоєць О. | Любочка В. ✅ 5.3 | ❌ BUG-015 |
| BL-50 | gross = 0,00 → 0,00 | 0,00 (нижня межа) | `PayrollCalculatorTest.netOfZeroIsZero` | @Test | Коломоєць О. | Любочка В. ✅ 5.3 | ✅ |
| BL-50 | gross < 0 або відсутня → виняток | −0,01 (межа − 1 коп.); null | `PayrollCalculatorTest.netRejectsNegativeOrMissingGross` | assertAll + assertThrows | Коломоєць О. | Любочка В. ✅ 5.3 | ✅ |
| BL-60 | hourlyRate = monthly / 168, HALF_UP | 10 000; 25 000 (приклади SRS); 100 (…,5952 — перевірка HALF_UP); 16 800; 0 | `PayrollCalculatorTest.hourlyRateIsRoundedHalfUp` | параметр. | Коломоєць О. | Любочка В. ✅ 5.3 | ❌ BUG-016 |
| BL-60 | monthly < 0 або відсутній → виняток | −1; null | `PayrollCalculatorTest.hourlyRateRejectsNegativeOrMissingMonthly` | assertAll + assertThrows | Коломоєць О. | Любочка В. ✅ 5.3 | ✅ |
| BL-70 | округлена ставка × 1,5 × hours, HALF_UP | (16 800; 2,5) — приклад SRS; (10 000; 1); (10 000; 0,5) дробові; (25 000; 2); hours = 0 | `PayrollCalculatorTest.overtimeUsesRoundedHourlyRate` | параметр. | Коломоєць О. | Любочка В. ✅ 5.3 | ❌ BUG-016 |
| BL-70 | hours / monthly < 0 або відсутні → виняток | hours −1; hours null; monthly −1 | `PayrollCalculatorTest.overtimeRejectsNegativeOrMissingArguments` | assertAll + assertThrows | Коломоєць О. | Любочка В. ✅ 5.3 | ✅ |
| BL-80 | email: локальна@домен.зона | крапка, `_`, `+`, `%`, дефіс, кілька рівнів, зона 2 літери | `EmployeeValidatorTest.acceptsWellFormedEmail` | параметр. | Любочка В. | Коломоєць О. ✅ 5.3 | ✅ |
| BL-80 | email некоректний | зона 1 літера (межа − 1), зона з цифр, без домену, без локальної частини, пробіл, два `@`, порожній, null | `EmployeeValidatorTest.rejectsMalformedEmail` | параметр. | Любочка В. | Коломоєць О. ✅ 5.3 | ✅ |
| BL-80 | РНОКПП — рівно 10 цифр | 1234567890 | `EmployeeValidatorTest.acceptsTenDigitTaxId` | @Test | Любочка В. | Коломоєць О. ✅ 5.3 | ✅ |
| BL-80 | РНОКПП не 10 цифр → false | 9 цифр (межа − 1), 11 (межа + 1), літера, пробіл, порожній, null | `EmployeeValidatorTest.rejectsTaxIdThatIsNotTenDigits` | параметр. | Любочка В. | Коломоєць О. ✅ 5.3 | ❌ BUG-017 |
| BL-80 | дата найму не пізніша за поточну | сьогодні (межа), вчора, завтра (межа + 1) | `EmployeeValidatorTest.hireDateMustNotBeInFuture` | assertAll | Любочка В. | Коломоєць О. ✅ 5.3 | ✅ |
| BL-80 | ≥ 16 повних років на дату найму | рівно 16; 16 р. + 1 дн.; 16 р. − 1 дн.; 15 р. при різниці років 16; 15 р. | `EmployeeValidatorTest.ageOnHireDateCountsBirthday` | параметр. | Любочка В. | Коломоєць О. ✅ 5.3 | ❌ BUG-018 |
| BL-80 | validate: коректні дані → [] | усі поля коректні | `EmployeeValidatorTest.validDataHasNoErrors` | @Test | Любочка В. | Коломоєць О. ✅ 5.3 | ✅ |
| BL-80 | validate: коди EMAIL, TAX_ID, HIRE_DATE, AGE у порядку | усі поля некоректні | `EmployeeValidatorTest.allInvalidFieldsAreReported` | assertIterableEquals | Любочка В. | Коломоєць О. ✅ 5.3 | ✅ |
| BL-80 | відсутня дата → виняток | null у isHireDateValid, isOldEnough, validate | `EmployeeValidatorTest.missingDateThrows` | assertAll + assertThrows | Любочка В. | Коломоєць О. ✅ 5.3 | ✅ |

**Покриття методів модуля:** 12 з 12 публічних нетривіальних методів (`LeaveCalculator` — 4, `PayrollCalculator` — 3, `EmployeeValidator` — 5). Тривіальних методів (get/set) у модулі немає; публічні константи окремо не тестуються — їх значення перевіряють тести правил.

## 2. Норматив (команда з 4 осіб)

| Показник | Норматив | Факт |
| :--- | :-: | :-: |
| Юніт-тестів JUnit 5 (параметризований метод = 1 тест) | ≥ 16 | **25** |
| Юніт-тестів на кожного (за комітами) | ≥ 3 | Шпилька — 5 · Мініцька — 4 · Коломоєць — 7 · Любочка — 9 |
| UI-сценаріїв WebDriver | ≥ 6 | **7** |
| Дефектів модуля (звіт + тест на кожен) | ≥ 4 з 6 | **6** |
| Сценаріїв Selenium IDE | 1–2 | 2 (`selenium-ide/personal.side`) |

## 3. UI-тести: тест-кейс спринта 3 → тест-метод

| TC | Вимога | Тест-метод | Автор | Рецензент | Результат |
| :-: | :-: | :--- | :--- | :--- | :-: |
| TC-001 | F-10 | `ui.LoginTest.adminLoginOpensDashboard` | Мініцька А. | Любочка В. ✅ 5.3 | ✅ |
| TC-002 | F-20, GUI-20 | `ui.LoginTest.wrongPasswordShowsInvalidCredentials` | Мініцька А. | Любочка В. ✅ 5.3 | ✅ |
| TC-004 | GUI-10 | `ui.LoginTest.loginPageHasRequiredElements` | Мініцька А. | Любочка В. ✅ 5.3 | ✅ |
| TC-006 | GUI-30 | `ui.NavigationTest.sideMenuSearchAndUserMenu` | Мініцька А. | Любочка В. ✅ 5.3 | ✅ |
| TC-007 | S-20 | `ui.NavigationTest.protectedPageAfterLogoutRedirectsToLogin` | Мініцька А. | Любочка В. ✅ 5.3 | ✅ |
| TC-019 | GUI-60 | `ui.EmployeeTest.employeeListCounterRowActionsAndSorting` | Мініцька А. | Любочка В. ✅ 5.3 | ✅ |
| TC-022 | GUI-40 | `ui.EmployeeTest.addEmployeeRequiresFirstAndLastName` | Мініцька А. | Любочка В. ✅ 5.3 | ✅ |

Page Object: `LoginPage`, `DashboardPage`, `EmployeeListPage`, `AddEmployeePage`; спільний життєвий цикл — `UiTestBase`. Кроки, локатори й очікування — [`ui-scenarios.md`](ui-scenarios.md).
