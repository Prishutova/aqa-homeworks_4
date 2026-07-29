package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

class CardDeliveryTest {
    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldSubmitTheFormDirectInputOfValues() {
        Selenide.open("http://localhost:9999");

        String planningDate = generateDate(3, "dd/MM/yyyy");

        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE)).setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79123456789");
        $("[data-test-id='agreement']").click();
        $(".button").find(withText("Забронировать")).click();
        $(withText("Встреча успешно забронирована"))
                .should(Condition.visible, Duration.ofSeconds(15));
    }
}
