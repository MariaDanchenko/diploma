package api;

import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthTests extends BaseAPITest {

    @Test
    public void testValidCredentials() {
        given(defaultApiSpec()).
                when().
                get("/members/me").
                then().
                statusCode(200).
                body("id", not(emptyString()));
    }

    @Test
    public void testInvalidCredentials() {
        given().
                spec(new RequestSpecBuilder().setBaseUri(TrelloApiConfig.BASE_URL).build()).
                queryParam("key", "invalidKey").
                queryParam("token", "invalidToken").
                when().
                get("/members/me").
                then().
                statusCode(401);
    }
}
