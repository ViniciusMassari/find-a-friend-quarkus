package com.viniciusmassari.exceptions;

import com.viniciusmassari.exceptions.dto.ValidationErrorDTO;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@Provider
public class ValidationErrorException implements ExceptionMapper<ConstraintViolationException> {
    private static final Logger LOG = Logger.getLogger(ValidationErrorException.class);
    List<ValidationErrorDTO> errors = new ArrayList<>();

    @Override
    public Response toResponse(ConstraintViolationException e) {
        e.getConstraintViolations().forEach(cv ->{
            String field = cv.getPropertyPath().toString().split("\\.")[2];
            String message = cv.getMessageTemplate();
            this.errors.add(new ValidationErrorDTO(message,field));
        });

        return Response.status(Response.Status.BAD_REQUEST).entity(this.errors).build();
    }


}
