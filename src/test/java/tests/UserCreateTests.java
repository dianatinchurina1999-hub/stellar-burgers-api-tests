package tests;

import client.UserClient;
import model.CreateUserRequest;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

public class UserCreateTests {

    private final UserClient userClient = new UserClient();

    private CreateUserRequest randomUser() {
        return new CreateUserRequest(
                "user_" + UUID.randomUUID() + "@mail.ru",
                "123456",
                "Test " + UUID.randomUUID()
        );
    }

    @Test
    public void createUniqueUser_shouldReturn200() {
        userClient.create(randomUser())
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue())
                .body("user.email", notNullValue())
                .body("user.name", notNullValue());
    }

    @Test
    public void createExistingUser_shouldReturn403() {
        CreateUserRequest user = randomUser();
        userClient.create(user).then().statusCode(200);

        userClient.create(user)
                .then()
                .statusCode(403)
                .body("success", is(false))
                .body("message", not(isEmptyOrNullString()));
    }

    @Test
    public void createUserWithoutRequiredField_shouldReturn403() {

        CreateUserRequest invalid = new CreateUserRequest(null, "123456", "Name");

        userClient.create(invalid)
                .then()
                .statusCode(403)
                .body("success", is(false))
                .body("message", not(isEmptyOrNullString()));
    }
}

