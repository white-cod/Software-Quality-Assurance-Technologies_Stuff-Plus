package ua.kpi.tzyapz.personal.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Comparator;
import java.util.List;

/** Page Object списку працівників PIM (/pim/viewEmployeeList). */
class EmployeeListPage {

    static final By RECORDS_FOUND = By.xpath("//span[contains(normalize-space(.), 'Records Found')]");
    static final By ROWS = By.cssSelector(".oxd-table-body .oxd-table-card");
    static final By ROW_CHECKBOX = By.cssSelector(".oxd-checkbox-input");
    static final By ROW_DELETE = By.cssSelector("i.bi-trash");
    static final By ROW_EDIT = By.cssSelector("i.bi-pencil-fill");
    /** The header cell also contains the hidden sort menu, so it is matched by its own text node. */
    static final String LAST_NAME_HEADER = "//div[contains(@class,'oxd-table-th')][text()[normalize-space()='Last Name']]";
    static final By LAST_NAME_SORT = By.xpath(LAST_NAME_HEADER + "//i[contains(@class,'oxd-table-header-sort-icon')]");
    /** Column order: checkbox, Id, First (& Middle) Name, Last Name, ... */
    static final int LAST_NAME_COLUMN = 3;

    private final WebDriver driver;
    private final WebDriverWait wait;

    EmployeeListPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    EmployeeListPage openFromMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@class,'oxd-main-menu-item--name')][text()='PIM']"))).click();
        wait.until(ExpectedConditions.urlContains("/pim/viewEmployeeList"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(RECORDS_FOUND));
        return this;
    }

    /** Number N from the label «(N) Records Found». */
    int recordsFound() {
        String label = wait.until(ExpectedConditions.visibilityOfElementLocated(RECORDS_FOUND)).getText();
        return Integer.parseInt(label.replaceAll("\\D", ""));
    }

    WebElement firstRow() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ROWS));
    }

    /** Opens the Last Name sort menu, picks Ascending / Descending and returns the first 5 last names once sorted. */
    List<String> sortByLastName(String order, Comparator<String> expectedOrder) {
        wait.until(ExpectedConditions.elementToBeClickable(LAST_NAME_SORT)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(LAST_NAME_HEADER
                + "//li[contains(@class,'oxd-table-header-sort-dropdown-item')][normalize-space(.)='" + order + "']"))).click();
        // Wait for the state under test: the table re-renders after the request completes.
        return wait.until(d -> {
            List<String> names = firstLastNames(5);
            return names.size() == 5 && names.equals(names.stream().sorted(expectedOrder).toList()) ? names : null;
        });
    }

    private List<String> firstLastNames(int count) {
        return driver.findElements(ROWS).stream().limit(count)
                .map(r -> r.findElements(By.cssSelector(".oxd-table-cell")).get(LAST_NAME_COLUMN).getText().trim())
                .toList();
    }
}
