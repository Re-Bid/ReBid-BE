package server.rebid.auth.security.oauth.dto;

import lombok.Builder;
import lombok.Getter;
import server.rebid.auth.security.oauth.enums.SocialType;

import java.util.Map;

@Getter
public class OAuth2Attributes {
    private Map<String, Object> attributes;
    private String email;
    private String nickname;
    private String profileImage;
    private Integer age;
    private String gender;

    @Builder
    private OAuth2Attributes(Map<String, Object> attributes, String email, String nickname, String profileImage, Integer age, String gender){
        this.attributes = attributes;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.age = age;
        this.gender = gender;
    }

    public static OAuth2Attributes of(SocialType socialType, Map<String, Object> attributes){
        return switch (socialType){
            case NAVER -> ofNaver(attributes);
        };
    }

    private static OAuth2Attributes ofNaver(Map<String, Object> attributes){
        Map<String, String> response = (Map<String, String>)attributes.get("response");
        return OAuth2Attributes.builder()
                .email(response.get("email"))
                .nickname(response.get("nickname"))
                .profileImage(response.get("profile_image"))
                .age(response.get("age") != null ? Integer.parseInt(response.get("age")) : null)
                .gender(response.get("gender"))
                .build();
    }
}