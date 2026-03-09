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
import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;

public class LoginTests {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().clearDriverCache().setup();

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
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("mashadanchenko75@gmail.com", "Abkzabkz66");

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("home"));

        HomePage homePage = new HomePage(driver);
        Assert.assertEquals(homePage.getHeaderText(), "Здравствуйте, Мария!");
    }

    @Test
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("mashadanchenko75@gmail.com", "wrongPassword");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Неверный адрес"));
    }
}
