package ru.netology.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class DashBoardPage {

    public DashBoardPage() {
        SelenideElement dashBoard = $("[data-test-id='dashboard']").shouldBe(Condition.allOf(
                Condition.visible,
                Condition.exactText("  Личный кабинет")
        ), Duration.ofSeconds(10));
    }
}