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

        //Проверка, что переходим на главную страницу
        Assert.assertTrue(trelloHomePage.isOnHomePage());

        //Проверка наличия ключевых элементов
        Assert.assertTrue(trelloHomePage.isCreateButtonDisplayed());
        Assert.assertTrue(trelloHomePage.isSearchInputDisplayed());
    }

    @Test
    public void testOpenProfileMenu() {
        trelloHomePage.openProfileMenu();

        //Проверка, что открывается меню профиля
        Assert.assertTrue(trelloHomePage.isProfileMenuOpened());
    }

    @Test
    public void testOpenInfo() {
        trelloHomePage.openInfo();

        //Проверка, что открывается меню информации
        Assert.assertTrue(trelloHomePage.isInfoVisible());
    }

    @Test
    public void testNotificationMenu() {
        trelloHomePage.openNotifications();

        //Проверка, что открывается меню уведомлений
        Assert.assertTrue(trelloHomePage.isNotificationsVisible());
    }

    @Test
    public void testOpenBoardsPage() {
        trelloHomePage.openBoardsPage();

        //Проверка, что открылась страница с досками
        Assert.assertTrue(trelloHomePage.isBoardsPageOpened());
    }

    @Test
    public void testOpenTemplatesPage() {
        trelloHomePage.openTemplatesPage();

        //Проверка, что открывается страница с шаблонами
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
