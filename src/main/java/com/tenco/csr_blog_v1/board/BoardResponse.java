package com.tenco.csr_blog_v1.board;

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

} // end of class
