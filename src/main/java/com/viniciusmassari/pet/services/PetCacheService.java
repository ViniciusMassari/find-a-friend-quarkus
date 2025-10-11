package com.viniciusmassari.pet.services;

import com.viniciusmassari.pet.entity.PetEntity;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class PetCacheService {

    private final ValueCommands<String, PetEntity> countCommands;

    public PetCacheService(RedisDataSource rds) {
        this.countCommands = rds.value(PetEntity.class);
    }


   public PetEntity get(String key) {
       return countCommands.get(key);
    }

    public void set(String key, PetEntity pet){
        this.countCommands.set(key, pet);
    }

    public void delete(String key) {
        this.countCommands.getdel(key);
    }
}
