package com.viniciusmassari.pet.usecases;

import com.viniciusmassari.pet.dto.GetPetByFiltersDTO;
import com.viniciusmassari.pet.entity.PetEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class GetPetByFilterUseCase {
    public List<PetEntity> execute(GetPetByFiltersDTO getPetByFilters){
       StringBuilder query = new StringBuilder();
       Map<String, Object> params = new HashMap<>();

       query.append("city= :city");
       params.put("city",getPetByFilters.city());

       if(getPetByFilters.age() != null){
        query.append(" and age = :age");
        params.put("age", getPetByFilters.age());
       }

       if(getPetByFilters.energy() != null){
           query.append(" and energy = :energy");
           params.put("energy", getPetByFilters.energy());
       }
       if(getPetByFilters.living_space() != null){
           query.append(" and living_space = :living_space");
           params.put("living_space", getPetByFilters.living_space());
       }

       if(getPetByFilters.independence() != null){
           query.append(" and independence = :independence");
           params.put("independence", getPetByFilters.independence());
       }

       var result = PetEntity.find(query.toString(), params);
       if(result != null){
           return result.list();
       }
    return List.of();
    }
}
