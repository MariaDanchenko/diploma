package api;

import api.client.BoardApiClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class CrudTests extends BaseAPITest {

    private final BoardApiClient api = new BoardApiClient();

    @Test
    public void testCreateBoard() {
        String boardName = "Test Board " + UUID.randomUUID();

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

        String boardId = response.path("id");
        log.info("Successfully created board with ID: {}", boardId);
        api.deleteBoard(boardId);
    }

    @Test
    public void testGetBoard() {
        String boardId = api.createBoard("Get Board " + UUID.randomUUID());
        try {
            given().
                    queryParam("key", API_KEY).
                    queryParam("token", TOKEN).
                    when().
                    get("/boards/" + boardId).
                    then().
                    statusCode(200).
                    body("id", equalTo(boardId));

            log.info("Successfully fetched board with ID: {}", boardId);
        } finally {
            api.deleteBoard(boardId);
        }
    }

    @Test
    public void testUpdateBoard() {
        String newName = "Updated Board Name";
        String boardId = api.createBoard("Update Board " + UUID.randomUUID());
        try {
            given().
                    queryParam("key", API_KEY).
                    queryParam("token", TOKEN).
                    queryParam("name", newName).
                    when().
                    put("/boards/" + boardId).
                    then().
                    statusCode(200).
                    body("name", equalTo(newName));

            log.info("Successfully updated board with ID: {}. New name: {}", boardId, newName);
        } finally {
            api.deleteBoard(boardId);
        }
    }

    @Test
    public void testDeleteBoard() {
        String boardId = api.createBoard("Delete Board " + UUID.randomUUID());
        api.deleteBoard(boardId);

        given().
                queryParam("key", API_KEY).
                queryParam("token", TOKEN).
                when().
                get("/boards/" + boardId).
                then().
                statusCode(404);

        log.info("Successfully deleted board with ID: {}", boardId);
    }
}
