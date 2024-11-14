package com.example.tech11_exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

public class ServiceTest {

    private Client client;
    //private static final String BASE_URL = "http://localhost:8080/tech11_exercise/users";
    private static final String BASE_URL = "http://localhost:8080/tech11_exercise_war_exploded/api/users";

    @BeforeEach
    public void setUp() {
        client = ClientBuilder.newClient();
        UserStorage.getInstance().getAllUsers().clear();
    }

    @Test
    public void testAddUser() {
        User user = new User("John", "Doe", "john.doe@example.com", "2000-01-01", "password123");

        Response response = client.target(BASE_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(user.getEmail(), response.readEntity(User.class).getEmail());
    }

    @Test
    public void testAddDuplicateUser() {
        User user = new User("Jane", "Doe", "jane.doe@example.com", "2000-01-01", "password123");
        client.target(BASE_URL).request().post(Entity.entity(user, MediaType.APPLICATION_JSON));

        Response duplicateResponse = client.target(BASE_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), duplicateResponse.getStatus());
        assertEquals("User with this email already exists.", duplicateResponse.readEntity(String.class));
    }

    @Test
    public void testGetUser() {
        User user = new User("Tom", "Hanks", "tom.hanks@example.com", "1995-01-01", "securepass");
        client.target(BASE_URL).request().post(Entity.entity(user, MediaType.APPLICATION_JSON));

        Response response = client.target(BASE_URL)
                .queryParam("email", user.getEmail())
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(user.getEmail(), response.readEntity(User.class).getEmail());
    }

    @Test
    public void testGetUserNotFound() {
        Response response = client.target(BASE_URL)
                .queryParam("email", "non.existent@example.com")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("User not found.", response.readEntity(String.class));
    }

    @Test
    public void testUpdateUser() {
        User originalUser = new User("Alice", "Smith", "alice.smith@example.com", "1999-05-05", "mypassword");
        client.target(BASE_URL).request().post(Entity.entity(originalUser, MediaType.APPLICATION_JSON));

        User updatedUser = new User("Alice", "Johnson", "alice.johnson@example.com", "1999-05-05", "newpassword");

        Response response = client.target(BASE_URL)
                .queryParam("email", originalUser.getEmail())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(updatedUser, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(updatedUser.getEmail(), response.readEntity(User.class).getEmail());
    }

    @Test
    public void testUpdateUserNotFound() {
        User updatedUser = new User("Bob", "White", "bob.white@example.com", "1990-08-15", "password");

        Response response = client.target(BASE_URL)
                .queryParam("email", "non.existent@example.com")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(updatedUser, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("User not found.", response.readEntity(String.class));
    }

    @Test
    public void testDeleteUser() {
        User user = new User("Carl", "Brown", "carl.brown@example.com", "1985-11-25", "adminpass");
        client.target(BASE_URL).request().post(Entity.entity(user, MediaType.APPLICATION_JSON));

        Response response = client.target(BASE_URL)
                .queryParam("email", user.getEmail())
                .queryParam("password", user.getPassword())
                .request()
                .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("User deleted successfully.", response.readEntity(String.class));
    }

    @Test
    public void testDeleteUserIncorrectPassword() {
        User user = new User("Dave", "Clark", "dave.clark@example.com", "1987-10-10", "correctpass");
        client.target(BASE_URL).request().post(Entity.entity(user, MediaType.APPLICATION_JSON));

        Response response = client.target(BASE_URL)
                .queryParam("email", user.getEmail())
                .queryParam("password", "wrongpass")
                .request()
                .delete();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        assertEquals("Incorrect password.", response.readEntity(String.class));
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User("Eve", "Doe", "eve.doe@example.com", "1994-09-09", "pass1");
        User user2 = new User("Frank", "Doe", "frank.doe@example.com", "1992-07-07", "pass2");
        client.target(BASE_URL).request().post(Entity.entity(user1, MediaType.APPLICATION_JSON));
        client.target(BASE_URL).request().post(Entity.entity(user2, MediaType.APPLICATION_JSON));

        Response response = client.target(BASE_URL + "/getAllUsers")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.readEntity(ArrayList.class).size() >= 2);
    }
}
