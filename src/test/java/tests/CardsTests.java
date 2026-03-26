package tests;

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

        boardPage.isCardInList("Test", "В процессе");
    }
}
