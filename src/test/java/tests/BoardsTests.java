package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BoardPage;

public class BoardsTests extends BaseTest {

    @Test(priority = 1)
    public void testCreateBoard() {
        String boardName = "TestBoard";

        BoardPage boardPage = trelloHomePage.createBoard(boardName);

        Assert.assertTrue(boardPage.isBoardLoaded());
        Assert.assertEquals(boardPage.getBoardHeader(), boardName);
    }

    @Test(priority = 2)
    public void testAddCard() {
        String boardName = "TestBoard";
        String cardName = "TestCard";

        BoardPage boardPage = trelloHomePage.createBoard(boardName);

        boardPage.addCardToFirstList(cardName);

        Assert.assertTrue(boardPage.isCardExists(cardName));
    }

    @Test(priority = 3)
    public void testEditCard() {
        String boardName = "TestBoard";
        String oldName = "TestCard";
        String newName = "UpdatedCard";

        BoardPage boardPage = trelloHomePage.createBoard(boardName);

        boardPage.addCardToFirstList(oldName);
        boardPage.editCard(oldName, newName);

        Assert.assertTrue(boardPage.isCardExists(newName));
    }

    @Test(priority = 10)
    public void testDeleteBoard() {
        String boardName = "TestBoard";

        BoardPage boardPage = trelloHomePage.createBoard(boardName);

        boardPage.deleteBoard();

        Assert.assertTrue(trelloHomePage.isOnHomePage());
    }
}
