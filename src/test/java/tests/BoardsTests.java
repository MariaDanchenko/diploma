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
        driver.get("https://trello.com/b/Z18mRpRD/%D0%BC%D0%BE%D1%8F-%D0%B4%D0%BE%D1%81%D0%BA%D0%B0-trello");

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
