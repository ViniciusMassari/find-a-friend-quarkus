package com.viniciusmassari.pet.controllers;

import com.viniciusmassari.exceptions.OrganizationNotFound;
import com.viniciusmassari.exceptions.PetNotFoundError;
import com.viniciusmassari.pet.dto.CreatePetRequestDTO;
import com.viniciusmassari.pet.dto.GetPetByFiltersDTO;
import com.viniciusmassari.pet.dto.GetPetByIdResponse;
import com.viniciusmassari.pet.dto.GetPetsByFiltersResponseDTO;
import com.viniciusmassari.pet.entity.PetEntity;
import com.viniciusmassari.pet.services.PetCacheService;
import com.viniciusmassari.pet.usecases.CreatePetUseCase;
import com.viniciusmassari.pet.usecases.GetPetByFilterUseCase;
import com.viniciusmassari.pet.usecases.GetPetByIdUseCase;
import io.smallrye.faulttolerance.api.RateLimit;
import io.smallrye.faulttolerance.api.RateLimitException;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jdk.jfr.ContentType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Path("/pet")
@Tag(description = "Operações referentes aos pets",name = "Pets")
public class PetController {

    private static final Logger LOG = Logger.getLogger(PetController.class);


    @Inject
    GetPetByIdUseCase getPetById;

    @Inject
    CreatePetUseCase createPet;

    @Inject
    GetPetByFilterUseCase getPetByFilterUseCase;

    @Inject
    JsonWebToken jwt;

    @Inject
    public PetCacheService petCache;

    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create_pet(@Valid CreatePetRequestDTO createPet, @Context SecurityContext context){
        if(!this.jwt.getSubject().equals(context.getUserPrincipal().getName())){
            LOG.error("Users não coincidem");
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON_TYPE).entity("Você não está autorizado a continuar").build();
        }
        try {
            this.createPet.execute(createPet, this.jwt.getSubject());
            return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (OrganizationNotFound e){
            LOG.error(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity("Não foi possível criar pet, verifique os dados e tente novamente").build();
        } catch (Exception e){
            LOG.error("Erro interno");
            return Response.serverError().type(MediaType.APPLICATION_JSON_TYPE).entity("Erro ao tentar criar pet, tente novamente mais tarde").build();
        }
    }

    @GET
    @PermitAll

    public Response get_pet_by_filters(@Valid @BeanParam GetPetByFiltersDTO getPetByFilters){
      try{
          List<PetEntity> response = this.getPetByFilterUseCase.execute(getPetByFilters);

          return Response.ok().entity(new GetPetsByFiltersResponseDTO(response)).build();
      } catch(Exception e){
          LOG.error(e.getLocalizedMessage());
          return Response.serverError().entity("Não foi possível buscar por pets, tente novamente").build();
      }
    }

    @GET
    @Path("/{id}")
    @PermitAll
    @RateLimit(value = 3,window = 10, windowUnit = ChronoUnit.SECONDS)
    public Response get_pet_info(@PathParam("id") String id){
      PetEntity cache =  this.petCache.get(id);
      if(cache != null){
          return Response.status(200).entity(cache).header("X-Cache", "HIT").build();
      }
        try{
            PetEntity pet = this.getPetById.execute(id);
            this.petCache.set(pet.id.toString(), pet);
            return Response.ok().entity(new GetPetByIdResponse(pet)).header("X-Cache", "MISS").type(MediaType.APPLICATION_JSON).build();
        }
        catch(PetNotFoundError | IllegalArgumentException e){
            LOG.error(e.getLocalizedMessage());
            return Response.status(400).entity("Pet solicitado não existe, verifique os dados e tente novamente").build();
        } catch(Exception e){
            LOG.error(e.getLocalizedMessage());
            return Response.serverError().entity("Não foi possível buscar pelo pet, tente novamente").build();
        }
    }
}
