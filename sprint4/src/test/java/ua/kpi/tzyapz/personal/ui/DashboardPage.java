package ua.kpi.tzyapz.personal.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/** Page Object робочої області після входу: верхня панель, бічне меню, меню користувача. */
class DashboardPage {

    static final By MODULE_TITLE = By.cssSelector("h6.oxd-topbar-header-breadcrumb-module");
    static final By MENU_ITEMS = By.cssSelector(".oxd-main-menu-item--name");
    static final By MENU_SEARCH = By.cssSelector(".oxd-main-menu-search input");
    static final By USER_DROPDOWN = By.cssSelector(".oxd-userdropdown-tab");
    static final By USER_MENU_LINKS = By.cssSelector("a.oxd-userdropdown-link");
    static final By LOGOUT = By.cssSelector("a.oxd-userdropdown-link[href$='/auth/logout']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    DashboardPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    DashboardPage waitUntilShown() {
        wait.until(ExpectedConditions.urlContains("/dashboard/index"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(MODULE_TITLE));
        return this;
    }

    String moduleTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(MODULE_TITLE)).getText().trim();
    }

    List<String> menuItems() {
        return texts(wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(MENU_ITEMS)));
    }

    /** Types into the menu search and waits until the menu shows exactly {@code expectedCount} items. */
    List<String> searchMenu(String text, int expectedCount) {
        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(MENU_SEARCH));
        search.sendKeys(text);
        return texts(wait.until(ExpectedConditions.numberOfElementsToBe(MENU_ITEMS, expectedCount)));
    }

    List<String> clearMenuSearch(int expectedCount) {
        WebElement search = driver.findElement(MENU_SEARCH);
        // clear() does not fire Vue's input event, so the text is removed key by key.
        while (!search.getAttribute("value").isEmpty()) {
            search.sendKeys(Keys.BACK_SPACE);
        }
        return texts(wait.until(ExpectedConditions.numberOfElementsToBe(MENU_ITEMS, expectedCount)));
    }

    List<String> openUserMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(USER_DROPDOWN)).click();
        return texts(wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(USER_MENU_LINKS)));
    }

    LoginPage logout() {
        if (driver.findElements(LOGOUT).isEmpty()) {
            openUserMenu();
        }
        wait.until(ExpectedConditions.elementToBeClickable(LOGOUT)).click();
        return new LoginPage(driver, wait).waitUntilShown();
    }

    private static List<String> texts(List<WebElement> elements) {
        return elements.stream().map(e -> e.getText().trim()).toList();
    }
}
