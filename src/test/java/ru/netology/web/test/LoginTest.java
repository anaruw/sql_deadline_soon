package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;
import ru.netology.web.data.UserInfo;
import ru.netology.web.pages.DashBoardPage;
import ru.netology.web.pages.LoginPage;
import ru.netology.web.pages.VerificationPage;
import ru.netology.web.util.DataHelper;
import ru.netology.web.util.SqlHelper;

public class LoginTest {
    @BeforeEach
    public void setUp() {
        Selenide.open("http://localhost:9999");
    }

    @AfterAll
    public static void endTest() {
        SqlHelper.cleaningDB();
    }

    @Test
    @DisplayName("valid_login_test")
    public void validLoginTest(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();

        LoginPage loginPage = new LoginPage();
        UserInfo registeredUser = DataHelper.getRegisteredUser();

        loginPage.loginInput(registeredUser.getLogin());
        loginPage.passwordInput(registeredUser.getPassword());
        VerificationPage verificationPage = loginPage.validLogin(testName);

        verificationPage.inputValidCode(SqlHelper.getVerifyCode(registeredUser.getLogin()));
        DashBoardPage dashBoardPage = verificationPage.validVerify(testName);
    }

    @Test
    @DisplayName("invalid_login_test")
    public void invalidLoginTest(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String errorMessage = "Ошибка! Неверно указан логин или пароль";

        LoginPage loginPage = new LoginPage();
        UserInfo registeredUser = DataHelper.getRegisteredUser();

        loginPage.loginInput(registeredUser.getLogin());
        loginPage.passwordInput(DataHelper.fakePassword(registeredUser));

        loginPage.nextButtonClick(testName);
        loginPage.shouldBeErrorNotification(errorMessage, testName);
    }

    @Test
    @DisplayName("user_should_be_blocked_with_wrong_password")
    public void userShouldBeBlockedWithWrongPassword(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String errorMessage = "Ошибка! Неверно указан логин или пароль";

        LoginPage loginPage = new LoginPage();
        UserInfo registeredUser = DataHelper.getRegisteredUser();

        loginPage.loginInput(registeredUser.getLogin());

        String actualStatus = SqlHelper.userStatus(registeredUser.getLogin());

        if (actualStatus.equals("blocked")) {
            SqlHelper.changeUserStatus(registeredUser.getLogin(), actualStatus);
        }

        for (int i = 0; i < 3; i++) {
            loginPage.passwordInput(DataHelper.fakePassword(registeredUser));
            loginPage.nextButtonClick(testName);
            loginPage.shouldBeErrorNotification(errorMessage, testName);
            loginPage.clearPasswordInput();
        }
        String expectedStatus = "blocked";
        actualStatus = SqlHelper.userStatus(registeredUser.getLogin());

        Assertions.assertEquals(expectedStatus, actualStatus);
    }

    @Test
    @DisplayName("blocked_user_can_not_login")
    public void blockedUserCanNotLogin(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String errorBlockingMessage = "Ошибка! Пользователь заблокирован";

        LoginPage loginPage = new LoginPage();
        UserInfo registeredUser = DataHelper.getRegisteredUser();

        String userStatus = SqlHelper.userStatus(registeredUser.getLogin());

        if (userStatus.equals("active")) {
            SqlHelper.changeUserStatus(registeredUser.getLogin(), userStatus);
        }
        loginPage.loginInput(registeredUser.getLogin());
        loginPage.passwordInput(registeredUser.getPassword());
        loginPage.nextButtonClick(testName);

        loginPage.shouldBeErrorNotification(errorBlockingMessage, testName);
    }
}