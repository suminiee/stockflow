package com.stockflow.user.service;

import com.stockflow.user.domain.User;
import com.stockflow.user.dto.UserResponseDto;
import com.stockflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto.UserInfoDetailDto getMyInfoByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return UserResponseDto.UserInfoDetailDto.from(user);
    }
}
