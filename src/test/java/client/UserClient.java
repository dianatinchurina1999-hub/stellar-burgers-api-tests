package client;

import constants.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.CreateUserRequest;
import model.LoginRequest;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {

    @Step("Create user")
    public Response create(CreateUserRequest body) {
        return given()
                .spec(spec)
                .body(body)
                .post(Endpoints.REGISTER);
    }

    @Step("Login user")
    public Response login(LoginRequest body) {
        return given()
                .spec(spec)
                .body(body)
                .post(Endpoints.LOGIN);
    }
}
