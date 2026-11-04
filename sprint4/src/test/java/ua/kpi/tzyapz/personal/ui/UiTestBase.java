package ua.kpi.tzyapz.personal.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Спільний життєвий цикл UI-тестів: новий браузер на кожен тест, quit() після кожного тесту.
 * Запуск: mvn test -DexcludedGroups=none; з видимим вікном — додати -Dheadless=false.
 */
@Tag("ui")
abstract class UiTestBase {

    /** The public demo is slow under load (catalogue, 5.8), so waits are generous. */
    static final Duration TIMEOUT = Duration.ofSeconds(20);

    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    void openBrowser() {
        ChromeOptions options = new ChromeOptions();
        if (!"false".equals(System.getProperty("headless"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1280,900", "--incognito");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, TIMEOUT);
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    LoginPage loginPage() {
        return new LoginPage(driver, wait).open();
    }

    DashboardPage loginAsAdmin() {
        return loginPage().loginAs(LoginPage.ADMIN, LoginPage.ADMIN_PASSWORD);
    }
}
