package server.rebid.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public static final int maxAge=300;

    @Value("${cookie.domain}")
    public String domain;
    public static final String accessTokenName = "accessToken";
    public static final String refreshTokenName = "refreshToken";

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .maxAge(maxAge)
                .path("/")
                .domain(domain)
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void createAccessTokenCookie(HttpServletResponse response, String token) {
        addCookie(response, accessTokenName, token, maxAge);
    }

    public void createRefreshTokenCookie(HttpServletResponse response, String token) {
        addCookie(response, refreshTokenName, token, maxAge);
    }

    public void addAddressWrittenHeader(HttpServletResponse response, boolean isWritten) {
        addCookie(response, "isAddressWritten", Boolean.toString(isWritten), maxAge);
    }

    public void clearAccessAndRefreshTokenCookie(HttpServletResponse response) {
        addCookie(response, accessTokenName, null, 0);
        addCookie(response, refreshTokenName, null, 0);
    }


}