package server.rebid.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequest {

    @Getter
    @NoArgsConstructor
    public static class AddAddressDTO{
        @NotBlank
        private String address;
    }


}
