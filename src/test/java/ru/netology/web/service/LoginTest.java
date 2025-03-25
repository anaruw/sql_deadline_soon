package ru.netology.web.service;

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

    @Test
    @DisplayName("valid_login_test")
    public void validLoginTest(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();

        LoginPage loginPage = new LoginPage();
        UserInfo registeredUser = DataHelper.getRegisteredUser();

        loginPage.loginInput(registeredUser.getLogin());
        loginPage.passwordInput(registeredUser.getPassword());
        VerificationPage verificationPage = loginPage.validLogin(testName);

        verificationPage.inputValidCode(registeredUser.getLogin());
        DashBoardPage dashBoardPage = verificationPage.validVerify(testName);
    }

    @Test
    @DisplayName("invalid_login_test")
    public void invalidLoginTest(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String errorMessage = "Ошибка! Неверно указан логин или пароль";

        LoginPage loginPage = new LoginPage();
        UserInfo registeredUser = DataHelper.getRegisteredUser();

        loginPage.loginInput(DataHelper.fakeLogin(registeredUser));
        loginPage.passwordInput(registeredUser.getPassword());

        loginPage.invalidLogin(errorMessage, testName);
    }

    @Test
    @DisplayName("invalid_verify_code_test")
    public void invalidVerifyCodeTest(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String errorMessage = "Ошибка! Неверно указан код! Попробуйте ещё раз.";

        LoginPage loginPage = new LoginPage();
        UserInfo registeredUser = DataHelper.getRegisteredUser();

        loginPage.loginInput(registeredUser.getLogin());
        loginPage.passwordInput(registeredUser.getPassword());
        VerificationPage verificationPage = loginPage.validLogin(testName);

        verificationPage.inputFakeCode(registeredUser.getLogin());
        verificationPage.invalidVerify(errorMessage, testName);
    }

    @Test
    @DisplayName("user_should_be_blocked_with_wrong_password")
    public void userShouldBeBlockedWithWrongPassword(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String errorMessage = "Ошибка! Неверно указан логин или пароль";
        String errorBlockingMessage = "Ошибка! Пользователь заблокирован";

        LoginPage loginPage = new LoginPage();
        UserInfo registeredUser = DataHelper.getRegisteredUser();

        loginPage.loginInput(registeredUser.getLogin());

        for (int i = 0; i < 3; i++) {
            loginPage.passwordInput(DataHelper.fakePassword(registeredUser));
            loginPage.invalidLogin(errorMessage, testName);
            loginPage.clearPasswordInput();
        }
        String expectedStatus = "blocked";
        String actualStatus = SqlHelper.userStatus(registeredUser.getLogin());

        Assertions.assertEquals(expectedStatus, actualStatus);

        loginPage.passwordInput(registeredUser.getPassword());
        loginPage.invalidLogin(errorBlockingMessage, testName);
    }

    @Test
    @DisplayName("user_should_be_blocked_with_wrong_verify_code")
    public void userShouldBeBlockedWithWrongVerifyCode(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String errorMessage = "Ошибка! Неверно указан код! Попробуйте ещё раз.";
        String errorBlockingMessage = "Ошибка! Пользователь заблокирован";

        LoginPage loginPage = new LoginPage();
        UserInfo registeredUser = DataHelper.getRegisteredUser();

        loginPage.loginInput(registeredUser.getLogin());
        loginPage.passwordInput(registeredUser.getPassword());
        VerificationPage verificationPage = loginPage.validLogin(testName);

        for (int i = 0; i < 3; i++) {
            verificationPage.inputFakeCode(registeredUser.getLogin());
            if (i == 2) break;
            verificationPage.invalidVerify(errorMessage, testName);
            verificationPage.clearCodeInput();
        }
        String expectedStatus = "blocked";
        String actualStatus = SqlHelper.userStatus(registeredUser.getLogin());

        Assertions.assertEquals(expectedStatus, actualStatus);

        verificationPage.invalidVerify(errorBlockingMessage, testName);
    }
}