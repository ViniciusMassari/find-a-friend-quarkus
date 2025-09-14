package com.viniciusmassari.organization.controllers;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import com.viniciusmassari.exceptions.OrganizationAlreadyExists;
import com.viniciusmassari.exceptions.OrganizationNotFound;
import com.viniciusmassari.organization.dtos.LoginOrganizationRequestDTO;
import com.viniciusmassari.organization.dtos.LoginOrganizationResponseDTO;
import com.viniciusmassari.organization.dtos.SignInOrganizationDTO;
import com.viniciusmassari.organization.usecases.LoginUseCase;
import com.viniciusmassari.organization.usecases.SignInUseCase;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/organization")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Organização", description = "Operações referentes as organizações")
public class OrganizationController {

    private static final Logger LOG = Logger.getLogger(OrganizationController.class);


    @Inject
    SignInUseCase signInUseCase;

    @Inject
    LoginUseCase loginUseCase;

    @POST
    @Path("/signin")
    @PermitAll()
    public Response signin_org(@Valid SignInOrganizationDTO signInOrganization) {
        try {
            this.signInUseCase.execute(signInOrganization);
            return Response.status(Status.CREATED).build();
        } catch (OrganizationAlreadyExists e) {
            LOG.error(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Não foi possível criar organização, tente novamente mais tarde").build();
        }
    }

    @POST
    @Path("/login")
    @PermitAll()
    public Response login_org(@Valid LoginOrganizationRequestDTO loginOrganization) {
        try {
            LoginOrganizationResponseDTO response = this.loginUseCase.execute(loginOrganization);
            return Response.ok().entity(response).build();
        } catch (OrganizationNotFound e) {
            LOG.error(e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Response.serverError().entity("Erro interno no servidor, tente novamente mais tarde").build();
        }
    }


}
