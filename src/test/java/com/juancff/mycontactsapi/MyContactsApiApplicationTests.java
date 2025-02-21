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
    public void when_post_a_new_contact_returns_HTTP_status_code_created() {
        String requestBody = contactAsJson("John Doe", "123457890");
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/contacts")
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .header("Location", matchesPattern("/contacts/[0-9]+"))
            .body("contactId", notNullValue());
    }

    @Test
    public void when_get_an_existing_contact_by_id_returns_HTTP_status_code_ok() {
        String requestBody = contactAsJson("Jane Doe", "987543210");
        var contactId = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/contacts")
        .then()
            .extract()
            .path("contactId");

        given()
            .pathParams("contactId", contactId)
        .when()
            .get("/contacts/{contactId}")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("contactId", equalTo(contactId))
            .body("name", equalTo("Jane Doe"))
            .body("phoneNumber", equalTo("987543210"));
    }

    @Test
    public void when_get_a_non_existing_contact_by_id_returns_HTTP_status_code_not_found() {
        given()
            .pathParams("contactId", 999)
        .when()
            .get("/contacts/{contactId}")
        .then()
            .statusCode(404)
            .contentType(ContentType.JSON)
            .body("error", equalTo("Contact not found"));
    }

    @Test
    public void when_get_all_contacts_without_any_added_contact_returns_HTTP_status_code_ok() {
        given()
        .when()
            .get("/contacts")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(0));
    }

    @Test
    public void when_get_all_contacts_with_some_added_contact_returns_HTTP_status_code_ok() {
        String requestBody1 = contactAsJson("John Doe", "123457890");
        String requestBody2 = contactAsJson("Jane Doe", "987543210");
        var contactId1 = given()
            .contentType(ContentType.JSON)
            .body(requestBody1)
        .when()
            .post("/contacts")
        .then()
            .extract()
            .path("contactId");
        var contactId2 = given()
            .contentType(ContentType.JSON)
            .body(requestBody2)
        .when()
            .post("/contacts")
        .then()
            .extract()
            .path("contactId");

        given()
        .when()
            .get("/contacts")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("contactId", contains(contactId1, contactId2))
            .body("name", contains("John Doe", "Jane Doe"))
            .body("phoneNumber", contains("123457890", "987543210"))
            .body("size()", is(2));
    }

    private static String contactAsJson(String name, String phoneNumber) {
        String requestBody = """
            {
                "name": "%s",
                "phoneNumber": "%s"
            }
            """;
        return requestBody.formatted(name, phoneNumber);
    }
}
