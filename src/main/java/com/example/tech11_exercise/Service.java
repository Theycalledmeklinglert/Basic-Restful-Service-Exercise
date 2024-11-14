package com.example.tech11_exercise;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/users")
public class Service {

    private UserStorage userStorage = UserStorage.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@DefaultValue("") @QueryParam("email") final String email) {
        User user = userStorage.getUser(email);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }

        return Response.status(Response.Status.OK).entity(user).build();
    }


    // The user password is stored in plain text which is of course a cardinal sin in real world use cases. However for the purpose of this exercise it should suffice.
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(@Valid User user) {
        if (userStorage.getUser(user.getEmail()) != null) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("User with this email already exists.")
                    .build();
        }
        userStorage.addUser(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    //updateUser uses current Email address as a Query Parameter to assign which user account to update. The JSON body contains the new/changed Account data.
    // --> In practice this is of course not sensible. Thus some kind of authentication process would have to take place
    //     beforehand, which assigns some kind of authentication token/ID to ensure only the account of the currently logged in user
    //     is allowed to be updated. Additionally the password or password hash should be checked beforehand.
    //     Both would require a suitable hashing algorithm and a permanent database. However as per the exercise instructions this is not required for this exercise.
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@Valid User updatedUser, @QueryParam("email") final String currentEmail) {
        User existingUser = userStorage.getUser(currentEmail);
        if (existingUser == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }
        //userStorage.deleteUser(existingUser);
        //userStorage.addUser(updatedUser);

        existingUser.setFirstname(updatedUser.getFirstname());
        existingUser.setLastname(updatedUser.getLastname());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setBirthday(updatedUser.getBirthday());
        existingUser.setPassword(updatedUser.getPassword());

        return Response.status(Response.Status.OK).entity(updatedUser).build();
    }

    // If an authentication mechanism was implemented this would be done using the Session ID/Token instead of the password/email
    // to ensure an account can only be deleted by its owner
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(@QueryParam("email") final String currentEmail, @QueryParam("password") final String password) {
        User existingUser = userStorage.getUser(currentEmail);
        if (existingUser == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }

        if (!existingUser.getPassword().equals(password)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Incorrect password.")
                    .build();
        }
        userStorage.deleteUser(existingUser);
        return Response.status(Response.Status.OK).entity("User deleted successfully.").build();
    }

    @GET
    @Path("/getAllUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        ArrayList<User> allUsers = userStorage.getAllUsers();
        if (allUsers == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No user accounts in memory.")
                    .build();
        }

        return Response.status(Response.Status.OK).entity(allUsers).build();
    }

}
