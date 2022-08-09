package ru.netology;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

public class OrderTest {
    private WebDriver driver;

    @BeforeAll  // запускается один раз перед всеми тестами
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999"); // открываем браузер
    }

    @AfterEach
    void quitFromBrowser() {
        driver.quit();
    }

    @Test
    public void shouldBeSuccessOrderOfCard() {

        driver.findElement(By.cssSelector("[data-test-id='name'] input"))
                .sendKeys("Мари-Анет Римская-Корсакова");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79807133080");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        // теперь необходимо проверить что открылась страница с сообщением об успешной отправке заполненной формы
        // ищем по части выражения
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                text.trim());
    }

    @Test
    public void shouldBeUnSuccessOrderBecauseOfInvalidName() {

        driver.findElement(By.cssSelector("span[data-test-id='name'] input"))
                .sendKeys("Alex");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы," +
                " пробелы и дефисы.", text);
    }

    @Test
    public void shouldBeUnSuccessOrderBecauseOfInvalidPhone() {

        driver.findElement(By.cssSelector("span[data-test-id='name'] input"))
                .sendKeys("Мари-Анет Римская-Корсакова");
        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("89807133080");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    public void shouldBeUnSuccessOrderBecauseOfMissedName() {

        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("+79807133080");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    public void shouldBeUnSuccessOrderBecauseOfMissedPhone() {

        driver.findElement(By.cssSelector("span[data-test-id='name'] input"))
                .sendKeys("Мари-Анет Римская-Корсакова");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    public void shouldBeUnSuccessOrderBecauseOfMissedNameAndPhone() {

        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    public void shouldBeUnSuccessOrderBecauseOfMissedCheckboxAgreement() {

        driver.findElement(By.cssSelector("span[data-test-id='name'] input"))
                .sendKeys("Мари-Анет Римская-Корсакова");
        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("+79807133080");
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText();
        Assertions.assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных " +
                "и разрешаю сделать запрос в бюро кредитных историй", text);
    }
}

