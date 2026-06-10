package com.tenco.csr_blog_v1.user;

import com.tenco.csr_blog_v1.auth.AuthRequest;
import com.tenco.csr_blog_v1.auth.AuthResponse;
import com.tenco.csr_blog_v1.core.handlr.errors.BadRequestException;
import com.tenco.csr_blog_v1.core.handlr.errors.ForbiddenException;
import com.tenco.csr_blog_v1.core.handlr.errors.NotFoundException;
import com.tenco.csr_blog_v1.core.handlr.errors.UnAuthorizedException;
import com.tenco.csr_blog_v1.core.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public AuthResponse.DTO 회원가입(AuthRequest.joinDTO requestDTO) {

        if (userRepository.findByUsername(requestDTO.username()).isPresent()) {
            throw new BadRequestException("이미 존재하는 이름입니다.");
        }

        String encPassword = bCryptPasswordEncoder.encode(requestDTO.password());
        User savedUser = userRepository.save(requestDTO.toEntity(encPassword));
        return new AuthResponse.DTO(savedUser);
    }

    public String 로그인(AuthRequest.LoginDTO requestDTO) {

        User finduser = userRepository.findByUsername(requestDTO.username())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        // 비밀번호 확인
        if (bCryptPasswordEncoder.matches(requestDTO.password(), finduser.getPassword()) == false) {
            throw new UnAuthorizedException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 자동 생성
        return JwtUtil.create(finduser);
    }

    public AuthResponse.AvailableDTO 유저네임중복체크(String username) {
        // 있으면 F / 없으면 T
        boolean isAvailable = !userRepository.existsByUsername(username);

        return new AuthResponse.AvailableDTO(isAvailable);
    }


    public AuthResponse.DTO 회원조회(Integer userId, Integer sessionUserId) {
        if(!userId.equals(sessionUserId)) {
            throw new ForbiddenException("조회 권한이 없습니다");
        }

        User findUser = userRepository.findById(sessionUserId).orElseThrow(
                () -> new NotFoundException("회원 정보를 찾을 수 없습니다"));
        return new AuthResponse.DTO(findUser);
    }

    @Transactional
    public AuthResponse.DTO 회원수정(UserRequest.UpdateDTO requestDTO,  Integer sessionUserId){
        User findUser = userRepository.findById(sessionUserId)
                .orElseThrow(() -> new NotFoundException("회원정보가 존재하지 않습니다."));

        // tip : 사용자 입력 : 1234 -> 암호화 처리 필수
        String encPassword = bCryptPasswordEncoder.encode(requestDTO.password());

        // 더티 체킹
        findUser.update(requestDTO.email(),encPassword);
        return new AuthResponse.DTO(findUser);
    }

} // end of class
