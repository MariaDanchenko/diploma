package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BoardPage;

public class CardsTests extends BaseTest {

    private BoardPage boardPage;

    @Test
    public void testMovingCard() {
        driver.get("https://trello.com/b/vGwHaVh4/test");

        boardPage = new BoardPage(driver);

        boardPage.addCardToFirstList("Test");

        boardPage.movingCard("Test", "В процессе");

        Assert.assertTrue(boardPage.isCardInList("Test", "В процессе"));

        boardPage.completeCard("Test");
        boardPage.archiveCard("Test");
    }

    @Test
    public void testCompleteCard() {
        driver.get("https://trello.com/b/vGwHaVh4/test");

        boardPage = new BoardPage(driver);

        boardPage.addCardToFirstList("Test");
        boardPage.completeCard("Test");

        Assert.assertTrue(boardPage.isCompleteButtonSelected());

        boardPage.archiveCard("Test");
    }

    @Test
    public void testArchiveCard() {
        driver.get("https://trello.com/b/vGwHaVh4/test");

        boardPage = new BoardPage(driver);

        boardPage.addCardToFirstList("Test");
        boardPage.completeCard("Test");
        boardPage.archiveCard("Test");

        Assert.assertTrue(boardPage.isCardArchived("Test"));
    }
}
