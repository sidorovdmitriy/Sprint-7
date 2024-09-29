import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Client {
    private static final String BASE_SCOOTER_URL = "https://qa-scooter.praktikum-services.ru";

    protected RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_SCOOTER_URL)
                .setContentType(ContentType.JSON)
                .build();
    }
}