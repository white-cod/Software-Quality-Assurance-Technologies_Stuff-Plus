package ua.kpi.tzyapz.personal.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** UI-тести модуля PIM: тест-кейси TC-019, TC-022 спринта 3 (вимоги GUI-60, GUI-40). Записів не створюють. */
@DisplayName("Працівники PIM (TC-019, TC-022)")
class EmployeeTest extends UiTestBase {

    @Test
    @DisplayName("TC-019 (GUI-60): лічильник записів, елементи рядка, сортування Last Name в обидва боки")
    void employeeListCounterRowActionsAndSorting() {
        loginAsAdmin();
        EmployeeListPage list = new EmployeeListPage(driver, wait).openFromMenu();
        WebElement row = list.firstRow();
        assertAll("Список працівників",
            () -> assertTrue(list.recordsFound() > 0, "Лічильник «(N) Records Found» має N > 0"),
            () -> assertFalse(row.findElements(EmployeeListPage.ROW_CHECKBOX).isEmpty(), "Немає прапорця вибору"),
            () -> assertFalse(row.findElements(EmployeeListPage.ROW_DELETE).isEmpty(), "Немає іконки видалення"),
            () -> assertFalse(row.findElements(EmployeeListPage.ROW_EDIT).isEmpty(), "Немає іконки редагування")
        );
        // Shared demo data mixes letter case; the list is compared case-insensitively, as the demo sorts it.
        List<String> ascending = list.sortByLastName("Ascending", String.CASE_INSENSITIVE_ORDER);
        List<String> descending = list.sortByLastName("Descending", String.CASE_INSENSITIVE_ORDER.reversed());
        assertAll("Сортування",
            () -> assertEquals(5, ascending.size(), "Ascending: " + ascending),
            () -> assertEquals(5, descending.size(), "Descending: " + descending)
        );
    }

    @Test
    @DisplayName("TC-022 (GUI-40): Save з порожніми First Name і Last Name — «Required», профіль не створено")
    void addEmployeeRequiresFirstAndLastName() {
        loginAsAdmin();
        AddEmployeePage form = new AddEmployeePage(driver, wait).openFromPim();
        assertTrue(form.fullNameLabelMarker().contains("*"), "Біля Employee Full Name немає зірочки");

        form.save();
        assertAll("Після Save з порожніми полями",
            () -> assertEquals("Required", form.errorUnder("firstName")),
            () -> assertEquals("Required", form.errorUnder("lastName")),
            () -> assertTrue(form.currentUrl().contains("/pim/addEmployee"), form.currentUrl())
        );
    }
}
