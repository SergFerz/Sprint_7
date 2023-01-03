import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {
    private Courier courier = new Courier("Courier1001", "007", "Robert");

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        postNewCourier();
    }

    @After
    public void tearDown() {
        deleteCourier();
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void createCourierLoginWithValidParametersAndReturnCode200WithId() {
        Response response = loginNewCourier(new CourierLogin(courier.getLogin(), courier.getPassword()));
        response.then().assertThat().statusCode(200).and().body("id", notNullValue());
    }

    @Test
    @DisplayName("Курьер не может авторизоваться без логина")
    public void createCourierLoginWithEmptyLoginAndReturnCode400WithErrorMessage() {
        Response response = loginNewCourier(new CourierLogin("", courier.getPassword()));
        response.then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться без пароля")
    public void createCourierLoginWithEmptyPasswordAndReturnCode400WithErrorMessage() {
        Response response = loginNewCourier(new CourierLogin(courier.getLogin(), ""));
        response.then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с неправильным паролем")
    public void createCourierLoginWithInvalidPasswordAndReturnCode404WithErrorMessage() {
        Response response = loginNewCourier(new CourierLogin(courier.getLogin(), "fake_password"));
        response.then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с неправильным логином")
    public void createCourierLoginWithInvalidLoginAndReturnCode404WithErrorMessage() {
        Response response = loginNewCourier(new CourierLogin("fake_login", courier.getPassword()));
        response.then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться под несуществующим пользователем")
    public void createCourierLoginWithFakeLoginAndReturnCode404WithErrorMessage() {
        Response response = loginNewCourier(new CourierLogin("fake_login", "fake_login"));
        response.then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Send POST for adding new courier")
    private Response postNewCourier() {
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Send POST for login courier")
    private Response loginNewCourier(CourierLogin courierLogin) {
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login");
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
