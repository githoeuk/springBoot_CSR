package com.tenco.csr_blog_v1.board;

import com.tenco.csr_blog_v1.core.handlr.errors.UnAuthorizedException;
import com.tenco.csr_blog_v1.core.util.Resp;
import com.tenco.csr_blog_v1.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/boards") // 자원을 하나만 요청하는게 아니기 때문에 복수형으로 작성한다.
@RestController // IoC -> RestController -> @Controller + ResponseBody
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    // PostMapping - /api/boards - GetMapping으로 동일한 주소 사용가능 (구분 가능)
    // RequestBody - json형식을 받기위해 필수로 사용
    @PostMapping
    public ResponseEntity<?> save(
            @AuthenticationPrincipal User sessionUser,
            @Valid @RequestBody BoardRequest.SaveDTO requestDTO,
            Errors errors) {
        BoardResponse.DTO responseDTO = boardService.게시글쓰기(requestDTO,sessionUser);
        return Resp.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        List<BoardResponse.DTO> responseDTO = boardService.게시글목록();
        return Resp.ok(responseDTO);
    }

    // /api/boards/{boardId}
    @GetMapping("/{boardId}")
    public ResponseEntity<?> detail(@AuthenticationPrincipal User sessionUser,
                                    @PathVariable(name = "boardId") Integer boardId){
        Integer sessionUserId = sessionUser != null ? sessionUser.getId() : null;
        BoardResponse.DetailDTO responseDTO = boardService.게시글상세보기(boardId, sessionUserId);
        return Resp.ok(responseDTO);

    }


    @GetMapping("/{boardId}/edit")
    public ResponseEntity<?> edit(@AuthenticationPrincipal User sessionUser,
                                  @PathVariable(name = "boardId") Integer boardId){
        if (sessionUser == null){
            throw new UnAuthorizedException("로그인이 필요합니다.");
        }
        BoardResponse.DTO reponseDTO = boardService.게시글정보(boardId,sessionUser.getId());

        return Resp.ok(reponseDTO);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User sessionUser,
                                    @PathVariable(name = "boardId") Integer boardId,
                                    @Valid BoardRequest.UpdateDTO requestDTO,
                                    Errors errors){
        //Errors(GlobalValidationHandler)를 통해 Put


        //  인증 검사 - 인터셉터에 잡힘
        BoardResponse.DTO responseDTO = boardService.게시글수정(requestDTO,boardId,sessionUser.getId());
        return Resp.ok(responseDTO);


    }

}

