package ru.netology.data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class DataGenerator {
    private DataGenerator() {
    }

    public static class RegistrationInfo {
        private RegistrationInfo() {
        }

        public static String makeUserPassword(String Locale) {
            Faker faker = new Faker(new Locale(Locale));
            return faker.internet().password();
        }

        public static String makeUserName(String Locale) {
            Faker faker = new Faker(new Locale(Locale));
            return faker.name().username();
        }

        public static UserInfo generateUserInfo(String Locale, boolean userStatusIsActive) {
            return new UserInfo(
                    makeUserName(Locale),
                    makeUserPassword(Locale),
                    (userStatusIsActive) ? "active" : "blocked"
            );
        }
    }
}
