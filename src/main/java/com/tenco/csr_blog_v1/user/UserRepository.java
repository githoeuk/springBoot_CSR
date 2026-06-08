package com.tenco.csr_blog_v1.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // 유저 이름으로 회원 정보 조회
    // 쿼리 메서드 동작
    @Query("select u from User u where u.username =:username")
    Optional<User> findByUsername(@Param("username") String username);


    // 유저이름 중복 여부 확인 ;
    @Query("select count(u) > 0 from User u where u.username =:username")
    boolean existsByUsername(@Param("username")String username);
}
