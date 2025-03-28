package ru.netology.web.util;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;
import ru.netology.web.data.UserInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@UtilityClass
public class DataHelper {
    Faker faker = new Faker();
    Random rnd = new Random();

    private Map<String, String> registeredLoginPassword = new HashMap<>(Map.of(
            "vasya", "qwerty123",
            "petya", "123qwerty"
    ));

    private String randomLogin() {
        String[] logins = registeredLoginPassword.keySet().toArray(new String[0]);
        int index = rnd.nextInt(logins.length);
        return logins[index];
    }

    public UserInfo getRegisteredUser() {
        String login = randomLogin();

        return new UserInfo(
                login,
                registeredLoginPassword.get(login)
        );
    }

    public String fakePassword(UserInfo registeredUser) {
        String result = registeredUser.getPassword();

        for (int i = 0; i < 3; i++) {
            result = faker.internet().password();
            if (!result.equals(registeredUser.getPassword())) break;
        }
        return result;
    }
}