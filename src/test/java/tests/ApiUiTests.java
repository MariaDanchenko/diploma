package tests;

import api.client.BoardApiClient;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiUiTests extends BaseTest {

    private final BoardApiClient api = new BoardApiClient();

    @Test
    public void testCreateBoard() {
        String boardName = "API_UI_";

        String boardId = api.createBoard(boardName);

        trelloHomePage.openBoardsPage();

        driver.navigate().refresh();

        wait.until(driver1 -> trelloHomePage.isBoardVisible(boardName));

        Assert.assertTrue(trelloHomePage.isBoardVisible(boardName), "Доска не появилась");

        api.deleteBoard(boardId);

        driver.navigate().refresh();

        wait.until(driver1 -> !trelloHomePage.isBoardVisible(boardName));

        Assert.assertFalse(trelloHomePage.isBoardVisible(boardName), "Доска не удалилась");
    }
}
