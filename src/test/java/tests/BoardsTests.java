package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BoardPage;
import pages.CreateBoardPage;

public class BoardsTests extends BaseTest {

    @Test
    public void testCreateBoard() {
        trelloHomePage.createBoard();

        CreateBoardPage createBoardPage = new CreateBoardPage(driver);
        createBoardPage.selectBackground();
        createBoardPage.inputTitle("test");
        createBoardPage.clickCreateButton();

        Assert.assertTrue(createBoardPage.isBackgroundSelected());
        Assert.assertEquals(createBoardPage.getBoardTitle(), "test");

        BoardPage boardPage = new BoardPage(driver);
        Assert.assertEquals(boardPage.getBoardHeader(), "test");
    }
}
