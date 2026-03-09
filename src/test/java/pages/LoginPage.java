package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By usernameField = By.id("username-uid1");
    private By continueButton = By.id("login-submit");
    private By passwordField = By.id("password");
    private By errorMessage = By.xpath("//div[contains(text(), 'Неверный адрес электронной почты')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterUsername(String username) {
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        usernameInput.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordInput.sendKeys(password);
    }

    public void clickContinueButton() {

        driver.findElement(continueButton).click();
    }

    public HomePage login(String username, String password) {
        enterUsername(username);
        clickContinueButton();

        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));

        enterPassword(password);
        clickContinueButton();
        return new HomePage(driver);
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }
}
