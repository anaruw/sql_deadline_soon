package ru.netology.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    private final SelenideElement codeInputField = $("[data-test-id='code'] input");
    private final SelenideElement nextButton = $("[data-test-id='action-verify']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

    public VerificationPage() {
        SelenideElement codeInputTop = $("[data-test-id='code'] .input__top").shouldBe(Condition.allOf(
                Condition.visible,
                Condition.exactText("Код из SMS или Push")
        ), Duration.ofSeconds(10));
    }

    private void nextButtonClick(String testName) {
        Selenide.screenshot(testName + "_before_click");
        nextButton.click();
        Selenide.screenshot(testName + "_after_click");
    }

    public void inputValidCode(String code) {
        codeInputField.setValue(code);
    }

    public DashBoardPage validVerify(String testName) {
        nextButtonClick(testName);
        return new DashBoardPage();
    }
}