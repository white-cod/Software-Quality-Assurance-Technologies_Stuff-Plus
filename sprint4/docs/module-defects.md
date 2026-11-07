# Дефекти модуля «Персонал+» v1 — Спринт 4

> Дзеркало звітів у трекері ([issues з міткою `module`](https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus/issues?q=label%3Amodule)). Еталон — розділ BL SRS 1.1 (`sprint2/SRS-1.1.md`), фактичні значення — прогін Surefire від 01.11.2026 (`docs/surefire-summary.md`).  
> Шаблон — 4.11 спринта 3; severity / priority — за 4.12 / 4.13. Тести, що фіксують дефекти, лишаються «червоними» до v2 (спринт 5: closed / reopened).

| Bug ID | Вимога | Клас.метод | Тест | Severity | Priority | Автор | Рецензент |
| :-: | :-: | :--- | :--- | :-: | :-: | :--- | :--- |
| [BUG-013](#bug-013) | BL-10 | `LeaveCalculator.accrued(int)` | `LeaveCalculatorTest.accrualIsCappedAtYearlyMaximum` | Major | P3 | Шпилька Т. | Любочка В. |
| [BUG-014](#bug-014) | BL-30 | `LeaveCalculator.durationDays(LocalDate, LocalDate)` | `LeaveCalculatorTest.durationCountsWorkingDaysInclusive` | Major | P3 | Мініцька А. | Любочка В. |
| [BUG-015](#bug-015) | BL-50 | `PayrollCalculator.net(BigDecimal)` | `PayrollCalculatorTest.netDeductsBothRatesFromGross` | Major | P3 | Коломоєць О. | Любочка В. |
| [BUG-016](#bug-016) | BL-60 | `PayrollCalculator.hourlyRate(BigDecimal)` | `PayrollCalculatorTest.hourlyRateIsRoundedHalfUp; overtimeUsesRoundedHourlyRate` | Minor | P3 | Коломоєць О. | Любочка В. |
| [BUG-017](#bug-017) | BL-80 | `EmployeeValidator.isValidTaxId(String)` | `EmployeeValidatorTest.rejectsTaxIdThatIsNotTenDigits` | Normal | P3 | Любочка В. | Коломоєць О. |
| [BUG-018](#bug-018) | BL-80 | `EmployeeValidator.isOldEnough(LocalDate, LocalDate)` | `EmployeeValidatorTest.ageOnHireDateCountsBirthday` | Major | P3 | Любочка В. | Коломоєць О. |

**Підсумок:** 6 дефектів із 6 закладених (норматив для 4 осіб — ≥ 4); Major — 4, Normal — 1, Minor — 1; усі P3. Методи без дефектів: `carryOver` (BL-20), `canRequest` (BL-40), `overtimePay` (BL-70 — падає лише через BUG-016), `isValidEmail`, `isHireDateValid`, `validate` (BL-80).

## BUG-013

**[BUG-013] Нараховані дні відпустки не обмежуються річним максимумом 24 при monthsWorked ≥ 13 (BL-10)**

**Bug ID:** BUG-013   **Severity:** Major   **Priority:** P3
**Linked:** requirement BL-10 (SRS 1.1); test `LeaveCalculatorTest.accrualIsCappedAtYearlyMaximum`; class `LeaveCalculator.accrued(int)`

### Description
За BL-10 accrued = min(2 × monthsWorked, 24): за рік нараховується не більше 24 днів (MAX_ACCRUAL_PER_YEAR). У v1 метод повертає 2 × monthsWorked без обмеження, тому працівник із 13 і більше місяцями отримує понад 24 дні (13 → 26, 24 → 48, 60 → 120). Межа: 12 → 24 правильно, 13 → 26 уже неправильно.

* Severity: Major (4.12): ключова функція нарахування відпусток працює неповністю — для всіх, хто працює понад рік, баланс завищено, і працівник може подати заявку на дні, яких йому не належить.
* Priority: P3 (4.13): дефект модуля не блокує виконання жодного іншого тесту чи тест-кейсу — решта набору виконується і дає результат.

### Steps to reproduce
1. Клонувати репозиторій https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus і перейти в каталог `sprint4/` (README, розд. 2: JDK 17, Maven 3.9).
2. Виконати `mvn test -Dtest=LeaveCalculatorTest#accrualIsCappedAtYearlyMaximum`.
3. Або викликати напряму: `new LeaveCalculator().accrued(13)`.

### Actual result
26 (для 24 → 48, для 60 → 120)

### Expected result
24 (BL-10: min(2 × 13, 24))

### Environment
Модуль «Персонал+» v1 (ua.kpi.tzyapz.personal, 1.0.0), каталог sprint4/ репозиторію; JDK 17.0.20 (Temurin), Maven 3.9.11, JUnit Jupiter 5.10.2, Windows 11 Pro 64-bit. Команда: `mvn test -Dtest=LeaveCalculatorTest`.

### Attachments
Фрагмент звіту Surefire (`target/surefire-reports/ua.kpi.tzyapz.personal.LeaveCalculatorTest.txt`):
```
accrualIsCappedAtYearlyMaximum(int, int)[1] <<< FAILURE!
expected: <24> but was: <26>
```

---
**Reporter:** Шпилька Т.   **Date:** 02.11.2026   **Status:** New   **Reviewer:** Любочка В. (03.11.2026)

---

## BUG-014

**[BUG-014] Тривалість відпустки не враховує останній день періоду (пн–пт = 4 замість 5) (BL-30)**

**Bug ID:** BUG-014   **Severity:** Major   **Priority:** P3
**Linked:** requirement BL-30 (SRS 1.1); test `LeaveCalculatorTest.durationCountsWorkingDaysInclusive`; class `LeaveCalculator.durationDays(LocalDate, LocalDate)`

### Description
За BL-30 тривалість — кількість робочих днів у проміжку включно з обома датами. У v1 останній день не рахується: пн 07.09.2026 – пт 11.09.2026 → 4 замість 5; один день 09.09–09.09 → 0 замість 1; пт 11.09 – пн 14.09 → 1 замість 2. Помилка на 1 день виникає щоразу, коли дата завершення — робочий день. Відхилення дат (start > end) і відсутніх дат працює правильно.

* Severity: Major (4.12): тривалість кожної заявки, що закінчується в робочий день, занижено на 1 день — баланс списується неправильно, а одноденну заявку (тривалість 0) неможливо подати через BL-40.
* Priority: P3 (4.13): дефект модуля не блокує виконання жодного іншого тесту чи тест-кейсу — решта набору виконується і дає результат.

### Steps to reproduce
1. Клонувати репозиторій https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus і перейти в каталог `sprint4/` (README, розд. 2: JDK 17, Maven 3.9).
2. Виконати `mvn test -Dtest=LeaveCalculatorTest#durationCountsWorkingDaysInclusive`.
3. Або викликати напряму: `new LeaveCalculator().durationDays(LocalDate.of(2026, 9, 7), LocalDate.of(2026, 9, 11))`.

### Actual result
4 (09.09–09.09 → 0; 11.09–14.09 → 1; 12.09–14.09 → 0; 07.09–18.09 → 9)

### Expected result
5 (BL-30: робочі дні пн–пт включно з обома датами)

### Environment
Модуль «Персонал+» v1 (ua.kpi.tzyapz.personal, 1.0.0), каталог sprint4/ репозиторію; JDK 17.0.20 (Temurin), Maven 3.9.11, JUnit Jupiter 5.10.2, Windows 11 Pro 64-bit. Команда: `mvn test -Dtest=LeaveCalculatorTest`.

### Attachments
Фрагмент звіту Surefire (`target/surefire-reports/ua.kpi.tzyapz.personal.LeaveCalculatorTest.txt`):
```
durationCountsWorkingDaysInclusive(LocalDate, LocalDate, int)[1] <<< FAILURE!
expected: <5> but was: <4>
```

---
**Reporter:** Мініцька А.   **Date:** 02.11.2026   **Status:** New   **Reviewer:** Любочка В. (03.11.2026)

---

## BUG-015

**[BUG-015] Сума «на руки» завищена: військовий збір утримується від суми після ПДФО, а не від нарахованої (BL-50)**

**Bug ID:** BUG-015   **Severity:** Major   **Priority:** P3
**Linked:** requirement BL-50 (SRS 1.1); test `PayrollCalculatorTest.netDeductsBothRatesFromGross`; class `PayrollCalculator.net(BigDecimal)`

### Description
За BL-50 net = gross − gross × 0,18 − gross × 0,05 = gross × 0,77: обидва утримання — від нарахованої суми. У v1 військовий збір 5 % рахується від суми після ПДФО (gross × 0,82 × 0,95 = gross × 0,779), тому net завищено на 0,9 % нарахованої суми: 10 000,00 → 7 790,00 замість 7 700,00. gross = 0,00 і від'ємна сума обробляються правильно.

* Severity: Major (4.12): головний розрахунок модуля зарплати дає неправильну суму виплати для будь-якої ненульової суми — недоплата військового збору 90 грн на кожні 10 000 грн.
* Priority: P3 (4.13): дефект модуля не блокує виконання жодного іншого тесту чи тест-кейсу — решта набору виконується і дає результат.

### Steps to reproduce
1. Клонувати репозиторій https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus і перейти в каталог `sprint4/` (README, розд. 2: JDK 17, Maven 3.9).
2. Виконати `mvn test -Dtest=PayrollCalculatorTest#netDeductsBothRatesFromGross`.
3. Або викликати напряму: `new PayrollCalculator().net(new BigDecimal("10000.00"))`.

### Actual result
7790.00 (12 345,67 → 9617.28; 1,00 → 0.78)

### Expected result
7700.00 (BL-50: 10 000,00 × 0,77; приклад SRS)

### Environment
Модуль «Персонал+» v1 (ua.kpi.tzyapz.personal, 1.0.0), каталог sprint4/ репозиторію; JDK 17.0.20 (Temurin), Maven 3.9.11, JUnit Jupiter 5.10.2, Windows 11 Pro 64-bit. Команда: `mvn test -Dtest=PayrollCalculatorTest`.

### Attachments
Фрагмент звіту Surefire (`target/surefire-reports/ua.kpi.tzyapz.personal.PayrollCalculatorTest.txt`):
```
netDeductsBothRatesFromGross(String, String)[1] <<< FAILURE!
expected: <7700.00> but was: <7790.00>
```

---
**Reporter:** Коломоєць О.   **Date:** 02.11.2026   **Status:** New   **Reviewer:** Любочка В. (03.11.2026)

---

## BUG-016

**[BUG-016] Годинна ставка округлюється вниз (DOWN) замість HALF_UP: 25 000 → 148,80 замість 148,81 (BL-60)**

**Bug ID:** BUG-016   **Severity:** Minor   **Priority:** P3
**Linked:** requirement BL-60 (SRS 1.1); test `PayrollCalculatorTest.hourlyRateIsRoundedHalfUp; overtimeUsesRoundedHourlyRate`; class `PayrollCalculator.hourlyRate(BigDecimal)`

### Description
За BL-60 hourlyRate = monthly / 168 з округленням HALF_UP до копійок. У v1 використано RoundingMode.DOWN, тому ставка занижується на 1 коп., коли третій знак ≥ 5: 25 000 / 168 = 148,8095… → 148,80 замість 148,81; 100 / 168 = 0,5952… → 0,59 замість 0,60. Значення, де округлення вниз і HALF_UP збігаються (10 000 → 59,52), правильні. Через BL-70 (оплата понаднормових від округленої ставки) той самий дефект дає 446,40 замість 446,43 для (25 000; 2 год) — це той самий звіт, а не окремий дефект.

* Severity: Minor (4.12): незначна грошова втрата — до 1 коп. на ставці і до кількох копійок в оплаті понаднормових; функція працює, розрахунок не блокується.
* Priority: P3 (4.13): дефект модуля не блокує виконання жодного іншого тесту чи тест-кейсу — решта набору виконується і дає результат.

### Steps to reproduce
1. Клонувати репозиторій https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus і перейти в каталог `sprint4/` (README, розд. 2: JDK 17, Maven 3.9).
2. Виконати `mvn test -Dtest=PayrollCalculatorTest#hourlyRateIsRoundedHalfUp`.
3. Або викликати напряму: `new PayrollCalculator().hourlyRate(new BigDecimal("25000"))`.

### Actual result
148.80 (100 → 0.59; overtimePay(25000, 2) → 446.40)

### Expected result
148.81 (BL-60: 148,8095… HALF_UP; приклад SRS)

### Environment
Модуль «Персонал+» v1 (ua.kpi.tzyapz.personal, 1.0.0), каталог sprint4/ репозиторію; JDK 17.0.20 (Temurin), Maven 3.9.11, JUnit Jupiter 5.10.2, Windows 11 Pro 64-bit. Команда: `mvn test -Dtest=PayrollCalculatorTest`.

### Attachments
Фрагмент звіту Surefire (`target/surefire-reports/ua.kpi.tzyapz.personal.PayrollCalculatorTest.txt`):
```
hourlyRateIsRoundedHalfUp(String, String)[2] <<< FAILURE!
expected: <148.81> but was: <148.80>
overtimeUsesRoundedHourlyRate(String, String, String)[4] <<< FAILURE!
expected: <446.43> but was: <446.40>
```

---
**Reporter:** Коломоєць О.   **Date:** 02.11.2026   **Status:** New   **Reviewer:** Любочка В. (03.11.2026)

---

## BUG-017

**[BUG-017] РНОКПП із 9 цифр (і коротший) визнається коректним (BL-80)**

**Bug ID:** BUG-017   **Severity:** Normal   **Priority:** P3
**Linked:** requirement BL-80 (SRS 1.1); test `EmployeeValidatorTest.rejectsTaxIdThatIsNotTenDigits`; class `EmployeeValidator.isValidTaxId(String)`

### Description
За BL-80 isValidTaxId — рівно 10 цифр без інших символів. У v1 перевірено length ≤ 10, тому код із 1 … 9 цифр приймається («123456789» → true). Коди з 11 цифр, з літерою, з пробілом, порожні та null відхиляються правильно.

* Severity: Normal (4.12): у картку працівника можна зберегти неповний податковий номер; функцією користуватися можна, але некоректні дані потраплять у звітність і виявляться пізніше.
* Priority: P3 (4.13): дефект модуля не блокує виконання жодного іншого тесту чи тест-кейсу — решта набору виконується і дає результат.

### Steps to reproduce
1. Клонувати репозиторій https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus і перейти в каталог `sprint4/` (README, розд. 2: JDK 17, Maven 3.9).
2. Виконати `mvn test -Dtest=EmployeeValidatorTest#rejectsTaxIdThatIsNotTenDigits`.
3. Або викликати напряму: `new EmployeeValidator().isValidTaxId("123456789")`.

### Actual result
true

### Expected result
false (BL-80: рівно 10 цифр)

### Environment
Модуль «Персонал+» v1 (ua.kpi.tzyapz.personal, 1.0.0), каталог sprint4/ репозиторію; JDK 17.0.20 (Temurin), Maven 3.9.11, JUnit Jupiter 5.10.2, Windows 11 Pro 64-bit. Команда: `mvn test -Dtest=EmployeeValidatorTest`.

### Attachments
Фрагмент звіту Surefire (`target/surefire-reports/ua.kpi.tzyapz.personal.EmployeeValidatorTest.txt`):
```
rejectsTaxIdThatIsNotTenDigits(String)[3] <<< FAILURE!
123456789 ==> expected: <false> but was: <true>
```

---
**Reporter:** Любочка В.   **Date:** 02.11.2026   **Status:** New   **Reviewer:** Коломоєць О. (03.11.2026)

---

## BUG-018

**[BUG-018] Вік на дату найму рахується за різницею років без урахування дня народження: 15-річного визнано 16-річним (BL-80)**

**Bug ID:** BUG-018   **Severity:** Major   **Priority:** P3
**Linked:** requirement BL-80 (SRS 1.1); test `EmployeeValidatorTest.ageOnHireDateCountsBirthday`; class `EmployeeValidator.isOldEnough(LocalDate, LocalDate)`

### Description
За BL-80 isOldEnough — на дату найму виповнилося не менше 16 повних років, день народження враховується. У v1 віднімаються лише роки: народження 02.11.2010, найм 01.11.2026 (15 років 364 дні) → true; народження 31.12.2010, найм 01.01.2026 (15 років) → true. Рівно 16 років (01.11.2010 → 01.11.2026) і випадки, де різниця років < 16, обробляються правильно.

* Severity: Major (4.12): перевірка, що захищає від найму неповнолітніх до 16 років, пропускає частину таких випадків — порушення трудового законодавства з юридичними наслідками для клієнта.
* Priority: P3 (4.13): дефект модуля не блокує виконання жодного іншого тесту чи тест-кейсу — решта набору виконується і дає результат.

### Steps to reproduce
1. Клонувати репозиторій https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus і перейти в каталог `sprint4/` (README, розд. 2: JDK 17, Maven 3.9).
2. Виконати `mvn test -Dtest=EmployeeValidatorTest#ageOnHireDateCountsBirthday`.
3. Або викликати напряму: `new EmployeeValidator().isOldEnough(LocalDate.of(2010, 11, 2), LocalDate.of(2026, 11, 1))`.

### Actual result
true (також для 31.12.2010 / 01.01.2026)

### Expected result
false (BL-80: 16 повних років ще не виповнилося)

### Environment
Модуль «Персонал+» v1 (ua.kpi.tzyapz.personal, 1.0.0), каталог sprint4/ репозиторію; JDK 17.0.20 (Temurin), Maven 3.9.11, JUnit Jupiter 5.10.2, Windows 11 Pro 64-bit. Команда: `mvn test -Dtest=EmployeeValidatorTest`.

### Attachments
Фрагмент звіту Surefire (`target/surefire-reports/ua.kpi.tzyapz.personal.EmployeeValidatorTest.txt`):
```
ageOnHireDateCountsBirthday(LocalDate, LocalDate, boolean)[3] <<< FAILURE!
expected: <false> but was: <true>
```

---
**Reporter:** Любочка В.   **Date:** 02.11.2026   **Status:** New   **Reviewer:** Коломоєць О. (03.11.2026)

---
