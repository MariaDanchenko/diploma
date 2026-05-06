package pages;

import config.TestConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class BoardPage extends BasePage {

    private final By boardHeader = By.cssSelector("h1[data-testid='board-name-display']");
    private final By lists = By.cssSelector("div[data-testid='list']");
    private final By cards = By.cssSelector("a[data-testid='card-name']");
    private final By textAreaList = By.cssSelector(
            "textarea[data-testid='list-name-textarea'], input[data-testid='list-name-textarea'], " +
                    "textarea[data-testid='list-composer-editor-input'], " +
                    "form[data-testid='list-composer'] textarea, form[data-testid='list-composer'] input, " +
                    "textarea[name='listName'], input[name='listName'], " +
                    "textarea[placeholder*='list title'], input[placeholder*='list title']"
    );
    private final By addListConfirmButton = By.cssSelector(
            "button[data-testid='list-composer-add-list-button'], " +
                    "button[data-testid*='list-composer-add-list']"
    );

    private final By addCardButton = By.cssSelector("button[data-testid='list-add-card-button']");
    private final By inputText = By.cssSelector("textarea[data-testid='list-card-composer-textarea']");
    private final By addCard = By.cssSelector("button[data-testid='list-card-composer-add-card-button']");

    private final By editButton = By.cssSelector("button[data-testid='quick-card-editor-button']");
    private final By cardTitleInput = By.cssSelector("textarea[data-testid='quick-card-editor-card-title']");
    private final By saveButton = By.cssSelector(
            "button[data-testid='quick-card-editor-save-button'], div[data-testid='quick-card-editor'] button[type='submit'], button[type='submit']"
    );
    private final By moveCardButton = By.cssSelector("button[data-testid='quick-card-editor-move']");
    private final By selectListButton = By.cssSelector("div[data-testid='move-card-popover-select-list-destination']");
    private final By moveConfirm = By.cssSelector("button[data-testid='move-card-popover-move-button']");
    private final By completeCardButton = By.cssSelector("button[data-testid='card-done-state-completion-button']");
    private final By cardBackButton = By.cssSelector("button[data-testid='card-back-actions-button']");
    private final By archiveCardButton = By.cssSelector("button[data-testid='card-back-archive-button']");
    private final By menuButton = By.xpath(
            "//button[@data-testid='board-menu-show-menu-button' " +
                    "or contains(@data-testid,'board-menu') " +
                    "or .//*[@data-testid='OverflowMenuHorizontalIcon']]"
    );

    private final By closeBoardButton = By.cssSelector("div[data-testid='board-menu-container'] li:last-child button");
    private final By confirmCloseButton = By.cssSelector("button[data-testid='popover-close-board-confirm']");
    private final By deleteBoardButton = By.cssSelector("button[data-testid='close-board-delete-board-button']");
    private final By confirmDeleteButton = By.cssSelector("button[data-testid='close-board-delete-board-confirm-button']");

    public BoardPage(WebDriver driver) {
        super(driver, TestConfig.LONG_WAIT);
    }

    public String getBoardHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(boardHeader)).getText();
    }

    public boolean isBoardLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(boardHeader)).isDisplayed();
    }

    private static WebElement firstVisible(List<WebElement> elements) {
        for (WebElement el : elements) {
            try {
                if (el.isDisplayed() && el.getSize().getHeight() > 0 && el.getSize().getWidth() > 0) {
                    return el;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return null;
    }

    private boolean isListComposerInputVisible() {
        return firstVisible(driver.findElements(textAreaList)) != null;
    }

    public void addList(String listName) {
        dismissOverlays();

        ((JavascriptExecutor) driver).executeScript(
                "const c = document.querySelector('[data-testid=\"board\"]') || document.querySelector('[class*=\"board\"][class*=\"canvas\"]');"
                        + " if (c) { c.scrollLeft = c.scrollWidth; }"
        );

        if (!isListComposerInputVisible()) {
            By openListComposer = By.cssSelector(
                    "button[data-testid='list-composer-button'], button[data-testid='list-add-another-list-button']"
            );
            WebElement openComposer = wait.until(ExpectedConditions.elementToBeClickable(openListComposer));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'end'});", openComposer);
            safeClick(openListComposer);
        }

        WebElement input = wait.until(d -> firstVisible(d.findElements(textAreaList)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);

        try {
            input.clear();
            input.sendKeys(listName);
        } catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                    input,
                    listName
            );
        }

        try {
            input.sendKeys(Keys.ENTER);
        } catch (ElementNotInteractableException ignored) {
        }
        safeClick(addListConfirmButton);
    }

    public void addCardToFirstList(String title) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                safeClick(addCardButton);

                WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputText));
                try {
                    input.clear();
                    input.sendKeys(title);
                } catch (ElementNotInteractableException e) {
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                            input,
                            title
                    );
                }

                safeClick(addCard);

                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//a[@data-testid='card-name' and text()='" + title + "']")
                ));
                return;
            } catch (StaleElementReferenceException | TimeoutException e) {
                if (attempt == 2) {
                    throw e;
                }
                wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(addCardButton)));
            }
        }
    }

    public boolean isCardExists(String title) {
        return !driver.findElements(
                By.xpath("//a[@data-testid='card-name' and text()='" + title + "']")
        ).isEmpty();
    }

    public void editCard(String oldTitle, String newTitle) {
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@data-testid='card-name' and normalize-space(.)='" + oldTitle + "']")
        ));

        Actions actions = new Actions(driver);
        actions.moveToElement(card).perform();

        safeClick(editButton);

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(cardTitleInput));
        input.clear();
        input.sendKeys(newTitle);

        safeClick(saveButton);
    }

    public void deleteBoard() {
        dismissOverlays();

        safeClick(menuButton);
        safeClick(closeBoardButton);
        safeClick(confirmCloseButton);
        safeClick(menuButton);
        safeClick(deleteBoardButton);
        safeClick(confirmDeleteButton);

        wait.until(ExpectedConditions.urlContains("boards"));
    }

    public void movingCard(String title, String targetListName) {
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@data-testid='card-name' and normalize-space(.)='" + title + "']")
        ));

        Actions actions = new Actions(driver);
        actions.moveToElement(card).perform();

        safeClick(editButton);

        safeClick(moveCardButton);

        safeClick(selectListButton);

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[id='move-card-list-select']")
        ));

        input.clear();
        input.sendKeys(targetListName);

        input.sendKeys(Keys.ENTER);

        safeClick(moveConfirm);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(moveConfirm));
    }

    public boolean isCardInList(String cardTitle, String listName) {
        List<WebElement> allLists = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(lists));

        for (WebElement currentList : allLists) {
            WebElement header = currentList.findElement(By.cssSelector("h2"));
            String currentListName = header.getText();
            if (currentListName.equals(listName)) {
                List<WebElement> cardsInCurrentList = currentList.findElements(cards);
                for (WebElement currentCard : cardsInCurrentList) {
                    String currentCardTitle = currentCard.getText();
                    if (currentCardTitle.equals(cardTitle)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isListExists(String listName) {
        By listTitle = By.xpath("//div[@data-testid='list']//h2[normalize-space()='" + listName + "']");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(listTitle));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void completeCard(String cardTitle) {
        By cardByTitle = By.xpath("//a[@data-testid='card-name' and normalize-space(.)='" + cardTitle + "']");
        dismissOverlays();
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(cardByTitle));

        Actions actions = new Actions(driver);
        actions.moveToElement(card).perform();
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                safeClick(completeCardButton);
                return;
            } catch (TimeoutException e) {
                dismissOverlays();
                safeClick(cardByTitle);
            }
        }
        safeClick(completeCardButton);
    }

    public boolean isCompleteButtonSelected() {
        WebElement completeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(completeCardButton));
        String ariaPressed = completeButton.getAttribute("aria-pressed");
        String dataState = completeButton.getAttribute("data-state");
        String ariaLabel = completeButton.getAttribute("aria-label");
        String className = completeButton.getAttribute("class");
        String buttonText = completeButton.getText();

        return completeButton.isSelected()
                || "true".equalsIgnoreCase(ariaPressed)
                || "complete".equalsIgnoreCase(dataState)
                || (className != null && className.toLowerCase().contains("checked"))
                || containsCompletedState(ariaLabel)
                || containsCompletedState(buttonText);
    }

    private boolean containsCompletedState(String value) {
        if (value == null) {
            return false;
        }
        String normalized = value.toLowerCase();
        return normalized.contains("completed")
                || normalized.contains("complete")
                || normalized.contains("done")
                || normalized.contains("checked");
    }

    public void archiveCard(String cardTitle) {
        By cardByTitle = By.xpath("//a[@data-testid='card-name' and normalize-space(.)='" + cardTitle + "']");

        dismissOverlays();
        safeClick(cardByTitle);
        safeClick(cardBackButton);
        safeClick(archiveCardButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(cardByTitle));
    }

    public boolean isCardAbsentFromBoard(String cardTitle) {

        List<WebElement> cards = driver.findElements(
                By.xpath("//a[@data-testid='card-name' and normalize-space(.)='" + cardTitle + "']")
        );

        return cards.isEmpty();
    }
}
