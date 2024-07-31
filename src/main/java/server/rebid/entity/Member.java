package server.rebid.entity;

import jakarta.persistence.*;
import lombok.*;
import server.rebid.entity.enums.MemberRole;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String userId;

    private String nickname;

    @Column(nullable = false)
    private String email;

    private String password;

    private Integer age;

    private String profileImage;
    private String gender;

    @Enumerated(EnumType.STRING)
    private MemberRole role;
    private String address; // 따로 입력 받음
    private String refreshToken;
    private Boolean isAllowed;

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void modifyAddress(String address){
        this.address = address;
    }
}
