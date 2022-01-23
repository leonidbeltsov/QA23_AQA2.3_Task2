package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.UserInfo;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

public class ApiLoginTest {

    @BeforeEach
    void headlessMode() {
        Configuration.headless = true;
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private void setUpUser(UserInfo user) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user)
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    private void loginForm(String login, String password) {
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(login);
        $("[data-test-id=password] input").setValue(password);
        $("[data-test-id=action-login]").click();
    }

    @Test
    public void shouldLoginExistsActiveUser() {
        UserInfo user = DataGenerator.RegistrationInfo.generateUserInfo("ru", true);
        setUpUser(user);
        loginForm(user.getLogin(), user.getPassword());
        $(byText("Личный кабинет")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotLoginExistsBlockedUser() {
        UserInfo user = DataGenerator.RegistrationInfo.generateUserInfo("ru", false);
        setUpUser(user);
        loginForm(user.getLogin(), user.getPassword());

        $(withText("Пользователь заблокирован")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotLoginWithWrongUsername() {
        UserInfo user = DataGenerator.RegistrationInfo.generateUserInfo("ru", true);
        setUpUser(user);
        loginForm(DataGenerator.RegistrationInfo.makeUserName("ru"), user.getPassword());
        $(withText("Неверно указан логин или пароль")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotLoginWithWrongPassword() {
        UserInfo user = DataGenerator.RegistrationInfo.generateUserInfo("ru", true);
        setUpUser(user);
        loginForm(user.getLogin(), DataGenerator.RegistrationInfo.makeUserPassword("ru"));
        $(withText("Неверно указан логин или пароль")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }
}
