package com.stockflow.user.controller;

import com.stockflow.user.domain.User;
import com.stockflow.user.dto.UserResponseDto;
import com.stockflow.user.repository.UserRepository;
import com.stockflow.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 카카오 닉네임과 정보를 반환합니다.")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto.UserInfoDetailDto> getMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal String email
    ) {
        UserResponseDto.UserInfoDetailDto result = userService.getMyInfoByEmail(email);

        return ResponseEntity.ok(result);
    }
}
