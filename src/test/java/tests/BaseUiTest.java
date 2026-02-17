package tests;

import config.DriverFactory;
import io.qameta.allure.Allure;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseUiTest {

    protected WebDriver driver;
    protected final String uiBaseUrl = System.getProperty("uiBaseUrl", "https://stellarburgers.education-services.ru/");

    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            attachScreenshot();
            attachPageSource();
        }
    };

    @Before
    public void setUp() {
        driver = DriverFactory.create();
        driver.get(uiBaseUrl);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void attachScreenshot() {
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot", new ByteArrayInputStream(bytes));
        } catch (Exception ignored) {
        }
    }

    protected void attachPageSource() {
        try {
            String html = driver.getPageSource();
            Allure.addAttachment("Page source", "text/html",
                    new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), ".html");
        } catch (Exception ignored) {
        }
    }
}
