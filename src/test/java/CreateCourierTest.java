import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void tearDown() {
        deleteCourier();
    }

    @Test
    @DisplayName("Курьера можно создать")
    public void createCourierWithValidParametersAndReturnCode201WithBodyTrue() {
        courier = new Courier("Courier1001", "007", "Robert");
        Response response = postNewCourier(courier);
        response.then().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void createDuplicateCourierAndReturnCode409WithErrorMessage() {
        courier = new Courier("Courier1001", "007", "Robert");
        postNewCourier(courier)
                .then().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
        postNewCourier(courier).then().statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьеров с логином, который уже есть, возвращается ошибка")
    public void createDuplicateLoginCourierAndReturnCode409WithErrorMessage() {
        courier = new Courier("Courier1001", "007", "Robert");
        postNewCourier(courier)
                .then().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
        Courier courier2 = new Courier(courier.getLogin(), "001", "Rob");
        postNewCourier(courier2).then().statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Send POST for adding new courier")
    private Response postNewCourier(Courier courier) {
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    private void deleteCourier() {
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
