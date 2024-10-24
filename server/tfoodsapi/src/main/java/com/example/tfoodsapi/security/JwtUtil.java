package com.example.tfoodsapi.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.SignedJWT;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 1 ngày

    public String generateToken(Integer userID) {
        try {
            JWSSigner signer = new MACSigner(jwtSecret);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(Integer.toString(userID))

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

    public boolean validateToken(String token, String username) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(jwtSecret);

            // Kiểm tra chữ ký
            if (!signedJWT.verify(verifier)) {
                return false;
            }

            // Kiểm tra username và thời gian hết hạn
            String extractedUsername = signedJWT.getJWTClaimsSet().getSubject();
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            return extractedUsername.equals(username) && expirationTime != null && !expirationTime.before(new Date());
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

}
