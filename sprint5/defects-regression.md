# Нові дефекти регресії — Спринт 5

> Формат спринта 3 (шаблон 4.11). Регресійний дефект — тест, що проходив на v1 і падає на v2 (перехід Passed → Failed). Трекер — issue з міткою `module` і `sprint:5`. Нових дефектів demo під час регресії не виявлено.

## BUG-019

**[BUG-019] Регресія v2: заявку на відпустку на весь залишок балансу відхилено — canRequest(5, 5) = false (BL-40)**

**Bug ID:** BUG-019   **Severity:** Major   **Priority:** P3
**Linked:** requirement BL-40 (SRS 1.1); test `LeaveCalculatorTest#canRequestOnlyWithinBalance` (набір [1]: 5, 5, true); class `LeaveCalculator.canRequest(int, int)`; **регресія v2**

### Description
За BL-40 canRequest(balance, duration) = true лише якщо duration ≥ 1 і duration ≤ balance; приклад SRS: (5, 5) → true. У модулі v2 метод переписано (`remaining = balance − duration; return duration >= 1 && remaining > 0`), і заявка, що дорівнює залишку, тепер відхиляється. На v1 той самий тест проходив, тобто v2 зламала правило, у якому дефекту не було. Решта наборів (5, 4), (5, 1), (5, 6), (5, 0), (0, 1) — правильні: помилка лише на межі duration = balance.

* Severity: Major (4.12): ключова функція відпусток працює неповністю — працівник не може використати останні дні балансу (наприклад, 5 днів при балансі 5), а BL-40 визначає, чи можна взагалі подати заявку.
* Priority: P3 (4.13): дефект модуля не блокує виконання інших тестів чи тест-кейсів.

### Steps to reproduce
1. Клонувати репозиторій https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus; у каталозі `sprint4/` замінити `src/main/java/ua/kpi/tzyapz/personal/` на модуль v2 (тести не змінювати).
2. Виконати `mvn test -Dtest=LeaveCalculatorTest#canRequestOnlyWithinBalance`.
3. Або викликати напряму: `new LeaveCalculator().canRequest(5, 5)`.

### Actual result
false (на v2, 13.11.2026)

### Expected result
true (BL-40: 1 ≤ 5 ≤ 5; приклад SRS «(5, 5) → true»)

### Result of the same test on v1
passed — 6 / 6 наборів (13.11.2026 і 01.11.2026, модуль v1 1.0.0)

### Environment
Модуль «Персонал+» v2 (ua.kpi.tzyapz.personal, 2.0.0) з тестами спринта 4 без змін; JDK 17.0.20 (Temurin), Maven 3.9.11, JUnit Jupiter 5.10.2, Windows 11 Pro 64-bit. Команда: `mvn -B clean test -DexcludedGroups=none` (13.11.2026, 11:55–11:56).

### Attachments
Фрагмент звіту Surefire v2 (`sprint5/surefire-v2/ua.kpi.tzyapz.personal.LeaveCalculatorTest.txt`):
```
canRequestOnlyWithinBalance(int, int, boolean)[1] <<< FAILURE!
expected: <true> but was: <false>
```

---
**Reporter:** Шпилька Т.   **Date:** 13.11.2026   **Status:** New   **Reviewer:** Любочка В. (__.11.2026)
