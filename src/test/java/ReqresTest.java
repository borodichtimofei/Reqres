import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

public class ReqresTest {

    public static final String URL = "https://reqres.in/";

    @Test
    public void listUsers() {
        when().
                get(URL + "api/users?page=2").
        then().
                log().all().
                statusCode(200).
                body("page", equalTo(2), "total", equalTo(12),
                        "data.id", hasItems(7, 8, 9, 10, 11, 12));
    }

    @Test
    public void singleUser() {
        when().
                get(URL + "api/users/2").
        then().
                log().all().
                statusCode(200).
                body("data.id", equalTo(2), "support.text",
                        equalTo("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    public void singleUserNotFound() {
        when().
                get(URL + "api/users/23").
        then().
                log().all().
                statusCode(404);
    }

    @Test
    public void listResource() {
        when().
                get(URL + "api/unknown").
        then().
                log().all().
                statusCode(200).
                body("page", equalTo(1), "data.id", hasItems(1, 2, 4, 5, 6));

    }

    @Test
    public void singleResource() {
        when().
                get(URL + "api/unknown/2").
        then().
                log().all().
                statusCode(200).
                body("data.id", equalTo(2));
    }

    @Test
    public void singleResourceNotFound() {
        when().
                get(URL + "api/unknown/23").
        then().
                log().all().
                statusCode(404);
    }

    @Test
    public void create() {
        given().
                contentType("application/json").
                body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}").
                log().all().
        when().
                post(URL + "api/unknown/23").
        then().
                log().all().
                statusCode(201).
                body("name", equalTo("morpheus"), "job", equalTo("leader"));
    }

    @Test
    public void updateFirst() {
        given().
                contentType("application/json").
                body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}").
                log().all().
        when().
                put(URL + "api/users/2").
        then().
                log().all().
                statusCode(200).
                body("name", equalTo("morpheus"), "job", equalTo("zion resident"));
    }

    @Test
    public void updateSecond() {
        given().
                contentType("application/json").
                body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}").
                log().all().
        when().
                patch(URL + "api/users/2").
        then().
                log().all().
                statusCode(200).
                body("name", equalTo("morpheus"), "job", equalTo("zion resident"));
    }

    @Test
    public void delete() {
        when().
                delete(URL + "api/users/2").
        then().
                log().all().
                statusCode(204);
    }

    @Test
    public void registerSuccessful() {
        given().
                contentType("application/json").
                body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}").
                log().all().
        when().
                post(URL + "api/register").
        then().
                log().all().
                statusCode(200);
    }

    @Test
    public void registerUnsuccessful() {
        given().
                contentType("application/json").
                body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}").
                log().all().
        when().
                post(URL + "api/register").
        then().
                log().all().
                statusCode(400).
                body("error", equalTo("Missing password"));
    }

    @Test
    public void loginSuccessful() {
        given().
                contentType("application/json").
                body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}").
        when().
                post(URL + "api/login").
        then().
                log().all().
                statusCode(200).
                body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void loginUnsuccessful() {
        given().
                contentType("application/json").
                body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}").
        when().
                post(URL + "api/login").
        then().
                log().all().
                statusCode(400).
                body("error", equalTo("Missing password"));
    }

    @Test
    public void delayedResponse() {
        when().
                get(URL + "api/users?delay=3").
        then().
                log().all().
                statusCode(200).
                body("total", equalTo(12));
    }
}
