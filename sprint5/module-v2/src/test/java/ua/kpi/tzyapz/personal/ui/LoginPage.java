package ua.kpi.tzyapz.personal.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Page Object сторінки входу OrangeHRM (/auth/login). */
class LoginPage {

    static final String BASE_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/";
    static final String URL = BASE_URL + "auth/login";
    /** Shared demo credentials published on the login page (catalogue, 5.5). */
    static final String ADMIN = "Admin";
    static final String ADMIN_PASSWORD = "admin123";

    static final By TITLE = By.cssSelector("h5.orangehrm-login-title");
    static final By USERNAME = By.name("username");
    static final By PASSWORD = By.name("password");
    static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    static final By ERROR = By.cssSelector(".oxd-alert-content-text");
    static final By LOGO = By.cssSelector(".orangehrm-login-branding img");
    static final By DEMO_CREDENTIALS = By.cssSelector(".orangehrm-demo-credentials p");
    static final By FORGOT_PASSWORD = By.cssSelector(".orangehrm-login-forgot-header");

    private final WebDriver driver;
    private final WebDriverWait wait;

    LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    LoginPage open() {
        driver.get(URL);
        waitUntilShown();
        return this;
    }

    LoginPage waitUntilShown() {
        wait.until(ExpectedConditions.urlContains("/auth/login"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME));
        return this;
    }

    void typeUsername(String username) {
        replaceText(driver.findElement(USERNAME), username);
    }

    void typePassword(String password) {
        replaceText(driver.findElement(PASSWORD), password);
    }

    /** clear() does not fire Vue's input event, so the old value is selected and deleted from the keyboard. */
    private static void replaceText(WebElement field, String text) {
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        field.sendKeys(text);
    }

    void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON)).click();
    }

    DashboardPage loginAs(String username, String password) {
        typeUsername(username);
        typePassword(password);
        submit();
        return new DashboardPage(driver, wait).waitUntilShown();
    }

    String errorText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR)).getText();
    }

    String text(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
    }
}
