package com.viniciusmassari.pet.entity;

import com.viniciusmassari.organization.entities.OrganizationEntity;
import com.viniciusmassari.pet.dto.CreatePetRequestDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "pet")
public class PetEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String description;

    @Column(nullable = false)
    @Enumerated
    public Age age;

    @Column(nullable = false)
    @Enumerated()
    public LivingSpace living_space;

    @Column(nullable = false)
    @Enumerated()
    public PetIndependence independence;

    @Column(nullable = false)
    @Enumerated
    public PetEnergy energy;

    @Column(nullable = false)
    @Enumerated
    public PetSize pet_size;


    @Column(nullable = true)
    public String requirements;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "organization_id", insertable = false, updatable = false)
    public OrganizationEntity organization;

    @Column(name="organization_id")
    public UUID organizationId;

    @CreationTimestamp
    public LocalDateTime createdAt;

    @UpdateTimestamp
    public LocalDateTime updatedAt;


    @Transactional
    public static void addNewPet(CreatePetRequestDTO createPet, UUID organizationId){
        PetEntity pet = new PetEntity();
        pet.name = createPet.name();
        pet.age = createPet.age();
        pet.pet_size = createPet.pet_size();
        pet.description = createPet.description();
        pet.energy = createPet.energy();
        pet.independence = createPet.independence();
        pet.living_space = createPet.living_space();
        pet.organizationId = organizationId;
        pet.requirements = createPet.requirements();
        persist(pet);
    }
}
