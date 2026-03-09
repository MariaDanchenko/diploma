    package pages;

    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;

    import java.time.Duration;

    public class HomePage {

        private WebDriver driver;
        private WebDriverWait wait;
        protected final By trelloButton = By.cssSelector(
                "a[href='https://trello.com/appSwitcherLogin?login_hint=mashadanchenko75@gmail.com']");

        private By homeHeader = By.xpath("//h1");

        public HomePage(WebDriver driver) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        }

        public String getHeaderText() {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(homeHeader)).getText();
        }

        public void clickTrelloButton() {
            driver.findElement(trelloButton).click();
        }
    }
