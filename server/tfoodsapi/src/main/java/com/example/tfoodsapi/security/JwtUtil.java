package com.example.tfoodsapi.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.example.tfoodsapi.projectpackage.projectenum.Role;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.SignedJWT;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 1 day

    public String generateToken(Integer userID, Role role) {
        try {
            JWSSigner signer = new MACSigner(jwtSecret);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(Integer.toString(userID))
                    .claim("role", role) // Add role to JWT claims
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            System.out.println(jwtSecret);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public boolean validateToken(String token, String username, String role) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(jwtSecret);

            if (!signedJWT.verify(verifier)) {
                return false;
            }

            // Validate username, role, and expiration time
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String extractedUsername = claims.getSubject();
            String extractedRole = claims.getStringClaim("role");
            Date expirationTime = claims.getExpirationTime();

            return extractedUsername.equals(username) &&
                    extractedRole.equals(role) &&
                    expirationTime != null &&
                    !expirationTime.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Integer extractToID(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return Integer.parseInt(signedJWT.getJWTClaimsSet().getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    public Role extractRole(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String roleString = signedJWT.getJWTClaimsSet().getStringClaim("role");
            return Role.valueOf(roleString); // Convert String to Role enum
        } catch (Exception e) {
            return null;
        }
    }

}
