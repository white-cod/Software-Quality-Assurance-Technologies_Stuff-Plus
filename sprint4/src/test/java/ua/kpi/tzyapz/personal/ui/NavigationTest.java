package ua.kpi.tzyapz.personal.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** UI-тести навігації і сесії: тест-кейси TC-006, TC-007 спринта 3 (вимоги GUI-30, S-20). */
@DisplayName("Меню і вихід із системи (TC-006, TC-007)")
class NavigationTest extends UiTestBase {

    private static final List<String> ADMIN_MENU = List.of("Admin", "PIM", "Leave", "Time", "Recruitment", "My Info",
            "Performance", "Dashboard", "Directory", "Maintenance", "Claim", "Buzz");

    @Test
    @DisplayName("TC-006 (GUI-30): 12 пунктів меню, пошук «Lea» лишає Leave, меню користувача з 4 пунктів")
    void sideMenuSearchAndUserMenu() {
        DashboardPage dashboard = loginAsAdmin();
        assertEquals(ADMIN_MENU, dashboard.menuItems());

        assertEquals(List.of("Leave"), dashboard.searchMenu("Lea", 1));
        assertEquals(ADMIN_MENU, dashboard.clearMenuSearch(ADMIN_MENU.size()));

        List<String> userMenu = dashboard.openUserMenu();
        assertAll("Меню користувача",
            () -> assertEquals(List.of("About", "Support", "Change Password", "Logout"), userMenu),
            () -> assertEquals("Dashboard", dashboard.moduleTitle())
        );
        dashboard.logout();
        assertTrue(driver.getCurrentUrl().contains("/auth/login"), driver.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-007 (S-20): після Logout захищена адреса переадресовує на сторінку входу")
    void protectedPageAfterLogoutRedirectsToLogin() {
        loginAsAdmin().logout();
        driver.get(LoginPage.BASE_URL + "pim/viewEmployeeList");
        new LoginPage(driver, wait).waitUntilShown();
        assertAll("Після звернення до /pim/viewEmployeeList",
            () -> assertTrue(driver.getCurrentUrl().contains("/auth/login"), driver.getCurrentUrl()),
            () -> assertTrue(driver.findElements(EmployeeListPage.RECORDS_FOUND).isEmpty(), "Показано список працівників")
        );
    }
}
