package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BoardPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private final By boardHeader = By.cssSelector("h1[data-testid='board-name-display']");
    private final By addCardToday = By.cssSelector("button[data-testid='list-add-card-button'][aria-label*='Сегодня']");
    private final By addCardThisWeek = By.cssSelector(
            "button[data-testid='list-add-card-button'][aria-label*='На этой неделе']");
    private final By addCardLater = By.cssSelector("button[data-testid='list-add-card-button'][aria-label*='Позже']");
    private final By inputText = By.cssSelector("textarea[data-testid='list-card-composer-textarea']");
    private final By addCard = By.cssSelector("button[data-testid='list-card-composer-add-card-button']");
    private final By cancelCard = By.cssSelector("button[data-testid='list-card-composer-cancel-button']");
    private final By menu = By.cssSelector(
            "button[class='QCfb_k37Q8MX7C PhzBALMp63PY_y ybVBgfOiuWZJtD Yt_v_LmarJM9ZS']");
    private final By addNewColumn = By.cssSelector("button[data-testid='list-composer-button']");

    public BoardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getBoardHeader() {
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(boardHeader));
        return header.getText();
    }
}
