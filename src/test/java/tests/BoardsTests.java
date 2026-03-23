package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BoardPage;

public class BoardsTests extends BaseTest {

    private BoardPage boardPage;

    @Test(priority = 1)
    public void testCreateBoard() {
        String boardName = "TestBoard";

        BoardPage boardPage = trelloHomePage.createBoard(boardName);

        Assert.assertTrue(boardPage.isBoardLoaded());
        Assert.assertEquals(boardPage.getBoardHeader(), boardName);
    }

    @Test
    public void testAddLists() {
        String boardName = "TestBoard";

        BoardPage boardPage = trelloHomePage.createBoard(boardName);

        boardPage.addList("List 1");
        boardPage.addList("List 2");

        Assert.assertTrue(driver.getPageSource().contains("List 1"));
        Assert.assertTrue(driver.getPageSource().contains("List 2"));
    }

    @Test
    public void testAddCard() {
        String boardName = "TestBoard";

        BoardPage boardPage = trelloHomePage.createBoard(boardName);

        boardPage.addList("List 1");
        boardPage.addCardToFirstList("TestCard");

        Assert.assertTrue(boardPage.isCardExists("TestCard"));
    }

    @Test
    public void testEditCard() {
        String boardName = "TestBoard";

        BoardPage boardPage = trelloHomePage.createBoard(boardName);

        boardPage.addList("List 1");
        boardPage.addCardToFirstList("TestCard");
        boardPage.editCard("TestCard", "UpdatedCard");

        Assert.assertTrue(boardPage.isCardExists("UpdatedCard"));
    }

    @Test
    public void testDeleteBoard() {
        String boardName = "TestBoard";

        BoardPage boardPage = trelloHomePage.createBoard(boardName);

        boardPage.deleteBoard();

        Assert.assertTrue(trelloHomePage.isOnHomePage());
    }
}
