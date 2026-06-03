package pages;

import config.TestConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class TrelloHomePage extends BasePage {

    private final By profileButton = By.xpath("//button[@data-testid = 'header-member-menu-button']");
    private final By createMenuButton = By.cssSelector("button[data-testid = 'header-create-menu-button']");
    private final By createBoardButton = By.cssSelector("button[data-testid='header-create-board-button']");
    private final By createBoardModalTitleInput = By.cssSelector("input[data-testid='create-board-title-input']");

    private final By searchInput = By.cssSelector("input[data-testid='cross-product-search-input-skeleton']");
    private final By advancedSearchInput = By.cssSelector("input[data-testid='advanced-search-input']");
    private final By searchResults = By.cssSelector("a[data-testid='advanced-search-board-result-item']");

    private final By boardsLink = By.xpath("//a[contains(@href,'/boards')]");
    private final By templatesLink = By.xpath("//a[contains(@href,'/templates')]");

    private final By notificationsButton = By.cssSelector("button[data-testid = 'header-notifications-button']");
    private final By infoButton = By.cssSelector("button[data-testid = 'header-info-button']");

    private final By accountMenu = By.id("account-menu-account-section-title");

    private final By infoMenu = By.id("header-info-menu-popover-content");
    private final By createBoard = By.cssSelector("button[data-testid = 'create-board-tile']");

    public TrelloHomePage(WebDriver driver) {
        super(driver, TestConfig.DEFAULT_WAIT);
    }

    public boolean isOnHomePage() {

        return wait.until(ExpectedConditions.visibilityOfElementLocated(profileButton)).isDisplayed();
    }

    public boolean isCreateButtonDisplayed() {
        WebElement create = wait.until(ExpectedConditions.visibilityOfElementLocated(createBoard));
        return create.isDisplayed();
    }

    public boolean isSearchInputDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput)).isDisplayed();
    }

    public void openProfileMenu() {
        dismissOverlays();
        safeClick(profileButton);
    }

    public boolean isProfileMenuOpened() {
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(accountMenu));
        return menu.isDisplayed();
    }

    public void openBoardsPage() {
        safeClick(boardsLink);
    }

    public boolean isBoardsPageOpened() {
        try {
            wait.until(ExpectedConditions.urlContains("/boards"));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void openTemplatesPage() {
        safeClick(templatesLink);
    }

    public boolean isTemplatesPageOpened() {
        try {
            wait.until(ExpectedConditions.urlContains("/templates"));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void goToHomePage() {
        driver.get("https://trello.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(profileButton));
        dismissOverlays();
    }

    public void openCreateMenu() {
        safeClick(createMenuButton);
    }

    public void clickCreateBoard() {
        safeClick(createBoardButton);
    }

    public void openSearchPage() {
        driver.get("https://trello.com/search");
    }

    public void search(String text) {
        requireNonBlank(text, "text");
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(advancedSearchInput));
        input.sendKeys(text);
    }

    public boolean isSearchResultRelevant(String expectedText) {
        requireNonBlank(expectedText, "expectedText");
        final List<WebElement> results;
        try {
            results = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResults));
        } catch (TimeoutException e) {
            if (driver.findElements(searchResults).isEmpty()) {
                return false;
            }
            throw new TimeoutException(
                    "Search hits are present in the DOM but did not become visible in time "
                            + "(slow UI, overlay, or outdated locators).",
                    e);
        }
        for (WebElement result : results) {
            if (result.getText().contains(expectedText)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNoSearchResults() {
        return driver.findElements(searchResults).isEmpty();
    }

    public void openNotifications() {
        safeClick(notificationsButton);
    }

    public void openInfo() {
        dismissOverlays();
        safeClick(infoButton);
    }

    public boolean isInfoVisible() {
        WebElement info = wait.until(ExpectedConditions.visibilityOfElementLocated(infoMenu));
        return info.isDisplayed();
    }

    public boolean isNotificationsVisible() {
        WebElement notifications = wait.until(ExpectedConditions.visibilityOfElementLocated(notificationsButton));
        String controlsId = notifications.getAttribute("aria-controls");
        if (controlsId == null || controlsId.isEmpty()) {
            controlsId = notifications.getAttribute("aria-owns");
        }
        if (controlsId != null && !controlsId.isEmpty()) {
            return !driver.findElements(By.id(controlsId)).isEmpty();
        }
        String expanded = notifications.getAttribute("aria-expanded");
        return "true".equalsIgnoreCase(expanded);
    }

    public boolean isCreateButtonOpened() {
        WebElement createBoard = wait.until(ExpectedConditions.visibilityOfElementLocated(createBoardButton));
        return createBoard.isDisplayed();
    }

    public boolean isCreateBoardModalOpened() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(createBoardModalTitleInput)).isDisplayed();
    }

    public boolean isBoardVisible(String boardName) {
        requireNonBlank(boardName, "boardName");
        return !driver.findElements(By.xpath("//span[text()=" + asXpathLiteral(boardName) + "]")).isEmpty();
    }

    public boolean isBoardVisibleByShortLink(String shortLink) {
        requireNonBlank(shortLink, "shortLink");
        return !driver.findElements(By.cssSelector("a[href*='/b/" + shortLink + "']")).isEmpty();
    }

    public boolean isBoardVisibleByShortLinkAndName(String shortLink, String boardName) {
        requireNonBlank(shortLink, "shortLink");
        requireNonBlank(boardName, "boardName");
        return !driver.findElements(By.xpath(
                "//a[contains(@href,'/b/" + shortLink + "') and .//span[text()=" + asXpathLiteral(boardName) + "]]"
        )).isEmpty();
    }

}
