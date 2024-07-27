package server.rebid.auth.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import server.rebid.auth.security.oauth.dto.CustomOAuth2User;
import server.rebid.auth.security.oauth.dto.OAuth2UserDTO;
import server.rebid.auth.service.JwtService;
import server.rebid.member.entity.Member;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.resolveToken(request);
        OAuth2UserDTO oAuth2UserDTO;

        // 로그인한 사용자
        if(accessToken != null && !jwtService.isAccessTokenExpired(accessToken)) {
            Member member = jwtService.getMemberFromAccessToken(accessToken);
            oAuth2UserDTO = OAuth2UserDTO.from(member);
            CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2UserDTO);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customOAuth2User,
                    null,
                    customOAuth2User.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 로그인하지 않은 사용자는 SecurityContext 에 없으므로 인증오류
        filterChain.doFilter(request, response);
    }
}
