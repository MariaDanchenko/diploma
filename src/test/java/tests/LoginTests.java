package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;

import java.time.Duration;

public class LoginTests {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.get("https://id.atlassian.com/login?application=trello");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSuccessLogin() {
        String email = System.getenv("TRELLO_EMAIL");
        String password = System.getenv("TRELLO_PASSWORD");
        Assert.assertNotNull(email, "TRELLO_EMAIL must be set in the environment");
        Assert.assertNotNull(password, "TRELLO_PASSWORD must be set in the environment");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);

        new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.urlContains("home"));
        Assert.assertTrue(driver.getCurrentUrl().contains("home"));
    }

    @Test
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.submitUsernameStep("not-valid-email-format");

        Assert.assertTrue(loginPage.isAuthErrorVisible(), "Expected auth error is not visible");
    }
}
