package tests;

import org.junit.Test;
import pages.RegisterPage;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class RegistrationTests extends BaseUiTest {

    @Test
    public void successfulRegistration() {
        driver.get("https://stellarburgers.education-services.ru/register");

        RegisterPage register = new RegisterPage(driver);
        String email = "user_" + UUID.randomUUID() + "@mail.ru";

        register.register("Test", email, "123456");

        assertTrue("Должны оказаться на /login после регистрации",
                driver.getCurrentUrl().contains("/login"));
    }

    @Test
    public void registrationWithShortPassword_shouldShowError() {
        driver.get("https://stellarburgers.education-services.ru/register");

        RegisterPage register = new RegisterPage(driver);
        String email = "user_" + UUID.randomUUID() + "@mail.ru";


        register.register("Test", email, "12345");

        assertTrue("Должна показаться ошибка некорректного пароля",
                register.isPasswordErrorVisible());
    }
}
