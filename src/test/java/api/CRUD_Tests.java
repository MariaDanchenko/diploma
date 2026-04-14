package api;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CRUD_Tests extends BaseAPITest {

    private final Logger logger = LogManager.getLogger(CRUD_Tests.class);

    @Test
    public void testCreateBoard() {
        String boardName = "Test Board " + System.currentTimeMillis();

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

        String boardID = response.path("id");
        logger.info("Successfully created board with ID: {}", boardID);

        deleteBoard(boardID);
    }

    @Test
    public void testGetBoard() {
        String boardID = createBoard("Get Board " + System.currentTimeMillis());
        try {
            given().
                    queryParam("key", API_KEY).
                    queryParam("token", TOKEN).
                    when().
                    get("/boards/" + boardID).
                    then().
                    statusCode(200).
                    body("id", equalTo(boardID));

            logger.info("Successfully fetched board with ID: {}", boardID);
        } finally {
            deleteBoard(boardID);
        }
    }

    @Test
    public void testUpdateBoard() {
        String newName = "Updated Board Name";
        String boardID = createBoard("Update Board " + System.currentTimeMillis());
        try {
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
        } finally {
            deleteBoard(boardID);
        }
    }

    @Test
    public void testDeleteBoard() {
        String boardID = createBoard("Delete Board " + System.currentTimeMillis());

        deleteBoard(boardID);

        given().
                queryParam("key", API_KEY).
                queryParam("token", TOKEN).
                when().
                get("/boards/" + boardID).
                then().
                statusCode(404);

        logger.info("Successfully deleted board with ID: {}", boardID);
    }

    private String createBoard(String boardName) {
        return given().
                queryParam("key", API_KEY).
                queryParam("token", TOKEN).
                queryParam("name", boardName).
                when().
                post("/boards/").
                then().
                statusCode(200).
                extract().
                path("id");
    }

    private void deleteBoard(String boardID) {
        given().
                queryParam("key", API_KEY).
                queryParam("token", TOKEN).
                when().
                delete("/boards/" + boardID).
                then().
                statusCode(200);
    }
}