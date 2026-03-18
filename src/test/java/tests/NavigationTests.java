package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.TrelloHomePage;

public class NavigationTests extends BaseTest {

    @Test(priority = 1)
    public void testHomePageElements() {

        //Проверка, что переходим на главную страницу
        Assert.assertTrue(trelloHomePage.isOnHomePage());

        //Проверка наличия ключевых элементов
        Assert.assertTrue(trelloHomePage.isCreateButtonDisplayed());
        Assert.assertTrue(trelloHomePage.isSearchInputDisplayed());
        Assert.assertTrue(trelloHomePage.isProfileButtonDisplayed());
    }

    @Test(priority = 2)
    public void testOpenProfileMenu() {
        trelloHomePage.openProfileMenu();

        //Проверка, что открывается меню профиля
        Assert.assertTrue(trelloHomePage.isProfileMenuOpened());
    }

    @Test(priority = 3)
    public void testOpenInfo() {
        trelloHomePage.openInfo();

        //Проверка, что открывается меню информации
        Assert.assertTrue(trelloHomePage.isInfoVisible());
    }

    @Test(priority = 4)
    public void testNotificationMenu() {
        trelloHomePage.openNotifications();

        //Проверка, что открывается меню уведомлений
        Assert.assertTrue(trelloHomePage.isNotificationsVisible());
    }

    @Test(priority = 5)
    public void testOpenBoardsLink() {
        trelloHomePage.openBoardsLink();

        //Проверка, что открылась страница с досками
        Assert.assertTrue(trelloHomePage.isBoardsVisible());
    }

    @Test(priority = 6)
    public void testOpenTemplates() {
        trelloHomePage.openTemplates();

        //Проверка, что открывается страница с шаблонами
        Assert.assertTrue(trelloHomePage.isTemplatesVisible());
    }

    @Test(priority = 7)
    public void testOpenMainPage() {
        trelloHomePage.openMainPage();

        //Проверка, что открылась главная страница
        Assert.assertTrue(trelloHomePage.isBoardsVisible());
    }

    @Test(priority = 8)
    public void testCreateButton() {
        trelloHomePage.clickCreateButton();

        //Проверка, что меню создания открылось
        Assert.assertTrue(trelloHomePage.isCreateButtonOpened());
    }

    @Test(priority = 9)
    public void testSearch() {
        driver.get("https://trello.com/search");
        trelloHomePage.search("Моя");

        Assert.assertTrue(trelloHomePage.isSearchResultRelevant("Моя"));
    }

    @Test(priority = 10)
    public void testSearchNonExistentText() {
        driver.get("https://trello.com/search");
        trelloHomePage.search("123");

        Assert.assertTrue(trelloHomePage.isNoSearchResults());
    }
}
