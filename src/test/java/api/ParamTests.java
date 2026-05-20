package api;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class ParamTests extends BaseAPITest {

    private final List<String> boardIds = new CopyOnWriteArrayList<>();

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
        String boardId = given().
                queryParam("key", API_KEY).
                queryParam("token", TOKEN).
                queryParam("name", boardName).
                when().
                post("/boards/").
                then().
                statusCode(200).
                body("name", equalTo(boardName)).
                extract().
                path("id");

        boardIds.add(boardId);
        log.info("Created board with ID: {}", boardId);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupBoards() {
        for (String boardId : new ArrayList<>(boardIds)) {
            given().
                    queryParam("key", API_KEY).
                    queryParam("token", TOKEN).
                    when().
                    delete("/boards/" + boardId).
                    then().
                    statusCode(200);
            log.info("Deleted board with ID: {}", boardId);
            boardIds.remove(boardId);
        }
    }
}
