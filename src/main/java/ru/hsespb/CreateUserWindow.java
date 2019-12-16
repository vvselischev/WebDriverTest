package ru.hsespb;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateUserWindow {
    private final WebDriver webDriver;

    private static final int TIMEOUT = 5000;

    private static final String LOGIN_FIELD_ID = "id_l.U.cr.login";
    private static final String PASSWORD_FIELD_ID = "id_l.U.cr.password";
    private static final String PASSWORD_CONFIRM_FIELD_ID = "id_l.U.cr.confirmPassword";
    private static final String OK_BUTTON_ID = "id_l.U.cr.createUserOk";
    private static final String CANCEL_BUTTON_ID = "id_l.U.cr.createUserCancel";
    private static final String CREATE_USER_DIALOG = "id_l.U.cr.createUserDialog";
    private static final String LOGIN_REQUIRED_MARKER_CLASS = "error-bulb2";
    private static final String INVALID_SYMBOL_MARKER_CLASS = "errorSeverity";

    public CreateUserWindow(WebDriver webDriver) {
        this.webDriver = webDriver;
        new WebDriverWait(webDriver, TIMEOUT).
                until(ExpectedConditions.visibilityOfElementLocated(
                        By.id(CREATE_USER_DIALOG)));
    }

    public void inputLogin(String login) {
        WebElement loginField = webDriver.findElement(By.id(LOGIN_FIELD_ID));
        loginField.sendKeys(login);
    }

    public void inputPassword(String password) {
        WebElement passwordField = webDriver.findElement(By.id(PASSWORD_FIELD_ID));
        passwordField.sendKeys(password);
    }

    public void confirmPassword(String password) {
        WebElement confirmPasswordField =
                webDriver.findElement(By.id(PASSWORD_CONFIRM_FIELD_ID));
        confirmPasswordField.sendKeys(password);
    }

    public void cancel() {
        webDriver.findElement(By.id(CANCEL_BUTTON_ID)).click();
    }

    public boolean confirm() {
        webDriver.findElement(By.id(OK_BUTTON_ID)).click();

        new WebDriverWait(webDriver, TIMEOUT).
                until(webDriver ->
                    !webDriver.findElements(By.className(LOGIN_REQUIRED_MARKER_CLASS)).isEmpty() ||
                    !webDriver.findElements(By.className(INVALID_SYMBOL_MARKER_CLASS)).isEmpty() ||
                    webDriver.findElements(By.id(CREATE_USER_DIALOG)).isEmpty()
                );

        return webDriver.findElements(By.className(LOGIN_REQUIRED_MARKER_CLASS)).isEmpty() &&
                webDriver.findElements((By.className(INVALID_SYMBOL_MARKER_CLASS))).isEmpty();
    }
}
