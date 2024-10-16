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
        courier = DataGenerator.getRandom();
        Courier courierTest = new Courier(DataGenerator.getRandom().getFirstName(),
                DataGenerator.getRandom().getLogin(),
                DataGenerator.getRandom().getPassword());
        ValidatableResponse response = courierClient.create(courierTest);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_CREATED, statusCode);

        ValidatableResponse loginResponse = courierClient.login(courierTest.getLogin(), courierTest.getPassword());

        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(SC_OK, loginStatusCode);

        courierId = loginResponse.extract().path("id");
        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Авторизация с пустым логином")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierEmptyLoginTest() {
        ValidatableResponse loginResponse = courierClient.login("", "");
        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Недостаточно данных для входа", bodyAnswer);
    }

    @Test
    @DisplayName("Авторизация с пустым паролем")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierEmptyPasswordTest() {
        ValidatableResponse loginResponse = courierClient.login("PETR1234", "");
        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Недостаточно данных для входа", bodyAnswer);
    }

    @Test
    @DisplayName("Авторизация со значением null(login)")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierNullLoginTest() {
        ValidatableResponse loginResponse = courierClient.login("", courier.getPassword());
        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Недостаточно данных для входа", bodyAnswer);
    }

    @Test
    @DisplayName("Авторизация с некорректным логином")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierWrongLoginTest() {
        ValidatableResponse loginResponse = courierClient.login(courier.getLogin(), courier.getLogin());
        int statusCode = loginResponse.extract().statusCode();

        assertEquals(SC_NOT_FOUND, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", bodyAnswer);
    }

    @Test
    @DisplayName("Авторизация с некорректным паролем")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierWrongPasswordTest() {
        ValidatableResponse loginResponse = courierClient.login(courier.getLogin(), "passw1234");
        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_NOT_FOUND, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", bodyAnswer);
    }

    @Test
    @DisplayName("Авторизация под несуществующим пользователем")
    @Description("Post-запрос к /api/v1/courier/login")
    public void courierThatNotExists() {
        ValidatableResponse loginResponse = courierClient.login("IvanD1234", "Paasw123");
        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_NOT_FOUND, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", bodyAnswer);
    }
}
