import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.services.praktikum.scooter.qa.Order;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

@RunWith(Parameterized.class)
public class OrdersCreatingTests {
    private final Order order;
    private int track;

    public OrdersCreatingTests(Order order) {
        this.order = order;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Parameterized.Parameters(name = "Тестовые данные")
    public static Object[][] data() {
        return new Object[][]{
                {new Order("Ivann", "Ivanov", "Ivanovskaya, 188 apt", "1", "+7 999 888 77 66", 1, "2024-02-12", "call before delivery", new String[]{"BLACK"})},
                {new Order("Petrr", "Petrov", "Petrovskaya, 189 apt", "2", "+7 990 888 77 66", 2, "2024-02-13", "I am waiting", new String[]{"GREY"})},
                {new Order("Sidorr", "Sidorov", "Sidorovskaya, 199 apt", "3", "+7 991 888 77 66", 3, "2024-02-14", "two colors?", new String[]{"BLACK", "GREY"})},
                {new Order("Egorr", "Egorov", "Egorovskaya, 190 apt", "4", "+7 992 888 77 66", 4, "2024-02-15", "I want a colorless scooter!", new String[]{})},
        };
    }

    @Test
    public void testCreatingOrder() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then().assertThat().statusCode(201).and().body("$", hasKey("track")).extract().response();
        track = response.path("track");
    }

}
