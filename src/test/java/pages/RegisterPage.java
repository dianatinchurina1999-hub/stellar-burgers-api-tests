package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By nameInput = By.xpath("//label[contains(normalize-space(.),'Имя')]/following::input[1]");
    private final By emailInput = By.xpath("//label[contains(normalize-space(.),'Email')]/following::input[1]");
    private final By passwordInput = By.xpath(
            "(//label[contains(normalize-space(.),'Пароль')]/following::input[@type='password'][1]) | (//input[@type='password'][1])"
    );

    private final By registerButton = By.xpath("//button[contains(.,'Зарегистрироваться')]");
    private final By loginLink = By.xpath("//a[contains(.,'Войти')]");
    private final By passwordError = By.xpath("//*[contains(.,'Некорректный пароль')]");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Зарегистрироваться: name={name}, email={email}")
    public void register(String name, String email, String password) {
        WebElement nameEl = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        nameEl.clear();
        nameEl.sendKeys(name);

        WebElement emailEl = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        emailEl.clear();
        emailEl.sendKeys(email);

        WebElement passEl = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        passEl.clear();
        passEl.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/login"),
                ExpectedConditions.visibilityOfElementLocated(passwordError)
        ));
    }

    @Step("В форме регистрации нажать ссылку 'Войти'")
    public void clickLoginLink() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
        wait.until(ExpectedConditions.urlContains("/login"));
    }

    @Step("Проверить, что показана ошибка 'Некорректный пароль'")
    public boolean isPasswordErrorVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordError)).isDisplayed();
    }
}
