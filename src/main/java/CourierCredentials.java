public class CourierCredentials {
    private final String PASSWORD;
    private final String LOGIN;

    public CourierCredentials(String login, String password) {
        this.LOGIN = login;
        this.PASSWORD = password;
    }

    public static CourierCredentials from(Courier courier) {
        return new CourierCredentials(courier.getLogin(), courier.getPassword());
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public String getLOGIN() {
        return LOGIN;
    }

    @Override
    public String toString() {
        return String.format("Курьер: логин - %s, пароль - %s", this.LOGIN, this.PASSWORD);
    }
}
