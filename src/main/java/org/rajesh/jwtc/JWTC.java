package org.rajesh.jwtc;

import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(name = "jwtc", mixinStandardHelpOptions = true, version = "jwtc 0.1",
        description = "Encode and decode JWT tokens.")
public class JWTC implements Callable<Integer> {

    @Parameters(index = "0", description = "Specify encode or decode.")
    String option;

    @Parameters(index = "1", description = "User provided payload.")
    String payload;

    @Option(names = {"-s", "--secret"}, description = "Secret key.")
    String secret;

    @Option(names = {"-a", "--algorithm"}, description = "Algorithm to be used.")
    String algorithm;

    @Option(names = {"-t", "--ttl"}, description = "Time to Live in Milliseconds.")
    long ttlMillis;

    @Option(names = {"-b", "--base64"}, description = "Encode payload in Base64.")
    boolean base64;

    @Option(names = {"-jwt", "--jwt"}, description = "JWT to decode.")
    String jwt;

    @Override
    public Integer call() throws Exception {
        /* Start: Build Claims */
        ObjectMapper payloadMapper = new ObjectMapper();
        HashMap<Object, Object> payloadMap = new HashMap<>();
        try {
            //Convert payload JSON to Map
            payloadMap = payloadMapper.readValue(payload, HashMap.class);
        } catch (Exception e) {
            System.out.println("Error while parsing payload JSON: " + e.getMessage());
        }

        long nowMillis = System.currentTimeMillis();

        payloadMap.put("iat", nowMillis);
        if (ttlMillis > 0) {
            payloadMap.put("exp", nowMillis + ttlMillis);
        }
        /* End: Build Claims */

        /* Start: Build Header */
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", algorithm);
        headerMap.put("typ", "JWT");
        /* End: Build Header */

        if (option.equals("encode")) {
            System.out.println("Encoding claims: " + payloadMap);
            System.out.println("Using secret: " + secret);
            System.out.println("Using algorithm: " + algorithm);

            String jwt = JWTUtil.createJWT(payloadMap, secret, algorithm, ttlMillis);
            System.out.println("JWT: " + jwt);
        } else if (option.equals("decode")) {
            System.out.println("Encoding claims: " + payloadMap);
            System.out.println("Using secret: " + secret);
            System.out.println("Using algorithm: " + algorithm);
            String decodedJWT = JWTUtil.decodeJWT(jwt, secret).toString();
            System.out.println("Decoded JWT: " + decodedJWT);

        } else {
            System.out.println("Invalid option: " + option);
        }
        return 0;
    }

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        int exitCode = new CommandLine(new JWTC()).execute(args);
        System.exit(exitCode);
    }

}
