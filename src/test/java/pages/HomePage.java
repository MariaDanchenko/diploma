    package pages;

    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;

    import java.time.Duration;
    import java.util.ArrayList;

    public class HomePage {

        private WebDriver driver;
        private WebDriverWait wait;
        protected final By trelloButton = By.cssSelector("a[href*='trello.com/appSwitcherLogin']");

        private By homeHeader = By.xpath("//h1");

        public HomePage(WebDriver driver) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        }

        public String getHeaderText() {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(homeHeader)).getText();
        }

        public void clickTrelloButton() {
            String currentWindowHandle = driver.getWindowHandle();

            WebElement trelloBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(trelloButton));
            trelloBtn.click();

            wait.until(ExpectedConditions.numberOfWindowsToBe(2));

            ArrayList<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
            windowHandles.remove(currentWindowHandle);
            driver.switchTo().window(windowHandles.get(0));
        }
    }
