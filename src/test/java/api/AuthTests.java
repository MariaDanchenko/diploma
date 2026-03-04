package api;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthTests extends BaseTest {

    @Test
    public void testValidCredentials() {
        given().
                queryParam("key", API_KEY).
                queryParam("token", TOKEN).
                when().
                get("/members/me").
                then().
                statusCode(200).
                body("id", not(emptyString()));
    }

    @Test
    public void tearInvalidCredentials() {
        given().
                queryParam("key", "invalidKey").
                queryParam("token", "invalidToken").
                when().
                get("/members/me").
                then().
                statusCode(401);
    }
}