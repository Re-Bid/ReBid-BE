package server.rebid.auth.security.oauth.dto;

import lombok.Builder;
import lombok.Getter;
import server.rebid.entity.Member;
import server.rebid.entity.enums.MemberRole;

@Getter
public class OAuth2UserDTO {
    private Long memberId;
    private String nickname;
    private String email;
    private String profileImage;
    private MemberRole role;

    @Builder
    private OAuth2UserDTO(Long memberId, String nickname, String email, String profileImage, MemberRole role){
        this.memberId = memberId;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.role = role;
    }

    public static OAuth2UserDTO from(Member member){
        return OAuth2UserDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImage(member.getProfileImage())
                .role(member.getRole())
                .build();
    }

}