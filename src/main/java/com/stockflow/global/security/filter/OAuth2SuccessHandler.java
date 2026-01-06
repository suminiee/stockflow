package com.stockflow.global.security.filter;

import com.stockflow.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(email, "ROLE_USER");

        // 헤더나 쿼리 파라미터로 전달 (여기서는 테스트를 위해 리다이렉트 파라미터로 예시)
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"accessToken\":\"" + accessToken + "\"}");

        // 실제 서비스 시에는 프론트엔드 주소로 리다이렉트하며 토큰 전달
        // getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/oauth/callback?token=" + accessToken);
    }
}
