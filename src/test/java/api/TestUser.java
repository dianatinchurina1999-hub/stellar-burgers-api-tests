package api;

import java.util.UUID;

public class TestUser {
    private final String name;
    private final String email;
    private final String password;

    public TestUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static TestUser random() {
        String email = "user_" + UUID.randomUUID() + "@mail.ru";
        return new TestUser("Test", email, "123456");
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}

