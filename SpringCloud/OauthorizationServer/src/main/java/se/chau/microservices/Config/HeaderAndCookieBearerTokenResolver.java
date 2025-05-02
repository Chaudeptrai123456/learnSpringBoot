package se.chau.microservices.Config;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public class HeaderAndCookieBearerTokenResolver implements BearerTokenResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String COOKIE_NAME = "Authorization";

    @Override
    public String resolve(HttpServletRequest request) {
        // 1. Ưu tiên header
        String headerAuth = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER_PREFIX)) {
            return headerAuth;
        }

        // 2. Nếu không có header, thử từ cookie
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                    .map(cookie -> {
                        String token = cookie.getValue();
                        return token.startsWith(BEARER_PREFIX) ? token : BEARER_PREFIX + token;
                    })
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }
}
