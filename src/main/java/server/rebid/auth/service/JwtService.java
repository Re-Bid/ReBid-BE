package server.rebid.auth.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.entity.Member;
import server.rebid.repository.MemberRepository;
import server.rebid.service.command.MemberCommandService;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final MemberRepository memberRepository;
    private final MemberCommandService memberCommandService;

    @Value("${jwt.secret.key}")
    private String secret;

    @Value("${jwt.access.exp}")
    private long accessTokenExpiresIn;

    @Value("${jwt.refresh.exp}")
    private long refreshTokenExpiresIn;

    private SecretKey accessTokenSecret;
    private SecretKey refreshTokenSecret;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @PostConstruct
    public void secretInit(){
        accessTokenSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        refreshTokenSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        // 에러 처리 추가 요망
        return null;
    }

    public String createAccessToken(Long memberId) {
        return createToken(memberId, accessTokenExpiresIn, accessTokenSecret);
    }

    public String createRefreshToken(Long memberId) {
        String refreshToken = createToken(memberId, refreshTokenExpiresIn, refreshTokenSecret);
        memberCommandService.setRefreshToken(memberId, refreshToken);
        return refreshToken;
    }

    public boolean isAccessTokenExpired(String accessToken) throws GeneralException {
        return isTokenExpired(accessToken, accessTokenSecret);
    }

    public boolean isRefreshTokenExpired(String refreshToken) throws GeneralException {
        return isTokenExpired(refreshToken, refreshTokenSecret);
    }

    // 토큰에서 memberId 를 추출해 사용자를 가져온다.
    public Member getMemberFromAccessToken(String token) {
        Long memberId = getMemberIdFromAccessToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(GlobalErrorCode.AUTHENTICATION_DENIED));
    }

    // 토큰의 만료여부 검사 후 만료되지 않았다면 호출할 것
    public Long getMemberIdFromAccessToken(String token) {
        return Jwts.parser().verifyWith(accessTokenSecret).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId",Long.class);
    }

    public Long getMemberIdFromRefreshToken(String token) {
        return Jwts.parser().verifyWith(refreshTokenSecret).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId",Long.class);
    }


    private boolean isTokenExpired(String token, SecretKey secretKey) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            if (e instanceof MalformedJwtException) {
                log.warn("올바르지 않은 형식의 JWT 토큰: {}", e.getMessage());
            } else if (e instanceof SignatureException) {
                log.warn("JWT 서명이 일치하지 않음: {}", e.getMessage());
            } else if (e instanceof UnsupportedJwtException) {
                log.warn("토큰의 특정 헤더나 클레임이 지원되지 않음: {}", e.getMessage());
            } else {
                log.warn("JWT 만료기간 검사 처리 중 오류 발생: {}", e.getMessage());
            }
            throw new GeneralException(GlobalErrorCode.AUTHENTICATION_DENIED);
        }
    }

    private String createToken(Long memberId, long expiresIn, SecretKey secretKey) {
        return Jwts.builder()
                .claim("memberId", memberId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiresIn * 1000))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}