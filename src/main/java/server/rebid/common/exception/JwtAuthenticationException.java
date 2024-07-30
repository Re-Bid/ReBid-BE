package server.rebid.common.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(GlobalErrorCode errorCode) {
        super(errorCode.getReason().getMessage());
    }
}
