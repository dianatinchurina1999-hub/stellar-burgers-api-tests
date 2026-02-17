package api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public class StellarBurgersApi {

    private static final String API_HOST =
            System.getProperty("apiHost", "https://stellarburgers.education-services.ru");
    private static final String API_BASE_PATH =
            System.getProperty("apiBasePath", "/api");

    private static RestAssuredConfig timeoutsConfig() {

        int connectTimeoutMs = Integer.parseInt(System.getProperty("apiConnectTimeoutMs", "20000"));
        int socketTimeoutMs = Integer.parseInt(System.getProperty("apiSocketTimeoutMs", "30000"));
        int connManagerTimeoutMs = Integer.parseInt(System.getProperty("apiConnManagerTimeoutMs", "20000"));

        return RestAssuredConfig.config().httpClient(
                HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", connectTimeoutMs)
                        .setParam("http.socket.timeout", socketTimeoutMs)
                        .setParam("http.connection-manager.timeout", (long) connManagerTimeoutMs)
        );
    }

    public static RequestSpecification given() {
        return RestAssured.given()
                .config(timeoutsConfig())
                .baseUri(API_HOST)
                .basePath(API_BASE_PATH)
                .contentType(JSON)
                .filter(new AllureRestAssured());
    }

    public static String normalizeBearer(String token) {
        if (token == null || token.isBlank()) return null;
        return token.startsWith("Bearer ") ? token : "Bearer " + token;
    }
}

