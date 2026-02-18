package tests;

import client.IngredientsClient;
import client.OrderClient;
import client.UserClient;
import constants.ApiMessages;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.CreateOrderRequest;
import model.CreateUserRequest;
import model.LoginRequest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTests {

    private static final IngredientsClient ingredientsClient = new IngredientsClient();
    private static final UserClient userClient = new UserClient();
    private static final OrderClient orderClient = new OrderClient();

    private static List<String> ingredientIds;
    private static String accessToken;

    @BeforeClass
    public static void init() {
        String ingredientsJson = ingredientsClient.getIngredients()
                .then().statusCode(200)
                .extract().asString();
        ingredientIds = from(ingredientsJson).getList("data._id", String.class);

        String email = "user_" + UUID.randomUUID() + "@mail.ru";
        String password = "123456";
        userClient.create(new CreateUserRequest(email, password, "Name"))
                .then().statusCode(200);

        accessToken = userClient.login(new LoginRequest(email, password))
                .then().statusCode(200)
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("POST /orders с токеном и валидными ингредиентами: 200, success=true, order.number != null.")
    public void createOrderWithAuthShouldReturn200() {
        orderClient.create(new CreateOrderRequest(ingredientIds.subList(0, 2)), accessToken)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("POST /orders без токена и с валидными ингредиентами: 200, success=true.")
    public void createOrderWithoutAuthShouldReturn200() {
        orderClient.create(new CreateOrderRequest(ingredientIds.subList(0, 2)), null)
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("POST /orders с валидными ингредиентами: 200, success=true.")
    public void createOrderWithIngredientsShouldReturn200() {
        orderClient.create(new CreateOrderRequest(ingredientIds.subList(0, 3)), null)
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("POST /orders без ingredients: 400, success=false, message='Ingredient ids must be provided'.")
    public void createOrderWithoutIngredientsShouldReturn400() {
        orderClient.create(new CreateOrderRequest(null), null)
                .then()
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo(ApiMessages.INGREDIENT_IDS_REQUIRED));
    }

    @Test
    @DisplayName("Создание заказа с неверными id ингредиентов")
    @Description("POST /orders с несуществующим id: 500, success=false, message про неверные id.")
    public void createOrderWithInvalidIngredientIdsShouldReturn500() {
        List<String> invalidIds = List.of("60d3463f7034a000269f45ff");

        orderClient.create(new CreateOrderRequest(invalidIds), null)
                .then()
                .statusCode(500)
                .body("success", is(false))
                .body("message", equalTo(ApiMessages.INVALID_INGREDIENT_IDS));
    }
}
