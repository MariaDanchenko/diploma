package pages;

import config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class LoginPage extends BasePage {

    private final By usernameField = By.cssSelector("input[data-testid='username'], input[name='username']");
    private final By continueButton = By.cssSelector("#login-submit, [data-testid='login-submit']");
    private final By passwordField = By.cssSelector("input#password, input[name='password']");
    private final List<By> authErrorLocators = List.of(
            By.cssSelector("[data-testid='form-error--content']"),
            By.cssSelector("[data-testid='form-error']"),
            By.cssSelector("div[role='alert']"),
            By.id("username-error"),
            By.cssSelector("#login-error")
    );

    public LoginPage(WebDriver driver) {
        super(driver, TestConfig.DEFAULT_WAIT);
    }

    public void enterUsername(String username) {
        requireNonBlank(username, "username");
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        usernameInput.sendKeys(username);
    }

    public void enterPassword(String password) {
        requireNonBlank(password, "password");
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordInput.sendKeys(password);
    }

    public void clickContinueButton() {
        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueBtn.click();
    }

    public void login(String username, String password) {
        requireNonBlank(username, "username");
        requireNonBlank(password, "password");
        enterUsername(username);
        clickContinueButton();

        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));

        enterPassword(password);
        clickContinueButton();
    }

    public void submitUsernameStep(String usernameOrEmail) {
        requireNonBlank(usernameOrEmail, "usernameOrEmail");
        enterUsername(usernameOrEmail);
        clickContinueButton();
    }

    public boolean isAuthErrorVisible() {
        WebDriverWait errorWait = new WebDriverWait(driver, TestConfig.AUTH_ERROR_WAIT);
        try {
            return errorWait.until(driver -> {
                try {
                    WebElement user = driver.findElement(usernameField);
                    if ("true".equalsIgnoreCase(user.getAttribute("aria-invalid"))) {
                        return true;
                    }
                } catch (NoSuchElementException ignored) {
                }
                for (By by : authErrorLocators) {
                    for (WebElement el : driver.findElements(by)) {
                        if (el.isDisplayed()) {
                            return true;
                        }
                    }
                }
                return false;
            });
        } catch (TimeoutException e) {
            return false;
        }
    }
}
