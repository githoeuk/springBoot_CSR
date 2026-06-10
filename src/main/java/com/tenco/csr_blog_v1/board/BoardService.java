package com.tenco.csr_blog_v1.board;

import com.tenco.csr_blog_v1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponse.DTO 게시글쓰기(BoardRequest.SaveDTO requestDTO, User sessionUser){
        Board saverdBoard = boardRepository.save(requestDTO.toEntity(sessionUser));
        return new BoardResponse.DTO(saverdBoard);
    }

//    stream을 이용하지 않을 시
//    public List<BoardResponse.DTO> 게시글목록(){
//        List<Board> boards = boardRepository.findAll();
//        // 새로운 List 자료구조 선언
//        List<BoardResponse.DTO> dtoList = new ArrayList<>();
//        for(Board board : boards){
//            dtoList.add(new BoardResponse.DTO(board));
//        }
//      return dtoList;
//    }
    // stream을 이용한 코드
    // stream 중간연산 , 최종 연산이란 개념이 있다. 항상 최종 연ㅅ간이 호출되어야 동작한다.
    public List<BoardResponse.DTO> 게시글목록(){
        return boardRepository.findAll().stream()   // 1. 컨베이어 벨트에 엔티티들을 올린다.
                .map(BoardResponse.DTO::new)        // 가공 로봇(map)이 엔티티들을 DTO로 변경한다.
                .toList();                          // 최종 연산 단계 : 완성된 DTO들을 리스트 상자에 담는 역할을 한다
    }


} // end of class
