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
        String expectedName = "Jane Doe";
        String expectedPhoneNumber = "987543210";
        var contactId = postNewContact(expectedName, expectedPhoneNumber);

        given()
            .pathParams("contactId", contactId)
        .when()
            .get("/contacts/{contactId}")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("contactId", equalTo(contactId))
            .body("name", equalTo(expectedName))
            .body("phoneNumber", equalTo(expectedPhoneNumber));
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
        String expectedName1 = "John Doe";
        String expectedName2 = "Jane Doe";
        String expectedPhoneNumber1 = "123457890";
        String expectedPhoneNumber2 = "987543210";
        var contactId1 = postNewContact(expectedName1, expectedPhoneNumber1);
        var contactId2 = postNewContact(expectedName2, expectedPhoneNumber2);

        given()
        .when()
            .get("/contacts")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("contactId", contains(contactId1, contactId2))
            .body("name", contains(expectedName1, expectedName2))
            .body("phoneNumber", contains(expectedPhoneNumber1, expectedPhoneNumber2))
            .body("size()", is(2));
    }

    private static Object postNewContact(String EXPECTED_NAME, String EXPECTED_PHONE_NUMBER) {
        String requestBody = contactAsJson(EXPECTED_NAME, EXPECTED_PHONE_NUMBER);

        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
            .when()
                .post("/contacts")
            .then()
                .extract()
                .path("contactId");
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
