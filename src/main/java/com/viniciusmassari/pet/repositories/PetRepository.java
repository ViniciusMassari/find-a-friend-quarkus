package com.viniciusmassari.pet.repositories;


import com.viniciusmassari.pet.entity.PetEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class PetRepository implements PanacheRepository<PetEntity> {
}
