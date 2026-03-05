package api;

import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ParamTests extends BaseTest {

    private List<String> boardIDs = new ArrayList<>(); // Список для хранения ID досок

    @DataProvider(name = "boardNames")
    public Object[][] boardNames() {
        return new Object[][]{
                {"Board 1"},
                {"Board 2"},
                {"Board 3"}
        };
    }

    @Test(dataProvider = "boardNames")
    public void testCreateBoardsWithParam(String boardName) {
        String boardID = given().
                queryParam("key", API_KEY).
                queryParam("token", TOKEN).
                queryParam("name", boardName).
                when().
                post("/boards/").
                then().
                statusCode(200).
                body("name", equalTo(boardName)).
                extract().
                path("id"); // Извлекаем ID созданной доски

        boardIDs.add(boardID); // Сохраняем ID в список
        System.out.println("Создана доска с ID: " + boardID);
    }

    @AfterClass
    public void cleanupBoards() {
        for (String boardID : boardIDs) {
            given().
                    queryParam("key", API_KEY).
                    queryParam("token", TOKEN).
                    when().
                    delete("/boards/" + boardID).
                    then().
                    statusCode(200);
            System.out.println("Удалена доска с ID: " + boardID);
        }
    }
}
