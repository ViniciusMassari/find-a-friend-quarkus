package com.viniciusmassari.organization.repositories;

import com.viniciusmassari.organization.entities.OrganizationEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrganizationRepository implements PanacheRepository<OrganizationEntity> {
}
