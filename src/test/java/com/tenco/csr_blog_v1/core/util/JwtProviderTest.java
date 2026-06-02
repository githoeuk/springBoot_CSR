package com.tenco.csr_blog_v1.core.util;

import com.tenco.csr_blog_v1.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtProviderTest {

    private JwtProvider jwtProvider;
    private User user;
    private String validToken;

    // 각각의 테스트 메서드가 실행하기전에 공통적으로 실행되는 코드를 사용할 때 선언
    @BeforeEach
    public void setUp() {

        // 테스트 초기값 세팅

        jwtProvider = new JwtProvider();

        user = User.builder()
                .id(2)
                .username("cos")
                .roles(List.of("USER","ADMIN"))
                .build();

        validToken = JwtUtil.create(user);
    }

    @Test
    public void resolveToken_test(){
        // given // 테스트 준비 // HTTP --> Header : Au... : Token...
        MockHttpServletRequest request = new MockHttpServletRequest(); // 가짜 요청 HTTP메서드
        request.addHeader(JwtUtil.Header,validToken);

        // when // 테스트 내용
        String resolvedToken = jwtProvider.resolveToken(request);

        // then
        String expectedToken = validToken.replace(JwtUtil.TOKEN_PREFIX,"");
        assertThat(resolvedToken).isEqualTo(expectedToken);

    }

} // end of class
