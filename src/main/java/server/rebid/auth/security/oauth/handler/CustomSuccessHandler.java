package server.rebid.auth.security.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.auth.service.CookieService;
import server.rebid.auth.service.JwtService;
import server.rebid.entity.enums.MemberRole;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final CookieService cookieService;

    @Value("${frontend.auth-redirect-url}")
    private String redirectUrl;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        Long memberId = customUserDetails.getMemberId();
        MemberRole role = customUserDetails.getRole();
        // jwt 생성
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        response.addCookie(cookieService.createAccessTokenCookie(accessToken));
        response.addCookie(cookieService.createRefreshTokenCookie(refreshToken));
        response.sendRedirect(redirectUrl);
    }
}
