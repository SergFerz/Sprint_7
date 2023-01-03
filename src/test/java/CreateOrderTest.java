import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private Order order = new Order("Naruto",
            "Uchiha",
            "Konoha, 142 apt.",
            4,
            "+7 800 355 35 35",
            5,
            "2020-06-06",
            "Saske, come back to Konoha",
            null);
    List<String> colors;

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] setColors() {
        return new Object[][]{
                {Arrays.asList("BLACK", "GREY")},
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {null}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        order.setColors(colors);
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void createOrderWithDifferentColors() {
        given()
                .header("Content-type", "application/json")
                .body(order)
                .post("/api/v1/orders")
                .then().statusCode(201)
                .and()
                .assertThat().body("track", notNullValue());
    }
}
