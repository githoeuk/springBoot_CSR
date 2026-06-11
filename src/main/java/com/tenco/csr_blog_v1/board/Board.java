package com.tenco.csr_blog_v1.board;


import com.tenco.csr_blog_v1.reply.Reply;
import com.tenco.csr_blog_v1.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "board_tb")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id") <--- 기본값을 설정 됨
    private User user;

    // 양방향 맵핑
    // mappedBy = "board" <-- 내 쪽(board)에서 조인커럼을 생성 금지
    // 즉 조인 컬럼은 reply테이블에 board_id가 생성되어야 한다.
    //  cascade = CascadeType.REMOVE = CASCADE 설정
    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<Reply> replies = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Board(Integer id, String title, String content, User user, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }


    public void update(String title,String content){
        this.title = title;
        this.content = content;
    }

} // end of class
