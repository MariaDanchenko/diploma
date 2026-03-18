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

    private final By boardHeader = By.cssSelector("h1[data-testid='board-name-display']");
    //Локаторы для столбцов
    private final By todayList = By.xpath("//span[@class='h3iSNmIaIueSDt' and text()='Сегодня']");
    private final By thisWeekList = By.xpath("//span[@class='h3iSNmIaIueSDt' and text()='На этой неделе']");
    private final By laterList = By.xpath("//span[@class='h3iSNmIaIueSDt' and text()='Позже']");

    //Локаторы для добавления карточки
    private final By addCardButton = By.cssSelector("button[data-testid='list-add-card-button']");
    private final By inputText = By.cssSelector("textarea[data-testid='list-card-composer-textarea']");
    private final By addCard = By.cssSelector("button[data-testid='list-card-composer-add-card-button']");
    private final By cardTitle = By.cssSelector("a[data-testid='card-name']");


    private final By menuButton = By.cssSelector(
            "button[class='QCfb_k37Q8MX7C PhzBALMp63PY_y ybVBgfOiuWZJtD Yt_v_LmarJM9ZS']");

    //Локаторы для удаления доски
    private final By closeBoardButton = By.xpath("//button[.//div[contains(text(), 'Закрыть доску')]]");
    private final By closeButton = By.cssSelector("button[data-testid='popover-close-board-confirm']");
    private final By deleteBoardButton = By.cssSelector("button[data-testid='close-board-delete-board-button']");
    private final By deleteButton = By.cssSelector("button[data-testid='close-board-delete-board-confirm-button']");
    private final By headerCloseBoard = By.cssSelector("div[class='q8mBNw86hnV81W']");
    private final By deleteHeader = By.cssSelector("h2[class='VmbXKMJLSqfD0U']");
    private final By addNewColumn = By.cssSelector("button[data-testid='list-composer-button']");

    private final By cardsInList = By.cssSelector("a[data-testid='card-name']");

    public BoardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getBoardHeader() {
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(boardHeader));
        return header.getText();
    }

    public void clickMenuButton() {
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(menuButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", menu);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menu);
    }

    public void clickCloseBoard() {
        WebElement closeBoard = wait.until(ExpectedConditions.elementToBeClickable(closeBoardButton));
        closeBoard.click();
    }

    public void clickCloseButton() {
        WebElement close = wait.until(ExpectedConditions.elementToBeClickable(closeButton));
        close.click();
    }

    public boolean isClosedBoardHeaderContains(String text) {
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(headerCloseBoard));
        String headerText = header.getText();
        return headerText.contains(text);
    }

    public void clickDeleteBoardButton() {
        WebElement deleteBoard = wait.until(ExpectedConditions.elementToBeClickable(deleteBoardButton));
        deleteBoard.click();
    }

    public void clickDeleteButton() {
        WebElement delete = wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        delete.click();
    }

    public void addCard(String name, String title) {
        WebElement list = getListByName(name);

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(list.findElement(addCardButton)));
        addBtn.click();

        WebElement titleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(inputText));
        titleInput.sendKeys(title);

        driver.findElement(addCard).click();
    }

    private WebElement getListByName(String name) {
        By listLocator = By.xpath(
                "//div[@data-testid='list']//span[text()='" + name + "']/ancestor::div[@data-testid='list']"
        );

        return wait.until(ExpectedConditions.visibilityOfElementLocated(listLocator));
    }

    public boolean isCardDisplayedInList(String name, String title) {
        //Получаем список
        WebElement list = getListByName(name);

        //Получаем все карточки внутри списка
        List<WebElement> cards = list.findElements(cardsInList);

        //Проходим по списку
        for (int i = 0; i < cards.size(); i++) {
            WebElement currentCard = cards.get(i);
            String cardText = currentCard.getText();

            if (cardText.equals(title)) {
                return true;
            }
        }
        return false;
    }
}
