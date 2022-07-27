package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.data.UserInfo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeAll
    static void setUpAll () { SelenideLogger.addListener( "allure", new AllureSelenide());}

    @AfterAll
    static void tearDownAll () { SelenideLogger.removeListener( "allure"); }

    @BeforeEach
    public void openPage() {
        open("http://localhost:9999/");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldChangeDateTest() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 4;
        val firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        val daysToAddForSecondMeeting = 7;
        val secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $x("//input[@placeholder=\"Город\"]").val(validUser.getCity());
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys("DELETE");
        $x("//input[@placeholder=\"Дата встречи\"]").val(firstMeetingDate);
        $("[data-test-id='name'] input").val(validUser.getName());
        $("[data-test-id='phone'] input").val(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Запланировать')]").click();
        $x("//*[contains(text(),'Успешно!')]").should(visible, Duration.ofSeconds(15));
        $("[data-test-id=success-notification] .notification__content").should(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys("DELETE");
        $x("//input[@placeholder=\"Дата встречи\"]").val(secondMeetingDate);
        $x("//*[contains(text(),'Запланировать')]").click();
        $("[data-test-id=replan-notification]").should(visible, Duration.ofSeconds(15));
        $x("//*[contains(text(),'Перепланировать')]").click();
        $("[data-test-id=success-notification] .notification__content").should(visible, Duration.ofSeconds(15)).should(exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }

}
