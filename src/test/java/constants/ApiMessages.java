package constants;

public class ApiMessages {

    public static final String REQUIRED_FIELDS = "Email, password and name are required fields";
    public static final String INGREDIENT_IDS_REQUIRED = "Ingredient ids must be provided";

    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String WRONG_CREDENTIALS = "email or password are incorrect";
    public static final String INVALID_INGREDIENT_IDS = "One or more ids provided are incorrect";

    private ApiMessages() {
    }
}

