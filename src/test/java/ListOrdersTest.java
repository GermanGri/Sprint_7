import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static helper.Helper.STATUS_OK;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListOrdersTest {
    @Before
    public void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }
    @Test
    public void ListOrders(){
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders")
                .then().extract().response();
        assertThat(response.jsonPath().getList("orders"), notNullValue());
    }
}
