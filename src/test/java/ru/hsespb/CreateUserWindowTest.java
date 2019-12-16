package ru.hsespb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserTest {
    private static final String BASE_URL = "localhost:8080";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String USERS_URL = BASE_URL + "/users";

    // Testing only login field right now.
    private static final String PASSWORD = "password";

    // It turns out that the limit is 50.
    private static final int MAX_LENGTH = 50;

    private WebDriver webDriver;

    private UsersPage usersPage;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("webdriver.chrome.driver","/usr/bin/chromedriver");
    }

    @BeforeEach
    void setUp() {
        webDriver = new ChromeDriver();

        LoginPage loginPage = new LoginPage(LOGIN_URL, webDriver);
        loginPage.login();

        usersPage = new UsersPage(USERS_URL, webDriver);
        usersPage.deleteUsers();
    }

    @AfterEach
    void close() {
        webDriver.quit();
    }

    @Test
    void testSimpleUser() {
        testUser("user");
    }

    @Test
    void testCapital() {
        testUser("UsEr");
    }

    @Test
    void testMixed() {
        testUser("User123");
    }

    @Test
    void testUnderscoreWithLetters() {
        testUser("_user");
        testUser("user_name");
        testUser("user_");
    }

    @Test
    void testPointWithLetters() {
        testUser(".user");
        testUser("user.name");
        testUser("user.");
    }

    @Test
    void testSlashInside() {
        testUser("\\test");
        testUser("user\\name");
        testUser("user\\");
    }

    @Test
    void testSimpleNoLetters() {
        testUser("123");
        testUser("\\_");
    }

    @Test
    void testPhone() {
        testUser("+7(900)123-45-67");
    }

    @Test
    void testMaxString() {
        char[] seq = new char[MAX_LENGTH];
        Arrays.fill(seq, 'a');
        testUser(String.copyValueOf(seq));
    }

    @Test
    // Bug or feature: if the length exceeds MAX_LENGTH, the user will be still created,
    // but its login will be truncated to MAX_LENGTH.
    void testMoreThanMaxString() {
        char[] seq = new char[MAX_LENGTH + 1];
        Arrays.fill(seq, 'b');
        String user = String.copyValueOf(seq);

        assertTrue(createUser(user));

        char[] actualSeq = new char[MAX_LENGTH];
        Arrays.fill(actualSeq, 'b');
        Set<String> users = usersPage.getUserNames();

        assertFalse(users.contains(user));
        assertTrue(users.contains(String.copyValueOf(actualSeq)));
    }

    @Test
    void testSpecialCharacters() {
        String special = "!\"#$%&'()*+,-:;=?@[\\]^_`{|}~";
        int count = 0;
        for (char c : special.toCharArray()) {
            testUser("" + c);

            // It cannot create more than 10 users (including root and guest).
            count++;
            if (count == 8) {
                count = 0;
                usersPage.deleteUsers();
            }
        }

        testUser(special);
    }

    @Test
    void testUnicodeCharacters() {
        testUser("©µ¡§");
    }

    @Test
    void testWebsite() {
        testUser("www.github.com");
    }

    @Test
    void testEmail() {
        testUser("user@mail.ru");
    }

    @Test
    void testForbidOnlyPoints() {
        testForbidUser(".");
        testForbidUser("..");
    }

    @Test
    void testForbidEmpty() {
        testForbidUser("");
    }

    @Test
    void testForbidSpaces() {
        testForbidUser("  user");
        testForbidUser("user name");
        testForbidUser("user  ");
    }

    @Test
    void testForbidBrackets() {
        testForbidUser("<");
        testForbidUser(">");
    }

    @Test
    void testForbidDirectSlash() {
        testForbidUser("/");
    }

    private void testForbidUser(String user) {
        assertFalse(createUser(user));
    }

    private void testUser(String user) {
        assertTrue(createUser(user));
        checkUser(user);
    }

    private void checkUser(String user) {
        Set<String> users = usersPage.getUserNames();
        assertTrue(users.contains(user));
    }

    private boolean createUser(String login) {
        return usersPage.createUser(login, PASSWORD);
    }

}