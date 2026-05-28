package api.client;

import api.TrelloApiConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BoardApiClient {

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri(TrelloApiConfig.BASE_URL)
            .addQueryParam("key", TrelloApiConfig.API_KEY)
            .addQueryParam("token", TrelloApiConfig.TOKEN)
            .build();

    public String createBoard(String name) {
        return given(spec)
                .queryParam("name", name)
                .when()
                .post("/boards/")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    public BoardData createBoardWithUrl(String name) {
        Response response = given(spec)
                .queryParam("name", name)
                .when()
                .post("/boards/")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String boardId = response.path("id");
        String boardUrl = response.path("url");

        return new BoardData(boardId, boardUrl);
    }

    public void deleteBoard(String boardId) {
        given(spec)
                .when()
                .delete("/boards/" + boardId)
                .then()
                .statusCode(200);
    }

    public void updateBoard(String boardId, String newName) {
        given(spec)
                .queryParam("name", newName)
                .when()
                .put("/boards/" + boardId)
                .then()
                .statusCode(200);
    }

    public String createList(String boardId, String listName) {
        return given(spec)
                .queryParam("name", listName)
                .queryParam("idBoard", boardId)
                .when()
                .post("/lists")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    public String createCard(String listId, String cardName) {
        return given(spec)
                .queryParam("name", cardName)
                .queryParam("idList", listId)
                .when()
                .post("/cards")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    public String getBoardIdByShortLink(String boardShortLink) {
        return given(spec)
                .when()
                .get("/boards/" + boardShortLink)
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    public record BoardData(String id, String url) {
    }
}
