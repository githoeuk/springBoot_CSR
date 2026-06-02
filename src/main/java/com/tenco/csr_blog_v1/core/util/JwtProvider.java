package com.tenco.csr_blog_v1.core.util;

/*
    방금 작업한 JwtUtil 클래스는 HTTP가 먼지 스프링 시큐리티가 무엇인지 전혀 모름
    순수하게 JWT 생성 및 확인하는 기능만 있을 뿐이다.

    우리가 JwtProvider를 설계하는 이유는
    HTTP 요청에서 Header를 열엇허 Bearer 토큰을 직접 꺼내서 역할에 클래스를 설계할 예정
 */

import com.tenco.csr_blog_v1.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component // IoC
public class JwtProvider {

    // 요청 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {

        try {
            String bearerToken = request.getHeader(JwtUtil.Header);
            if (bearerToken != null && bearerToken.startsWith(JwtUtil.TOKEN_PREFIX)) {
                return bearerToken.replace(JwtUtil.TOKEN_PREFIX, "");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    } // end of resolveToken

    // 토큰이 유효한지 단순 체크
    public boolean validateToken(String token) {
        try {
            JwtUtil.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 시큐리티 사용 중이기 때문에 필요
    // 토큰을 검증하고 Authentication 객체를 반환해서 시큐리티 임시 저장소에 등록 함
    public Authentication getAuthentication(String token) {

        try {
            User user = JwtUtil.verify(token);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } catch (Exception e) {
            return null;
        }


    } // end of getAuthentication


} // end of class
