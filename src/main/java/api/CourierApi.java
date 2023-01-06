package api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import model.CourierLogin;

public class CourierApi extends BaseApi {

    private static final String CREATE_COURIER_URL = "/api/v1/courier";
    private static final String LOGIN_COURIER_URL = "/api/v1/courier/login";

    @Step("Send POST for adding new courier")
    public static Response postNewCourier(Courier courier) {
        Response response = RestAssured.given()
                .spec(getRequestSpecification())
                .body(courier)
                .when()
                .post(CREATE_COURIER_URL);
        return response;
    }

    @Step("Send POST for login courier")
    public static Response loginNewCourier(CourierLogin courierLogin) {
        Response response = RestAssured.given()
                .spec(getRequestSpecification())
                .body(courierLogin)
                .when()
                .post(LOGIN_COURIER_URL);
        return response;
    }

    @Step("Send DELETE for deleting courier")
    public static void deleteCourier(Courier courier) {
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        int id = RestAssured.given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .path("id");
        RestAssured.given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + id);
    }
}
