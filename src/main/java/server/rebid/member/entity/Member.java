package server.rebid.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    @Column(nullable = false)
    private String email;
    private String profileImage;
    private MemberRole role;
    private String refreshToken;

    @Builder
    private Member(String nickname, String email, String profileImage, MemberRole role){
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.role = role;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
