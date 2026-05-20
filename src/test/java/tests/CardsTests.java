package tests;

import api.client.BoardApiClient;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BoardPage;

import java.util.UUID;

public class CardsTests extends BaseTest {

    private final BoardApiClient api = new BoardApiClient();
    private BoardPage boardPage;
    private String boardId;
    private String toDoListId;

    @BeforeMethod
    public void prepareBoard() {
        String boardName = "CardsTests_" + UUID.randomUUID();
        BoardApiClient.BoardData boardData = api.createBoardWithUrl(boardName);
        boardId = boardData.getId();

        toDoListId = api.createList(boardId, "To Do");
        api.createList(boardId, "В процессе");

        driver.get(boardData.getUrl());
        boardPage = new BoardPage(driver);
        Assert.assertTrue(boardPage.isBoardLoaded(), "Board did not load");
        trelloHomePage.dismissBlockingOverlaysIfPresent();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupBoard() {
        if (boardId != null) {
            api.deleteBoard(boardId);
        }
    }

    @Test
    public void testMovingCard() {
        String cardTitle = "TestMoveCard_" + UUID.randomUUID();

        createCardViaApi(cardTitle);

        boardPage.movingCard(cardTitle, "В процессе");

        Assert.assertTrue(boardPage.isCardInList(cardTitle, "В процессе"), "Card was not moved to the target list");
    }

    @Test
    public void testCompleteCard() {
        String cardTitle = "TestCompleteCard_" + UUID.randomUUID();

        createCardViaApi(cardTitle);
        boardPage.completeCard(cardTitle);

        Assert.assertTrue(boardPage.isCompleteButtonSelected(), "Complete button is not in selected state");
    }

    @Test
    public void testArchiveCard() {
        String cardTitle = "TestArchiveCard_" + UUID.randomUUID();

        createCardViaApi(cardTitle);
        boardPage.archiveCard(cardTitle);

        Assert.assertTrue(boardPage.isCardAbsentFromBoard(cardTitle), "Archived card is still visible on board");
    }

    private void createCardViaApi(String cardTitle) {
        api.createCard(toDoListId, cardTitle);
        driver.navigate().refresh();
        trelloHomePage.dismissBlockingOverlaysIfPresent();
    }
}
