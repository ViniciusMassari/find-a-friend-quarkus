package com.viniciusmassari.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Collections;
import java.util.Map;

public class RateLimitTestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "test-no-rate-limit";
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        return Collections.singletonMap("quarkus.fault-tolerance.enabled", "true");
    }
}
