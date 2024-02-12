import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.*;
import ru.services.praktikum.scooter.qa.Courier;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTests {
    private static int courierId;
    private String randomLogin;
    private String randomPassword;
    private String randomFirstName;
    @Before
    @Step("Generate credentials")
    public void setUp(){
        RestAssured.baseURI = BASE_URI;
        randomLogin =  generateRandomString(5);
        randomPassword = generateRandomString(6);
        randomFirstName = generateRandomString(7);
        Courier courier = new Courier(randomLogin, randomPassword, randomFirstName);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(courier)
                .when()
                .post(COURIER_URL)
                .then().assertThat().statusCode(201).and().body(STATUS_OK, Matchers.is(true));
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(courier)
                .when()
                .post(COURIER_LOGIN_URL)
                .then().assertThat().statusCode(200);
    }
    @Test
    @Step("Positive: Log in courier")
    public void testLoginCourier() {
        Courier courier = new Courier(randomLogin, randomPassword, null);
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(courier)
                .when()
                .post(COURIER_LOGIN_URL);
                response.then().assertThat().statusCode(200).and().body("id", notNullValue());
        courierId = response.jsonPath().getInt("id");
    }

    @Test
    @Step("Negative: Log in without field")
    public void testLoginCourierWhithoutField(){
        Courier courier = new Courier(null, randomPassword, null);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(courier)
                .when()
                .post(COURIER_LOGIN_URL)
                .then().assertThat().statusCode(400).and().body(MESSAGE_LABEL, Matchers.is("Недостаточно данных для входа"));
    }
    @Test
    @Step("Negative: Log in with wrong login")
    public void testLoginCourierWrongLogin(){
        Courier courier = new Courier(randomLogin+"wrongLogin", randomPassword, null);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(courier)
                .when()
                .post(COURIER_LOGIN_URL)
                .then().assertThat().statusCode(404).and().body(MESSAGE_LABEL, Matchers.is(ACCOUNT_NOT_FOUND));
    }
    @Test
    @Step("Negative: Log in with wrong password")
    public void testLoginCourierWrongPassword(){
        Courier courier = new Courier(randomLogin, randomPassword+"wrongPassword", null);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(courier)
                .when()
                .post(COURIER_LOGIN_URL)
                .then().assertThat().statusCode(404).and().body(MESSAGE_LABEL, Matchers.is(ACCOUNT_NOT_FOUND));
    }

    @AfterClass
    @Step("Delete credentials")
    static public void DeleteCourier() {
        given()
                .when()
                .delete(COURIER_URL + courierId)
                .then()
                .assertThat()
                .statusCode(200);
    }
}







