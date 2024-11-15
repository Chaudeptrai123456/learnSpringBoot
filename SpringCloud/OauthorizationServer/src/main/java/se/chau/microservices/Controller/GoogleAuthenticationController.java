//package se.chau.microservices.Controller;
//
//import ch.qos.logback.core.model.Model;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.web.bind.annotation.RestController;
//import se.chau.microservices.api.core.User.GoogleService;
//
//@RestController
//public class GoogleAuthenticationController implements GoogleService {
//    @Override
//    public ResponseEntity<String> loginGoogle() {
//        System.out.println("test google");
//        return new ResponseEntity<>("test", HttpStatusCode.valueOf(200));
//    }
//
//    @Override
//    public String home(OAuth2User principal, Model model) {
//        return null;
//    }
//}
