package Booking;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utilities.TestBase;
import org.json.JSONObject;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookingTests extends TestBase {

        @Test
        public void createBookingTest() {
                // Request body as JSON object
                // Map<String,Object> requestBody=new ListHashMap<>();
                JSONObject requestBody = new JSONObject()
                                .put("firstname", "Param")
                                .put("lastname", "Tech")
                                .put("totalprice", 200)
                                .put("depositpaid", true);

                JSONObject bookingDates = new JSONObject()
                                .put("checkin", "2023-01-01")
                                .put("checkout", "2024-01-01");

                requestBody.put("bookingdates", bookingDates);
                requestBody.put("additionalneeds", "Breakfast");

                // Send POST request and validate
                Response response = given()
                                .contentType(ContentType.JSON)
                                .body(requestBody.toString())
                                .when()
                                .post("/booking")
                                .then()
                                .assertThat()
                                .statusCode(200)
                                .body("booking.firstname", equalTo("Param"),
                                                "booking.lastname", equalTo("Tech"),
                                                "booking.totalprice", equalTo(200),
                                                "booking.depositpaid", equalTo(true),
                                                "booking.additionalneeds", equalTo("Breakfast"))
                                .extract()
                                .response();

                int bookingId = response.jsonPath().getInt("bookingid");

                String responseBody = response.getBody().asString();
                System.out.println("Response body: " + responseBody);

                System.out.println("Created booking with ID: " + bookingId);

        }

        @Test
        public void deleteBookingTest() {
                Response createResponse = createBooking();
                createResponse.then().statusCode(200);
                int bookingId = createResponse.jsonPath().getInt("bookingid");

                String token = RestAssured
                                .given()
                                .contentType(ContentType.JSON)
                                .body("{\"username\":\"admin\",\"password\":\"password123\"}")
                                .post("/auth")
                                .then()
                                .statusCode(200)
                                .extract()
                                .path("token");

                System.out.println("Token: " + token);

                Response response = RestAssured
                                .given()
                                .contentType(ContentType.JSON)
                                .header("Cookie", "token=" + token)
                                .delete("/booking/" + bookingId)
                                .then()
                                .statusCode(201)
                                .extract()
                                .response();

                System.out.println("Response: " + response.asString());

        }

        private Response createBooking() {

                String requestBody = "{\n" +
                                "  \"firstname\": \"Param\",\n" +
                                "  \"lastname\": \"Tech\",\n" +
                                "  \"totalprice\": 200,\n" +
                                "  \"depositpaid\": true,\n" +
                                "  \"bookingdates\": {\n" +
                                "    \"checkin\": \"2023-08-01\",\n" +
                                "    \"checkout\": \"2024-07-05\"\n" +
                                "  },\n" +
                                "  \"additionalneeds\": \"Breakfast\"\n" +
                                "}";

                Response response = given()
                                .contentType(ContentType.JSON)
                                .body(requestBody)
                                .when()
                                .post("/booking")
                                .then()
                                .extract()
                                .response();

                return response;
        }

}