package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NavigationTests extends BaseTest {

    @BeforeMethod
    public void resetState() {
        trelloHomePage.goToHomePage();
    }

    @Test
    public void testHomePageElements() {
        Assert.assertTrue(trelloHomePage.isOnHomePage(), "Home page is not displayed");
        Assert.assertTrue(trelloHomePage.isCreateButtonDisplayed(), "Create board button is not displayed");
        Assert.assertTrue(trelloHomePage.isSearchInputDisplayed(), "Search input is not displayed");
    }

    @Test
    public void testOpenProfileMenu() {
        trelloHomePage.openProfileMenu();
        Assert.assertTrue(trelloHomePage.isProfileMenuOpened(), "Profile menu is not opened");
    }

    @Test
    public void testOpenInfo() {
        trelloHomePage.openInfo();
        Assert.assertTrue(trelloHomePage.isInfoVisible(), "Info panel is not visible");
    }

    @Test
    public void testNotificationMenu() {
        trelloHomePage.openNotifications();
        Assert.assertTrue(trelloHomePage.isNotificationsVisible(), "Notifications panel is not visible");
    }

    @Test
    public void testOpenBoardsPage() {
        trelloHomePage.openBoardsPage();
        Assert.assertTrue(trelloHomePage.isBoardsPageOpened(), "Boards page did not open");
    }

    @Test
    public void testOpenTemplatesPage() {
        trelloHomePage.openTemplatesPage();
        Assert.assertTrue(trelloHomePage.isTemplatesPageOpened(), "Templates page did not open");
    }

    @Test
    public void testCreateMenu() {
        trelloHomePage.openCreateMenu();
        Assert.assertTrue(trelloHomePage.isCreateButtonOpened(), "Create menu is not opened");

        trelloHomePage.clickCreateBoard();
        Assert.assertTrue(trelloHomePage.isCreateBoardModalOpened(), "Create board modal is not opened");

        trelloHomePage.goToHomePage();
    }

    @Test
    public void testSearch() {
        trelloHomePage.openSearchPage();
        trelloHomePage.search("Моя");

        Assert.assertTrue(trelloHomePage.isSearchResultRelevant("Моя"), "Search results do not contain expected text");
    }

    @Test
    public void testSearchNonExistentText() {
        trelloHomePage.openSearchPage();
        trelloHomePage.search("123");

        Assert.assertTrue(trelloHomePage.isNoSearchResults(), "Search returned results for non-existent text");
    }
}
