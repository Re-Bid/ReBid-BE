package server.rebid.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class AddAddressDTO{
        @NotBlank
        private String address;
    }

    @Getter
    public static class signup {
        private String email;
        private String nickname;
        private String password;
        private String address;
    }

    @Getter
    public static class login {
        private String email;
        private String password;
    }

}
