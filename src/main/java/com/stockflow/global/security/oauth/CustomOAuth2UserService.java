package com.stockflow.global.security.oauth;

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

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 1. 어떤 벤더인지 확인 (kakao, naver 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 2. Factory를 통해 적절한 UserInfo 객체 생성
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        // 3. 인터페이스 메서드만 사용하여 DB 저장/업데이트
        User user = saveOrUpdate(userInfo);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
        );
    }

    private User saveOrUpdate(OAuth2UserInfo userInfo) {
        User user = userRepository.findByEmail(userInfo.getEmail())
                .map(entity -> entity.update(userInfo.getName()))
                .orElse(User.builder()
                        .kakaoId(userInfo.getProviderId())
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .point(0L)
                        .build());

        return userRepository.save(user);
    }
}
