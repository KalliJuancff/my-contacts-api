package com.juancff.mycontactsapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyContactsApiApplicationTests {
    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void when_post_a_new_contact_then_return_location_and_HTTP_status_code_of_created() {
        String requestBody = """
                {
                    "name": "John Doe",
                    "phoneNumber": "123457890"
                }
                """;
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/contacts")
        .then()
            .statusCode(201)
            .header("Location", startsWith("/contacts/"))
            .body("contactId", notNullValue());
    }

    @Test
    public void when_get_an_existing_contact_by_id_then_return_contact_data_and_HTTP_status_code_of_ok() {
        String requestBody = """
            {
                "name": "Jane Doe",
                "phoneNumber": "987543210"
            }
            """;
        var contactId = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/contacts")
        .then()
            .statusCode(201)
            .extract()
            .path("contactId");

        given()
            .pathParams("contactId", contactId)
        .when()
            .get("/contacts/{contactId}")
        .then()
            .statusCode(200)
            .body("contactId", equalTo(contactId))
            .body("name", equalTo("Jane Doe"))
            .body("phoneNumber", equalTo("987543210"));
    }
}
