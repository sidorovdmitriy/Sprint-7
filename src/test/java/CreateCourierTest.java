import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateCourierTest {
    private Courier courier;
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Post-запрос к /api/v1/courier")
    public void createPositiveTest() {
        courier = DataGenerator.getRandom();
        ValidatableResponse response = courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(SC_OK, loginStatusCode);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_CREATED, statusCode);


        boolean isCreated = response.extract().path("ok");
        assertTrue(isCreated);

        courierId = loginResponse.extract().path("id");
        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Создание курьера с теми же кредами")
    @Description("Post-запрос к /api/v1/courier")
    public void createTwoSameCouriersTest() {
        Courier courierTest = new Courier(DataGenerator.getRandom().getFirstName(),
                DataGenerator.getRandom().getLogin(),
                DataGenerator.getRandom().getPassword());
        courierClient.create(courierTest);
        ValidatableResponse response = courierClient.create(courierTest);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courierTest));
        courierId = loginResponse.extract().path("id");

        String bodyAnswer = response.extract().path("message");
        assertEquals("Этот логин уже используется. Попробуйте другой.", bodyAnswer);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_CONFLICT, statusCode);
    }

    @Test
    @DisplayName("Создание курьера со значением null(login)")
    @Description("Post-запрос к /api/v1/courier")
    public void createWithNullLoginTest() {
        Courier courierTest = new Courier(DataGenerator.getRandom().getFirstName(),
                null,
                DataGenerator.getRandom().getPassword());
        ValidatableResponse response = courierClient.create(courierTest);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = response.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", bodyAnswer);
    }

    @Test
    @DisplayName("Создание курьера со значением null(password)")
    @Description("Post-запрос к /api/v1/courier")
    public void createWithNullPasswordTest() {
        Courier courierTest = new Courier(DataGenerator.getRandom().getFirstName(),
                DataGenerator.getRandom().getLogin(),
                null);
        ValidatableResponse response = courierClient.create(courierTest);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = response.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", bodyAnswer);
    }

    @Test
    @DisplayName("Создание курьера со значением null(First Name)")
    @Description("Post-запрос к /api/v1/courier")
    public void createWithNullFirstNameTest() {
        Courier courierTest = new Courier(null,
                DataGenerator.getRandom().getLogin(),
                DataGenerator.getRandom().getPassword());
        ValidatableResponse response = courierClient.create(courierTest);
        courierId = courierClient.login(CourierCredentials.from(courierTest)).extract().path("id");

        int statusCode = response.extract().statusCode();
        assertEquals(SC_CREATED, statusCode);

        boolean isCreated = response.extract().path("ok");
        assertTrue(isCreated);
    }

    @Test
    @DisplayName("Создание курьера с пустым логином")
    @Description("Post-запрос к /api/v1/courier")
    public void createWithEmptyLoginTest() {
        Courier courierTest = new Courier(DataGenerator.getRandom().getFirstName(),
                "",
                DataGenerator.getRandom().getPassword());
        ValidatableResponse response = courierClient.create(courierTest);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = response.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", bodyAnswer);
    }

    @Test
    @DisplayName("Создание курьера с пустым паролем")
    @Description("Post-запрос к /api/v1/courier")
    public void createWithEmptyPasswordTest() {
        Courier courierTest = new Courier(DataGenerator.getRandom().getFirstName(),
                DataGenerator.getRandom().getLogin(),
                "");
        ValidatableResponse response = courierClient.create(courierTest);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = response.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", bodyAnswer);
    }
}