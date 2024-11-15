package se.chau.microservices.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import se.chau.microservices.Config.PlainTextPasswordEncoder;

@Service
public class UserAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticationProvider.class);

    @Autowired
    private JpaUserDetailsService userDetailsService;
    @Autowired
    //    @Autowired
    private PlainTextPasswordEncoder plainTextPasswordEncoder;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        System.out.println("test authenticate " + password);
        CustomUserDetails user = userDetailsService.loadUserByUsername(username);
//        return switch (user.getUser().getAlgorithm()) {
//            case BCRYPT -> this.checkPassword(user, password, this.bCryptPasswordEncoder());
//            case SCRYPT -> this.checkPassword(user, password, this.sCryptPasswordEncoder());
//            case SHA512 -> this.checkPassword(user, password, plainTextPasswordEncoder);
//        };
        return checkPassword(user,password,plainTextPasswordEncoder);
    }
    private Authentication checkPassword(CustomUserDetails user, String rawPassword, PasswordEncoder encoder) {
        if (encoder.matches(rawPassword, extractHashedPassword(user.getPassword()))) {
            return new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities());
        } else {

            throw new BadCredentialsException("Bad credentials");
        }
    }
    //    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //    @Bean
    public SCryptPasswordEncoder sCryptPasswordEncoder() {
        int cpuCost = (int) Math.pow(2, 14); // N (CPU cost factor), nên là lũy thừa của 2
        int memoryCost = 8;  // r (Memory cost factor)
        int parallelization = 1; // p (Parallelization factor)
        int keyLength = 32; // Độ dài khóa
        int saltLength = 16; // Độ dài của salt
        // Tạo SCryptPasswordEncoder với constructor tùy chỉnh
        return new SCryptPasswordEncoder(
                cpuCost, memoryCost, parallelization, keyLength, saltLength
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
    public static String extractHashedPassword(String password) {
        if (password.startsWith("{BCRYPT}")) {
            return password.substring("{BCRYPT}".length());
        }
        if (password.startsWith("{SCRYPT}")) {
            return password.substring("{SCRYPT}".length());
        }
        if (password.startsWith("{SHA512}")) {
            return password.substring("{SHA512}".length());
        }
        throw new IllegalArgumentException("Invalid password format");
    }
}
