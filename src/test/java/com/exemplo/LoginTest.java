package com.exemplo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

class LoginTest extends BaseUiTest {

    static final By EMAIL_INPUT = By.cssSelector(
            System.getProperty("login.email.selector", "#email")
    );
    static final By PASSWORD_INPUT = By.cssSelector(
            System.getProperty("login.password.selector", "#password")
    );
    static final By SUBMIT_BUTTON = By.cssSelector(
            System.getProperty("login.submit.selector", "button[type='submit']")
    );
    static final By SUCCESS_MESSAGE = By.cssSelector(
            System.getProperty("login.success.selector", ".success-message")
    );

    static final String VALID_EMAIL = System.getProperty("login.valid.email", "usuario@exemplo.com");
    static final String VALID_PASSWORD = System.getProperty("login.valid.password", "senha123");
    static final String EXPECTED_SUCCESS_TEXT = System.getProperty(
            "login.success.text",
            "Login realizado com sucesso"
    );

    @Test
    void givenCredenciaisValidas_whenSubmeterLogin_thenDeveRedirecionarEExibirMensagemDeSucesso() {
        String initialUrl = driver.getCurrentUrl();

        waitUntilVisible(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        waitUntilVisible(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        waitUntilClickable(SUBMIT_BUTTON).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.not(ExpectedConditions.urlToBe(initialUrl)),
                ExpectedConditions.visibilityOfElementLocated(SUCCESS_MESSAGE)
        ));

        WebElement successMessage = waitUntilVisible(SUCCESS_MESSAGE);
        assertTrue(successMessage.getText().contains(EXPECTED_SUCCESS_TEXT));
    }
}