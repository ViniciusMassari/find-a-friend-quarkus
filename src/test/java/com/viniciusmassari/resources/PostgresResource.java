package com.viniciusmassari.resources;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

public class PostgresResource implements QuarkusTestResourceLifecycleManager {

    private PostgreSQLContainer<?> db;

    @Override
    public Map<String, String> start() {

        db = new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test").withExposedPorts(5432);
        db.start();

        return Map.of(
                "quarkus.datasource.db-kind", "postgresql",
                "quarkus.datasource.jdbc.url", db.getJdbcUrl(),
                "quarkus.datasource.username", db.getUsername(),
                "quarkus.datasource.password", db.getPassword(),
                "quarkus.hibernate-orm.database.generation", "drop-and-create"
        );
    }

    @Override
    public void stop() {
        if (db != null) {
            db.stop();
        }
    }
}
