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
import server.rebid.service.query.MemberQueryService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final MemberQueryService memberQueryService;


    @Value("${frontend.auth_redirect_url}")
    private String redirectUrl;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        Long memberId = customUserDetails.getMemberId();
        MemberRole role = customUserDetails.getRole();
        boolean isWritten = memberQueryService.isMemberAddressWrittenV2(memberId);

        // jwt 생성, 쿠키 설정
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        cookieService.createAccessTokenCookie(response, accessToken);
        cookieService.createRefreshTokenCookie(response, refreshToken);
        cookieService.addAddressWrittenHeader(response, isWritten);


        response.sendRedirect(redirectUrl);
    }
}
