import helper.Helper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.*;
import ru.services.praktikum.scooter.qa.Courier;

import static helper.Helper.BASE_URI;
import static helper.Helper.generateRandomString;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTests {
    private static int courierId;
    private String randomLogin;
    private String randomPassword;
    private String randomFirstName;
    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URI;
        randomLogin =  generateRandomString(5);
        randomPassword = generateRandomString(6);
        randomFirstName = generateRandomString(7);
        Courier courier = new Courier(randomLogin, randomPassword, randomFirstName);
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().assertThat().statusCode(201).and().body("ok", Matchers.is(true));
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().assertThat().statusCode(200);
    }
    @Test
    public void testLoginCourier() {
//        int courierId;
        Courier courier = new Courier(randomLogin, randomPassword, null);
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
                response.then().assertThat().statusCode(200).and().body("id", notNullValue());
        courierId = response.jsonPath().getInt("id");
//        given()
//                .when()
//                .delete("/api/v1/courier/" + courierId)
//                .then()
//                .assertThat()
//                .statusCode(200);
    }

    @Test
    public void testLoginCourierWhithoutField(){
        Courier courier = new Courier(null, randomPassword, null);
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для входа"));
    }
    @Test
    public void testLoginCourierWrongLogin(){
        Courier courier = new Courier(randomLogin+"wrongLogin", randomPassword, null);
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().assertThat().statusCode(404).and().body("message", Matchers.is("Учетная запись не найдена"));
    }
    @Test
    public void testLoginCourierWrongPassword(){
        Courier courier = new Courier(randomLogin, randomPassword+"wrongPassword", null);
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().assertThat().statusCode(404).and().body("message", Matchers.is("Учетная запись не найдена"));
    }

    @AfterClass
    static public void DeleteCourier() {
        given()
                .when()
                .delete("/api/v1/courier/" + courierId)
                .then()
                .assertThat()
                .statusCode(200);
    }
}







