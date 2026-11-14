# Surefire v1 — стан «до» регресії

* `TEST-*.xml`, `*.txt` юніт-класів — прогін `mvn -B clean test` на модулі **v1** (1.0.0) 09.11.2026 перед регресією: `Tests run: 79, Failures: 17, Errors: 0, Skipped: 0` (17 failed — BUG-013 … BUG-018).
* `ua.kpi.tzyapz.personal.ui.*.txt` — UI-тести на demo з офіційного прогону спринта 4 (01.11.2026, `mvn -B clean test -DexcludedGroups=none`, 7 / 7 passed); копія з `sprint4/docs/surefire-reports/`.
