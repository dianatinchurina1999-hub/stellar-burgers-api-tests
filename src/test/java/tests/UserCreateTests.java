package tests;

import client.UserClient;
import constants.ApiMessages;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.CreateUserRequest;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.Callable;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

public class UserCreateTests {

    private final UserClient userClient = new UserClient();

    private static final int RETRY_ATTEMPTS = 3;
    private static final long RETRY_SLEEP_MS = 1000;

    private CreateUserRequest randomUser() {
        return new CreateUserRequest(
                "user_" + UUID.randomUUID() + "@mail.ru",
                "123456",
                "Test " + UUID.randomUUID()
        );
    }

    private <T> T withRetryOnConnectTimeout(Callable<T> action) {
        Throwable last = null;

        for (int attempt = 1; attempt <= RETRY_ATTEMPTS; attempt++) {
            try {
                return action.call();
            } catch (Throwable t) {
                last = t;

                String msg = String.valueOf(t.getMessage());
                boolean isConnectTimeout = msg.contains("Connection timed out")
                        || msg.contains("Connect timed out")
                        || (t.getClass().getName().contains("ConnectException"));

                if (!isConnectTimeout || attempt == RETRY_ATTEMPTS) {
                    if (t instanceof RuntimeException) throw (RuntimeException) t;
                    throw new RuntimeException(t);
                }

                try {
                    Thread.sleep(RETRY_SLEEP_MS);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    fail("Interrupted while waiting for retry");
                }
            }
        }

        throw new RuntimeException(last);
    }

    @Test
    @DisplayName("Создать уникального пользователя")
    @Description("POST /auth/register: 200, success=true, токены и user.* заполнены.")
    public void createUniqueUserShouldReturn200() {
        withRetryOnConnectTimeout(() -> {
            userClient.create(randomUser())
                    .then()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("accessToken", startsWith("Bearer "))
                    .body("refreshToken", notNullValue())
                    .body("user.email", notNullValue())
                    .body("user.name", notNullValue());
            return null;
        });
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    @Description("Повторная регистрация того же пользователя: 403, success=false, message='User already exists'.")
    public void createExistingUserShouldReturn403() {
        CreateUserRequest user = randomUser();

        withRetryOnConnectTimeout(() -> {
            userClient.create(user).then().statusCode(200);
            return null;
        });

        withRetryOnConnectTimeout(() -> {
            userClient.create(user)
                    .then()
                    .statusCode(403)
                    .body("success", is(false))
                    .body("message", equalTo(ApiMessages.USER_ALREADY_EXISTS));
            return null;
        });
    }

    @Test
    @DisplayName("Создать пользователя без email")
    @Description("POST /auth/register без email: 403, success=false, message про обязательные поля.")
    public void createUserWithoutEmailShouldReturn403() {
        CreateUserRequest invalid = new CreateUserRequest(null, "123456", "Name");

        withRetryOnConnectTimeout(() -> {
            userClient.create(invalid)
                    .then()
                    .statusCode(403)
                    .body("success", is(false))
                    .body("message", equalTo(ApiMessages.REQUIRED_FIELDS));
            return null;
        });
    }

    @Test
    @DisplayName("Создать пользователя без password")
    @Description("POST /auth/register без password: 403, success=false, message про обязательные поля.")
    public void createUserWithoutPasswordShouldReturn403() {
        CreateUserRequest invalid = new CreateUserRequest("user_" + UUID.randomUUID() + "@mail.ru", null, "Name");

        withRetryOnConnectTimeout(() -> {
            userClient.create(invalid)
                    .then()
                    .statusCode(403)
                    .body("success", is(false))
                    .body("message", equalTo(ApiMessages.REQUIRED_FIELDS));
            return null;
        });
    }

    @Test
    @DisplayName("Создать пользователя без name")
    @Description("POST /auth/register без name: 403, success=false, message про обязательные поля.")
    public void createUserWithoutNameShouldReturn403() {
        CreateUserRequest invalid = new CreateUserRequest("user_" + UUID.randomUUID() + "@mail.ru", "123456", null);

        withRetryOnConnectTimeout(() -> {
            userClient.create(invalid)
                    .then()
                    .statusCode(403)
                    .body("success", is(false))
                    .body("message", equalTo(ApiMessages.REQUIRED_FIELDS));
            return null;
        });
    }
}
