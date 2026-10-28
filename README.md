# Software-Quality-Assurance-Technologies_Stuff-Plus

> **Дисципліна:** «Технології забезпечення якості програмних засобів» (ТЗЯПЗ)  
> **Цільовий продукт:** «Персонал+» — хмарна система управління персоналом
> **Команда:** `QualityPulse` (4 особи)

## 1. Про продукт

**«Персонал+»** — хмарна HRM-система від львівської продуктової компанії для організацій середнього бізнесу (100–1000 працівників). Вебзастосунок побудовано на базі REST API (PHP / Symfony), клієнтського інтерфейсу на Vue.js та реляційної СУБД MySQL. Ключові модулі системи охоплюють кадровий облік (PIM), управління відпустками (Leave), облік робочого часу (Time), рекрутинг (Recruitment) та самообслуговування співробітників (ESS).

У межах курсу команда **QualityPulse** виконує роль новоствореного відділу забезпечення якості (QA), який супроводжує продукт протягом 5 спринтів: від моделювання бізнес-процесів і формування вимог до аудиту специфікації (SRS), проєктування тест-кейсів, розробки юніт- та UI-автотестів і проведення регресійного тестування.


## 2. Склад команди та ротація ролей

Відповідно до Статуту команди ([`team/charter.md`](team/charter.md)), кожен учасник проходить повний цикл базових ролей за Спринти 1–4:
* **КТ** — Керівник тестування (Test Lead)
* **ТА** — Тест-аналітик (Test Analyst)
* **ІТ** — Інженер з тестування (Test Engineer / SDET)
* **СМ** — Скрам-майстер / Рецензент (Scrum Master / Reviewer)

