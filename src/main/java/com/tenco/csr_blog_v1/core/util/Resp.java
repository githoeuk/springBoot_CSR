package com.tenco.csr_blog_v1.core.util;

// 목적 : HTTP통신에서 Json 형식으로 데이털르 내려 줄 때
// 우체국 규격 상자에 담아서 택배를 보내듯이
// 프론트엔드와 API규격 약속을 지켜서 내려 주어야 하기 때문에 설계 함.

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// ResponseEntity의 바디 역할
// 제네릭 <> 으로 선언
@Data
public class Resp<T> {

    private Integer status; // 상태 코드
    private String msg;     //
    private T body;         //

    public Resp(Integer status, String msg, T body) {
        this.status = status;
        this.msg = msg;
        this.body = body;
    }

    // 편의 메서드 설계 - 실무 패턴 : 팩토리 메서드 패턴 설계
    // ResponseEntity로 Resp<T>를 감싼다
    // 사용하는 측 - Resp.ok(ex: board); 호출 / Resp.ok(User), Resp.ok(...)
    public static <T> ResponseEntity<Resp<T>> ok(T body) {
        Resp<T> resp = new Resp<>(200, "성공", body);

        return new ResponseEntity<>(resp, HttpStatus.OK);
    } // ok

    // status.value() -> Http : 응답상태 코드
    public static <T> ResponseEntity<Resp<T>> fail(HttpStatus status, String msg) {
        Resp<T> resp = new Resp<>(status.value(), msg, null);

        return new ResponseEntity<>(resp, status);
    } // fail


}  // end of class
