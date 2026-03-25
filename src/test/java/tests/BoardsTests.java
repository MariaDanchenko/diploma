package tests;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.BoardPage;

public class BoardsTests extends BaseTest {

    private BoardPage boardPage;

    @Test
    public void testCreateBoard() {
        String boardName = "TestBoard";

        boardPage = trelloHomePage.createBoard(boardName);

        Assert.assertTrue(boardPage.isBoardLoaded());
        Assert.assertEquals(boardPage.getBoardHeader(), boardName);
    }

    @Test
    public void testAddLists() {
        String boardName = "TestBoard";

        boardPage = trelloHomePage.createBoard(boardName);

        boardPage.addList("List 1");
        boardPage.addList("List 2");

        Assert.assertTrue(driver.getPageSource().contains("List 1"));
        Assert.assertTrue(driver.getPageSource().contains("List 2"));
    }

    @Test
    public void testAddCard() {
        String boardName = "TestBoard";

        boardPage = trelloHomePage.createBoard(boardName);

        boardPage.addList("List 1");
        boardPage.addCardToFirstList("TestCard");

        Assert.assertTrue(boardPage.isCardExists("TestCard"));
    }

    @Test
    public void testEditCard() {
        String boardName = "TestBoard";

        boardPage = trelloHomePage.createBoard(boardName);

        boardPage.addList("List 1");
        boardPage.addCardToFirstList("TestCard");
        boardPage.editCard("TestCard", "UpdatedCard");

        Assert.assertTrue(boardPage.isCardExists("UpdatedCard"));
    }

    @Test
    public void testDeleteBoard() {
        String boardName = "TestBoard";

        boardPage = trelloHomePage.createBoard(boardName);

        boardPage.deleteBoard();

        Assert.assertTrue(trelloHomePage.isOnHomePage());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        //Пропускаем cleanup для теста удаления доски
        if (result.getMethod().getMethodName().equals("testDeleteBoard")) {
            return;
        }

        try {
            if (boardPage != null && boardPage.isBoardLoaded()) {
                boardPage.deleteBoard();
            }
        } catch (Exception e) {
            System.out.println("Cleanup failed: " + e.getMessage());
        }
    }
}
