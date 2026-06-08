package com.tenco.csr_blog_v1.auth;

import com.tenco.csr_blog_v1.user.User;

import java.util.List;

public class AuthResponse {

    // 회원 가입 후 내려줄 데이터
    public record DTO(Integer id, String username, String email, List<String> roles){

        // 기존 -> ( Integer id, String username, String email, List<String> roles )
        // 사용자 정의 생성자(User user)
        public DTO(User user){
            this(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
        }
        // service단에서 User 하나로 값을 받아올 수 있다.
    } // end of dto

    // 로그인 - JWT토큰에 사용자 정보가 들어가 있다.
    public record TokenDTO(String accessToken){ }

    // 이름 중복 확인
    public record AvailableDTO(boolean isAvailable){}
} // end of class
