package org.rajesh.jwtc;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

public class JWTUtil {
    public static String createJWT(HashMap payload, String secret, String algorithm, long ttlMillis) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = switch (algorithm) {
            case "HS256" -> SignatureAlgorithm.HS256;
            case "HS384" -> SignatureAlgorithm.HS384;
            case "HS512" -> SignatureAlgorithm.HS512;
            case "RS256" -> SignatureAlgorithm.RS256;
            case "RS384" -> SignatureAlgorithm.RS384;
            case "RS512" -> SignatureAlgorithm.RS512;
            case "ES256" -> SignatureAlgorithm.ES256;
            case "ES384" -> SignatureAlgorithm.ES384;
            case "ES512" -> SignatureAlgorithm.ES512;
            default -> SignatureAlgorithm.HS256;
        };

        long nowMillis = System.currentTimeMillis();


        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setClaims(payload)
                .setExpiration(new Date(nowMillis + ttlMillis))
                .signWith(signatureAlgorithm, secret);

        String jwt = builder.compact();
        return jwt;
    }

    public static Claims decodeJWT(String jwt, String secret) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
