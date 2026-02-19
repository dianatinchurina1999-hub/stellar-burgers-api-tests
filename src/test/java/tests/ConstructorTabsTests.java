package tests;

import org.junit.Test;
import pages.MainPage;

import static org.junit.Assert.assertTrue;

public class ConstructorTabsTests extends BaseUiTest {

    @Test
    public void bunsTabShouldOpen() {
        MainPage main = new MainPage(driver);

        assertTrue("Должна быть активна вкладка 'Булки'", main.isBunsTabActive());
    }

    @Test
    public void saucesTabShouldOpen() {
        MainPage main = new MainPage(driver);
        main.clickSauces();
        assertTrue("Должна быть активна вкладка 'Соусы'", main.isSaucesTabActive());
    }

    @Test
    public void fillingsTabShouldOpen() {
        MainPage main = new MainPage(driver);
        main.clickFillings();
        assertTrue("Должна быть активна вкладка 'Начинки'", main.isFillingsTabActive());
    }
}


