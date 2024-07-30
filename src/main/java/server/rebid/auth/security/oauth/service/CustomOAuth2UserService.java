package server.rebid.auth.security.oauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.auth.security.oauth.dto.OAuth2Attributes;
import server.rebid.auth.security.oauth.dto.OAuth2UserDTO;
import server.rebid.auth.security.oauth.enums.SocialType;
import server.rebid.entity.Member;
import server.rebid.entity.enums.MemberRole;
import server.rebid.repository.MemberRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String clientRegistrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = SocialType.NAVER.getSocialType(clientRegistrationId);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(socialType, attributes);

        Member member = memberRepository.findByEmail(oAuth2Attributes.getEmail())
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .nickname(oAuth2Attributes.getNickname())
                            .email(oAuth2Attributes.getEmail())
                            .profileImage(oAuth2Attributes.getProfileImage())
                            .age(oAuth2Attributes.getAge())
                            .gender(oAuth2Attributes.getGender())
                            .role(MemberRole.ROLE_USER)
                            .isAllowed(Boolean.FALSE)
                            .build();

                    log.info("가입한 사용자 이메일: {}", newMember.getEmail());
                    return memberRepository.save(newMember);
                });
        log.info("로그인한 사용자 이메일: {}", member.getEmail());
        OAuth2UserDTO oAuth2UserDTO = OAuth2UserDTO.from(member);
        return new CustomOAuth2User(oAuth2UserDTO);
    }
}
