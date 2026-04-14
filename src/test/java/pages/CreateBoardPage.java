package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CreateBoardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By boardTitle = By.cssSelector("input[data-testid='create-board-title-input']");
    private final By createButton = By.cssSelector("button[data-testid='create-board-submit-button']");

    public CreateBoardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void inputTitle(String text) {
        WebElement titleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(boardTitle));
        titleInput.sendKeys(text);
    }

    public void clickCreateButton() {
        WebElement createBtn = wait.until(ExpectedConditions.presenceOfElementLocated(createButton));
        safeClick(createBtn);
    }

    private void safeClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }
}
