package api;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class BaseAPITest {

    protected static final String API_KEY = System.getenv("TRELLO_API_KEY");
    protected static final String TOKEN = System.getenv("TRELLO_TOKEN");
    protected static final String BASE_URL = "https://api.trello.com/1";

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeClass
    public void validateEnv() {
        if (API_KEY == null || TOKEN == null) {
            throw new IllegalStateException("API_KEY or TOKEN is not set");
        }
    }
}
