package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class TrelloHomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    private final By profileButton = By.xpath("//button[@data-testid = 'header-member-menu-button']");
    private final By createMenuButton = By.cssSelector("button[data-testid = 'header-create-menu-button']");
    private final By createBoardButton = By.cssSelector("button[data-testid='header-create-board-button']");
    private final By createBoardTile = By.cssSelector("[data-testid='create-board-tile']");

    private final By searchInput = By.cssSelector("input[data-testid='cross-product-search-input-skeleton']");
    private final By advancedSearchInput = By.cssSelector("input[data-testid='advanced-search-input']");
    private final By searchResults = By.cssSelector("a[data-testid='advanced-search-board-result-item']");

    private final By boardsLink = By.xpath("//a[contains(@href,'/boards')]");
    private final By templatesLink = By.xpath("//a[contains(@href,'/templates')]");

    private final By homeLink = By.cssSelector("div[data-testid = 'team25-header-logo']");

    private final By notificationsButton = By.cssSelector("button[data-testid = 'header-notifications-button']");
    private final By infoButton = By.cssSelector("button[data-testid = 'header-info-button']");

    private final By accountMenu = By.id("account-menu-account-section-title");
    private final By boardsHeader = By.xpath("//h3[contains(text(),'ПРОСТРАНСТВА')]");
    private final By templatesHeader = By.xpath("//h1[contains(text(),'категории')]");

    private final By infoMenu = By.id("header-info-menu-popover-content");
    private final By notificationsMenu = By.cssSelector("h2[class = 'HsONwDlXlvyo7z']");
    private final By createBoard = By.cssSelector("button[data-testid = 'create-board-tile']");

    public TrelloHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOnHomePage() {

        return wait.until(ExpectedConditions.visibilityOfElementLocated(profileButton)).isDisplayed();
    }

    public boolean isCreateButtonDisplayed() {
        WebElement create = wait.until(ExpectedConditions.visibilityOfElementLocated(createBoard));
        return create.isDisplayed();
    }

    public boolean isSearchInputDisplayed() {
        return driver.findElement(searchInput).isDisplayed();
    }

    public void openProfileMenu() {

        WebElement profile = wait.until(ExpectedConditions.visibilityOfElementLocated(profileButton));
        profile.click();
    }

    public boolean isProfileMenuOpened() {
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(accountMenu));
        return menu.isDisplayed();
    }

    public void openBoardsPage() {

        wait.until(ExpectedConditions.elementToBeClickable(boardsLink)).click();
    }

    public boolean isBoardsPageOpened() {

        return wait.until(ExpectedConditions.visibilityOfElementLocated(boardsHeader)).isDisplayed();
    }

    public void openTemplatesPage() {

        wait.until(ExpectedConditions.elementToBeClickable(templatesLink)).click();
    }

    public boolean isTemplatesPageOpened() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(templatesHeader)).isDisplayed();
    }

    public void goToHomePage() {

        wait.until(ExpectedConditions.elementToBeClickable(homeLink)).click();
    }

    public void openCreateMenu() {

        wait.until(ExpectedConditions.elementToBeClickable(createMenuButton)).click();
    }

    public void clickCreateBoard() {
        wait.until(ExpectedConditions.elementToBeClickable(createBoardButton)).click();
    }

    public CreateBoardPage openCreateBoardModal() {
        openCreateMenu();

        WebElement createBoardButton = wait.until(ExpectedConditions.elementToBeClickable(this.createBoardButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createBoardButton);

        WebElement createBoardTile = wait.until(ExpectedConditions.elementToBeClickable(this.createBoardTile));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", createBoardTile);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createBoardTile);

        return new CreateBoardPage(driver);
    }

    public BoardPage createBoard(String boardName) {
        CreateBoardPage createBoardPage = openCreateBoardModal();

        return createBoardPage.createBoard(boardName);
    }

    public void openSearchPage() {
        driver.get("https://trello.com/search");
    }

    public void search(String text) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(advancedSearchInput));
        input.sendKeys(text);
    }

    public boolean isSearchResultRelevant(String expectedText) {

        try {
            List<WebElement> results =
                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResults));

            if (results.isEmpty()) {
                return false;
            }

            for (WebElement result : results) {
                if (result.getText().contains(expectedText)) {
                    return true; //Если хотя бы 1 результат содержит текст
                }
            }
            return false; //Если ни один результат не содержит текст
        } catch (Exception e) {
            return false; //Если результатов нет вообще
        }
    }

    public boolean isNoSearchResults() {
        return driver.findElements(searchResults).isEmpty();
    }

    public void openNotifications() {

        wait.until(ExpectedConditions.elementToBeClickable(notificationsButton)).click();
    }

    public void openInfo() {

        wait.until(ExpectedConditions.elementToBeClickable(infoButton)).click();
    }

    public boolean isInfoVisible() {
        WebElement info = wait.until(ExpectedConditions.visibilityOfElementLocated(infoMenu));
        return info.isDisplayed();
    }

    public boolean isNotificationsVisible() {
        WebElement textNotificationsMenu = wait.until(ExpectedConditions.
                visibilityOfElementLocated(notificationsMenu));
        return textNotificationsMenu.isDisplayed();
    }

    public boolean isCreateButtonOpened() {
        WebElement createBoard = wait.until(ExpectedConditions.visibilityOfElementLocated(createBoardButton));
        return createBoard.isDisplayed();
    }
}
