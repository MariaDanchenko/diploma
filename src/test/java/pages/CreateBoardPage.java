package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CreateBoardPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private final By boardBackground = By.cssSelector("button[style*='rgb(220, 234, 254)']");
    private final By backgroundCheckIcon = By.cssSelector("span[data-testid='CheckIcon']");
    private final By boardTitle = By.cssSelector("input[data-testid='create-board-title-input']");
    private final By visibilityDropdown = By.cssSelector("div[data-testid='create-board-select-visibility']");
    private final By createButton = By.cssSelector("button[data-testid='create-board-submit-button']");

    public CreateBoardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void selectBackground() {
        WebElement background = wait.until(ExpectedConditions.elementToBeClickable(boardBackground));
        background.click();
    }

    public boolean isBackgroundSelected() {
        return driver.findElement(backgroundCheckIcon).isDisplayed();
    }

    public void inputTitle(String text) {
        driver.findElement(boardTitle).sendKeys(text);
    }

    public String getBoardTitle() {
        return driver.findElement(boardTitle).getAttribute("value");
    }

    public void openVisibilityDropdown() {
        driver.findElement(visibilityDropdown).click();
    }

    public void selectPrivateVisibility() {
    }

    public void clickCreateButton() {
        driver.findElement(createButton).click();
    }

}
