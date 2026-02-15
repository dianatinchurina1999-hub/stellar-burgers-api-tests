package tests;

import org.junit.Test;
import pages.MainPage;

import static org.junit.Assert.assertTrue;

public class ConstructorTabsTests extends BaseUiTest {

    @Test
    public void constructorTabs_shouldSwitch() {
        MainPage main = new MainPage(driver);

        main.clickSauces();
        main.clickFillings();
        main.clickBuns();


        assertTrue(driver.getCurrentUrl().contains("stellarburgers.education-services.ru"));
    }
}
