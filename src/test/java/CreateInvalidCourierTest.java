import api.CourierApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CreateInvalidCourierTest {
    private Courier courier;
    private static final String ERROR_MESSAGE_400 = "Недостаточно данных для создания учетной записи";

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

    @Test
    @DisplayName("Создание курьера невозможно без логина или пароля, а также с пустым логином или пустым паролем")
    public void createCourierWithoutLoginAndPasswordReturnCode400WithErrorMessage() {
        Response response = CourierApi.postNewCourier(courier);
        response.then().assertThat().statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo(ERROR_MESSAGE_400));
    }
}
