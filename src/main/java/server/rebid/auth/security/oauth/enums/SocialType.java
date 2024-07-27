package server.rebid.auth.security.oauth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    NAVER("naver");

    private final String clientRegistrationId;

    public SocialType getSocialType(String clientRegistrationId){
        for(SocialType socialType: SocialType.values()){
            if(socialType.getClientRegistrationId().equals(clientRegistrationId)){
                return socialType;
            }
        }
        throw new OAuth2AuthenticationException("올바른 OAuth 제공자가 아님 : %s".formatted(clientRegistrationId));
    }
}
