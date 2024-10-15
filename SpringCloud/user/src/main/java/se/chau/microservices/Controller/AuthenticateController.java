package se.chau.microservices.Controller;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;
import se.chau.microservices.Config.PlainTextPasswordEncoder;
import se.chau.microservices.Entity.Authority;
import se.chau.microservices.Entity.EncryptionAlgorithm;
import se.chau.microservices.Entity.UserEntity;
import se.chau.microservices.Service.AuthorityRepository;
import se.chau.microservices.Service.UserRepository;
import se.chau.microservices.api.core.User.Account;
import se.chau.microservices.api.core.User.Token;
import se.chau.microservices.api.core.User.User;
import se.chau.microservices.api.core.User.UserService;
import se.chau.microservices.jwt.JwtService;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static se.chau.microservices.jwt.JwtService.generateAccessTokenFromRefreshToken;

@RestController
public class AuthenticateController implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SCryptPasswordEncoder sCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PlainTextPasswordEncoder encoder;
    private Authentication authentication;

    @Autowired
    public AuthenticateController(AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder, SCryptPasswordEncoder sCryptPasswordEncoder, UserRepository userRepository, AuthorityRepository authorityRepository, PlainTextPasswordEncoder encoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sCryptPasswordEncoder = sCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
    }

    @Override
    public ResponseEntity<String> Register(User temp) {
        System.out.println("test authentication register");
        if (userRepository.existsByEmail(temp.getEmail())){
            return new ResponseEntity<>("Email has existed",HttpStatusCode.valueOf(400));
        }
        if (userRepository.existsByUsername(temp.getUsername())) {
            return new ResponseEntity<>("Username has existed",HttpStatusCode.valueOf(400));
        }
        //random encryptionAlgorithm
        Random random = new Random();
        EncryptionAlgorithm[] tempEn = EncryptionAlgorithm.values();
        EncryptionAlgorithm encryptionAlgorithm = tempEn[random.nextInt(tempEn.length)];
        UserEntity user = new UserEntity();
        user.setAlgorithm(encryptionAlgorithm);
        //hash password
        String hashedPassword = switch (encryptionAlgorithm) {
            case SHA512 -> encoder.hashWithSHA512(temp.getPassword());
            case SCRYPT -> sCryptPasswordEncoder.encode(temp.getPassword());
            case BCRYPT -> bCryptPasswordEncoder.encode(temp.getPassword());
            default -> throw new IllegalArgumentException("Unsupported encryption algorithm: " + encryptionAlgorithm);
        };
        user.setPassword("{" + encryptionAlgorithm.toString() + "}" + hashedPassword);
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
        return new ResponseEntity<>("password " + temp.getPassword(),HttpStatusCode.valueOf(200));
    }

    @Override
    public ResponseEntity<Token> Login(Account account) {
        try {
            Authentication authenticationRequest =  UsernamePasswordAuthenticationToken.unauthenticated(account.getUsername(), account.getPassword());
            Authentication a=  this.authenticationManager.authenticate(authenticationRequest);
            authentication = a;
            String accessToken = JwtService.generateAccessToken(a);
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
            String jwt = request.getHeader("Authorization").substring(7); // Get JWT
        try {
            return new ResponseEntity<String>(generateAccessTokenFromRefreshToken(jwt),HttpStatusCode.valueOf(200));
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }

    }

//    @Override
//    public ResponseEntity<String> testSecurity() {
////        LOG.debug("test create authority");
////        Authority authority = new Authority();
////        authority.setName("USER");
////        Authority authority1 = new Authority();
////        authority.setName("ADMIN");
////        authorityRepository.save(authority);
////        authorityRepository.save(authority1);
////        UserEntity user = new UserEntity();
////        user.setAlgorithm(EncryptionAlgorithm.SCRYPT);
////        user.setPassword(encoder.hashWithSHA512("123"));
////        user.setUsername("chau");
////        user.setRegistrationDate(LocalDateTime.now());
////        Authority a = authorityRepository.findAuthorityByName("USER").orElseThrow();
////        List<Authority> list = new ArrayList<>();
////        list.add(a);
////        user.setAuthorities(list);
////        user.setEmail("test123@gmail.com");
////        List<UserEntity> b = new ArrayList<>();
////        b.add(user);
////        a.setUser(b);
////        userRepository.save(user);
//        return new ResponseEntity<>("test authentication",HttpStatusCode.valueOf(200));
//    }
}
