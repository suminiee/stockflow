package com.stockflow.user.repository;

import com.stockflow.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 1. 카카오 고유 ID로 기존 가입 여부 확인
    Optional<User> findByKakaoId(String kakaoId);

    // 2. 이메일로 사용자 정보 조회 (JWT 인증 및 내 정보 조회 시 사용)
    Optional<User> findByEmail(String email);

    // 3. 이메일 중복 체크 (필요 시)
    boolean existsByEmail(String email);
}
