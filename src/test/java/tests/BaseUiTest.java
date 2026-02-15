package tests;

import config.DriverFactory;
import io.qameta.allure.Attachment;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.*;

public abstract class BaseUiTest {

    protected WebDriver driver;

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
        driver.get("https://stellarburgers.education-services.ru/");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] attachScreenshot() {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception ex) {
            return new byte[0];
        }
    }

    @Attachment(value = "Page source", type = "text/html")
    public String attachPageSource() {
        try {
            return driver.getPageSource();
        } catch (Exception ex) {
            return "Cannot get page source: " + ex.getMessage();
        }
    }
}
