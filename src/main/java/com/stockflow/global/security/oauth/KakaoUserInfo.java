package com.stockflow.global.security.oauth;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() { return String.valueOf(attributes.get("id")); }

    @Override
    public String getProvider() { return "kakao"; }

    @Override
    @SuppressWarnings("unchekced") //경고 무시 명시
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        return account != null ? (String) account.get("email") : null;
    }

    @Override
    @SuppressWarnings("unchekced") //경고 무시 명시
    public String getName() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        if (account == null) return null;
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        return profile != null ? (String) profile.get("nickname") : null;
    }

}
