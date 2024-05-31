package com.nemanja02.projekat.filters;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<ValidationError> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            ValidationError error = new ValidationError(
                    violation.getMessage(),
                    violation.getPropertyPath().toString(),
                    violation.getInvalidValue()
            );
            errors.add(error);
        }
        HashMap<String, Object> response = new HashMap<>();
        response.put("errors", errors);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    public static class ValidationError {
        private String message;
        private String path;
        private Object invalidValue;

        public ValidationError(String message, String path, Object invalidValue) {
            this.message = message;
            this.path = path;
            this.invalidValue = invalidValue;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Object getInvalidValue() {
            return invalidValue;
        }

        public void setInvalidValue(Object invalidValue) {
            this.invalidValue = invalidValue;
        }
    }
}
