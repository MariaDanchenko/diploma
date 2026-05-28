package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;

public class BaseAPITest {

    protected static final String API_KEY = TrelloApiConfig.API_KEY;
    protected static final String TOKEN = TrelloApiConfig.TOKEN;

    protected static RequestSpecification defaultApiSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(TrelloApiConfig.BASE_URL)
                .addQueryParam("key", API_KEY)
                .addQueryParam("token", TOKEN)
                .build();
    }

    @BeforeSuite
    public void validateEnv() {
        TrelloApiConfig.validateEnv();
    }
}
