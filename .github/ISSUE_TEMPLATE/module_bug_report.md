---
name: Дефект модуля бізнес-логіки
about: Дефект модуля «Персонал+» (Java), знайдений юніт-тестом (Спринт 4, шаблон 4.11 спринта 3)
title: "[BUG-___] <Проблема> при <вхідні дані> (<ID вимоги BL>)"
labels: ["status:new", "sprint:4", "module"]
---

**Bug ID:** BUG-___   **Severity:** Blocker | Critical | Major | Normal | Minor | Trivial   **Priority:** P1 | P2 | P3
**Linked:** requirement BL-__ (SRS 1.1); test `<КласTest>.<метод>`; class `<Клас>.<метод>(<параметри>)`

### Description
<!-- Правило BL і що робить v1; межі, на яких дефект проявляється і на яких ні; одне речення severity (4.12) і одне — priority (4.13) -->

### Steps to reproduce
1. Клонувати репозиторій і перейти в каталог `sprint4/` (README, розд. 2: JDK 17, Maven 3.9).
2. Виконати `mvn test -Dtest=<КласTest>#<метод>`.
3. Або викликати напряму: `new <Клас>().<метод>(<вхідні дані>)`.

### Actual result

### Expected result
<!-- Значення, обчислене вручну з вимоги BL SRS 1.1 -->

### Environment
Модуль «Персонал+» v1 (1.0.0); JDK 17 (Temurin), Maven 3.9, JUnit 5.10; <ОС>. Команда: `mvn test -Dtest=<КласTest>`.

### Attachments
Фрагмент звіту Surefire (`target/surefire-reports/ua.kpi.tzyapz.personal.<КласTest>.txt`):
```
```

---
**Reporter:** ___   **Date:** __.__.2026   **Status:** New   **Reviewer:** ___ (__.__.2026)
<!-- Мітки: severity:<рівень>, priority:P<n>, status:<стан>, sprint:4, module -->
