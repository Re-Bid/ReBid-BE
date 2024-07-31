package server.rebid.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {
    /**
     * SecurityContext에 유저 정보가 저장되는 지점
     * Request가 들어올 때 JwtFilter의 doFilter 에서 저장
     * @return
     */
    public static Long getCurrentMemberId(){
        // SecurityContext의 Authentication 겍체를 이용해 회원 정보를 가져온다.
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //인증 정보가 없는 경우
        if (authentication == null || authentication.getName() == null){
            throw new GeneralException(GlobalErrorCode.AUTHENTICATION_DENIED);
        }
        return Long.parseLong(authentication.getName());
    }
}
