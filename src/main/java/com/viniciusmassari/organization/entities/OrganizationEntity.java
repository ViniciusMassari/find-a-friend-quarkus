package com.viniciusmassari.organization.entities;

import com.viniciusmassari.pet.entity.PetEntity;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



@Entity(name = "organization")
public class OrganizationEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    public String owner_name;

    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false,updatable = true)
    public String phone_number;

    @Column(nullable = false)
    public String cep;

    @Column(nullable = false)
    public String address;



    @Column(updatable = false, insertable = false)
    @OneToMany(orphanRemoval = true,fetch = FetchType.LAZY, mappedBy = "organization")
    public List<PetEntity> pets;



    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Transactional
    public static void addNewOrganization(String owner_name, String email, String password, String phone_number, String cep,String address){
        OrganizationEntity organization = new OrganizationEntity();
        organization.owner_name = owner_name;
        organization.email = email;
        organization.phone_number = phone_number;
        organization.cep = cep;
        organization.address = address;
        organization.password = BcryptUtil.bcryptHash(password,8);
        persist(organization);
    }

}
