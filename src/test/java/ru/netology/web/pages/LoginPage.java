package ru.netology.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement loginInputField = $("[data-test-id='login'] input");
    private final SelenideElement passwordInputField = $("[data-test-id='password'] input");
    private final SelenideElement nextButton = $("[data-test-id='action-login']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

    public void loginInput(String login) {
        loginInputField.setValue(login);
    }

    public void passwordInput(String password) {
        passwordInputField.setValue(password);
    }

    private void nextButtonClick(String testName) {
        Selenide.screenshot(testName + "_before_click");
        nextButton.click();
        Selenide.screenshot(testName + "_after_click");
    }

    private void shouldBeErrorNotification(String errorMessage, String testName) {
        errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(10));
        Selenide.screenshot(testName + "_notification");
        errorNotification.shouldHave(Condition.exactText(errorMessage));
    }

    public VerificationPage validLogin(String testName) {
        nextButtonClick(testName);
        return new VerificationPage();
    }

    public void invalidLogin(String errorMessage, String testName) {
        nextButtonClick(testName);
        shouldBeErrorNotification(errorMessage, testName);
    }

    public void clearInput() {
        passwordInputField.press(Keys.chord(Keys.HOME, Keys.SHIFT)).press(Keys.BACK_SPACE);
    }
}