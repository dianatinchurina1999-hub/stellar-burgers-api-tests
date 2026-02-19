package tests;

import api.TestUser;
import api.UserApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pages.ForgotPasswordPage;
import pages.LoginPage;
import pages.MainPage;
import pages.RegisterPage;

import static org.junit.Assert.assertTrue;

public class LoginTests extends BaseUiTest {

    private final UserApi userApi = new UserApi();
    private TestUser user;
    private String accessToken;

    @Before
    public void createUserViaApi() {
        user = TestUser.random();
        userApi.createUser(user).then().statusCode(200);
        accessToken = userApi.loginAndGetAccessToken(user);
    }

    @After
    public void deleteUserViaApi() {
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }

    @Test
    public void loginFromMainButtonShouldWork() {
        new MainPage(driver).clickLoginButton();
        new LoginPage(driver).login(user.getEmail(), user.getPassword());
        assertTrue("После логина должна быть видна кнопка 'Оформить заказ'", new MainPage(driver).isOrderButtonVisible());
    }

    @Test
    public void loginFromPersonalAccountShouldWork() {
        new MainPage(driver).clickPersonalAccount();
        new LoginPage(driver).login(user.getEmail(), user.getPassword());
        assertTrue("После логина должна быть видна кнопка 'Оформить заказ'", new MainPage(driver).isOrderButtonVisible());
    }

    @Test
    public void loginFromRegisterFormShouldWork() {
        driver.get(uiBaseUrl + "register");
        new RegisterPage(driver).clickLoginLink();
        new LoginPage(driver).login(user.getEmail(), user.getPassword());
        assertTrue("После логина должна быть видна кнопка 'Оформить заказ'", new MainPage(driver).isOrderButtonVisible());
    }

    @Test
    public void loginFromForgotPasswordFormShouldWork() {
        driver.get(uiBaseUrl + "login");
        new LoginPage(driver).clickForgotPasswordLink();
        new ForgotPasswordPage(driver).clickLoginLink();
        new LoginPage(driver).login(user.getEmail(), user.getPassword());
        assertTrue("После логина должна быть видна кнопка 'Оформить заказ'", new MainPage(driver).isOrderButtonVisible());
    }
}

