package tests;

import api.client.BoardApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BoardPage;

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

    @Test
    public void testUpdateBoard() {
        String initialName = "API_UI_Update_";
        String updatedName = "Updated_Name_";

        String boardId = api.createBoard(initialName);

        trelloHomePage.openBoardsPage();
        driver.navigate().refresh();

        wait.until(driver1 -> trelloHomePage.isBoardVisible(initialName));
        Assert.assertTrue(trelloHomePage.isBoardVisible(initialName));

        api.updateBoard(boardId, updatedName);

        driver.navigate().refresh();

        wait.until(driver1 -> trelloHomePage.isBoardVisible(updatedName));
        Assert.assertTrue(trelloHomePage.isBoardVisible(updatedName), "Имя не обновилось");

        api.deleteBoard(boardId);
    }
}
