public class  Courier {
    private String firstName;
    private String login;
    private String password;

    public Courier(String firstName, String login, String password) {
        this.firstName = firstName;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return String.format("Курьер: логин - %s, пароль - %s, имя - %s", this.login, this.password, this.firstName);
    }
}