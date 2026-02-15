package tests;

import org.junit.Before;
import org.junit.Test;
import pages.ForgotPasswordPage;
import pages.LoginPage;
import pages.MainPage;
import pages.RegisterPage;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class LoginTests extends BaseUiTest {

    private String email;
    private final String password = "123456";

    @Before
    public void registerUser() {
        driver.get("https://stellarburgers.education-services.ru/register");

        email = "user_" + UUID.randomUUID() + "@mail.ru";
        new RegisterPage(driver).register("Test", email, password);

        assertTrue("После регистрации должны быть на /login",
                driver.getCurrentUrl().contains("/login"));
    }

    @Test
    public void loginFromMainButton() {
        driver.get("https://stellarburgers.education-services.ru/");
        new MainPage(driver).clickLoginButton();
        new LoginPage(driver).login(email, password);

        assertTrue(driver.getCurrentUrl().contains("/"));
    }

    @Test
    public void loginFromPersonalAccount() {
        driver.get("https://stellarburgers.education-services.ru/");
        new MainPage(driver).clickPersonalAccount();
        new LoginPage(driver).login(email, password);

        assertTrue(driver.getCurrentUrl().contains("/"));
    }

    @Test
    public void loginFromRegisterForm() {
        driver.get("https://stellarburgers.education-services.ru/register");
        new RegisterPage(driver).clickLoginLink();
        new LoginPage(driver).login(email, password);

        assertTrue(driver.getCurrentUrl().contains("/"));
    }

    @Test
    public void loginFromForgotPasswordForm() {
        driver.get("https://stellarburgers.education-services.ru/login");
        new LoginPage(driver).clickForgotPasswordLink();

        new ForgotPasswordPage(driver).clickLoginLink();
        new LoginPage(driver).login(email, password);

        assertTrue(driver.getCurrentUrl().contains("/"));
    }
}

