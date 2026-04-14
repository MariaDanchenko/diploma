package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BoardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    //Board
    private final By boardHeader = By.cssSelector("h1[data-testid='board-name-display']");

    //Lists & Cards
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

    //Add card
    private final By addCardButton = By.cssSelector("button[data-testid='list-add-card-button']");
    private final By inputText = By.cssSelector("textarea[data-testid='list-card-composer-textarea']");
    private final By addCard = By.cssSelector("button[data-testid='list-card-composer-add-card-button']");

    // Edit card
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


    private final By menuButton = By.cssSelector(
            "button[data-testid='board-menu-show-menu-button'], " +
                    "button[data-testid*='board-menu'], " +
                    "button[aria-label*='Board menu'], " +
                    "button[aria-label*='Show menu'], " +
                    "button[title*='Show menu']"
    );

    //Локаторы для удаления доски
    private final By closeBoardButton = By.cssSelector(
            "button[data-testid='board-menu-close-board-button'], " +
                    "button[data-testid='close-board-button'], " +
                    "button[data-testid='board-menu-item-close-board'], " +
                    "button[data-testid*='close-board']:not([data-testid*='confirm']):not([data-testid*='delete'])"
    );
    private final By confirmCloseButton = By.cssSelector("button[data-testid='popover-close-board-confirm']");
    private final By deleteBoardButton = By.cssSelector("button[data-testid='close-board-delete-board-button']");
    private final By confirmDeleteButton = By.cssSelector("button[data-testid='close-board-delete-board-confirm-button']");

    public BoardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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
        new TrelloHomePage(driver).dismissBlockingOverlaysIfPresent();

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
            safeClick(openComposer);
        }

        WebElement input = wait.until(d -> firstVisible(d.findElements(textAreaList)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);

        try {
            safeClick(input);
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
        for (WebElement btn : driver.findElements(addListConfirmButton)) {
            if (btn.isDisplayed()) {
                safeClick(btn);
                break;
            }
        }
    }

    public void addCardToFirstList(String title) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement addCardBtn = wait.until(ExpectedConditions.presenceOfElementLocated(addCardButton));
                safeClick(addCardBtn);

                WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputText));
                try {
                    safeClick(input);
                    input.clear();
                    input.sendKeys(title);
                } catch (ElementNotInteractableException e) {
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                            input,
                            title
                    );
                }

                WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(addCard));
                safeClick(submitButton);

                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//a[@data-testid='card-name' and text()='" + title + "']")
                ));
                return;
            } catch (StaleElementReferenceException | TimeoutException e) {
                if (attempt == 2) {
                    throw e;
                }
            }
        }
    }

    private void safeClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
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

        WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(editButton));
        editBtn.click();

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(cardTitleInput));
        input.clear();
        input.sendKeys(newTitle);

        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        submit.click();
    }

    public void deleteBoard() {
        new TrelloHomePage(driver).dismissBlockingOverlaysIfPresent();

        WebElement menu = wait.until(ExpectedConditions.presenceOfElementLocated(menuButton));
        safeClick(menu);

        WebElement closeBoard = wait.until(ExpectedConditions.presenceOfElementLocated(closeBoardButton));
        safeClick(closeBoard);

        WebElement confirmClose = wait.until(ExpectedConditions.presenceOfElementLocated(confirmCloseButton));
        safeClick(confirmClose);

        WebElement menuBtn = wait.until(ExpectedConditions.presenceOfElementLocated(menuButton));
        safeClick(menuBtn);

        WebElement deleteBoard = wait.until(ExpectedConditions.presenceOfElementLocated(deleteBoardButton));
        safeClick(deleteBoard);

        WebElement confirmDelete = wait.until(ExpectedConditions.presenceOfElementLocated(confirmDeleteButton));
        safeClick(confirmDelete);

        wait.until(ExpectedConditions.urlContains("boards"));
    }

    public void movingCard(String title, String targetListName) {
        //Находим карточку
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@data-testid='card-name' and normalize-space(.)='" + title + "']")
        ));

        //Наводим на карточку, чтобы появился элемент изменения
        Actions actions = new Actions(driver);
        actions.moveToElement(card).perform();

        //Нажимаем на кнопку, чтобы изменить карточку
        WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(editButton));
        editBtn.click();

        //Нажимаем на перемещение
        wait.until(ExpectedConditions.elementToBeClickable(moveCardButton)).click();

        //Нажимаем на список
        wait.until(ExpectedConditions.elementToBeClickable(selectListButton)).click();

        //Находим input внутри списка
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[id='move-card-list-select']")
        ));

        //Вводим название списка
        input.clear();
        input.sendKeys(targetListName);

        //Подтверждаем выбор
        input.sendKeys(Keys.ENTER);

        //Нажимаем кнопку перемещения
        wait.until(ExpectedConditions.elementToBeClickable(moveConfirm)).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(moveConfirm));
    }

    public boolean isCardInList(String cardTitle, String listName) {
        //Получаем список всех колонок
        List<WebElement> allLists = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(lists));

        //Перебираем колонки по индексу
        for (int i = 0; i < allLists.size(); i++) {
            WebElement currentList = allLists.get(i);

            //Находим заголовок колонки
            WebElement header = currentList.findElement(By.cssSelector("h2"));

            String currentListName = header.getText();

            //Проверяем, что это нужная колонка
            if (currentListName.equals(listName)) {

                //Получаем список карточек в этой колонке
                List<WebElement> cardsInCurrentList = currentList.findElements(cards);

                //Перебираем карточки
                for (int j = 0; j < cardsInCurrentList.size(); j++) {
                    WebElement currentCard = cardsInCurrentList.get(j);

                    String currentCardTitle = currentCard.getText();

                    //Если нашли нужную возвращаем true
                    if (currentCardTitle.equals(cardTitle)) {
                        return true;
                    }
                }
            }
        }
        //Если ничего не нашли
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
        new TrelloHomePage(driver).dismissBlockingOverlaysIfPresent();
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@data-testid='card-name' and normalize-space(.)='" + cardTitle + "']")
        ));

        Actions actions = new Actions(driver);
        actions.moveToElement(card).perform();
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                WebElement completeButton = wait.until(ExpectedConditions.elementToBeClickable(completeCardButton));
                safeClick(completeButton);
                return;
            } catch (TimeoutException e) {
                new TrelloHomePage(driver).dismissBlockingOverlaysIfPresent();
                safeClick(card);
            }
        }
        WebElement completeButton = wait.until(ExpectedConditions.elementToBeClickable(completeCardButton));
        safeClick(completeButton);
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

        new TrelloHomePage(driver).dismissBlockingOverlaysIfPresent();
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(cardByTitle));
        safeClick(card);

        WebElement backActions = wait.until(ExpectedConditions.elementToBeClickable(cardBackButton));
        safeClick(backActions);

        WebElement archiveButton = wait.until(ExpectedConditions.elementToBeClickable(archiveCardButton));
        safeClick(archiveButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(cardByTitle));
    }

    public boolean isCardAbsentFromBoard(String cardTitle) {

        List<WebElement> cards = driver.findElements(
                By.xpath("//a[@data-testid='card-name' and normalize-space(.)='" + cardTitle + "']")
        );

        return cards.isEmpty();
    }
}
