package com.example.Aiking.Service.Oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOauthUser2  implements OAuth2User {
    private OAuth2User oAuth2User;

    public CustomOauthUser2(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }
    public String getEmail(){
        return oAuth2User.<String>getAttribute("email");
    }
    public String toString1(){
        return "email " + this.getEmail() + " name " + getName();
    }
}
