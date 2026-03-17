package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Duration;

public class TrelloHomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    private final By profileButton = By.xpath("//button[@data-testid = 'header-member-menu-button']");
    private final By boardsLink = By.cssSelector("a[href = '/u/user51818084/boards']");
    private final By templatesLink = By.cssSelector("a[href = '/templates']");
    private final By homeLink = By.cssSelector("div[data-testid = 'team25-header-logo']");
    private final By createButton = By.cssSelector("button[data-testid = 'header-create-menu-button']");
    private final By searchInput = By.cssSelector("input[data-testid='cross-product-search-input-skeleton']");
    private final By notificationsButton = By.cssSelector("button[data-testid = 'header-notifications-button']");
    private final By infoButton = By.cssSelector("button[data-testid = 'header-info-button']");
    private final By accountMenu = By.cssSelector("#account-menu-account-section-title");
    private final By infoMenu = By.cssSelector("h3[class = 'oksVR59krTcAPX']");
    private final By notificationsMenu = By.cssSelector("h2[class = 'HsONwDlXlvyo7z']");
    private final By boardsHeader = By.cssSelector("h3[class = 'xtkiiaSp5ulDJM']");
    private final By templatesHeader = By.cssSelector("h1[class = 'eEr3CRE26U2u5R']");
    private final By createBoardButton = By.cssSelector("button[data-testid = 'header-create-board-button']");
    private final By createBoard = By.cssSelector("button[data-testid = 'create-board-tile']");

    public TrelloHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOnHomePage() {
        return wait.until(ExpectedConditions.urlContains("trello.com/"));
    }

    public void openProfileMenu() {
        driver.findElement(profileButton).click();
    }

    public String isProfileMenuVisible() {
        WebElement textAccountMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(accountMenu));
        return textAccountMenu.getText();
    }

    public void openInfo() {
        driver.findElement(infoButton).click();
    }

    public boolean isInfoVisible() {
        WebElement textInfoMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(infoMenu));
        return textInfoMenu.isDisplayed();
    }

    public void openNotifications() {
        driver.findElement(notificationsButton).click();
    }

    public boolean isNotificationsVisible() {
        WebElement textNotificationsMenu = wait.until(ExpectedConditions.
                visibilityOfElementLocated(notificationsMenu));
        return textNotificationsMenu.isDisplayed();
    }

    public void openBoardsLink() {
        driver.findElement(boardsLink).click();
    }

    public boolean isBoardsVisible() {
        WebElement textBoardsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(boardsHeader));
        return textBoardsHeader.isDisplayed();
    }

    public void openTemplates() {
        driver.findElement(templatesLink).click();
    }

    public boolean isTemplatesVisible() {
        WebElement headerTemplates = wait.until(ExpectedConditions.visibilityOfElementLocated(templatesHeader));
        return headerTemplates.isDisplayed();
    }

    public void openMainPage() {
        driver.findElement(homeLink).click();
    }

    public void clickCreateButton() {
        driver.findElement(createButton).click();
    }

    public boolean isCreateButtonOpened() {
        WebElement createBoard = wait.until(ExpectedConditions.visibilityOfElementLocated(createBoardButton));
        return createBoard.isDisplayed();
    }

    public void search(String text) throws InterruptedException {
        WebElement searchInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        searchInputElement.click();
        searchInputElement.clear();

        searchInputElement.sendKeys(text);

        // подождать появление dropdown (если нужно)
        Thread.sleep(500);

        searchInputElement.sendKeys(Keys.ARROW_DOWN);
        searchInputElement.sendKeys(Keys.ENTER);
    }

    public String isTextVisible() {
        WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        return inputField.getCssValue("value");
    }

    public void createBoard() {
        WebElement createBoardButton = wait.until(ExpectedConditions.visibilityOfElementLocated(createBoard));
        createBoardButton.click();
    }
}
