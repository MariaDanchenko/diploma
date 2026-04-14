package api;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class BaseAPITest {

    protected static final String API_KEY = TrelloApiConfig.API_KEY;
    protected static final String TOKEN = TrelloApiConfig.TOKEN;
    protected static final String BASE_URL = TrelloApiConfig.BASE_URL;

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeClass
    public void validateEnv() {
        TrelloApiConfig.validateEnv();
    }
}
