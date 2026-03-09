package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.TrelloHomePage;

public class NavigationTests extends BaseTest {

    @Test
    public void testHomePageElements() {

        TrelloHomePage trelloHomePage = new TrelloHomePage(driver);

        //Проверка, что на главной странице
        Assert.assertTrue(trelloHomePage.isOnHomePage());

        //Проверка видимости элементов
        Assert.assertTrue(driver.findElement(trelloHomePage.profileButton).isDisplayed());
        Assert.assertTrue(driver.findElement(trelloHomePage.boardsLink).isDisplayed());
        Assert.assertTrue(driver.findElement(trelloHomePage.templatesLink).isDisplayed());
        Assert.assertTrue(driver.findElement(trelloHomePage.homeLink).isDisplayed());
        Assert.assertTrue(driver.findElement(trelloHomePage.createButton).isDisplayed());
        Assert.assertTrue(driver.findElement(trelloHomePage.searchInput).isDisplayed());
        Assert.assertTrue(driver.findElement(trelloHomePage.notificationsButton).isDisplayed());
        Assert.assertTrue(driver.findElement(trelloHomePage.infoButton).isDisplayed());
    }
}
