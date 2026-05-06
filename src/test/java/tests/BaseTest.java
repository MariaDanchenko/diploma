package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import listener.Listener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
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
    private static volatile WebDriver sharedDriver;
    private static volatile WebDriverWait sharedWait;
    private static volatile boolean isAuthenticated;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeSuite(alwaysRun = true)
    public synchronized void globalSetUp() {
        if (sharedDriver == null) {
            WebDriverManager.chromedriver().setup();
            sharedDriver = new ChromeDriver();
            sharedWait = new WebDriverWait(sharedDriver, Duration.ofSeconds(30));
        }

        if (!isAuthenticated) {
            sharedDriver.get("https://id.atlassian.com/login?application=trello");

            LoginPage suiteLoginPage = new LoginPage(sharedDriver);
            String email = System.getenv("TRELLO_EMAIL");
            String password = System.getenv("TRELLO_PASSWORD");
            suiteLoginPage.login(email, password);

            sharedWait.until(driver -> {
                String url = driver.getCurrentUrl();
                return url.contains("home") || url.contains("trello.com");
            });

            HomePage suiteHomePage = new HomePage(sharedDriver);
            suiteHomePage.clickTrelloButton();

            sharedWait.until(ExpectedConditions.urlContains("trello.com/"));
            new TrelloHomePage(sharedDriver).dismissBlockingOverlaysIfPresent();
            isAuthenticated = true;
        }
    }

    @BeforeClass(alwaysRun = true)
    public void setUpClassContext() {
        driver = sharedDriver;
        wait = sharedWait;
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        trelloHomePage = new TrelloHomePage(driver);
    }

    @BeforeMethod(alwaysRun = true)
    public void dismissRandomOverlayBeforeEachTest() {
        if (driver == null || trelloHomePage == null) {
            return;
        }
        try {
            trelloHomePage.dismissBlockingOverlaysIfPresent();
        } catch (TimeoutException ignored) {
            // Continue test execution even if no overlay appeared.
        }
    }

    @AfterSuite(alwaysRun = true)
    public void globalTearDown() {
        if (sharedDriver != null) {
            sharedDriver.quit();
            sharedDriver = null;
            sharedWait = null;
            isAuthenticated = false;
        }
    }

}
