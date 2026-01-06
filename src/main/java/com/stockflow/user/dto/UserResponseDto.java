package com.stockflow.user.dto;

import lombok.*;

public class UserResponseDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class UserInfoDetailDto {
        private String email;
        private String name;
        private Long point;

        public static UserInfoDetailDto from(com.stockflow.user.domain.User user) {
            return UserInfoDetailDto.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .point(user.getPoint())
                    .build();
        }
    }
}
