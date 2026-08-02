package ru.netology.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.time.Duration.ofSeconds;

class CardDeliveryTest {
    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldSubmitTheFormDirectInputOfValues() {
        Selenide.open("http://localhost:9999");
        String planningDate = generateDate(3, "dd.MM.yyyy");

        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE)).setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79600000000");
        $("[data-test-id='agreement']").click();
        $(".button").find(withText("Забронировать")).click();
        $("[data-test-id='notification']")
                .shouldBe(visible, ofSeconds(15))
                .shouldHave(text("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    void shouldSubmitTheFormWithAutocomplete() {
        Selenide.open("http://localhost:9999");
        LocalDate futureDate = LocalDate.now().plusDays(7);
        String dayToClick = futureDate.format(DateTimeFormatter.ofPattern("d"));
        String expectedDate = futureDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String expectedMonth = futureDate.format(DateTimeFormatter.ofPattern("MMM")).replace(".", "");
        String expectedYear = futureDate.format(DateTimeFormatter.ofPattern("yyyy"));

        $("[data-test-id='city'] input").setValue("Ка");
        $$(".menu-item__control").find(exactText("Казань")).click();

        $("[data-test-id='date'] .icon-button").click();
        $(".calendar").shouldBe(visible, Duration.ofSeconds(5));
        String calendarTitle = $(".calendar__name").getText().toLowerCase();
        if (!calendarTitle.contains(expectedMonth) || !calendarTitle.contains(expectedYear)) {
            $(".calendar__arrow_direction_right[data-step='1']").click();
        }
        $$("td.calendar__day")
                .filter(visible)
                .find(exactText(dayToClick))
                .shouldBe(visible)
                .click();

        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79600000000");
        $("[data-test-id='agreement']").click();
        $(".button").find(withText("Забронировать")).click();
        $("[data-test-id='notification']")
                .shouldBe(visible, ofSeconds(15))
                .shouldHave(text("Встреча успешно забронирована на " + expectedDate));
    }
}