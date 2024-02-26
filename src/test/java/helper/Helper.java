package helper;

import java.util.Random;

public class Helper {
    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    public static final String COURIER_URL = "/api/v1/courier/";
    public static final String COURIER_LOGIN_URL = "/api/v1/courier/login";
    public static final String CONTENT_TYPE_LABEL = "Content-type";
    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String STATUS_OK = "ok";
    public static final String MESSAGE_LABEL = "message";
    public static final String ERROR_MESSAGE_NOT_ENOUGH_DATA = "Недостаточно данных для создания учетной записи";
    public static final String ERROR_MESSAGE_THIS_LOGIN_USED = "Этот логин уже используется. Попробуйте другой.";
    public static final String ACCOUNT_NOT_FOUND = "Учетная запись не найдена";
    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }

}
