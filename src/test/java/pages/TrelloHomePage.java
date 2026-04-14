package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TrelloHomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By profileButton = By.xpath("//button[@data-testid = 'header-member-menu-button']");
    private final By createMenuButton = By.cssSelector("button[data-testid = 'header-create-menu-button']");
    private final By createBoardButton = By.cssSelector("button[data-testid='header-create-board-button']");
    private final By createBoardModalTitleInput = By.cssSelector("input[data-testid='create-board-title-input']");

    private final By searchInput = By.cssSelector("input[data-testid='cross-product-search-input-skeleton']");
    private final By advancedSearchInput = By.cssSelector("input[data-testid='advanced-search-input']");
    private final By searchResults = By.cssSelector("a[data-testid='advanced-search-board-result-item']");

    private final By boardsLink = By.xpath("//a[contains(@href,'/boards')]");
    private final By templatesLink = By.xpath("//a[contains(@href,'/templates')]");

    private final By homeLink = By.cssSelector("div[data-testid = 'team25-header-logo']");

    private final By notificationsButton = By.cssSelector("button[data-testid = 'header-notifications-button']");
    private final By infoButton = By.cssSelector("button[data-testid = 'header-info-button']");

    private final By accountMenu = By.id("account-menu-account-section-title");

    private final By infoMenu = By.id("header-info-menu-popover-content");
    private final By createBoard = By.cssSelector("button[data-testid = 'create-board-tile']");

    public TrelloHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Закрывает типичные блокирующие оверлеи (онбординг, cookie-баннеры, объявления), если они есть.
     * Не бросает исключений, если попапа нет.
     */
    public void dismissBlockingOverlaysIfPresent() {
        List<By> dismissSelectors = List.of(
                By.id("onetrust-accept-btn-handler"),
                By.cssSelector("button#onetrust-accept-btn-handler"),
                By.xpath("//button[.//span[normalize-space()='Отклонить']]"),
                By.xpath("//button[normalize-space()='Отклонить']"),
                By.cssSelector("[role='dialog'] [data-testid='close-button']"),
                By.cssSelector("[data-testid='close-button']"),
                By.cssSelector("button[data-testid='popover-close-button']"),
                By.xpath("//button[contains(@aria-label,'Close') or contains(@aria-label,'close')]")
        );

        long deadline = System.currentTimeMillis() + 8000;
        int idleRounds = 0;
        while (System.currentTimeMillis() < deadline && idleRounds < 2) {
            boolean dismissedAny = false;
            try {
                new Actions(driver).pause(Duration.ofMillis(100)).sendKeys(Keys.ESCAPE).perform();
            } catch (WebDriverException ignored) {
            }

            for (By selector : dismissSelectors) {
                try {
                    for (WebElement el : driver.findElements(selector)) {
                        if (!el.isDisplayed()) {
                            continue;
                        }
                        try {
                            el.click();
                        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                        }
                        dismissedAny = true;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }

            if (dismissedAny) {
                idleRounds = 0;
            } else {
                idleRounds++;
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
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
        dismissBlockingOverlaysIfPresent();
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
        wait.until(ExpectedConditions.urlContains("/boards"));
        return true;
    }

    public void openTemplatesPage() {
        safeClick(templatesLink);
    }

    public boolean isTemplatesPageOpened() {
        wait.until(ExpectedConditions.urlContains("/templates"));
        return true;
    }

    public void goToHomePage() {
        safeClick(homeLink);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(profileButton));
        } catch (TimeoutException ignored) {
        }
        dismissBlockingOverlaysIfPresent();
    }

    private void safeClick(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
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
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(advancedSearchInput));
        input.sendKeys(text);
    }

    public boolean isSearchResultRelevant(String expectedText) {
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
        dismissBlockingOverlaysIfPresent();
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
        return !driver.findElements(By.xpath("//span[text()='" + boardName + "']")).isEmpty();
    }

    public boolean isBoardVisibleByShortLink(String shortLink) {
        return !driver.findElements(By.cssSelector("a[href*='/b/" + shortLink + "']")).isEmpty();
    }

    public boolean isBoardVisibleByShortLinkAndName(String shortLink, String boardName) {
        return !driver.findElements(By.xpath(
                "//a[contains(@href,'/b/" + shortLink + "') and .//span[text()='" + boardName + "']]"
        )).isEmpty();
    }
}
