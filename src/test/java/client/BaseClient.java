package client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.RestAssuredConfig.config;

public class BaseClient {

    protected final RequestSpecification spec;

    public BaseClient() {
        String baseUri = System.getProperty("baseUri", "https://stellarburgers.education-services.ru");
        String basePath = System.getProperty("basePath", "/api");

        RestAssuredConfig raConfig = config()
                .httpClient(httpClientConfig()
                        .setParam("http.connection.timeout", 60000)
                        .setParam("http.socket.timeout", 60000)
                        .setParam("http.connection-manager.timeout", 60000));

        this.spec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setBasePath(basePath)
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addFilter(new AllureRestAssured())
                .setConfig(raConfig)
                .build();
    }
}

