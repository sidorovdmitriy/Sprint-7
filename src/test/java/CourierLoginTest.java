import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CourierLoginTest {
    private Courier courier;
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courier = DataGenerator.getRandom();
        courierClient = new CourierClient();
        courierClient.create(courier);
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Успешная авторизация")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierValidLoginTest() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_OK, statusCode);

        courierId = loginResponse.extract().path("id");
        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Авторизация с пустым логином")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierEmptyLoginTest() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials("", courier.getPassword()));
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Недостаточно данных для входа", bodyAnswer);
    }

    @Test
    @DisplayName("Авторизация с пустым паролем")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierEmptyPasswordTest() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), ""));
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Недостаточно данных для входа", bodyAnswer);
    }

    @Test
    @DisplayName("Авторизация со значением null(login)")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierNullLoginTest() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(null, courier.getPassword()));
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Недостаточно данных для входа", bodyAnswer);
    }

    @Test
    @DisplayName("Авторизация с некорректным логином")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierWrongLoginTest() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials("PETR1234", courier.getPassword()));
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_NOT_FOUND, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", bodyAnswer);
    }

    @Test
    @DisplayName("Авторизация с некорректным паролем")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierWrongPasswordTest() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), "passw1234"));
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_NOT_FOUND, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", bodyAnswer);
    }

    @Test
    @DisplayName("Авторизация под несуществующим пользователем")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierThatNotExists() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials("IvanD1234", "Paasw123"));
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_NOT_FOUND, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", bodyAnswer);
    }
}
