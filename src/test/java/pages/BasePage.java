package pages;

import config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
    }

    protected BasePage(WebDriver driver) {
        this(driver, TestConfig.DEFAULT_WAIT);
    }

    protected void safeClick(By locator) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
                return;
            } catch (TimeoutException | ElementClickInterceptedException | StaleElementReferenceException e) {
                dismissOverlays();
                try {
                    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    return;
                } catch (StaleElementReferenceException ignored) {
                    if (attempt == 2) {
                        throw ignored;
                    }
                }
            }
        }
    }

    protected void safeClick(WebElement element) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                return;
            } catch (ElementClickInterceptedException e) {
                dismissOverlays();
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    return;
                } catch (StaleElementReferenceException ignored) {
                    if (attempt == 2) {
                        throw ignored;
                    }
                }
            } catch (StaleElementReferenceException e) {
                if (attempt == 2) {
                    throw e;
                }
            }
        }
    }

    public void dismissOverlays() {
        By dismissSelector = By.cssSelector("div[data-testid='spotlight--dialog-footer'] button:last-of-type");

        long deadline = System.currentTimeMillis() + 8000;
        int idleRounds = 0;
        while (System.currentTimeMillis() < deadline && idleRounds < 2) {
            boolean dismissedAny = false;
            try {
                new Actions(driver).pause(Duration.ofMillis(100)).sendKeys(Keys.ESCAPE).perform();
            } catch (WebDriverException ignored) {
            }

            try {
                for (WebElement element : driver.findElements(dismissSelector)) {
                    if (!element.isDisplayed()) {
                        continue;
                    }
                    safeClick(element);
                    dismissedAny = true;
                }
            } catch (StaleElementReferenceException ignored) {
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
}
