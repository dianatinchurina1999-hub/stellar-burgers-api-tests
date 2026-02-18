package tests;

import client.UserClient;
import constants.ApiMessages;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.CreateUserRequest;
import model.LoginRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

public class UserLoginTests {

    private final UserClient userClient = new UserClient();

    private static boolean userCreated = false;
    private static String email;
    private static String password;

    @Before
    public void setUp() {
        if (userCreated) {
            return;
        }

        email = "user_" + UUID.randomUUID() + "@mail.ru";
        password = "123456";

        int attempts = 3;
        for (int i = 1; i <= attempts; i++) {
            try {
                userClient.create(new CreateUserRequest(email, password, "Name"))
                        .then()
                        .statusCode(200);

                userCreated = true;
                return;
            } catch (Exception e) {
                if (i == attempts) {
                    throw e;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("POST /auth/login с валидными данными: 200, success=true, токены присутствуют.")
    public void loginExistingUserShouldReturn200() {
        userClient.login(new LoginRequest(email, password))
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Вход с неверным логином")
    @Description("POST /auth/login с неверным email: 401, success=false.")
    public void loginWithWrongEmailShouldReturn401() {
        userClient.login(new LoginRequest("nope_" + UUID.randomUUID() + "@mail.ru", password))
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo(ApiMessages.WRONG_CREDENTIALS));
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("POST /auth/login с неверным password: 401, success=false.")
    public void loginWithWrongPasswordShouldReturn401() {
        userClient.login(new LoginRequest(email, "wrong_password"))
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo(ApiMessages.WRONG_CREDENTIALS));
    }
}
