package com.example.tech11_exercise;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class Service {

    private UserStorage userStorage = UserStorage.getInstance();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(@Valid User user) {
        if (userStorage.getUser(user.getEmail()) != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("User already exists with this email.")
                    .build();
        }
        userStorage.addUser(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(User user) {
        User existingUser = userStorage.getUser(user.getEmail());
        if (existingUser == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }
        userStorage.updateUser(user);
        return Response.status(Response.Status.OK).entity(user).build();
    }

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser() {
        return "This should work";
    }

}
