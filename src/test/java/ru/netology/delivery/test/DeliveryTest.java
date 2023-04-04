package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;


import java.util.Random;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeAll
    static void setUpAll(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }

    private static Faker faker;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }


    @Test
    @DisplayName("Should successful plan meeting")
    void shouldSuccessfulPlanMeeting() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $x("//*[contains(text(), 'Успешно')]").should(Condition.appear);
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + DataGenerator.generateDate(3))).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[placeholder='Дата встречи']").setValue(firstMeetingDate);
        $("[name='name']").setValue(validUser.getName());
        ;
        $("[name='phone']").setValue(validUser.getPhone());
        $("[class='checkbox__box']").click();
        $("[class='button__text']").click();
        $("[class='notification__title']").shouldBe(visible);
        $x("//div[contains(text(), 'Встреча успешно')]").shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(visible);
        $("[placeholder='Дата встречи']").sendKeys(Keys.CONTROL + "a");
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(secondMeetingDate);
        $("[class='button__text']").click();
        $x("//div[contains(text(), 'У вас уже запланирована встреча')]").shouldBe(visible);
        $x("//div[contains(text(), 'Перепланировать')]").click();
        $x("//div[contains(text(), 'Встреча успешно')]").shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(visible);
    }

    @Test
    @DisplayName("Should not be successful because few days to delivery")
    void shouldNotBeSuccessLessTimeToDelivery() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");


        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(2));
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=date] .input_invalid").should(Condition.appear);

    }

    @Test
    @DisplayName("Should not be successful because wrong city used")
    void shouldNotBeSuccessWrongCity() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

        $x("//input[@placeholder='Город']").setValue(DataGenerator.generateWrongCity("en-US"));
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=city].input_invalid").should(Condition.appear);
    }

    @Test
    @DisplayName("Should not be successful because name of city is empty")
    void shouldNotBeSuccessEmptyCity() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

        $x("//input[@placeholder='Город']").setValue("");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=city].input_invalid").should(Condition.appear);
    }

    @Test
    @DisplayName("Should not be successful because latin name used")
    void shouldNotBeSuccessWrongLatinName() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("en-US");

        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=name].input_invalid").should(Condition.appear);
    }

    @Test
    @DisplayName("Should not be successful because name contains digit")
    void shouldNotBeSuccessWrongNameWithDigit() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int number = new Random().nextInt(10);
        String invalidUserName = String.valueOf(validUser.getName()) + number;

        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue(invalidUserName);
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=name].input_invalid").should(Condition.appear);
    }

    @Test
    @DisplayName("Should not be successful because name contains special character")
    void shouldNotBeSuccessWrongNameWithSpecialCharacter() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        String invalidUserName = String.valueOf(validUser.getName()) + DataGenerator.generateSpecialCharacter();

        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue(invalidUserName);
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=name].input_invalid").should(Condition.appear);
    }

    @Test
    @DisplayName("Should not be successful because name is empty")
    void shouldNotBeSuccessWrongEmptyName() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue("");
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=name].input_invalid").should(Condition.appear);
    }

    @Test
    @DisplayName("Should not be successful because phone number is too short")
    void shouldNotBeSuccessWrongShortPhone() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(DataGenerator.generateShortPhone("ru"));
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=phone].input_invalid").should(Condition.appear);
    }

    @Test
    @DisplayName("Should not be successful because phone number is too long")
    void shouldNotBeSuccessWrongLongPhone() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(DataGenerator.generateLongPhone("ru"));
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=phone].input_invalid").should(Condition.appear);
    }

    @Test
    @DisplayName("Should not be successful because phone number is empty")
    void shouldNotBeSuccessEmptyPhone() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue("");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=phone].input_invalid").should(Condition.appear);
    }

    @Test
    @DisplayName("Should not be successful because agreement is not accepted")
    void shouldNotBeSuccessCheckMarkNotChecked() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(validUser.getPhone());
//        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=agreement].input_invalid").should(Condition.appear);
    }
}