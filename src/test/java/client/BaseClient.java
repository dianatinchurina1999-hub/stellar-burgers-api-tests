package client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseClient {

    protected final RequestSpecification spec;

    public BaseClient() {
        String baseUri = System.getProperty("baseUri", "https://stellarburgers.education-services.ru");
        String basePath = System.getProperty("basePath", "/api");

        this.spec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setBasePath(basePath)
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addFilter(new AllureRestAssured())
                .build();
    }
}
