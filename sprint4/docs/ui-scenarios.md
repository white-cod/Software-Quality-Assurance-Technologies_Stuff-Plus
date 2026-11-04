# UI-сценарії для автоматизації — Спринт 4 «Персонал+»

> **Автор переліку:** Шпилька Т. (ТА) · **Реалізація WebDriver:** Мініцька А. (ІТ) · **Рецензент:** Любочка В. (СМ)  
> **Demo:** OrangeHRM OS 5.9, `https://opensource-demo.orangehrmlive.com` · обліковий запис Admin / admin123 (спільний, опублікований на сторінці входу)  
> **Запуск:** `mvn test -DexcludedGroups=none -Dgroups=ui` · з видимим браузером — додати `-Dheadless=false`

## 1. Як обирали сценарії

Джерело — тест-кейси спринта 3 (`sprint3/test-cases.xlsx`), які в Run 1 мали статус **passed**: автотест регресії має бути зеленим на поточній версії й «почервоніти», коли поведінка зміниться. Критерії відбору:

1. **Ключові для продукту й найчастіше повторювані** — вхід, навігація, вихід, головний список PIM, форма з валідацією (вимоги F-10, F-20, GUI-10, GUI-30, S-20, GUI-60, GUI-40).
2. **Не створюють і не змінюють спільних даних demo** (етика §1.3 каталогу): жоден сценарій не зберігає записів, тож повторний запуск безпечний і не потребує очищення.
3. **Не потребують SETUP-1.** Кейси з власними користувачами й працівниками (TC-009 … TC-017, відпустки, табелі) на спільному demo нестабільні: дані скидаються, а повторне створення записів у кожному прогоні суперечить етиці. Для них потрібен локальний резерв (Docker-образ OrangeHRM) — це план спринта 5.

Failed-кейси спринта 3 (BUG-001 … BUG-012) в UI-набір не входять: DoD вимагає, щоб `mvn test -DexcludedGroups=none` проходив повністю, а «червоні» тести допускаються лише для дефектів модуля.

## 2. Перелік сценаріїв

| TC (спринт 3) | Вимога | Тест-метод | Кроки (дія → локатор) | Очікування перед перевіркою | Перевірка |
| :-: | :-: | :--- | :--- | :--- | :--- |
| TC-001 | F-10 | `LoginTest.adminLoginOpensDashboard` | `get(/auth/login)` → `name=username` «Admin» → `name=password` «admin123» → клік `button[type='submit']` → меню користувача → Logout | `urlContains("/dashboard/index")`, видимість `h6.oxd-topbar-header-breadcrumb-module`; після Logout — `urlContains("/auth/login")` | **м'яко** (`assertAll`): адреса, заголовок «Dashboard», пункти меню Admin, PIM, Leave, Time, Recruitment, My Info, Dashboard; **жорстко**: адреса входу після Logout |
| TC-002 | F-20, GUI-20 | `LoginTest.wrongPasswordShowsInvalidCredentials` | Admin / WrongPass1 → Login → повторно ввести Admin → admin123 → Login | видимість `.oxd-alert-content-text`; після входу — Dashboard | **м'яко**: текст «Invalid credentials» і адреса `/auth/login`; **жорстко**: повідомлення лишається після редагування Username, зникає після входу |
| TC-004 | GUI-10 | `LoginTest.loginPageHasRequiredElements` | `get(/auth/login)` → ввести пароль | видимість `name=username`; `attributeToBe(password, value, …)` | **м'яко**: логотип `.orangehrm-login-branding img`, заголовок «Login», панель «Username : Admin / Password : admin123», кнопка Login, «Forgot your password?», заповнювачі, `type=password` |
| TC-006 | GUI-30 | `NavigationTest.sideMenuSearchAndUserMenu` | вхід → `.oxd-main-menu-search input` «Lea» → стерти → клік `.oxd-userdropdown-tab` → Logout | `numberOfElementsToBe(.oxd-main-menu-item--name, 1 / 12)`; видимість `a.oxd-userdropdown-link` | **жорстко**: 12 пунктів у порядку SRS; лише Leave; знову 12; **м'яко**: About, Support, Change Password, Logout + модуль «Dashboard» |
| TC-007 | S-20 | `NavigationTest.protectedPageAfterLogoutRedirectsToLogin` | вхід → Logout → `get(/pim/viewEmployeeList)` | `urlContains("/auth/login")`, видимість `name=username` | **м'яко**: адреса входу; списку працівників немає |
| TC-019 | GUI-60 | `EmployeeTest.employeeListCounterRowActionsAndSorting` | вхід → меню PIM → іконка сортування Last Name → Ascending → Descending | видимість «(N) Records Found»; лямбда: перші 5 прізвищ упорядковані | **м'яко**: N > 0, прапорець `.oxd-checkbox-input`, `i.bi-trash`, `i.bi-pencil-fill`; сортування в обидва боки |
| TC-022 | GUI-40 | `EmployeeTest.addEmployeeRequiresFirstAndLastName` | вхід → PIM → вкладка Add Employee → Save без імені | `urlContains("/pim/addEmployee")`, видимість `name=firstName` | **жорстко**: зірочка біля «Employee Full Name» (`::after` мітки); **м'яко**: «Required» під First Name і Last Name, адреса не змінилась |

