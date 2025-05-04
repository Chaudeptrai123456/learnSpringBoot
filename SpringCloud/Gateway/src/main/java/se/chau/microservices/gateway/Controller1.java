package se.chau.microservices.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import se.chau.microservices.gateway.WebBuild.OAuth2TokenResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

//@RestController
@Controller
public class Controller1 {
    // https://localhost:8443/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write
    // http://localhost:9999/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write
    // http://gatea:9999/oauth2/authorize?response_type=codq&client_id=chau&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write
    // https://gateway:8443/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write
    // https://gateway:8443/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write

    // http://14.225.206.109:9999/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://14.225.206.109:8443/oauth2/code&scope=openid%20product:read%20product:write
    // https://chaudeptrai.pro.vn:8443/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://backend.chaudeptrai.pro.vn:8443/oauth2/code&scope=openid%20product:read%20product:write

    private static final Logger logger = LoggerFactory.getLogger(Controller1.class);

    @Value("${host.auth}")
    private String authohost;
    @Value("${port.auth}")

    private String authport;


    @GetMapping(value = "/oauth2/code")
    public Mono<Rendering> exchangeCodeForToken(
            @RequestParam(name = "code", required = false) String code) throws Exception {
        ///"http://" + authohost + ":9999/oauth2/token"
        logger.debug("test oauth " + "http://" + authohost + ":"+authport+"/oauth2/token");
        String uri =  "http://" + authohost + ":"+authport+"/oauth2/token";
        if (code == null || code.isEmpty()) {
            logger.error("Authorization code is missing or empty");
            throw new IllegalArgumentException("Authorization code is required");
        }

        HttpClient client = HttpClient.newHttpClient();

        String credentials = "chau" + ":" + "{noop}123";
        String auth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
        logger.info("Sending OAuth2 token request to {}", authohost);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code&code=" + code + "&redirect_uri=https://" + authohost + ":8443/oauth2/code"))
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .setHeader("Authorization", auth)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                logger.error("Error from OAuth2 server: {}", response.body());
                throw new RuntimeException("Failed to obtain OAuth2 token");
            }
            OAuth2TokenResponse token = parseTokenResponse(response.body());
            return Mono.just(
                    Rendering.view("admin-page")
                            .modelAttribute("accessToken", token.getAccess_token())
                            .modelAttribute("refreshToken", token.getRefresh_token())
                            .build()
            );
        } catch (Exception e) {
            logger.error("Error during OAuth2 token exchange", e);
            throw new RuntimeException("OAuth2 token exchange failed", e);
        }
    }
    public static OAuth2TokenResponse parseTokenResponse(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, OAuth2TokenResponse.class);
    }
    @GetMapping("/admin/page")
    public Mono<Rendering> adminPage(@RequestHeader("Authorization") String token) {
        System.out.println("test admin page");
        return Mono.just(Rendering.view("admin-page")
                .modelAttribute("accessToken", token)
                .build());
    }
    //http://localhost:9999/oauth2/authorize?grant_type=authorization_code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/get-token&code=NsSH5JcKp2cTOaGC0aw4Qo765dkKNK__45rPA2beE_imhn6wYBSBuMtNSb9CAE_9MQssoo4R5TsYJbcM3fG_2iNwEjL3pUIp4unZqMvJk0SIqNFhcoWMMD6h1FDLTwrd
    @GetMapping(value = "/oauth2/get-token")
    public String test1(@RequestBody RequestBody res) {
        System.out.println("oauth2 token");
        return res.toString();
    }
}


//    @GetMapping("/oauth2/code")
//    public Mono<Rendering> exchangeCodeAndDisplayProducts(@RequestParam(name = "code", required = false) String code) throws Exception {
//        if (code == null || code.isEmpty()) {
//            throw new IllegalArgumentException("Authorization code is required");
//        }
//
//        String uri = "http://" + authohost + ":" + authport + "/oauth2/token";
//        String credentials = "chau:{noop}123";
//        String auth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
//
//        HttpClient client = HttpClient.newHttpClient();
//
//        HttpRequest tokenRequest = HttpRequest.newBuilder()
//                .uri(URI.create(uri))
//                .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code&code=" + code +
//                        "&redirect_uri=https://" + authohost + ":8443/oauth2/code"))
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .header("Authorization", auth)
//                .build();
//
//        HttpResponse<String> tokenResponse = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
//        OAuth2TokenResponse token = parseTokenResponse(tokenResponse.body());
//
//
//        return Mono.just(
//                Rendering.view("product-view")
//                        .modelAttribute("accessToken", token.getAccess_token())
//                        .modelAttribute("refreshToken", token.getRefresh_token())
////                        .modelAttribute("productData", apiResponse.body())
//                        .build()
//        );
//    }