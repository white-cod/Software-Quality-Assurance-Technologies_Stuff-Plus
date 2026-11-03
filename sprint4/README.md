# Спринт 4 — Автоматизація: репозиторій автотестів «Персонал+» (Досьє якості v4)

> **Команда:** QualityPulse · **Продукт:** П3 «Персонал+» (HRM) · **Склад:** 4 особи → норматив «4»  
> **Інкремент:** юніт-тести JUnit 5 модуля бізнес-логіки v1 + WebDriver-тести demo OrangeHRM + протокол запуску Surefire + звіти про дефекти модуля  
> **Трекер:** [issues з міткою `module`](https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus/issues?q=label%3Amodule) (BUG-013 … BUG-018) · дефекти demo спринта 3 — мітка `sprint:3`

## 1. Ролі спринта 4 (ротація)

| Учасник | GitHub | Роль | Відповідальність | Юніт-тести (автор) |
| :--- | :--- | :-: | :--- | :--- |
| **Коломоєць Ольга** | [`@foxic-1234`](https://github.com/foxic-1234) | **КТ** | Декомпозиція, план покриття, контроль нормативу «≥ 3 на особу», README, протокол Surefire, Demo (`mvn clean test`) | `PayrollCalculatorTest` — BL-50 … BL-70 (7) |
| **Шпилька Тимофій** | [`@white-cod`](https://github.com/white-cod) | **ТА** | Матриця «BL → правило → тести» з межами, вибір UI-сценаріїв спринта 3, кроки й локатори | `LeaveCalculatorTest` — BL-10, BL-20 (5) |
| **Мініцька Анастасія** | [`@anastminic-ipt28-ops`](https://github.com/anastminic-ipt28-ops) | **ІТ** | Каркас проєкту і модуль v1, збірка на всіх машинах, Selenium IDE (запис і експорт), WebDriver-тести, читання Surefire | `LeaveCalculatorTest` — BL-30, BL-40 (4) |
| **Любочка Вікторія** | [`@twixt-bit`](https://github.com/twixt-bit) | **СМ** | Таблиця задач і standup, code review кожного класу за чек-листом 5.3, перевірка звітів про дефекти, бліц, ретро | `EmployeeValidatorTest` — BL-80 (9) |

Code review: класи КТ, ТА, ІТ рецензує Любочка В. (СМ); клас СМ — Коломоєць О. (КТ). Звіти про дефекти: автор звіту — автор тесту, що знайшов дефект; рецензент — Любочка В., для її власних звітів — Коломоєць О.

## 2. Вимоги до робочого місця

| Що | Версія | Як перевірити |
| :--- | :--- | :--- |
| JDK (Temurin) | 17 | `java -version` → `openjdk version "17.…"` |
| Apache Maven | 3.9 | `mvn -v` → рядок `Java version: 17.…` (якщо інша — задайте `JAVA_HOME`, див. розд. 7) |
| Google Chrome | актуальна | драйвер не встановлюється: його завантажує Selenium Manager під час першого UI-запуску |
| Git | будь-яка | `git --version` |

Інтернет потрібен для першої збірки (Maven завантажує залежності у `~/.m2`) і для UI-тестів (публічний demo).

## 3. Як зібрати і запустити

Усі команди — з каталогу `sprint4/` (тут лежить `pom.xml`):

```bash
git clone https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus.git
cd Software-Quality-Assurance-Technologies_Stuff-Plus/sprint4

mvn clean test                                   # юніт-тести (UI виключено тегом "ui"), ~5 с
mvn clean test -DexcludedGroups=none             # юніт- + UI-тести (Chrome headless), ~1 хв
mvn test -DexcludedGroups=none -Dgroups=ui       # лише UI-тести
mvn test -DexcludedGroups=none -Dgroups=ui -Dheadless=false   # UI-тести з видимим вікном браузера (Demo)
mvn test -Dtest=LeaveCalculatorTest              # один клас
mvn test -Dtest=LeaveCalculatorTest#durationCountsWorkingDaysInclusive   # один метод
```

**Очікуваний результат на модулі v1:**

| Команда | Підсумок | Пояснення |
| :--- | :--- | :--- |
| `mvn clean test` | `Tests run: 79, Failures: 17, Errors: 0, Skipped: 0` · `BUILD FAILURE` | 17 «червоних» запусків у 7 тест-методах — **6 закладених дефектів модуля** (BUG-013 … BUG-018); усі інші проходять |
| `mvn clean test -DexcludedGroups=none` | `Tests run: 86, Failures: 17, Errors: 0, Skipped: 0` | + 7 UI-тестів, усі passed |

`BUILD FAILURE` — очікуваний стан: тести, що фіксують дефекти, не вимикаються і не «підганяються» (методичка 4.9); у спринті 5 на v2 вони покажуть closed / reopened. Errors має бути 0: Error — це тест, що не зміг виконатися, а не дефект.

**Звіти:** `target/surefire-reports/` (`*.txt` — підсумок класу, `TEST-*.xml` — усі запуски з `@DisplayName`). Протокол прогону команди — [`docs/surefire-summary.md`](docs/surefire-summary.md).

## 4. Структура

```
sprint4/
├── README.md                       # цей файл
├── .gitignore                      # target/, .idea/, *.iml, out/, .DS_Store
├── pom.xml                         # JDK 17, JUnit 5.10.2, Selenium 4.21.0, Surefire 3.2.5; тег ui виключено за замовчуванням
├── docs/
│   ├── coverage-matrix.md          # BL-вимога → правило → межі → тест → автор → рецензент → результат
│   ├── ui-scenarios.md             # TC спринта 3 → кроки, локатори, очікування, тест-метод
│   ├── surefire-summary.md         # протокол запуску: дата, команда, Tests run, failed → Bug ID
│   ├── surefire-reports/           # текстові звіти Surefire цього прогону
│   ├── code-review.md              # code review за чек-листом 5.3: зауваження R-1 … R-5 і виправлення
│   └── module-defects.md           # дзеркало issues BUG-013 … BUG-018
├── selenium-ide/
│   ├── personal.side               # проєкт Selenium IDE: TC-002, TC-006
│   └── export/                     # експорт Java JUnit (чернетки, не збираються)
└── src/
    ├── main/java/ua/kpi/tzyapz/personal/     # модуль v1 з пакета — БЕЗ ЗМІН
    │   ├── LeaveCalculator.java  PayrollCalculator.java  EmployeeValidator.java
    └── test/java/ua/kpi/tzyapz/personal/
        ├── LeaveCalculatorTest.java   PayrollCalculatorTest.java   EmployeeValidatorTest.java
        ├── ExampleTest.java           # зразок із пакета (не входить до нормативу)
        └── ui/
            ├── UiTestBase.java        # браузер на кожен тест, headless, quit() в @AfterEach
            ├── LoginPage.java  DashboardPage.java  EmployeeListPage.java  AddEmployeePage.java   # Page Object
            └── LoginTest.java  NavigationTest.java  EmployeeTest.java
```

Модуль підключено за **варіантом А** README стартового проєкту: модуль v1 став робочим проєктом команди. У спринті 5 замінюється лише `src/main/java/` на v2, тести лишаються на місці.

## 5. Розділ досьє «Автоматизація»

**Норматив (план / факт):** юніт-тестів ≥ 16 / **25** · на особу ≥ 3 / **5, 4, 7, 9** · UI-сценаріїв ≥ 6 / **7** · дефектів модуля ≥ 4 / **6 з 6** · Selenium IDE 1–2 / **2**.

**Дефекти модуля v1** (повний текст — [`docs/module-defects.md`](docs/module-defects.md)):

| Bug ID | Вимога | Клас.метод | Тест | Severity |
| :-: | :-: | :--- | :--- | :-: |
| BUG-013 | BL-10 | `LeaveCalculator.accrued` — немає максимуму 24 | `accrualIsCappedAtYearlyMaximum` | Major |
| BUG-014 | BL-30 | `LeaveCalculator.durationDays` — останній день не рахується | `durationCountsWorkingDaysInclusive` | Major |
| BUG-015 | BL-50 | `PayrollCalculator.net` — збір від суми після ПДФО | `netDeductsBothRatesFromGross` | Major |
| BUG-016 | BL-60 | `PayrollCalculator.hourlyRate` — округлення DOWN замість HALF_UP | `hourlyRateIsRoundedHalfUp` (+ BL-70) | Minor |
| BUG-017 | BL-80 | `EmployeeValidator.isValidTaxId` — приймає < 10 цифр | `rejectsTaxIdThatIsNotTenDigits` | Normal |
| BUG-018 | BL-80 | `EmployeeValidator.isOldEnough` — не враховує день народження | `ageOnHireDateCountsBirthday` | Major |

Усі — **P3**: дефект модуля не блокує виконання інших тестів (4.13).

**Висновок про стан модуля v1:** модуль **не готовий до випуску**. Із 12 нетривіальних методів 6 порушують вимоги BL: неправильні суми виплати (BL-50) і ставки (BL-60, через неї — BL-70), завищені нарахування відпусток (BL-10), занижена тривалість заявок (BL-30) і дві прогалини валідації даних працівника (BL-80). Правильно працюють `carryOver`, `canRequest`, `isValidEmail`, `isHireDateValid`, `validate` і всі перевірки некоректних аргументів (винятки). Набір із 25 юніт-тестів запускається за секунди і стане регресійним для v2.

**UI:** 7 ключових сценаріїв спринта 3 (вхід, неправильний пароль, сторінка входу, меню, вихід і захищена адреса, список PIM, валідація Add Employee) проходять стабільно (два прогони поспіль — 7 / 7). Сценарії обрано так, щоб не створювати даних на спільному demo — див. [`docs/ui-scenarios.md`](docs/ui-scenarios.md).

**Матриця покриття:** [`docs/coverage-matrix.md`](docs/coverage-matrix.md) · **Code review:** [`docs/code-review.md`](docs/code-review.md) · **Таблиця задач:** [`task-board.md`](task-board.md) · **Ретро:** [`retro.md`](retro.md)

## 6. Етика demo

UI-тести працюють лише з опублікованим спільним обліковим записом Admin / admin123, нічого не зберігають і не видаляють, не запускаються в циклі. Набір запускався вручну кілька разів за спринт.

## 7. Типові проблеми

* **`release version 17 not supported` / `mvn -v` показує інший JDK** — задайте `JAVA_HOME` на JDK 17: Windows PowerShell — `$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17…-hotspot"`; macOS — `export JAVA_HOME=$(/usr/libexec/java_home -v 17)`.
* **UI-тести не запускаються (`Tests run: 79`)** — тег `ui` виключено за замовчуванням: додайте `-DexcludedGroups=none`.
* **`SessionNotCreatedException`** — Selenium Manager не отримав драйвер: перевірте інтернет і Chrome; кеш — `~/.cache/selenium`.
* **`TimeoutException` в UI-тесті** — demo повільний або тимчасово недоступний: відкрийте https://opensource-demo.orangehrmlive.com у браузері, повторіть запуск з `-Dheadless=false`.
* **Попередження `Unable to find CDP implementation matching …`** — на результат не впливає: тести не використовують DevTools Protocol.
* **Кирилиця у консолі Windows — `????`** — `chcp 65001` перед запуском або запуск з IntelliJ IDEA.
