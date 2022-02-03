package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.UserInfo;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.RegistrationInfo.setUpUser;

public class ApiLoginTest {

    @BeforeEach
    void headlessMode() {
        Configuration.headless = true;
    }

    private void loginForm(String login, String password) {
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(login);
        $("[data-test-id=password] input").setValue(password);
        $("[data-test-id=action-login]").click();
    }

    @Test
    public void shouldLoginExistsActiveUser() {
        UserInfo user = DataGenerator.RegistrationInfo.generateUserInfo("ru", "active");
        setUpUser(user);
        loginForm(user.getLogin(), user.getPassword());
        $(byText("Личный кабинет")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotLoginExistsBlockedUser() {
        UserInfo user = DataGenerator.RegistrationInfo.generateUserInfo("ru", "blocked");
        setUpUser(user);
        loginForm(user.getLogin(), user.getPassword());

        $(withText("Пользователь заблокирован")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotLoginWithWrongUsername() {
        UserInfo user = DataGenerator.RegistrationInfo.generateUserInfo("ru", "active");
        setUpUser(user);
        loginForm(DataGenerator.RegistrationInfo.makeUserName("ru"), user.getPassword());
        $(withText("Неверно указан логин или пароль")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotLoginWithWrongPassword() {
        UserInfo user = DataGenerator.RegistrationInfo.generateUserInfo("ru", "active");
        setUpUser(user);
        loginForm(user.getLogin(), DataGenerator.RegistrationInfo.makeUserPassword("ru"));
        $(withText("Неверно указан логин или пароль")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }
}

//java -jar ./artifacts/app-ibank.jar -P:profile=test