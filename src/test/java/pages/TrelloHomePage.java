package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TrelloHomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    public By profileButton = By.xpath("//button[@data-testid = 'header-member-menu-button']");
    public By boardsLink = By.cssSelector("a[href = '/u/user51818084/boards']");
    public By templatesLink = By.cssSelector("a[href = '/templates']");
    public By homeLink = By.cssSelector("div[data-testid = 'team25-header-logo']");
    public By createButton = By.cssSelector("button[data-testid = 'header-create-menu-button']");
    public By searchInput = By.cssSelector("input[data-testid='cross-product-search-input-skeleton']");
    public By notificationsButton = By.cssSelector("button[data-testid = 'header-notifications-button']");
    public By infoButton = By.cssSelector("button[data-testid = 'header-info-button']");

    public TrelloHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOnHomePage() {
        return wait.until(ExpectedConditions.urlContains("trello.com/"));
    }
}
