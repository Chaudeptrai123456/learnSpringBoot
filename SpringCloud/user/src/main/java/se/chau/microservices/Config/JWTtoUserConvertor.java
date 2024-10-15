package se.chau.microservices.Config;
import java.util.Collections;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import se.chau.microservices.api.core.User.User;

@Component
public class JWTtoUserConvertor implements Converter<User,UsernamePasswordAuthenticationToken> {
    @Override
    public UsernamePasswordAuthenticationToken convert(User source) {
        User user = new User();
        user.setUsername(source.getUsername());
        return new UsernamePasswordAuthenticationToken(user, source, Collections.EMPTY_LIST);
    }

    @Override
    public <U> Converter<User, U> andThen(Converter<? super UsernamePasswordAuthenticationToken, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
