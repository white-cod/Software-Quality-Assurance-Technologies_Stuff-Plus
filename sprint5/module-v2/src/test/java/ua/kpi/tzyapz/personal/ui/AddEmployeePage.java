package ua.kpi.tzyapz.personal.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Page Object форми PIM → Add Employee (/pim/addEmployee). */
class AddEmployeePage {

    static final By ADD_EMPLOYEE_TAB = By.xpath("//a[contains(@class,'oxd-topbar-body-nav-tab-item')][normalize-space(.)='Add Employee']");
    static final By FULL_NAME_LABEL = By.xpath("//label[normalize-space(.)='Employee Full Name']");
    static final By FIRST_NAME = By.name("firstName");
    static final By LAST_NAME = By.name("lastName");
    static final By SAVE = By.cssSelector("button[type='submit']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    AddEmployeePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    AddEmployeePage openFromPim() {
        new EmployeeListPage(driver, wait).openFromMenu();
        wait.until(ExpectedConditions.elementToBeClickable(ADD_EMPLOYEE_TAB)).click();
        wait.until(ExpectedConditions.urlContains("/pim/addEmployee"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME));
        return this;
    }

    /** The required-field asterisk is drawn by CSS ::after of the label with class oxd-input-field-required. */
    String fullNameLabelMarker() {
        WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(FULL_NAME_LABEL));
        return (String) ((JavascriptExecutor) driver).executeScript(
                "return getComputedStyle(arguments[0], '::after').content;", label);
    }

    void save() {
        wait.until(ExpectedConditions.elementToBeClickable(SAVE)).click();
    }

    /** Validation message under the input with the given name. */
    String errorUnder(String inputName) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//input[@name='" + inputName + "']/ancestor::div[contains(@class,'oxd-input-group')]"
                        + "//span[contains(@class,'oxd-input-field-error-message')]"))).getText().trim();
    }

    String currentUrl() {
        return driver.getCurrentUrl();
    }
}
