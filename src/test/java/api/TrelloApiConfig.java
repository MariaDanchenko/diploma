package api;

public final class TrelloApiConfig {

    public static final String API_KEY = System.getenv("TRELLO_API_KEY");
    public static final String TOKEN = System.getenv("TRELLO_TOKEN");
    public static final String BASE_URL = "https://api.trello.com/1";

    private TrelloApiConfig() {
    }

    public static void validateEnv() {
        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException("TRELLO_API_KEY is not set");
        }
        if (TOKEN == null || TOKEN.isBlank()) {
            throw new IllegalStateException("TRELLO_TOKEN is not set");
        }
    }
}
