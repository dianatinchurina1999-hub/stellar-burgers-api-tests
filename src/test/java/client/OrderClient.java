package client;

import constants.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {

    @Step("Create order (authorized={authorized})")
    public Response create(CreateOrderRequest body, String accessTokenOrNull) {
        boolean authorized = accessTokenOrNull != null;

        if (!authorized) {
            return given()
                    .spec(spec)
                    .body(body)
                    .post(Endpoints.ORDERS);
        }

        return given()
                .spec(spec)
                .header("Authorization", accessTokenOrNull)
                .body(body)
                .post(Endpoints.ORDERS);
    }
}


