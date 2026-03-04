package api;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CRUD_Tests extends BaseTest {

    private String boardID;
    private final Logger logger = LogManager.getLogger(CRUD_Tests.class);

    @Test(priority = 1)
    public void testCreateBoard() {
        String boardName = "Test Board ";

        Response response = given().
                queryParam("key", API_KEY).
                queryParam("token", TOKEN).
                queryParam("name", boardName).
                when().
                post("/boards/").
                then().
                statusCode(200).
                body("name", equalTo(boardName)).
                extract().response();

        boardID = response.path("id");
        logger.info("Successfully created board with ID: {}", boardID);
    }

    @Test(priority = 2, dependsOnMethods = "testCreateBoard")
    public void testGetBoard() {

        given().
                queryParam("key", API_KEY).
                queryParam("token", TOKEN).
                when().
                get("/boards/" + boardID).
                then().
                statusCode(200).
                body("id", equalTo(boardID));

        logger.info("Successfully fetched board with ID: {}", boardID);
    }

    @Test(priority = 3, dependsOnMethods = "testGetBoard")
    public void testUpdateBoard() {
        String newName = "Updated Board Name";

        given().
                queryParam("key", API_KEY).
                queryParam("token", TOKEN).
                queryParam("name", newName).
                when().
                put("/boards/" + boardID).
                then().
                statusCode(200).
                body("name", equalTo(newName));

        logger.info("Successfully updated board with ID: {}. New name: {}", boardID, newName);
    }

    @Test(priority = 4, dependsOnMethods = "testUpdateBoard")
    public void testDeleteBoard() {

        given().
                queryParam("key", API_KEY).
                queryParam("token", TOKEN).
                when().
                delete("/boards/" + boardID).
                then().
                statusCode(200);

        logger.info("Successfully deleted board with ID: {}", boardID);
    }
}