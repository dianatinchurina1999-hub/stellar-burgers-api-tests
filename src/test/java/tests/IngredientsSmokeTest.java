package tests;

import client.IngredientsClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class IngredientsSmokeTest {

    private final IngredientsClient ingredientsClient = new IngredientsClient();

    @Test
    @DisplayName("GET /ingredients возвращает 200")
    @Description("Проверка: success=true, data не null, Content-Type JSON.")
    public void getIngredientsShouldReturn200AndSuccessTrue() {
        ingredientsClient.getIngredients()
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("success", is(true))
                .body("data", notNullValue());
    }
}


