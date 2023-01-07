import api.CourierApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import model.CourierLogin;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {
    private Courier courier;
    private static final String ERROR_MESSAGE_400 = "Недостаточно данных для входа";
    private static final String ERROR_MESSAGE_404 = "Учетная запись не найдена";

    @After
    public void tearDown() {
        CourierApi.deleteCourier(courier);
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void createCourierLoginWithValidParametersAndReturnCode200WithId() {
        courier = new Courier("Courier1001", "007", "Robert");
        CourierApi.postNewCourier(courier);
        Response response = CourierApi.loginNewCourier(new CourierLogin(courier.getLogin(), courier.getPassword()));
        response.then().assertThat().statusCode(SC_OK).and().body("id", notNullValue());
    }

    @Test
    @DisplayName("Курьер не может авторизоваться без логина")
    public void createCourierLoginWithEmptyLoginAndReturnCode400WithErrorMessage() {
        courier = new Courier("Courier1001", "007", "Robert");
        CourierApi.postNewCourier(courier);
        Response response = CourierApi.loginNewCourier(new CourierLogin("", courier.getPassword()));
        response.then().statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo(ERROR_MESSAGE_400));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться без пароля")
    public void createCourierLoginWithEmptyPasswordAndReturnCode400WithErrorMessage() {
        courier = new Courier("Courier1001", "007", "Robert");
        CourierApi.postNewCourier(courier);
        Response response = CourierApi.loginNewCourier(new CourierLogin(courier.getLogin(), ""));
        response.then().statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo(ERROR_MESSAGE_400));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с неправильным паролем")
    public void createCourierLoginWithInvalidPasswordAndReturnCode404WithErrorMessage() {
        courier = new Courier("Courier1001", "007", "Robert");
        CourierApi.postNewCourier(courier);
        Response response = CourierApi.loginNewCourier(new CourierLogin(courier.getLogin(), "fake_password"));
        response.then().statusCode(SC_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo(ERROR_MESSAGE_404));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с неправильным логином")
    public void createCourierLoginWithInvalidLoginAndReturnCode404WithErrorMessage() {
        courier = new Courier("Courier1001", "007", "Robert");
        CourierApi.postNewCourier(courier);
        Response response = CourierApi.loginNewCourier(new CourierLogin("fake_login", courier.getPassword()));
        response.then().statusCode(SC_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo(ERROR_MESSAGE_404));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться под несуществующим пользователем")
    public void createCourierLoginWithFakeLoginAndReturnCode404WithErrorMessage() {
        courier = new Courier("Courier1001", "007", "Robert");
        CourierApi.postNewCourier(courier);
        Response response = CourierApi.loginNewCourier(new CourierLogin("fake_login", "fake_login"));
        response.then().statusCode(SC_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo(ERROR_MESSAGE_404));
    }
}
