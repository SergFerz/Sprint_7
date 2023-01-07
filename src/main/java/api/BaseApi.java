package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApi {
    private static final String BASE_URI = "http://qa-scooter.praktikum-services.ru/";

    protected static RequestSpecification getRequestSpecification() {
        RestAssured.baseURI = BASE_URI;
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .log(LogDetail.ALL)
                .build();
    }
}
