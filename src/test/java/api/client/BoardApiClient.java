package api.client;

import static io.restassured.RestAssured.given;

public class BoardApiClient {

    private final String API_KEY = System.getenv("TRELLO_API_KEY");
    private final String TOKEN = System.getenv("TRELLO_TOKEN");

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
}
