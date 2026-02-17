package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static api.StellarBurgersApi.given;
import static api.StellarBurgersApi.normalizeBearer;

public class UserApi {

    private static final int MAX_ATTEMPTS = Integer.parseInt(System.getProperty("apiRetryAttempts", "3"));
    private static final long RETRY_SLEEP_MS = Long.parseLong(System.getProperty("apiRetrySleepMs", "1500"));

    private <T> T withRetry(String actionName, Supplier<T> action) {
        RuntimeException last = null;

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                return action.get();
            } catch (RuntimeException e) {
                last = e;
                if (attempt == MAX_ATTEMPTS) break;
                sleep(Duration.ofMillis(RETRY_SLEEP_MS));
            }
        }
        throw new RuntimeException(actionName + " failed after " + MAX_ATTEMPTS + " attempts", last);
    }

    private void sleep(Duration d) {
        try {
            Thread.sleep(d.toMillis());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    @Step("API: создать пользователя")
    public Response createUser(TestUser user) {
        return withRetry("createUser", () -> {
            Map<String, Object> body = new HashMap<>();
            body.put("email", user.getEmail());
            body.put("password", user.getPassword());
            body.put("name", user.getName());

            Response resp = given()
                    .body(body)
                    .when()
                    .post("/auth/register");

            int code = resp.getStatusCode();
            
            if (code == 200 || code == 403) {
                return resp;
            }
            throw new RuntimeException("Unexpected status code in createUser: " + code);
        });
    }

    @Step("API: логин пользователя")
    public String loginAndGetAccessToken(TestUser user) {
        return withRetry("login", () -> {
            Map<String, Object> body = new HashMap<>();
            body.put("email", user.getEmail());
            body.put("password", user.getPassword());

            Response resp = given()
                    .body(body)
                    .when()
                    .post("/auth/login");

            int code = resp.getStatusCode();
            if (code != 200) {
                throw new RuntimeException("Unexpected status code in login: " + code);
            }

            String token = resp.then().extract().path("accessToken");
            String bearer = normalizeBearer(token);
            if (bearer == null) {
                throw new RuntimeException("accessToken is null/blank");
            }
            return bearer;
        });
    }

    @Step("API: удалить пользователя")
    public Response deleteUser(String accessToken) {

        return given()
                .header("Authorization", accessToken)
                .when()
                .delete("/auth/user");
    }
}

