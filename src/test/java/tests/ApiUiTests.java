package tests;

import api.client.BoardApiClient;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiUiTests extends BaseTest {

    private final BoardApiClient api = new BoardApiClient();
    private final List<String> boardsToCleanup = new ArrayList<>();

    @Test
    public void testBoardVisibleAfterApiCreate() {
        String boardName = "API_UI_" + UUID.randomUUID();

        BoardApiClient.BoardData boardData = api.createBoardWithUrl(boardName);
        String boardId = boardData.getId();
        String boardShortLink = extractBoardShortLink(boardData.getUrl());
        boardsToCleanup.add(boardId);

        trelloHomePage.openBoardsPage();

        driver.navigate().refresh();

        wait.until(driver1 -> trelloHomePage.isBoardVisibleByShortLink(boardShortLink));

        Assert.assertTrue(trelloHomePage.isBoardVisibleByShortLink(boardShortLink),
                "Board is not visible on the boards page after API creation");
    }

    @Test
    public void testBoardGoneAfterApiDelete() {
        String boardName = "API_UI_" + UUID.randomUUID();

        BoardApiClient.BoardData boardData = api.createBoardWithUrl(boardName);
        String boardId = boardData.getId();
        String boardShortLink = extractBoardShortLink(boardData.getUrl());
        boardsToCleanup.add(boardId);

        trelloHomePage.openBoardsPage();
        driver.navigate().refresh();

        wait.until(driver1 -> trelloHomePage.isBoardVisibleByShortLink(boardShortLink));
        api.deleteBoard(boardId);
        boardsToCleanup.remove(boardId);

        driver.navigate().refresh();

        wait.until(driver1 -> !trelloHomePage.isBoardVisibleByShortLink(boardShortLink));

        Assert.assertFalse(trelloHomePage.isBoardVisibleByShortLink(boardShortLink),
                "Board is still visible after API deletion");
    }

    @Test
    public void testUpdateBoard() {
        String initialName = "API_UI_Update_" + UUID.randomUUID();
        String updatedName = "Updated_Name_" + UUID.randomUUID();

        BoardApiClient.BoardData boardData = api.createBoardWithUrl(initialName);
        String boardId = boardData.getId();
        String boardShortLink = extractBoardShortLink(boardData.getUrl());
        boardsToCleanup.add(boardId);

        trelloHomePage.openBoardsPage();
        driver.navigate().refresh();

        wait.until(driver1 -> trelloHomePage.isBoardVisibleByShortLinkAndName(boardShortLink, initialName));
        Assert.assertTrue(trelloHomePage.isBoardVisibleByShortLinkAndName(boardShortLink, initialName),
                "Board with initial name is not visible");

        api.updateBoard(boardId, updatedName);

        driver.navigate().refresh();

        wait.until(driver1 -> trelloHomePage.isBoardVisibleByShortLinkAndName(boardShortLink, updatedName));
        Assert.assertTrue(trelloHomePage.isBoardVisibleByShortLinkAndName(boardShortLink, updatedName),
                "Board name was not updated on the UI");

        api.deleteBoard(boardId);
        boardsToCleanup.remove(boardId);
    }

    private String extractBoardShortLink(String boardUrl) {
        Matcher matcher = Pattern.compile("/b/([^/]+)").matcher(boardUrl);
        if (!matcher.find()) {
            throw new IllegalStateException("Cannot extract board short link from URL: " + boardUrl);
        }
        return matcher.group(1);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupBoards() {
        for (String boardId : new ArrayList<>(boardsToCleanup)) {
            try {
                api.deleteBoard(boardId);
            } finally {
                boardsToCleanup.remove(boardId);
            }
        }
    }
}
