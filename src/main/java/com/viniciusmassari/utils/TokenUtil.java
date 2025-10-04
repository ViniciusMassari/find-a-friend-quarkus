package com.viniciusmassari.utils;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import java.time.Duration;
import java.time.Instant;
import java.util.*;


import java.security.KeyFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.viniciusmassari.organization.controllers.OrganizationController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.smallrye.jwt.build.Jwt;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TokenUtil {

    private static final Logger LOG = Logger.getLogger(TokenUtil.class);

    public String createToken(String id){
        return Jwt.subject(id).issuer("find-a-friend").expiresAt(Instant.now().plus(30, TimeUnit.MINUTES.toChronoUnit())).groups(Set.of("Organization")).sign();
    }

    private RSAPrivateKey getPrivateKey() throws Exception {
        return (RSAPrivateKey) getKey("private_key.pem", true);
    }

    public RSAPublicKey getPublicKey() throws Exception {
        return (RSAPublicKey) getKey("public_key.pem", false);
    }

    private Object getKey(String filename, boolean isPrivate) throws Exception {
        try (InputStream keyStream = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (keyStream == null) {
                throw new FileNotFoundException("Key file not found: " + filename);
            }

            String keyContent = new String(keyStream.readAllBytes(), StandardCharsets.UTF_8);
            keyContent = keyContent.replaceAll("\\r?\\n", "")
                    .replace("-----BEGIN " + (isPrivate ? "PRIVATE" : "PUBLIC") + " KEY-----", "")
                    .replace("-----END " + (isPrivate ? "PRIVATE" : "PUBLIC") + " KEY-----", "");

            byte[] encoded = Base64.getDecoder().decode(keyContent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return isPrivate
                    ? keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encoded))
                    : keyFactory.generatePublic(new X509EncodedKeySpec(encoded));
        }
    }
}
