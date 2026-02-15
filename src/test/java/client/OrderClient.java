package client;

import io.restassured.response.Response;
import model.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {

    public Response create(CreateOrderRequest body, String accessTokenOrNull) {
        if (accessTokenOrNull == null) {
            return given().spec(spec).body(body).post("/orders");
        }
        return given().spec(spec)
                .header("Authorization", accessTokenOrNull)
                .body(body)
                .post("/orders");
    }
}

