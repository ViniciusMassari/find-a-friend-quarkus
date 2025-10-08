package com.viniciusmassari.exceptions;

import io.smallrye.faulttolerance.api.RateLimitException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TooManyRequestsException  implements ExceptionMapper<RateLimitException> {
    @Override
    public Response toResponse(RateLimitException e) {
        return Response.status(Response.Status.TOO_MANY_REQUESTS).entity("Too many requests").build();
    }
}
