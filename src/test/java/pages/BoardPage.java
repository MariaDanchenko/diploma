package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BoardPage {

    private WebDriver driver;
    private WebDriverWait wait;

    //Board
    private final By boardHeader = By.cssSelector("h1[data-testid='board-name-display']");

    //Lists & Cards
    private final By lists = By.cssSelector("div[data-testid='list']");
    private final By cards = By.cssSelector("a[data-testid='card-name']");
    private final By addListButton = By.cssSelector("button[data-testid='list-composer-button']");
    private final By textAreaList = By.cssSelector("textarea[data-testid='list-name-textarea']");
    private final By addListConfirmButton = By.cssSelector("button[data-testid='list-composer-add-list-button']");

    //Add card
    private final By addCardButton = By.cssSelector("button[data-testid='list-add-card-button']");
    private final By inputText = By.cssSelector("textarea[data-testid='list-card-composer-textarea']");
    private final By addCard = By.cssSelector("button[data-testid='list-card-composer-add-card-button']");

    // Edit card
    private final By cardTitleInput = By.cssSelector("textarea[data-testid='card-back-title-input']");
    private final By closeCardButton = By.cssSelector("button[aria-label='Закрыть диалоговое окно']");


    private final By menuButton = By.cssSelector(
            "button[class='QCfb_k37Q8MX7C PhzBALMp63PY_y ybVBgfOiuWZJtD Yt_v_LmarJM9ZS']");

    //Локаторы для удаления доски
    private final By closeBoardButton = By.xpath("//button[.//div[contains(text(), 'Закрыть доску')]]");
    private final By confirmCloseButton = By.cssSelector("button[data-testid='popover-close-board-confirm']");
    private final By deleteBoardButton = By.cssSelector("button[data-testid='close-board-delete-board-button']");
    private final By confirmDeleteButton = By.cssSelector("button[data-testid='close-board-delete-board-confirm-button']");
    private final By headerCloseBoard = By.cssSelector("div[class='q8mBNw86hnV81W']");
    private final By deleteHeader = By.cssSelector("h2[class='VmbXKMJLSqfD0U']");
    private final By addNewColumn = By.cssSelector("button[data-testid='list-composer-button']");

    private final By cardsInList = By.cssSelector("a[data-testid='card-name']");

    public BoardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getBoardHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(boardHeader)).getText();
    }

    public boolean isBoardLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(boardHeader)).isDisplayed();
    }

    public void addList(String listName) {

        WebElement addListBtn = wait.until(ExpectedConditions.elementToBeClickable(addListButton));
        addListBtn.click();

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(textAreaList));
        input.sendKeys(listName);

        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(addListConfirmButton));
        confirmBtn.click();
    }

    private WebElement getFirstList() {
        List<WebElement> allLists = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(lists)
        );

        return allLists.get(0);
    }

    public void addCardToFirstList(String title) {
        WebElement list = getFirstList();

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                list.findElement(addCardButton)
        ));
        addBtn.click();

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputText));
        input.sendKeys(title);

        driver.findElement(addCard).click();

        // Ждём появления карточки
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@data-testid='card-name' and text()='" + title + "']")
        ));
    }

    public boolean isCardExists(String title) {
        return !driver.findElements(
                By.xpath("//a[@data-testid='card-name' and text()='" + title + "']")
        ).isEmpty();
    }

    public void openCard(String title) {
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@data-testid='card-name' and text()='" + title + "']")
        ));
        card.click();
    }

    public void editCard(String oldTitle, String newTitle) {
        openCard(oldTitle);

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(cardTitleInput));

        input.clear();
        input.sendKeys(newTitle);

        driver.findElement(closeCardButton).click();
    }

    public void deleteBoard() {

        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(menuButton));
        menu.click();

        WebElement closeBoard = wait.until(ExpectedConditions.elementToBeClickable(closeBoardButton));
        closeBoard.click();

        wait.until(ExpectedConditions.elementToBeClickable(confirmCloseButton)).click();

        wait.until(ExpectedConditions.elementToBeClickable(deleteBoardButton)).click();

        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
    }
}
