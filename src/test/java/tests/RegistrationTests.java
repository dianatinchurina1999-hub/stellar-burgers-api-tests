package tests;

import api.TestUser;
import api.UserApi;
import io.qameta.allure.Allure;
import org.junit.Test;
import pages.RegisterPage;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

public class RegistrationTests extends BaseUiTest {

    private final UserApi userApi = new UserApi();

    @Test
    public void successfulRegistrationShouldWork() {
        driver.get(uiBaseUrl + "register");

        TestUser user = TestUser.random();
        new RegisterPage(driver).register(user.getName(), user.getEmail(), user.getPassword());

        assertTrue("После регистрации должны оказаться на /login",
                driver.getCurrentUrl().contains("/login"));


        try {
            String token = userApi.loginAndGetAccessToken(user);
            if (token != null) {
                userApi.deleteUser(token);
            }
        } catch (Exception e) {
            String msg = "Cleanup failed (API timeout?): " + e.getClass().getSimpleName() + ": " + e.getMessage();
            Allure.addAttachment("User cleanup error", "text/plain",
                    new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8)), ".txt");
        }
    }

    @Test
    public void registrationWithShortPasswordShouldShowError() {
        driver.get(uiBaseUrl + "register");

        TestUser user = new TestUser(
                "Test",
                "user_" + java.util.UUID.randomUUID() + "@mail.ru",
                "12345"
        );

        RegisterPage register = new RegisterPage(driver);
        register.register(user.getName(), user.getEmail(), user.getPassword());

        assertTrue("Должна показаться ошибка некорректного пароля",
                register.isPasswordErrorVisible());
    }
}
