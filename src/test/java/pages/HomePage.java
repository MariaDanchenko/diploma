package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By trelloButton = By.cssSelector("a[href*='trello.com/appSwitcherLogin']");
    private final By homeHeader = By.xpath("//h1");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getHeaderText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(homeHeader)).getText();
    }

    public void clickTrelloButton() {
        String currentWindowHandle = driver.getWindowHandle();
        int windowsBeforeClick = driver.getWindowHandles().size();

        WebElement trelloBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(trelloButton));
        trelloBtn.click();

        wait.until(driver1 -> {
            boolean openedInSameWindow = driver1.getCurrentUrl().contains("trello.com");
            boolean openedInNewWindow = driver1.getWindowHandles().size() > windowsBeforeClick;
            return openedInSameWindow || openedInNewWindow;
        });

        ArrayList<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
        if (windowHandles.size() > windowsBeforeClick) {
            for (String handle : windowHandles) {
                if (!handle.equals(currentWindowHandle)) {
                    driver.switchTo().window(handle);
                    if (driver.getCurrentUrl().contains("trello.com")) {
                        return;
                    }
                }
            }
            driver.switchTo().window(windowHandles.get(windowHandles.size() - 1));
        }
    }
}
