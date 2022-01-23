package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static class RegistrationInfo {
        private RegistrationInfo() {
        }

        public static void setUpUser(UserInfo user) {
            // сам запрос
            given() // "дано"
                    .spec(requestSpec) // указываем, какую спецификацию используем
                    .body(user)
                    .when() // "когда"
                    .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                    .then() // "тогда ожидаем"
                    .statusCode(200); // код 200 OK
        }

        public static String makeUserPassword(String Locale) {
            Faker faker = new Faker(new Locale(Locale));
            return faker.internet().password();
        }

        public static String makeUserName(String Locale) {
            Faker faker = new Faker(new Locale(Locale));
            return faker.name().username();
        }

        public static String setUserStatus(String status) {
            return status;
        }

        public static UserInfo generateUserInfo(String Locale, String userStatus) {
            return new UserInfo(
                    makeUserName(Locale),
                    makeUserPassword(Locale),
                    setUserStatus(userStatus)
            );
        }
    }
}