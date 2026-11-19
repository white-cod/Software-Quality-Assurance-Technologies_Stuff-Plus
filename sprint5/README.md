# Спринт 5 — Регресія і звіт про якість (Досьє якості v5)

> **Команда:** QualityPulse (4 особи) · **Продукт:** «Персонал+» — модуль бізнес-логіки **v2** і demo OrangeHRM OS 5.9 · **Еталон:** SRS 1.1 (39 вимог)  
> **Питання замовника:** чи можна випускати версію 2? · **Відповідь:** **no-go** — [`quality-report.pdf`](quality-report.pdf)  
> **Трекер:** [усі дефекти](https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus/issues?q=label%3Asprint%3A3%2Cmodule%2Csprint%3A5) · [модуль і регресія](https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus/issues?q=label%3Amodule)

## 1. Ролі (спринт 5 — вільний вибір за статутом)

| Учасник | GitHub | Роль | Артефакти ролі |
| :--- | :--- | :-: | :--- |
| Любочка Вікторія | [`@twixt-bit`](https://github.com/twixt-bit) | **КТ** | план регресії й критерії go / no-go, затвердження статусів, звіт (титул, резюме, розд. 1–5, 9), презентація |
| Коломоєць Ольга | [`@foxic-1234`](https://github.com/foxic-1234) | **ТА** | матриця простежуваності, непокриті вимоги, метрики (розд. 8) |
| Шпилька Тимофій | [`@white-cod`](https://github.com/white-cod) | **ІТ** | прогін ручних кейсів і автонабору на v2, журнал регресії, Surefire v1 / v2, статуси в трекері, BUG-019, розд. 6–7 |
| Мініцька Анастасія | [`@anastminic-ipt28-ops`](https://github.com/anastminic-ipt28-ops) | **СМ** | рецензія звіту, таблиця задач, репетиція захисту, ретро семестру |

## 2. Результат регресії

| | Значення |
| :--- | :--- |
| Кейсів у регресії | **82** = 50 ручних + 25 JUnit + 7 WebDriver (100 % спринтів 3–4) |
| Результат v2 | **65 passed · 17 failed · 0 blocked · 0 skipped**; pass rate **79,3 %**; виконано 100 % |
| Перехід v1 → v2 | Passed → Passed 61 · Failed → Passed 4 · Failed → Failed 16 · **Passed → Failed 1** |
| Surefire | v1: `Tests run: 79, Failures: 17` (юніт) · v2: `Tests run: 86, Failures: 6, Errors: 0` |
| Дефекти модуля v1 | BUG-013, 014, 015, 017 — **Closed (verified)** · BUG-016, 018 — **Reopened** |
| Регресія v2 | **BUG-019** (BL-40): `canRequest(5, 5) = false` — заявку на весь залишок відхилено; на v1 тест проходив |
| Дефекти demo | 12 / 12 відтворюються — Open (постачальник виправлень не заявляв) |
| Покриття вимог | **92,3 %** (36 / 39), High — **100 %** (22 / 22); не покрито P-20, U-20, D-20 з причинами |
| Щільність дефектів на 10 вимог | F 0,7 · GUI 6,0 · **BL 11,3** · NFR 4,3 · разом 4,9 |
| Частка автоматизації | **39,0 %** (32 / 82) |

**Рішення no-go:** порушено безумовний К4 (регресійний BUG-019 відкритий) і умовні К3 (79,3 % < 90 %) та К5 (3 відкриті Major у BL). Критерії затверджено 09.11.2026 — до прогонів 12–13.11.

## 3. Артефакти (розд. 7 методички)

| Файл | Зміст |
| :--- | :--- |
| [`regression-plan.md`](regression-plan.md) | склад набору, розподіл виконавців, критерії К1–К6 з обґрунтуванням порогів |
| [`regression-log.xlsx`](regression-log.xlsx) | «Регресія» (82 рядки: v1 / v2, дата, виконавець, дефект), «Підсумок», «Дефекти», «Виконання», «Environment» |
| [`traceability-matrix.xlsx`](traceability-matrix.xlsx) | «Матриця» (39 вимог × кейси × автотести × результат v2 × дефекти), «Непокриті», «За розділами» |
| [`surefire-v1/`](surefire-v1/), [`surefire-v2/`](surefire-v2/) | звіти Surefire обох версій |
| [`module-v2/`](module-v2/) | модуль v2 + тести спринта 4 без змін — живий запуск на захисті |
| [`defects-regression.md`](defects-regression.md) | регресійний дефект BUG-019 у форматі спринта 3 |
| [`quality-report.docx`](quality-report.docx) / [`.pdf`](quality-report.pdf) | «Звіт про якість продукту»: титул, резюме, розділи 1–9, матриця — додаток А |
| [`presentation.pdf`](presentation.pdf) | 10 слайдів захисту |
| [`review-checklist.md`](review-checklist.md) | рецензія СМ: повнота, DoD, зауваження R-1 … R-8 (усі виправлено) |
| [`retro-semester.md`](retro-semester.md) | ретроспектива семестру |
| [`tasks.md`](tasks.md) | таблиця розподілу задач |

## 4. Як відтворити автоматизовану частину

[`module-v2/`](module-v2/) — модуль v2 з тестами спринта 4 без змін (варіант п. 3.2 методички):

```bash
cd sprint5/module-v2
mvn clean test                         # Tests run: 79, Failures: 6, Errors: 0
mvn clean test -DexcludedGroups=none   # Tests run: 86, Failures: 6, Errors: 0
```

Падають рівно `canRequestOnlyWithinBalance` (BUG-019), `hourlyRateIsRoundedHalfUp`, `overtimeUsesRoundedHourlyRate` (BUG-016), `ageOnHireDateCountsBirthday` (BUG-018). Модуль v1 з тими самими тестами — `sprint4/`.