**Разом:** 7 сценаріїв (норматив для 4 осіб — ≥ 6), 4 тест-класи у `src/test/java/ua/kpi/tzyapz/personal/ui/`.

## 3. Локатори і чому саме такі

OrangeHRM не має атрибутів `data-test`, тому порядок вибору — `name` полів форм → стабільні класи бібліотеки компонентів `oxd-*` і `orangehrm-*` → відносний XPath за текстом там, де класи однакові для багатьох елементів. Абсолютних XPath немає. Кожен локатор перевірено в DevTools на demo 01.11.2026 (`Ctrl+F` у Elements → «1 of 1» або очікувана кількість).

| Елемент | Локатор | Примітка |
| :--- | :--- | :--- |
| Поля входу | `By.name("username")`, `By.name("password")` | найстійкіші: `name` не залежить від верстки |
| Кнопка Login, Save | `button[type='submit']` | на кожній формі одна кнопка submit |
| Помилка входу | `.oxd-alert-content-text` | |
| Заголовок модуля | `h6.oxd-topbar-header-breadcrumb-module` | |
| Пункти меню | `.oxd-main-menu-item--name` | список; для PIM — XPath за текстом |
| Меню користувача / Logout | `.oxd-userdropdown-tab` / `a.oxd-userdropdown-link[href$='/auth/logout']` | Logout — за адресою посилання, а не за текстом |
| Лічильник PIM | `//span[contains(normalize-space(.), 'Records Found')]` | число змінюється, тому `contains` |
| Заголовок Last Name | `//div[contains(@class,'oxd-table-th')][text()[normalize-space()='Last Name']]` | у комірці заголовка є приховане меню «Ascending / Descending», тому збіг — за власним текстовим вузлом |
| Помилка поля | `//input[@name='firstName']/ancestor::div[contains(@class,'oxd-input-group')]//span[contains(@class,'oxd-input-field-error-message')]` | відносно поля, а не за позицією |

## 4. Стабільність

* Тайм-аут явних очікувань — **20 с** (каталог, 5.8: demo повільний під навантаженням). `Thread.sleep` і `implicitlyWait` не використовуються.
* Поле Vue не отримує подію `input` від `clear()`, тому старе значення видаляється з клавіатури (`Ctrl+A`, `Backspace`) — інакше в моделі форми лишається попередній пароль.
* Сортування перевіряється умовою-лямбдою на сам стан (методичка 4.15): таблиця перемальовується після запиту, і читати її одразу після кліку не можна. Регістр імен на спільному demo змішаний, тому порівняння — без урахування регістру, як сортує сам demo.
* Кожен тест — новий браузер в інкогніто (`@BeforeEach`), власний вхід, `quit()` у `@AfterEach`; порядок виконання не важливий.

## 5. Selenium IDE → WebDriver

Проєкт [`../selenium-ide/personal.side`](../selenium-ide/personal.side) (Selenium IDE 4, десктопний застосунок): тести «TC-002 Invalid credentials» (18 команд) і «TC-006 Menu search» (15 команд) з `wait for element visible / not present`, `assert text`, `assert element present`, `verify element present / not present`; обидва пройшли у застосунку. Експорт Java JUnit — [`../selenium-ide/export/`](../selenium-ide/export/), лише як чернетка: JUnit 4 + Hamcrest, `new ChromeDriver()` без headless, застарілий `WebDriverWait(driver, 20)`.

Чому експорт не можна просто запустити: команда IDE `type` **замінює** вміст поля, а експортований `sendKeys` **дописує** до нього. На кроці повторного входу TC-002 експорт ввів би `AdminAdmin` / `WrongPass1admin123` і впав би. В адаптованому `LoginTest.wrongPasswordShowsInvalidCredentials` поле спочатку очищується (`LoginPage.replaceText`), імпорти й анотації замінено на JUnit 5, додано `@Tag("ui")`, headless і `Duration`.
