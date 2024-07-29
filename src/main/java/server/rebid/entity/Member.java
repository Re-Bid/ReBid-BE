package server.rebid.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.rebid.entity.enums.MemberRole;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Column(nullable = false)
    private String email;
    private Integer age;

    private String profileImage;
    private String gender;

    @Enumerated(EnumType.STRING)
    private MemberRole role;
    private String address; // 따로 입력 받음
    private String refreshToken;
    private Boolean isAllowed;

    @Builder
    private Member(String nickname, String email, String profileImage, MemberRole role, Integer age, String gender, Boolean isAllowed){
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.role = role;
        this.age = age;
        this.gender = gender;
        this.isAllowed = isAllowed;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void modifyAddress(String address){
        this.address = address;
    }
}
