package tests;

import api.TrelloApiConfig;
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
import pages.LoginPage;
import pages.TrelloHomePage;

import java.time.Duration;

@Listeners(Listener.class)
public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected LoginPage loginPage;
    protected TrelloHomePage trelloHomePage;
    private static volatile WebDriver sharedDriver;
    private static volatile WebDriverWait sharedWait;
    private static volatile boolean isAuthenticated;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeSuite(alwaysRun = true)
    public synchronized void globalSetUp() {
        TrelloApiConfig.validateUiCredentials();

        if (sharedDriver == null) {
            WebDriverManager.chromedriver().setup();
            sharedDriver = new ChromeDriver();
            sharedWait = new WebDriverWait(sharedDriver, Duration.ofSeconds(30));
        }

        if (!isAuthenticated) {
            sharedDriver.get("https://id.atlassian.com/login?application=trello&continue=https%3A%2F%2Ftrello.com%2Fauth%2Fatlassian%2Fcallback%3FreturnUrl%3D%252Fu%252Fuser51818084%252Fboards%26display%3D%26aaOnboarding%3D%26updateEmail%3D%26traceId%3D%26ssoVerified%3D%26createMember%3D%26jiraInviteLink%3D");

            LoginPage suiteLoginPage = new LoginPage(sharedDriver);
            String email = System.getenv("TRELLO_EMAIL");
            String password = System.getenv("TRELLO_PASSWORD");
            suiteLoginPage.login(email, password);

            TrelloHomePage suiteTrelloHomePage = new TrelloHomePage(sharedDriver);
            sharedWait.until(ExpectedConditions.urlContains("trello.com"));
            sharedWait.until(driver -> suiteTrelloHomePage.isOnHomePage());
            suiteTrelloHomePage.dismissBlockingOverlaysIfPresent();
            isAuthenticated = true;
        }
    }

    @BeforeClass(alwaysRun = true)
    public void setUpClassContext() {
        driver = sharedDriver;
        wait = sharedWait;
        loginPage = new LoginPage(driver);
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
