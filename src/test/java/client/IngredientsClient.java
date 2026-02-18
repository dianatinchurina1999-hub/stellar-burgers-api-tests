package client;

import constants.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends BaseClient {

    @Step("Get ingredients")
    public Response getIngredients() {
        return given()
                .spec(spec)
                .get(Endpoints.INGREDIENTS);
    }
}

