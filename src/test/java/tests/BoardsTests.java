package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BoardPage;
import pages.CreateBoardPage;

public class BoardsTests extends BaseTest {

    @Test(priority = 1)
    public void testCreateBoard() {
        trelloHomePage.createBoard();

        CreateBoardPage createBoardPage = new CreateBoardPage(driver);
        BoardPage boardPage = createBoardPage.createBoard("test");

        Assert.assertEquals(boardPage.getBoardHeader(), "test");
    }

    @Test(priority = 2)
    public void testAddCard() {
        BoardPage boardPage = new BoardPage(driver);
        boardPage.addCard("Сегодня", "Тестовая карточка");

        Assert.assertTrue(boardPage.isCardDisplayedInList("Сегодня", "Тестовая карточка"));
    }

    @Test(priority = 10)
    public void testDeleteBoard() {
        BoardPage boardPage = new BoardPage(driver);
        boardPage.clickMenuButton();
        boardPage.clickCloseBoard();
        boardPage.clickCloseButton();

        Assert.assertTrue(boardPage.isClosedBoardHeaderContains("доска закрыта"));

        boardPage.clickMenuButton();
        boardPage.clickDeleteBoardButton();
        boardPage.clickDeleteButton();

        Assert.assertTrue(trelloHomePage.isOnHomePage());
    }
}
