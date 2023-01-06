import api.OrderApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest {

    @Test
    @DisplayName("Запрос списка заказов. В тело ответа возвращается список заказов.")
    public void getOrdersList() {
        Response response = OrderApi.getOrders();
        response
                .then().statusCode(SC_OK)
                .and()
                .assertThat().body("orders", notNullValue());
    }
}
