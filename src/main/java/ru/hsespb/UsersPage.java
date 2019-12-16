package ru.hsespb;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UsersPage {
    private final WebDriver webDriver;

    private static final int TIMEOUT = 5000;

    private static final String CREATE_USER_LINK = "Create user";
    private static final String DELETE_LINK = "Delete";
    private static final String CLOSE_LINK = "close";
    private static final String USERS_LINK = "Users";
    private static final String DELETE_POPUP_ID = "__popup__1";
    private static final String USER_LOGIN_XPATH =
            ".//a[starts-with(@id, 'id_l.U.usersList.UserLogin.editUser')]";

    public UsersPage(String url, WebDriver webDriver) {
        this.webDriver = webDriver;
        webDriver.get(url);
    }

    public boolean createUser(String login, String password) {
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.linkText(CREATE_USER_LINK)))
                .click();

        CreateUserWindow createUserWindow = new CreateUserWindow(webDriver);
        createUserWindow.inputLogin(login);
        createUserWindow.inputPassword(password);
        createUserWindow.confirmPassword(password);

        if (!createUserWindow.confirm()) {
            createUserWindow.cancel();
            return false;
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.linkText(USERS_LINK)))
                .click();
        return true;
    }

    public Set<String> getUserNames() {
        List<WebElement> userElements =
                webDriver.findElements(By.xpath(USER_LOGIN_XPATH));
        return userElements.stream().
                map(WebElement::getText).
                collect(Collectors.toSet());
    }

    public void deleteUsers() {
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);

        while (!webDriver.findElements(By.linkText(DELETE_LINK)).isEmpty()) {
            List<WebElement> deleteElements =
                    webDriver.findElements(By.linkText(DELETE_LINK));

            if (deleteElements.isEmpty()) {
                break;
            }

            deleteElements.get(0).click();

            wait.until(ExpectedConditions.alertIsPresent());
            webDriver.switchTo().alert().accept();

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id(DELETE_POPUP_ID)));
            webDriver.findElement(By.className(CLOSE_LINK)).click();
        }
    }
}
