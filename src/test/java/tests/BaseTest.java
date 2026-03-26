package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import listener.Listener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import pages.HomePage;
import pages.LoginPage;
import pages.TrelloHomePage;

import java.time.Duration;

@Listeners(Listener.class)
public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected LoginPage loginPage;
    protected HomePage homePage;
    protected TrelloHomePage trelloHomePage;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().clearDriverCache().setup();

        driver = new ChromeDriver();
        driver.get("https://id.atlassian.com/login?application=trello");

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        loginPage = new LoginPage(driver);
        loginPage.login("mashadanchenko75@gmail.com", "Abkzabkz66");

        wait.until(ExpectedConditions.urlContains("home"));

        homePage = new HomePage(driver);
        homePage.clickTrelloButton();

        wait.until(ExpectedConditions.urlContains("trello.com/"));

        trelloHomePage = new TrelloHomePage(driver);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
