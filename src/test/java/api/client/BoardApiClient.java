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
}
