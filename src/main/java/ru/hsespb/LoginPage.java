package ru.hsespb;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private final WebDriver webDriver;

    private static final int TIMEOUT = 5000;

    private static final String LOGIN_FIELD_ID = "id_l.L.login";
    private static final String PASSWORD_FIELD_ID = "id_l.L.password";
    private static final String LOGIN_BUTTON_ID = "id_l.L.loginButton";

    private static final String LOGIN = "root";
    private static final String PASSWORD = "root";

    public LoginPage(String url, WebDriver webDriver) {
        this.webDriver = webDriver;
        webDriver.get(url);
        new WebDriverWait(webDriver, TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.id(LOGIN_FIELD_ID)));
    }

    public void login() {
        webDriver.findElement(By.id(LOGIN_FIELD_ID)).sendKeys(LOGIN);
        webDriver.findElement(By.id(PASSWORD_FIELD_ID)).sendKeys(PASSWORD);
        webDriver.findElement(By.id(LOGIN_BUTTON_ID)).click();
    }
}
