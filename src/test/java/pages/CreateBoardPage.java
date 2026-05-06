package pages;

import config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CreateBoardPage extends BasePage {

    private final By boardTitle = By.cssSelector("input[data-testid='create-board-title-input']");
    private final By createButton = By.cssSelector("button[data-testid='create-board-submit-button']");

    public CreateBoardPage(WebDriver driver) {
        super(driver, TestConfig.DEFAULT_WAIT);
    }

    public void inputTitle(String text) {
        WebElement titleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(boardTitle));
        titleInput.sendKeys(text);
    }

    public void clickCreateButton() {
        safeClick(createButton);
    }
}
