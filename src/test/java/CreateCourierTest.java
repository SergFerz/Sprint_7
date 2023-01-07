import api.CourierApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private Courier courier;
    private static final String ERROR_MESSAGE_409 = "Этот логин уже используется. Попробуйте другой.";

    @Before
    public void setUp() {
        courier = new Courier("Courier1001", "007", "Robert");
    }

    @After
    public void tearDown() {
        CourierApi.deleteCourier(courier);
    }

    @Test
    @DisplayName("Курьера можно создать")
    public void createCourierWithValidParametersAndReturnCode201WithBodyTrue() {
        Response response = CourierApi.postNewCourier(courier);
        response.then().statusCode(SC_CREATED)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void createDuplicateCourierAndReturnCode409WithErrorMessage() {
        CourierApi.postNewCourier(courier)
                .then().statusCode(SC_CREATED)
                .and()
                .assertThat().body("ok", equalTo(true));
        CourierApi.postNewCourier(courier).then().statusCode(SC_CONFLICT)
                .and()
                .assertThat().body("message", equalTo(ERROR_MESSAGE_409));
    }

    @Test
    @DisplayName("Нельзя создать курьеров с логином, который уже есть, возвращается ошибка")
    public void createDuplicateLoginCourierAndReturnCode409WithErrorMessage() {
        CourierApi.postNewCourier(courier)
                .then().statusCode(SC_CREATED)
                .and()
                .assertThat().body("ok", equalTo(true));
        Courier courier2 = new Courier(courier.getLogin(), "001", "Rob");
        CourierApi.postNewCourier(courier2).then().statusCode(SC_CONFLICT)
                .and()
                .assertThat().body("message", equalTo(ERROR_MESSAGE_409));
    }
}
