package api.client;

import api.TrelloApiConfig;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static io.restassured.RestAssured.given;

public class BoardApiClient {

    private final String API_KEY = TrelloApiConfig.API_KEY;
    private final String TOKEN = TrelloApiConfig.TOKEN;

    public String createBoard(String name) {

        return given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .queryParam("name", name)
                .when()
                .post("https://api.trello.com/1/boards/")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    public BoardData createBoardWithUrl(String name) {
        Response response = given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .queryParam("name", name)
                .when()
                .post("https://api.trello.com/1/boards/")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String boardId = response.path("id");
        String boardUrl = response.path("url");

        return new BoardData(boardId, boardUrl);
    }

    public void deleteBoard(String boardId) {
        given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .when()
                .delete("https://api.trello.com/1/boards/" + boardId)
                .then()
                .statusCode(200);
    }

    public void updateBoard(String boardId, String newName) {
        given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .queryParam("name", newName)
                .when()
                .put("https://api.trello.com/1/boards/" + boardId)
                .then()
                .statusCode(200);
    }

    public String createList(String boardId, String listName) {
        return given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .queryParam("name", listName)
                .queryParam("idBoard", boardId)
                .when()
                .post("https://api.trello.com/1/lists")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    public String createCard(String listId, String cardName) {
        return given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .queryParam("name", cardName)
                .queryParam("idList", listId)
                .when()
                .post("https://api.trello.com/1/cards")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    public void archiveCard(String cardId) {
        given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .queryParam("closed", true)
                .when()
                .put("https://api.trello.com/1/cards/" + cardId)
                .then()
                .statusCode(200);
    }

    public boolean isCardArchived(String cardId) {
        Boolean isClosed = given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .when()
                .get("https://api.trello.com/1/cards/" + cardId)
                .then()
                .statusCode(200)
                .extract()
                .path("closed");
        return Boolean.TRUE.equals(isClosed);
    }

    public String getFirstListIdByBoardShortLink(String boardShortLink) {
        return given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .when()
                .get("https://api.trello.com/1/boards/" + boardShortLink + "/lists")
                .then()
                .statusCode(200)
                .extract()
                .path("[0].id");
    }

    public String getBoardIdByShortLink(String boardShortLink) {
        return given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .when()
                .get("https://api.trello.com/1/boards/" + boardShortLink)
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Getter
    @RequiredArgsConstructor
    public static class BoardData {
        private final String id;
        private final String url;
    }
}
