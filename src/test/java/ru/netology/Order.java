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

public class Order {
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
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    public void test() {
        driver.get("http://localhost:9999"); // открываем браузер

        driver.findElement(By.cssSelector("span[data-test-id='name'] input")).sendKeys("Александр");     // находим первый элемент - поле ввода имени, и вводим в него имя Андрей (командой sendkeys)
        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("+79807133080"); // заполняем следующее поле заявки - телефон
        driver.findElement(By.className("checkbox")).click(); // следующее поле - чекбокс, командой click ставим в нем галочку
        driver.findElement(By.tagName("button")).click(); // следующее поле - кнопка ОТПРАВИТЬ - нажимаем ее той же командой click

        // теперь необходимо проверить что открылась страница с сообщением об успешной отправке заполненной формы

        String text = driver.findElement(By.className("paragraph")).getText(); // ищем по части выражения

//        Assertions.assertEquals("  Ваша заявка успешно отправлена!", text);
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());  // или убираем пробелы методом .trim
    }

}

