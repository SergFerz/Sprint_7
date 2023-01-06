package api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Order;

public class OrderApi extends BaseApi {

    private static final String CREATE_ORDER_URL = "/api/v1/orders";

    @Step("Send POST for creating new order to /api/v1/orders")
    public static Response postNewOrder(Order order) {
        Response response = RestAssured.given()
                .spec(getRequestSpecification())
                .body(order)
                .when()
                .post(CREATE_ORDER_URL);
        return response;
    }

    @Step("Send GET for getting all orders to /api/v1/orders")
    public static Response getOrders() {
        Response response = RestAssured.given()
                .spec(getRequestSpecification())
                .when()
                .get(CREATE_ORDER_URL);
        return response;
    }
}
