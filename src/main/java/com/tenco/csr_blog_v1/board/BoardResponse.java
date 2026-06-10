package com.tenco.csr_blog_v1.board;

import com.tenco.csr_blog_v1.reply.Reply;

import java.util.List;

public class BoardResponse {

    // 게시글 목록용 DTO

    public record DTO(
            Integer id,
            String title,
            String content,
            String writerName, // 작성자
            String createdAt) {
        public DTO(Board board) {
            this(board.getId(),
                    board.getTitle(),
                    board.getContent(),
                    board.getUser().getUsername(),
                    // 날짜가 있으면  String으로 변환 없으면 ""여백으로 표시
                    board.getCreatedAt() != null ? board.getCreatedAt().toString() : "");
        }
    }


    // 게시글 상세보기용 DTO
    public record DetailDTO(
            Integer boardId,
            String title,
            String content,
            String username,
            Boolean isOwner, // 게시글 작성자 여부 체크
            List<ReplyDTO> replies
    ) {
        public DetailDTO(Board board,Integer sessionUserId) {
            this(board.getId(),
                    board.getTitle(),
                    board.getContent(),
                    board.getUser().getUsername(),
                    sessionUserId != null ? sessionUserId.equals(board.getUser().getId()):null,
                    board.getReplies().stream()
                            .map(reply -> new ReplyDTO(reply,sessionUserId))
                            .toList()
                    );
        }
    }

    // 댓글 정보 DTO
    public record ReplyDTO(
            Integer id,
            String username,
            String comment,
            Boolean isOwner // 댓글 작성자 여부 체크
    ) {
        public ReplyDTO(Reply reply, Integer sessionUserId) {
            this(reply.getId(),
                    reply.getUser().getUsername(),
                    reply.getComment(),
                    // 방어적 코드
                    sessionUserId != null ? sessionUserId.equals(reply.getUser().getId()) : null
                    //sessionUserId.equals(reply.getUser().getId()) //  .(점)연산자는 null일시 들어갈 수 없음
        ); // 댓글 사용자 확인
        }
    }


} // end of class
