package com.stockflow.user.service;

import com.stockflow.user.domain.User;
import com.stockflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 카카오 로그인 시 보낸 데이터 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "kakao"
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); // "id"

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 카카오는 kakao_account 내부에 email이 있고, 그 안의 profile 내부에 nickname이 있음
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String kakaoId = String.valueOf(attributes.get("id"));
        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");

        // DB 저장 혹은 업데이트
        saveOrUpdate(kakaoId, email, nickname);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userNameAttributeName
        );
    }

    private void saveOrUpdate(String kakaoId, String email, String nickname) {
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(nickname)) // 이미 있으면 이름 업데이트
                .orElse(User.builder() // 없으면 신규 생성
                        .kakaoId(kakaoId)
                        .email(email)
                        .name(nickname)
                        .point(0L)
                        .build());

        userRepository.save(user);
    }
}
