package tests;

import api.client.BoardApiClient;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.BoardPage;
import pages.CreateBoardPage;

import java.util.UUID;

@Slf4j
public class BoardsTests extends BaseTest {

    private final BoardApiClient api = new BoardApiClient();
    private BoardPage boardPage;
    private String boardId;
    private boolean boardDeletedByTest;

    @Test
    public void testCreateBoard() {
        String boardName = "BoardsTests_" + UUID.randomUUID();
        driver.get("https://trello.com/");
        trelloHomePage.dismissOverlays();
        trelloHomePage.openCreateMenu();
        trelloHomePage.clickCreateBoard();

        CreateBoardPage createBoardPage = new CreateBoardPage(driver);
        createBoardPage.inputTitle(boardName);
        createBoardPage.clickCreateButton();
        boardPage = new BoardPage(driver);

        wait.until(driver1 -> driver1.getCurrentUrl().contains("/b/"));
        String shortLink = extractBoardShortLink(driver.getCurrentUrl());
        boardId = api.getBoardIdByShortLink(shortLink);

        Assert.assertTrue(boardPage.isBoardLoaded(), "Board did not load after creation");
        Assert.assertEquals(boardPage.getBoardHeader(), boardName, "Board header does not match expected name");
    }

    @Test
    public void testAddLists() {
        createBoardViaApiAndOpen();

        boardPage.addList("List 1");
        boardPage.addList("List 2");

        Assert.assertTrue(boardPage.isListExists("List 1"), "List 1 is not visible on board");
        Assert.assertTrue(boardPage.isListExists("List 2"), "List 2 is not visible on board");
    }

    @Test
    public void testAddCard() {
        String cardTitle = "TestCard_" + UUID.randomUUID();

        createBoardViaApiAndOpen();
        String listName = "ListApi_" + UUID.randomUUID();
        api.createList(boardId, listName);
        driver.navigate().refresh();
        boardPage.dismissOverlays();
        wait.until(driver1 -> boardPage.isListExists(listName));
        boardPage.addCardToList(listName, cardTitle);

        wait.until(driver1 -> boardPage.isCardExists(cardTitle));
        Assert.assertTrue(boardPage.isCardExists(cardTitle), "Card is not visible on the board");
    }

    @Test
    public void testEditCard() {
        String oldCardTitle = "TestCard_" + UUID.randomUUID();
        String updatedCardTitle = "UpdatedCard_" + UUID.randomUUID();

        createBoardViaApiAndOpen();
        String listId = api.createList(boardId, "ListApi_" + UUID.randomUUID());
        api.createCard(listId, oldCardTitle);
        driver.navigate().refresh();
        wait.until(driver1 -> boardPage.isCardExists(oldCardTitle));

        boardPage.editCard(oldCardTitle, updatedCardTitle);

        wait.until(driver1 -> boardPage.isCardExists(updatedCardTitle));
        Assert.assertTrue(boardPage.isCardExists(updatedCardTitle), "Updated card is not visible on the board");
    }

    @Test
    public void testDeleteBoard() {
        createBoardViaApiAndOpen();

        boardPage.deleteBoard();
        wait.until(driver1 -> driver1.getCurrentUrl().contains("/boards"));

        Assert.assertTrue(driver.getCurrentUrl().contains("/boards"), "Expected boards page URL after deletion");
        boardDeletedByTest = true;
        boardId = null;
    }

    private void createBoardViaApiAndOpen() {
        String boardName = "BoardsTests_" + UUID.randomUUID();
        BoardApiClient.BoardData boardData = api.createBoardWithUrl(boardName);
        boardId = boardData.id();
        log.info("Created board for test with ID: {}", boardId);
        driver.get(boardData.url());
        boardPage = new BoardPage(driver);
        Assert.assertTrue(boardPage.isBoardLoaded(), "Board did not load");
        boardPage.dismissOverlays();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (boardDeletedByTest) {
            boardDeletedByTest = false;
            return;
        }

        try {
            if (boardId != null) {
                api.deleteBoard(boardId);
                log.info("Deleted board in teardown with ID: {}", boardId);
            }
        } catch (Exception e) {
            log.error("Cleanup failed for boardId={}", boardId, e);
        } finally {
            boardId = null;
        }
    }
}
