package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CreateBoardPage;

public class BoardsTests extends BaseTest {

    @Test
    public void testCreateBoard() {
        trelloHomePage.createBoard();

        CreateBoardPage createBoardPage = new CreateBoardPage(driver);
        createBoardPage.selectBackground();
        Assert.assertTrue(createBoardPage.isBackgroundSelected());
    }
}
