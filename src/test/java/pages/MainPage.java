package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By loginButton = By.xpath("//button[contains(.,'Войти в аккаунт')]");
    private final By personalAccountLink = By.cssSelector("a[href='/account']");
    private final By orderButton = By.xpath("//button[contains(.,'Оформить заказ')]");

    private final By bunsTab = By.xpath("//span[normalize-space()='Булки']/parent::div");
    private final By saucesTab = By.xpath("//span[normalize-space()='Соусы']/parent::div");
    private final By fillingsTab = By.xpath("//span[normalize-space()='Начинки']/parent::div");

    // Важный кусочек: активная вкладка отмечается классом tab_tab_type_current...
    private static final String ACTIVE_TAB_CLASS_PART = "tab_tab_type_current";

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", el
        );

        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            new Actions(driver).moveToElement(el).click().perform();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private boolean waitTabActive(By tabLocator) {

        return wait.until(ExpectedConditions.attributeContains(tabLocator, "class", ACTIVE_TAB_CLASS_PART));
    }

    @Step("Нажать 'Войти в аккаунт' на главной")
    public void clickLoginButton() {
        safeClick(loginButton);
        wait.until(ExpectedConditions.urlContains("/login"));
    }

    @Step("Нажать 'Личный кабинет' на главной")
    public void clickPersonalAccount() {
        safeClick(personalAccountLink);
        wait.until(ExpectedConditions.urlContains("/login"));
    }

    @Step("Перейти на вкладку 'Булки'")
    public void clickBuns() {
        safeClick(bunsTab);
        waitTabActive(bunsTab);
    }

    @Step("Перейти на вкладку 'Соусы'")
    public void clickSauces() {
        safeClick(saucesTab);
        waitTabActive(saucesTab);
    }

    @Step("Перейти на вкладку 'Начинки'")
    public void clickFillings() {
        safeClick(fillingsTab);
        waitTabActive(fillingsTab);
    }

    @Step("Проверить, что активна вкладка 'Булки'")
    public boolean isBunsTabActive() {
        return waitTabActive(bunsTab);
    }

    @Step("Проверить, что активна вкладка 'Соусы'")
    public boolean isSaucesTabActive() {
        return waitTabActive(saucesTab);
    }

    @Step("Проверить, что активна вкладка 'Начинки'")
    public boolean isFillingsTabActive() {
        return waitTabActive(fillingsTab);
    }

    @Step("Проверить, что видна кнопка 'Оформить заказ'")
    public boolean isOrderButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(orderButton)).isDisplayed();
    }
}

