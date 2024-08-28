import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class BookingTests {

    // Базовий URL для API
    private static final String BASE_URL = "http://restful-booker.herokuapp.com";

    @BeforeClass
    public void setup() {
        // Встановлення базового URI для RestAssured
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void createBooking() {
        String requestBody = "{\n" +
                "    \"firstname\": \"Abaldui\",\n" +
                "    \"lastname\": \"Abalduev\",\n" +
                "    \"totalprice\": 123,\n" +
                "    \"depositpaid\": true,\n" +
                "    \"bookingdates\": {\n" +
                "        \"checkin\": \"2024-07-01\",\n" +
                "        \"checkout\": \"2023-07-30\"\n" +
                "    },\n" +
                "    \"additionalneeds\": \"Breakfast\"\n" +
                "}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/booking");

        response.then().statusCode(200)
                .body("booking.firstname", equalTo("Abaldui"))
                .body("booking.lastname", equalTo("Abalduev"));

        // ID створеного
        int bookingId = response.jsonPath().getInt("bookingid");
        System.out.println("Created booking ID: " + bookingId);
    }

    @Test
    public void getAllBookingIds() {
        Response response = RestAssured.given()
                .when()
                .get("/booking");

        response.then().statusCode(200);

        // всі ID
        System.out.println("Booking IDs: " + response.jsonPath().getList("bookingid"));
    }

    @Test
    public void updateBookingPrice() {
        int bookingId = 12; // Ціна

        String requestBody = "{\n" +
                "    \"totalprice\": 150\n" +
                "}";

        Response response = RestAssured.given()
                .auth().preemptive().basic("admin", "password123") // Додаємо авторизацію
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch("/booking/" + bookingId);

        response.then().statusCode(200)
                .body("totalprice", equalTo(150));
    }

    @Test
    public void updateBookingDetails() {
        int bookingId = 278; // оновлення деталей

        String requestBody = "{\n" +
                "    \"firstname\": \"Azaza\",\n" +
                "    \"lastname\": \"Lalka\",\n" +
                "    \"totalprice\": 200,\n" +
                "    \"depositpaid\": false,\n" +
                "    \"bookingdates\": {\n" +
                "        \"checkin\": \"2023-08-01\",\n" +
                "        \"checkout\": \"2023-08-20\"\n" +
                "    },\n" +
                "    \"additionalneeds\": \"Lunch\"\n" +
                "}";

        Response response = RestAssured.given()
                .auth().preemptive().basic("admin", "password123") // авторизація
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/booking/" + bookingId);

        response.then().statusCode(200)
                .body("firstname", equalTo("Azaza"))
                .body("additionalneeds", equalTo("Lunch"));
    }

    @Test
    public void deleteBooking() {
        int bookingId = 482; // видалення

        Response response = RestAssured.given()
                .auth().preemptive().basic("admin", "password123") //  авторизація
                .when()
                .delete("/booking/" + bookingId);

        response.then().statusCode(201); //
    }
}

