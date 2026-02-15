package client;

import io.restassured.response.Response;
import model.CreateUserRequest;
import model.LoginRequest;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {

    public Response create(CreateUserRequest body) {
        return given().spec(spec)
                .body(body)
                .when()
                .post("/auth/register");
    }

    public Response login(LoginRequest body) {
        return given().spec(spec)
                .body(body)
                .when()
                .post("/auth/login");
    }
}
