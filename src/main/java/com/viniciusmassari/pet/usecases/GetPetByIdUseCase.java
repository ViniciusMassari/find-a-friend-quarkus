package com.viniciusmassari.pet.usecases;

import com.viniciusmassari.exceptions.PetNotFoundError;
import com.viniciusmassari.pet.entity.PetEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class GetPetByIdUseCase {

    public PetEntity execute(String id){

        try{
          UUID petId =   UUID.fromString(id);
            PetEntity pet = PetEntity.findById(petId);
            if(pet == null){
                throw new PetNotFoundError("Pet doesn't exist");
            }
            return pet;
        } catch(IllegalArgumentException e){
            throw new PetNotFoundError("Passed Id is not a valid id");
        }
    }
}
