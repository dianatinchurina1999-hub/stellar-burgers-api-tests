package tests;

import client.UserClient;
import model.CreateUserRequest;
import model.LoginRequest;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

public class UserLoginTests {

    private final UserClient userClient = new UserClient();

    @Test
    public void loginExistingUser_shouldReturn200() {
        String email = "user_" + UUID.randomUUID() + "@mail.ru";
        String password = "123456";

        userClient.create(new CreateUserRequest(email, password, "Name"))
                .then().statusCode(200);

        userClient.login(new LoginRequest(email, password))
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue());
    }

    @Test
    public void loginWithWrongCredentials_shouldReturn401() {
        userClient.login(new LoginRequest("nope@mail.ru", "wrong"))
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", not(isEmptyOrNullString()));
    }
}