| Член команди | GitHub-профіль | Спринт 1 | Спринт 2 | Спринт 3 | Спринт 4 | Спринт 5 |
| :--- | :--- | :-: | :-: | :-: | :-: | :-: |
| **Любочка Вікторія** | [`@twixt-bit`](https://github.com/twixt-bit) | **КТ** | **ТА** | **ІТ** | - | - |
| **Коломоєць Ольга** | [`@foxic-1234`](https://github.com/foxic-1234) | **ТА** | **ІТ** | **СМ** | - | - |
| **Шпилька Тимофій** | [`@white-cod`](https://github.com/white-cod) | **ІТ** | **СМ** | **КТ** | - | - |
| **Мініцька Анастасія** | [`@anastminic-ipt28-ops`](https://github.com/anastminic-ipt28-ops) | **СМ** | **КТ** | **ТА** | - | - |

## 3. Поточні інкременти та навігація

### Спринт 1: «Продукт і вимоги» 
- **Паспорт Спринту 1:** [`sprint1/README.md`](sprint1/README.md)
- **Характеристика продукту та специфікації BPMN:** [`sprint1/product.md`](sprint1/product.md)
- **Беклог вимог (User Stories) та матриця трасування:** [`sprint1/backlog.md`](sprint1/backlog.md)
- **Діаграми бізнес-процесів (BPMN 2.0):** [`sprint1/bpmn/README.md`](sprint1/bpmn/README.md)
- **Протокол рецензування моделей BPMN:** [`sprint1/bpmn/review.md`](sprint1/bpmn/review.md)
- **Таблиця розподілу задач:** [`sprint1/tasks.md`](sprint1/tasks.md)
- **Протокол ретроспективи:** [`sprint1/retro.md`](sprint1/retro.md)

### Спринт 2: «Аудит вимог і стратегія тестування»
- **Паспорт Спринту 2:** [`sprint2/README.md`](sprint2/README.md)
- **Реєстр дефектів вимог (Excel):** [`sprint2/requirements-defects.xlsx`](sprint2/requirements-defects.xlsx)
- **Реєстр дефектів вимог (Markdown):** [`sprint2/requirements-defects.md`](sprint2/requirements-defects.md)
- **Виправлена специфікація вимог (SRS v1.1):** [`sprint2/SRS-1.1.md`](sprint2/SRS-1.1.md)
- **План тестування (Test Plan):** [`sprint2/test-plan.md`](sprint2/test-plan.md)
- **Таблиця декомпозиції задач:** [`sprint2/tasks.md`](sprint2/tasks.md)
- **Протокол ретроспективи:** [`sprint2/retro.md`](sprint2/retro.md)

### Спринт 3: «Тест-кейси і дефекти» (Поточний)
- **Паспорт Спринту 3 (статистика, покриття «вимога → TC»):** [`sprint3/README.md`](sprint3/README.md)
- **Трекер дефектів:** [GitHub Issues з міткою `sprint:3`](https://github.com/white-cod/Software-Quality-Assurance-Technologies_Stuff-Plus/issues?q=label%3Asprint%3A3)
- **Тест-кейси (Excel: Test cases, Coverage, Review):** [`sprint3/test-cases.xlsx`](sprint3/test-cases.xlsx) · [Markdown](sprint3/test-cases.md)
- **Журнал виконання (Run 1, Environment):** [`sprint3/execution-log.xlsx`](sprint3/execution-log.xlsx)
- **Звіти про дефекти:** [`sprint3/bug-reports.md`](sprint3/bug-reports.md) · вкладення [`sprint3/attachments/`](sprint3/attachments/)
- **Таблиця розподілу задач:** [`sprint3/task-board.md`](sprint3/task-board.md)
- **Протокол ретроспективи:** [`sprint3/retro.md`](sprint3/retro.md)

---

## 4. Структура репозиторію

```text
├── README.md                          # Головна сторінка репозиторію команди QualityPulse
├── team/
│   └── charter.md                    # Статут команди (склад, ротація ролей, домовленості)
├── sprint1/                           # Інкремент Спринту 1 (Досьє якості v1)
│   ├── README.md                      # Паспорт та навігатор Спринту 1
│   ├── product.md                     # Характеристика продукту та таблиці елементів BPMN
│   ├── backlog.md                     # Беклог User Stories (US-01..US-14) і трасування
│   ├── tasks.md                       # Таблиця розподілу задач і трудовитрат
│   ├── retro.md                       # Протокол ретроспективи Спринту 1
│   └── bpmn/                          # Моделі бізнес-процесів у нотації BPMN 2.0
│       ├── README.md                  # Опис моделей та інструкція
│       ├── review.md                  # Протокол перехресного рецензування моделей
│       ├── 01_leave_approval.drawio   # Модель 1: Подання й погодження відпустки (+ .png)
│       ├── 02_timesheet.drawio        # Модель 2: Облік робочого часу (+ .png)
│       └── 03_offboarding.drawio      # Модель 3: Звільнення співробітника (+ .png)
├── sprint2/                           # Інкремент Спринту 2 (Досьє якості v2)
│   ├── README.md                      # Паспорт та навігатор Спринту 2
│   ├── requirements-defects.xlsx      # Реєстр 25 дефектів вимог (Excel)
│   ├── requirements-defects.md        # Реєстр дефектів вимог у Markdown
│   ├── SRS-1.1.md                     # Оновлена специфікація вимог з журналом змін
│   ├── test-plan.md                   # Стратегія тестування (7 рівнів, 16 типів)
│   ├── tasks.md                       # Таблиця декомпозиції 14 задач
│   └── retro.md                       # Протокол ретроспективи Start/Stop/Continue
├── sprint3/                           # Інкремент Спринту 3 (Досьє якості v3)
│   ├── README.md                      # Паспорт: трекер, статистика, покриття, DoD, сценарій Demo
│   ├── test-cases.xlsx                # 50 тест-кейсів: Test cases, Coverage, Review (+ test-cases.md)
│   ├── execution-log.xlsx             # Журнал виконання Run 1 і середовище
│   ├── bug-reports.md                 # 12 звітів про дефекти (дзеркало GitHub Issues)
│   ├── attachments/                   # Знімки екрана BUG-NNN-k.png
│   ├── task-board.md                  # Таблиця розподілу задач
│   └── retro.md                       # Протокол ретроспективи Start/Stop/Continue
└── .github/ISSUE_TEMPLATE/bug_report.md  # Шаблон звіту про дефект для GitHub Issues
