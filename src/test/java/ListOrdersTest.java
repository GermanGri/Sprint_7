import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListOrdersTest {
    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URI;
    }
    @Test
    @Step("Positive: Check orders")
    public void ListOrders(){
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .when()
                .get("/api/v1/orders")
                .then().extract().response();
        assertThat(response.jsonPath().getList("orders"), notNullValue());
    }
}
