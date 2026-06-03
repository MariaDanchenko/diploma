package api;

import api.client.BoardApiClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * API contract tests use raw {@code given()} for Act/Assert; setup and cleanup use {@link BoardApiClient}.
 */
@Slf4j
public class CrudTests extends BaseAPITest {

    private final BoardApiClient api = new BoardApiClient();

    @Test
    public void testCreateBoard() {
        String boardName = "Test Board " + UUID.randomUUID();

        Response response = given(defaultApiSpec()).
                queryParam("name", boardName).
                when().
                post("/boards/").
                then().
                statusCode(200).
                extract().response();

        String boardId = response.path("id");
        try {
            response.then().body("name", equalTo(boardName));
            log.info("Successfully created board with ID: {}", boardId);
        } finally {
            api.deleteBoard(boardId);
        }
    }

    @Test
    public void testGetBoard() {
        String boardId = api.createBoard("Get Board " + UUID.randomUUID());
        try {
            given(defaultApiSpec()).
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
        String newName = "Updated Board Name " + UUID.randomUUID();
        String boardId = api.createBoard("Update Board " + UUID.randomUUID());
        try {
            given(defaultApiSpec()).
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

        given(defaultApiSpec()).
                when().
                get("/boards/" + boardId).
                then().
                statusCode(404);

        log.info("Successfully deleted board with ID: {}", boardId);
    }
}
