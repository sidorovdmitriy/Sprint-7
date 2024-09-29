import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    private static final String ORDER_CREATE_URL ="/api/v1/orders";

    @Step("Создать заказ")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDER_CREATE_URL)
                .then();
    }

    @Step("Получить список заказов")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getSpec())
                .when()
                .get(ORDER_CREATE_URL)
                .then();
    }
}
