package tests;

import client.BaseClient;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class IngredientsSmokeTest extends BaseClient {

    @Test
    public void getIngredients_shouldReturn200AndSuccessTrue() {
        given()
                .spec(spec)
                .when()
                .get("/ingredients")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("success", is(true))
                .body("data", notNullValue());
    }
}


