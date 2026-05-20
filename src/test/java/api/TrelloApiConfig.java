package api;

import org.testng.SkipException;

public final class TrelloApiConfig {

    public static final String API_KEY = System.getenv("TRELLO_API_KEY");
    public static final String TOKEN = System.getenv("TRELLO_TOKEN");
    public static final String BASE_URL = "https://api.trello.com/1";

    private TrelloApiConfig() {
    }

    public static void validateEnv() {
        if (API_KEY == null || API_KEY.isBlank() || TOKEN == null || TOKEN.isBlank()) {
            throw new SkipException("Environment not configured: TRELLO_API_KEY and TRELLO_TOKEN are required");
        }
    }

    public static void validateUiCredentials() {
        String email = System.getenv("TRELLO_EMAIL");
        String password = System.getenv("TRELLO_PASSWORD");
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new SkipException("Environment not configured: TRELLO_EMAIL and TRELLO_PASSWORD are required");
        }
    }
}
