import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CreateInvalidCourierTest {
    private Courier courier;

    public CreateInvalidCourierTest(String login, String password) {
        this.courier = new Courier(login, password, "Robert");
    }

    @Parameterized.Parameters
    public static Object[][] init() {
        return new Object[][]{
                {null, "007"},
                {"Courier1007", null},
                {"", "007"},
                {"Courier1007", ""},
                {null, null}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Создание курьера невозможно без логина или пароля, а также с пустым логином или пустым паролем")
    public void createCourierWithoutLoginAndPasswordReturnCode400WithErrorMessage() {
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
