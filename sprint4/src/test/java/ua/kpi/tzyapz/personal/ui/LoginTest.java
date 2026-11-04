package ua.kpi.tzyapz.personal.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** UI-тести входу: тест-кейси TC-001, TC-002, TC-004 спринта 3 (вимоги F-10, F-20, GUI-10). */
@DisplayName("Вхід у систему (TC-001, TC-002, TC-004)")
class LoginTest extends UiTestBase {

    @Test
    @DisplayName("TC-001 (F-10): вхід Admin відкриває Dashboard із меню модулів; Logout повертає на вхід")
    void adminLoginOpensDashboard() {
        DashboardPage dashboard = loginAsAdmin();
        List<String> menu = dashboard.menuItems();
        assertAll("Dashboard після входу",
            () -> assertTrue(driver.getCurrentUrl().contains("/dashboard/index"), driver.getCurrentUrl()),
            () -> assertEquals("Dashboard", dashboard.moduleTitle()),
            () -> assertTrue(menu.containsAll(List.of("Admin", "PIM", "Leave", "Time", "Recruitment", "My Info", "Dashboard")),
                    "Пункти меню: " + menu)
        );
        dashboard.logout();
        assertTrue(driver.getCurrentUrl().contains("/auth/login"), driver.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-002 (F-20): неправильний пароль — «Invalid credentials», повідомлення лишається до нового входу")
    void wrongPasswordShowsInvalidCredentials() {
        LoginPage login = loginPage();
        login.typeUsername(LoginPage.ADMIN);
        login.typePassword("WrongPass1");
        login.submit();
        String error = login.errorText();
        assertAll("Після неправильного пароля",
            () -> assertEquals("Invalid credentials", error),
            () -> assertTrue(driver.getCurrentUrl().contains("/auth/login"), driver.getCurrentUrl())
        );

        login.typeUsername(LoginPage.ADMIN);
        assertTrue(driver.findElement(LoginPage.ERROR).isDisplayed(), "Повідомлення зникло після редагування Username (GUI-20)");

        login.typePassword(LoginPage.ADMIN_PASSWORD);
        login.submit();
        new DashboardPage(driver, wait).waitUntilShown();
        assertTrue(driver.findElements(LoginPage.ERROR).isEmpty(), "Повідомлення лишилося після успішного входу");
    }

    @Test
    @DisplayName("TC-004 (GUI-10): сторінка входу містить логотип, заголовок, тестові дані, кнопку і посилання")
    void loginPageHasRequiredElements() {
        LoginPage login = loginPage();
        List<String> credentials = driver.findElements(LoginPage.DEMO_CREDENTIALS).stream()
                .map(WebElement::getText).map(String::trim).toList();
        WebElement username = driver.findElement(LoginPage.USERNAME);
        WebElement password = driver.findElement(LoginPage.PASSWORD);
        assertAll("Склад сторінки входу",
            () -> assertTrue(driver.findElement(LoginPage.LOGO).isDisplayed(), "Логотип не показано"),
            () -> assertEquals("Login", login.text(LoginPage.TITLE)),
            () -> assertEquals(List.of("Username : Admin", "Password : admin123"), credentials),
            () -> assertEquals("Login", driver.findElement(LoginPage.LOGIN_BUTTON).getText().trim()),
            () -> assertEquals("Forgot your password?", login.text(LoginPage.FORGOT_PASSWORD)),
            () -> assertEquals("Username", username.getAttribute("placeholder")),
            () -> assertEquals("Password", password.getAttribute("placeholder")),
            () -> assertEquals("password", password.getAttribute("type"), "Пароль не маскується")
        );
        login.typePassword(LoginPage.ADMIN_PASSWORD);
        wait.until(ExpectedConditions.attributeToBe(LoginPage.PASSWORD, "value", LoginPage.ADMIN_PASSWORD));
        assertFalse(driver.findElement(LoginPage.PASSWORD).getText().contains(LoginPage.ADMIN_PASSWORD));
    }
}
