package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By loginButton = By.xpath("//button[contains(.,'Войти в аккаунт')]");

    private final By personalAccountByHref1 = By.cssSelector("a[href='/account']");
    private final By personalAccountByHref2 = By.cssSelector("a[href='/account/profile']");
    private final By personalAccountByHrefContains = By.cssSelector("a[href*='/account']");


    private final By personalAccountTextNode = By.xpath(
            "//*[normalize-space()='Личный кабинет' or contains(normalize-space(.),'Личный кабинет')]"
    );

    private final By bunsTab = By.xpath("//span[contains(.,'Булки')]");
    private final By saucesTab = By.xpath("//span[contains(.,'Соусы')]");
    private final By fillingsTab = By.xpath("//span[contains(.,'Начинки')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void clickPersonalAccount() {

        if (tryClick(personalAccountByHref1)) return;
        if (tryClick(personalAccountByHref2)) return;
        if (tryClick(personalAccountByHrefContains)) return;


        WebElement textEl = wait.until(ExpectedConditions.presenceOfElementLocated(personalAccountTextNode));

        WebElement clickable = findClickableAncestorOrSelf(textEl);
        wait.until(ExpectedConditions.elementToBeClickable(clickable));
        safeClick(clickable);
    }

    private boolean tryClick(By locator) {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            safeClick(el);
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
    }

    private WebElement findClickableAncestorOrSelf(WebElement el) {

        String tag = el.getTagName().toLowerCase();
        if (tag.equals("a") || tag.equals("button")) return el;


        try {
            return el.findElement(By.xpath("./ancestor::*[self::a or self::button][1]"));
        } catch (NoSuchElementException ignored) {

            return el.findElement(By.xpath("./ancestor::*[self::div][1]"));
        }
    }

    private void safeClick(WebElement el) {
        try {
            el.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    public void clickBuns() {
        wait.until(ExpectedConditions.elementToBeClickable(bunsTab)).click();
    }

    public void clickSauces() {
        wait.until(ExpectedConditions.elementToBeClickable(saucesTab)).click();
    }

    public void clickFillings() {
        wait.until(ExpectedConditions.elementToBeClickable(fillingsTab)).click();
    }
}

