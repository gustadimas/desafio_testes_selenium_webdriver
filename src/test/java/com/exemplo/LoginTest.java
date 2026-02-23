package com.exemplo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
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
    static final By ERROR_MESSAGE = By.cssSelector(
            System.getProperty("login.error.selector", ".error-message")
    );

    static final String VALID_EMAIL = System.getProperty("login.valid.email", "usuario@exemplo.com");
    static final String VALID_PASSWORD = System.getProperty("login.valid.password", "senha123");
    static final String INVALID_EMAIL = System.getProperty("login.invalid.email", "invalido@exemplo.com");
    static final String INVALID_PASSWORD = System.getProperty("login.invalid.password", "senhaErrada");
    static final String EXPECTED_SUCCESS_TEXT = System.getProperty(
            "login.success.text",
            "Login realizado com sucesso"
    );
    static final String EXPECTED_ERROR_TEXT = System.getProperty(
            "login.error.text",
            "E-mail ou senha inv\u00E1lidos."
    );
    static final String SUCCESS_URL_CONTAINS = System.getProperty("login.success.url.contains", "");

    @Test
    void givenCredenciaisValidas_whenSubmeterLogin_thenDeveRedirecionarEExibirMensagemDeSucesso() {
        String initialUrl = driver.getCurrentUrl();

        fillField(EMAIL_INPUT, VALID_EMAIL);
        fillField(PASSWORD_INPUT, VALID_PASSWORD);
        waitUntilClickable(SUBMIT_BUTTON).click();

        wait.until(ExpectedConditions.or(
                expectedSuccessNavigation(initialUrl),
                ExpectedConditions.visibilityOfElementLocated(SUCCESS_MESSAGE)
        ));

        WebElement successMessage = waitUntilVisible(SUCCESS_MESSAGE);
        assertTrue(successMessage.isDisplayed(), "Mensagem de sucesso deve estar visivel");
        assertTrue(
                successMessage.getText().contains(EXPECTED_SUCCESS_TEXT),
                "Texto de sucesso inesperado"
        );
    }

    @Test
    void givenCredenciaisInvalidas_whenSubmeterLogin_thenDeveExibirErroEContinuarNaTelaDeLogin() {
        String initialUrl = driver.getCurrentUrl();

        fillField(EMAIL_INPUT, INVALID_EMAIL);
        fillField(PASSWORD_INPUT, INVALID_PASSWORD);
        waitUntilClickable(SUBMIT_BUTTON).click();

        WebElement errorMessage = waitUntilVisible(ERROR_MESSAGE);
        assertEquals(EXPECTED_ERROR_TEXT, errorMessage.getText().trim(), "Mensagem de erro incorreta");

        boolean sameUrl = driver.getCurrentUrl().equals(initialUrl);
        boolean loginStillVisible = waitUntilVisible(EMAIL_INPUT).isDisplayed();
        assertTrue(sameUrl || loginStillVisible, "Tela de login deveria continuar visivel");
    }

    ExpectedCondition<?> expectedSuccessNavigation(String initialUrl) {
        if (!SUCCESS_URL_CONTAINS.isEmpty()) {
            return ExpectedConditions.urlContains(SUCCESS_URL_CONTAINS);
        }
        return ExpectedConditions.not(ExpectedConditions.urlToBe(initialUrl));
    }

    void fillField(By locator, String value) {
        WebElement field = waitUntilVisible(locator);
        field.clear();
        field.sendKeys(value);
    }
}