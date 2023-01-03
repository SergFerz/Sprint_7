import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Запрос списка заказов. В тело ответа возвращается список заказов.")
    public void getOrdersList() {
        given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders")
                .then().statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue());
    }
}
