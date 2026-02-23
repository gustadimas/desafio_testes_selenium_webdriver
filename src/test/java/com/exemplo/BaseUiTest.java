package com.exemplo;

import java.time.Duration;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

abstract class BaseUiTest {

    static final Duration IMPLICIT_WAIT = Duration.ZERO;
    static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);
    static final Duration SCRIPT_TIMEOUT = Duration.ofSeconds(30);
    static final Duration EXPLICIT_WAIT_TIMEOUT = Duration.ofSeconds(10);

    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = createDriver();
        configureTimeouts(driver);
        wait = new WebDriverWait(driver, EXPLICIT_WAIT_TIMEOUT);
        driver.get(getBaseUrl());
        waitForPageReady();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    WebElement waitUntilVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    WebElement waitUntilClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    void waitForPageReady() {
        wait.until(currentDriver -> "complete".equals(
                ((JavascriptExecutor) currentDriver).executeScript("return document.readyState")
        ));
    }

    WebDriver createDriver() {
        String browser = System.getProperty("browser", "chrome")
                .trim()
                .toLowerCase(Locale.ROOT);

        if ("firefox".equals(browser)) {
            return new FirefoxDriver();
        }
        return new ChromeDriver();
    }

    void configureTimeouts(WebDriver currentDriver) {
        currentDriver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
        currentDriver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);
        currentDriver.manage().timeouts().scriptTimeout(SCRIPT_TIMEOUT);
    }

    String getBaseUrl() {
        return System.getProperty("base.url", "https://example.com");
    }
}