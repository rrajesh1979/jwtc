package org.rajesh.jwtc;

import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JWTJ {
    private static final String SECRET_KEY = "FREE_MASON";
    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

    /* Function to create JWT token */
    public static String createJWT(String userInput) {
        // These claims are used to create the JWT
        JSONObject payload = new JSONObject();
        payload.put("userInput", userInput);
        payload.put("iss", "Rajesh");
        payload.put("exp", System.currentTimeMillis() + 60000);
        payload.put("iat", System.currentTimeMillis());

        String encodedPayload = encode(payload.toString().getBytes());

        String encodedHeader = encode(new JSONObject(JWT_HEADER).toString().getBytes());

        String signature = encode(hmacSha256(encodedHeader + "." + encodedPayload, SECRET_KEY));

        String JWToken = encodedHeader + "." + encodedPayload + "." + signature;

        return JWToken;

    }

    private static String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static byte[] hmacSha256(String data, String secret) {
        try {

            //MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = secret.getBytes(StandardCharsets.UTF_8);//digest.digest(secret.getBytes(StandardCharsets.UTF_8));

            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);

            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return signedBytes;
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    public static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }


    public static void main(String[] args) {
        String jwtToken = createJWT("{\"Hello\": \"World\"}");
        System.out.println(jwtToken);

        String[] parts = jwtToken.split("\\.");
        System.out.println(decode(parts[1]));
    }

}
