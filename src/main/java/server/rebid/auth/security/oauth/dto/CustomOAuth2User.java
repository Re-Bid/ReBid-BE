package server.rebid.auth.security.oauth.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import server.rebid.entity.enums.MemberRole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2UserDTO oAuth2UserDTO;

    @Override
    public <A> A getAttribute(String name) {
        return (A) getAttributes().get(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "email", oAuth2UserDTO.getEmail(),
                "nickname", oAuth2UserDTO.getNickname(),
                "profileImage", oAuth2UserDTO.getProfileImage()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> oAuth2UserDTO.getRole().toString());
        return collection;
    }

    @Override
    public String getName() {
        return oAuth2UserDTO.getEmail();
    }

    public Long getMemberId(){
        return oAuth2UserDTO.getMemberId();}

    public String getEmail(){
        return oAuth2UserDTO.getEmail();
    }

    public String getNickname(){
        return oAuth2UserDTO.getNickname();
    }

    public String getProfileImage(){
        return oAuth2UserDTO.getProfileImage();
    }

    public MemberRole getRole(){
        return oAuth2UserDTO.getRole();
    }
}
