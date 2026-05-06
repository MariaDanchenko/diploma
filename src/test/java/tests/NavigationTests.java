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
        Assert.assertTrue(trelloHomePage.isOnHomePage());
        Assert.assertTrue(trelloHomePage.isCreateButtonDisplayed());
        Assert.assertTrue(trelloHomePage.isSearchInputDisplayed());
    }

    @Test
    public void testOpenProfileMenu() {
        trelloHomePage.openProfileMenu();
        Assert.assertTrue(trelloHomePage.isProfileMenuOpened());
    }

    @Test
    public void testOpenInfo() {
        trelloHomePage.openInfo();
        Assert.assertTrue(trelloHomePage.isInfoVisible());
    }

    @Test
    public void testNotificationMenu() {
        trelloHomePage.openNotifications();
        Assert.assertTrue(trelloHomePage.isNotificationsVisible());
    }

    @Test
    public void testOpenBoardsPage() {
        trelloHomePage.openBoardsPage();
        Assert.assertTrue(trelloHomePage.isBoardsPageOpened());
    }

    @Test
    public void testOpenTemplatesPage() {
        trelloHomePage.openTemplatesPage();
        Assert.assertTrue(trelloHomePage.isTemplatesPageOpened());
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

        Assert.assertTrue(trelloHomePage.isSearchResultRelevant("Моя"));
    }

    @Test
    public void testSearchNonExistentText() {
        trelloHomePage.openSearchPage();
        trelloHomePage.search("123");

        Assert.assertTrue(trelloHomePage.isNoSearchResults());
    }
}
