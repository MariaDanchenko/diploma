package tests;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import api.TrelloApiConfig;
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
        driver.get("https://id.atlassian.com/login?application=trello&continue=https%3A%2F%2Ftrello.com%2Fauth%2Fatlassian%2Fcallback%3Fdisplay%3DeyJ2ZXJpZmljYXRpb25TdHJhdGVneSI6InNvZnQifQ%253D%253D&display=eyJ2ZXJpZmljYXRpb25TdHJhdGVneSI6InNvZnQifQ%3D%3D");
    }

    @Test
    public void testSuccessLogin() {
        TrelloApiConfig.validateUiCredentials();
        String email = System.getenv("TRELLO_EMAIL");
        String password = System.getenv("TRELLO_PASSWORD");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);

        new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.urlContains("trello.com"));
        Assert.assertTrue(driver.getCurrentUrl().contains("trello.com"), "Expected Trello URL after successful login");
    }

    @Test
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.submitUsernameStep("not-valid-email-format");

        Assert.assertTrue(loginPage.isAuthErrorVisible(), "Expected auth error is not visible");
    }
}
