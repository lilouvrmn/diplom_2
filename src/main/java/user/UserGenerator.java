package user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    private static final int count = 10;

    public static String generateString(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }

    public static User getUser() {
        return new User(generateString(count) + "@mail.ru", generateString(count), generateString(count));
    }

    public static User getUserIncorrect() {
        int countIncorrectPassword = 123;
        return new User(generateString(count) + "@mail.ru", generateString(countIncorrectPassword), generateString(count));
    }

    public static User getUserUpdate() {
        return new User(generateString(count) + "@mail.ru", generateString(count), generateString(count));
    }

    public static User getUserSecond() {
        return new User(generateString(count) + "@mail.ru", generateString(count), generateString(count));
    }
}
