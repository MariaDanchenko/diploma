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
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

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

    protected static String asXpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        String[] parts = value.split("'");
        StringBuilder xpathBuilder = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                xpathBuilder.append(", \"'\", ");
            }
            xpathBuilder.append("'").append(parts[i]).append("'");
        }
        xpathBuilder.append(")");
        return xpathBuilder.toString();
    }

    protected static void requireNonBlank(String value, String paramName) {
        Objects.requireNonNull(value, paramName + " must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(paramName + " must not be blank");
        }
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

        FluentWait<WebDriver> overlayWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofMillis(TestConfig.OVERLAY_DISMISS_TIMEOUT_MS))
                .pollingEvery(Duration.ofMillis(TestConfig.OVERLAY_POLL_INTERVAL_MS))
                .ignoring(StaleElementReferenceException.class, WebDriverException.class);

        try {
            overlayWait.until(d -> {
                try {
                    new Actions(d).sendKeys(Keys.ESCAPE).perform();
                } catch (WebDriverException ignored) {
                }

                List<WebElement> overlays = d.findElements(dismissSelector);
                boolean anyVisible = false;
                for (WebElement element : overlays) {
                    try {
                        if (!element.isDisplayed()) {
                            continue;
                        }
                        anyVisible = true;
                        try {
                            element.click();
                        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                            ((JavascriptExecutor) d).executeScript("arguments[0].click();", element);
                        }
                    } catch (StaleElementReferenceException ignored) {
                    }
                }
                return !anyVisible;
            });
        } catch (TimeoutException ignored) {
            // Best-effort overlay dismissal
        }
    }
}
