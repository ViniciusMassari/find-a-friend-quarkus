package com.viniciusmassari.pet.controllers;

import com.viniciusmassari.exceptions.OrganizationNotFound;
import com.viniciusmassari.pet.dtos.CreatePetRequestDTO;
import com.viniciusmassari.pet.usecases.CreatePetUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/pet")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(description = "Operações referentes aos pets",name = "Pets")
public class PetController {

    @Inject
    CreatePetUseCase createPet;

    @Inject
    JsonWebToken jwt;

    @POST()
    @Path("/")
    public Response create_pet(@Valid CreatePetRequestDTO createPet, @Context SecurityContext context){
        if(!this.jwt.getSubject().equals(context.getUserPrincipal().getName())){
            return Response.status(Response.Status.UNAUTHORIZED).entity("Você não está autorizado a continuar").build();
        }
        try {
            this.createPet.execute(createPet, this.jwt.getSubject());
            return Response.status(Response.Status.CREATED).build();
        } catch (OrganizationNotFound e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Não foi possível criar pet, verifique os dados e tente novamente").build();
        } catch (Exception e){
            return Response.serverError().entity("Erro ao tentar criar pet, tente novamente mais tarde").build();
        }
    }
}
