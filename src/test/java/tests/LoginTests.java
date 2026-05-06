package tests;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;

import java.time.Duration;

public class LoginTests extends BaseIsolatedTest {

    @BeforeMethod(alwaysRun = true)
    @Override
    public void setUpIsolatedDriver() {
        super.setUpIsolatedDriver();
        driver.get("https://id.atlassian.com/login?application=trello");
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
