package config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    public static WebDriver create() {
        String browser = System.getProperty("browser", "chrome");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");

        if ("yandex".equalsIgnoreCase(browser)) {
            String yandexBinary = System.getProperty("yandexBinary");
            if (yandexBinary == null || yandexBinary.isBlank()) {
                throw new IllegalArgumentException("Для -Dbrowser=yandex нужно -DyandexBinary=.../browser.exe");
            }
            options.setBinary(yandexBinary);

            String yandexMajor = System.getProperty("yandexMajor", "142");

            WebDriverManager.chromedriver()
                    .browserVersion(yandexMajor)
                    .clearResolutionCache()
                    .clearDriverCache()
                    .setup();

            return new ChromeDriver(options);
        }

        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }
}

