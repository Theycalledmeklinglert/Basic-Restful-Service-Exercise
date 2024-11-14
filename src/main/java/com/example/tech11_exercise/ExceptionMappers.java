package com.example.tech11_exercise;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.stream.Collectors;

@Provider
public class ExceptionMappers {

    // for JSON parsing errors
    public static class JsonParseExceptionMapper implements ExceptionMapper<JsonMappingException> {
        @Override
        public Response toResponse(JsonMappingException exception) {
            String errorMessage = "Invalid JSON format: " + exception.getOriginalMessage();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .type("text/plain")
                    .build();
        }
    }

    // for validation errors
    public static class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
        @Override
        public Response toResponse(ConstraintViolationException exception) {
            String errorMessage = exception.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid fields for user object in JSON: " + errorMessage)
                    .type("text/plain")
                    .build();
        }
    }
}
