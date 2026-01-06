package com.stockflow.user.domain;

import com.stockflow.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "kakao_id")
    private String kakaoId;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "birthDate")
    private String brithDate;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNum;

    @Column(name = "point")
    private Long point;

    /**
     * 사용자의 이름을 업데이트하는 메서드
     * 카카오 프로필 정보가 변경되었을 때 호출됩니다.
     */
    public User update(String name) {
        this.name = name;
        return this;
    }
}