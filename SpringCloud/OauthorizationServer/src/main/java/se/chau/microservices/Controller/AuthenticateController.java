package se.chau.microservices.Controller;

import io.netty.handler.codec.MessageAggregationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import se.chau.microservices.Config.PlainTextPasswordEncoder;
import se.chau.microservices.Entity.Authority;
import se.chau.microservices.Entity.UserEntity;
import se.chau.microservices.Service.*;
import se.chau.microservices.api.core.User.Account;
import se.chau.microservices.api.core.User.Token;
import se.chau.microservices.api.core.User.User;
import se.chau.microservices.api.core.User.UserService;
import se.chau.microservices.util.http.JwtService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//@RestController
@Controller
public class AuthenticateController implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);
    private final AuthenticationManager authenticationManager;
    @Value("${email.url}")
    private String EMAIL_SERVICE_URL;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PlainTextPasswordEncoder encoder;
    private Authentication authentication;
    private final EmailService emailService;
    private final RedisService redisService;
    private final OptService optService;
    @Autowired
    public AuthenticateController(AuthenticationManager authenticationManager, UserRepository userRepository, AuthorityRepository authorityRepository, PlainTextPasswordEncoder encoder , EmailService emailService, RedisService redisService, OptService optService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
        this.emailService = emailService;
        this.redisService = redisService;
        this.optService = optService;
    }
    @GetMapping(
            value = "/user/test"
    )
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("test", HttpStatusCode.valueOf(200));
    }
    @Override
    public ResponseEntity<String> Register(@ModelAttribute User temp) {
        System.out.println("test authentication register");
        if (userRepository.existsByEmail(temp.getEmail())){
            return new ResponseEntity<>("Email has existed",HttpStatusCode.valueOf(400));
        }
        if (userRepository.existsByUsername(temp.getUsername())) {
            return new ResponseEntity<>("Username has existed",HttpStatusCode.valueOf(400));
        }
        try {
            LOG.info("test password " + temp.getPassword());
            String otp = optService.generateOtp(temp.getEmail());
            this.emailService.sendOpt(temp,otp);
            this.redisService.writeUser(temp,otp);

            return ResponseEntity.ok("check your email");
        }catch (MessageAggregationException ex) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("error " + ex);
        }
    }


    @Override
    public ResponseEntity<Token> Login(Account account) {
        try {
            Authentication authenticationRequest =  UsernamePasswordAuthenticationToken.unauthenticated(account.getUsername(), account.getPassword());
            Authentication a=  this.authenticationManager.authenticate(authenticationRequest);
            authentication = a;
            String accessToken = JwtService.generateAccessToken(a);
            LOG.info("User login userId: " + account.getUsername());
            Token token = new Token();
            token.setAccess(accessToken);
            token.setRefresh(JwtService.generateRefreshToken(a));
            token.setUsername(authenticationRequest.getName());

            JwtService.validateToken(accessToken);

            return ResponseEntity.ok(token);
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> testSecurity() {
        return null;
    }

    @Override
    public ResponseEntity<String> getAccessToken(HttpServletRequest request)  {
            return new ResponseEntity<String>(String.valueOf(request.getAttribute("refresh")),HttpStatusCode.valueOf(200));
    }
    private Boolean checkOtp(String email,String opt) {
        return this.optService.validateOtp(email,opt);
    }
    @Override
//    public ResponseEntity<String> verifyRegister(@RequestParam String email, @RequestParam String otp) {
        // check otp
    public String verifyRegister(@RequestParam String email, @RequestParam String otp) {
        if (!this.checkOtp(email,otp)) {
            return "Error";
        } else {
            User temp = this.redisService.getUser(email,otp).block();
            LOG.info("verifyRegister " + temp.getUsername() + " " + temp.getEmail() + " " + temp.getPassword());
            String hashedPassword = encoder.encode(temp.getPassword());
            UserEntity user = new UserEntity();
            user.setPassword("{SHA512}"+hashedPassword);
            user.setUsername(temp.getUsername());
            user.setRegistrationDate(LocalDateTime.now());
            Authority a = authorityRepository.findAuthorityByName("USER").orElseThrow();
            List<Authority> list = new ArrayList<>();
            list.add(a);
            user.setAuthorities(list);
            user.setEmail(temp.getEmail());
            List<UserEntity> b = new ArrayList<>();
            b.add(user);
            a.setUser(b);
            userRepository.save(user);
            authorityRepository.save(a);
//            return new ResponseEntity<String>("Sign up Successfully", HttpStatusCode.valueOf(200));
            return "custom_login";
        }
    }

    @GetMapping(
            value = "/user/info",
            produces = "application/json"
    )
    public CustomUserDetails getInfo(String sub) {
        return new CustomUserDetails(this.userRepository.findUserByUsername(sub).get());
    }

}
