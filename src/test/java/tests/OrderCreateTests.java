package tests;

import client.OrderClient;
import client.UserClient;
import model.CreateOrderRequest;
import model.CreateUserRequest;
import model.LoginRequest;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.path.json.JsonPath.from;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderCreateTests extends client.BaseClient {

    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();

    private List<String> ingredientIds() {
        String json = given().spec(spec)
                .when().get("/ingredients")
                .then().statusCode(200)
                .extract().asString();

        return from(json).getList("data._id");
    }

    private String createUserAndGetToken() {
        String email = "user_" + UUID.randomUUID() + "@mail.ru";
        String password = "123456";

        userClient.create(new CreateUserRequest(email, password, "Name"))
                .then().statusCode(200);

        return userClient.login(new LoginRequest(email, password))
                .then().statusCode(200)
                .extract().path("accessToken");
    }

    @Test
    public void createOrder_withAuth_shouldReturn200() {
        String token = createUserAndGetToken();
        List<String> ids = ingredientIds();

        orderClient.create(new CreateOrderRequest(ids.subList(0, 2)), token)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrder_withoutAuth_shouldReturn200() {
        List<String> ids = ingredientIds();

        orderClient.create(new CreateOrderRequest(ids.subList(0, 2)), null)
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void createOrder_withIngredients_shouldReturn200() {
        List<String> ids = ingredientIds();

        orderClient.create(new CreateOrderRequest(ids.subList(0, 3)), null)
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void createOrder_withoutIngredients_shouldReturn400() {
        orderClient.create(new CreateOrderRequest(null), null)
                .then()
                .statusCode(400)
                .body("success", is(false));
    }

    @Test
    public void createOrder_withInvalidIngredientHash_shouldReturnError() {
        orderClient.create(new CreateOrderRequest(List.of("invalid_hash")), null)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }
}

