import net.datafaker.Faker;

public class DataGenerator {
    static Faker faker = new Faker();

    public static Courier getRandom() {
        String login = faker.name().fullName();
        String password = String.valueOf(faker.password());
        String firstName = faker.name().fullName();

        return new Courier(firstName, login, password);
    }

    public static Order getRandomWithoutColor(String[] color) {
        String firstName = faker.name().toString();
        String lastName = faker.name().lastName();
        String address = faker.address().toString();
        String metroStation = faker.number().toString();
        String phone = faker.phoneNumber().toString();
        int rentTime = 12;
        String deliveryDate = "2024-08-15";
        String comment = "Тестовый комментарий";
        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}
