package api;

public final class TrelloApiConfig {

    public static final String API_KEY = System.getenv("TRELLO_API_KEY");
    public static final String TOKEN = System.getenv("TRELLO_TOKEN");
    public static final String BASE_URL = "https://api.trello.com/1";

    private TrelloApiConfig() {
    }

    public static void validateEnv() {
        if (API_KEY == null || TOKEN == null) {
            throw new IllegalStateException("API_KEY or TOKEN is not set");
        }
    }
}
