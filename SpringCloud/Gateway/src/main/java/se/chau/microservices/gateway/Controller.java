package se.chau.microservices.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.chau.microservices.gateway.WebBuild.OAuth2TokenResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@RestController
public class Controller {
    // http://localhost:9999/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write
//  https://localhost:8443/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write
    // http://db3415538ba9:auth-server:9999/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=http://546b7e13a730:gateway:8443/oauth2/code&scope=openid%20product:read%20product:write

    /*
    *
    *
    * .
    *
    * db3415538ba9:auth-server:9999
    http://chau:{noop}123@localhost:9999/oauth2/authorize?response_type=code&redirect_uri=https://localhost:8443/oauth2/code&scope=openid%20product:read%20product:write

curl -X POST http://chau:{noop}123@localhost:9999/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code" \
  -d "code=T6g1srJlwkB4Eiwaopx2fMh05khPFZ8AFen4hoHtLD22OhpSLfYvcDJlsbs5aI6vtGeDr_Om9CRC5qrCBAEah1vmVKcBnT_5wI4MM0tSfNIfOqZORta2YhDvvZuOUr3y"\
  -d "redirect_uri=https://localhost:8443/oauth2/code"

    *
    *
    * */
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Value("${app.auth.host}")
    private String authohost;

    @GetMapping(value = "/oauth2/code")
    public OAuth2TokenResponse exchangeCodeForToken(
            @RequestParam(name = "code", required = false) String code) throws Exception {
        ///"http://" + authohost + ":9999/oauth2/token"
        logger.debug("test oauth " + "http://" + authohost + ":9999/oauth2/token");
        if (code == null || code.isEmpty()) {
            logger.error("Authorization code is missing or empty");
            throw new IllegalArgumentException("Authorization code is required");
        }

        HttpClient client = HttpClient.newHttpClient();

        String credentials = "chau" + ":" + "{noop}123";
        String auth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
        logger.info("Sending OAuth2 token request to {}", authohost);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + authohost + ":9999/oauth2/token"))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code&code=" + code + "&redirect_uri=https://localhost:8443/oauth2/code"))
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .setHeader("Authorization", auth)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                logger.error("Error from OAuth2 server: {}", response.body());
                throw new RuntimeException("Failed to obtain OAuth2 token");
            }
            return parseTokenResponse(response.body());
        } catch (Exception e) {
            logger.error("Error during OAuth2 token exchange", e);
            throw new RuntimeException("OAuth2 token exchange failed", e);
        }
    }
    public static OAuth2TokenResponse parseTokenResponse(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, OAuth2TokenResponse.class);
    }

    //http://localhost:9999/oauth2/authorize?grant_type=authorization_code&client_id=chau&redirect_uri=https://localhost:8443/oauth2/get-token&code=NsSH5JcKp2cTOaGC0aw4Qo765dkKNK__45rPA2beE_imhn6wYBSBuMtNSb9CAE_9MQssoo4R5TsYJbcM3fG_2iNwEjL3pUIp4unZqMvJk0SIqNFhcoWMMD6h1FDLTwrd
    @GetMapping(value = "/oauth2/get-token")
    public String test1(@RequestBody RequestBody res) {
        System.out.println("oauth2 token");
        return res.toString();
    }
}
