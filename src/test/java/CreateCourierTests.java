import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import ru.services.praktikum.scooter.qa.Courier;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;


public class CreateCourierTests {
    private String randomLogin;
    private String randomPassword;
    private String randomFirstName;
    @Before
    @Step("Generate credentials")
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        randomLogin =  generateRandomString(5);
        randomPassword = generateRandomString(6);
        randomFirstName = generateRandomString(7);
    }

    @Test
    @Step("Positive: Create Courier")
    @DisplayName("Positive: Create Courier")
    public void testCreateCourier() {
        getCourier(randomPassword, randomFirstName);
    }

    @Test
    @Step("Negative : Create same courier")
    public void testCreateSameCourier() {
        Courier courier = getCourier(randomPassword, randomFirstName);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(courier)
                .when()
                .post(COURIER_URL)
                .then().assertThat().statusCode(409).and().body(MESSAGE_LABEL, Matchers.is(ERROR_MESSAGE_THIS_LOGIN_USED));
    }

    @Test
    @Step("Negative: Creation courier without field")
    public void testCreateCourierWithoutField() {
        Courier courier = new Courier(null, randomPassword, randomFirstName);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(courier)
                .when()
                .post(COURIER_URL)
                .then().assertThat().statusCode(400).and().body(MESSAGE_LABEL, Matchers.is(ERROR_MESSAGE_NOT_ENOUGH_DATA));
    }

    @Test
    @Step("Negative: Creation courier without one value")
    public void testCreateCourierOneValue() {
        Courier courier = new Courier(randomLogin, "", randomFirstName);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(courier)
                .when()
                .post(COURIER_URL)
                .then().assertThat().statusCode(400).and().body(MESSAGE_LABEL, Matchers.is(ERROR_MESSAGE_NOT_ENOUGH_DATA));
    }

    @Test
    @Step("Negative: Creation courier with used login")
    public void testCreateCourierWithUsedLogin() {
        getCourier("FirstRandomPassword", "randomFirstName");
        Courier courierWithSameLogin = new Courier(randomLogin, "randomPassword", "randomFirstName");
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(courierWithSameLogin)
                .when()
                .post(COURIER_URL)
                .then().assertThat().statusCode(409).and().body(MESSAGE_LABEL, Matchers.is(ERROR_MESSAGE_THIS_LOGIN_USED));

    }
    @Step("Positive: Create Courier")
    private Courier getCourier(String randomPassword, String randomFirstName) {
        Courier courier = new Courier(randomLogin, randomPassword, randomFirstName);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(courier)
                .when()
                .post(COURIER_URL)
                .then().assertThat().statusCode(201).and().body(STATUS_OK, Matchers.is(true));
        return courier;
    }

}
