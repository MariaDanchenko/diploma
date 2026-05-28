package tests;

import listener.Listener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(Listener.class)
public abstract class BaseIsolatedTest {

    protected WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpIsolatedDriver() {
        driver = new ChromeDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownIsolatedDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
