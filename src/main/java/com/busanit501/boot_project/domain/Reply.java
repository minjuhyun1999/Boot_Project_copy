package com.busanit501.boot_project.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "board") // Reply 출력시, 부모 테이블은 제외 일단,
@ToString
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
public class Reply extends BaseEntity {
    
    @Id // pk
    // 마리아 디비의 자동 순선 생성 정책을 따르게요.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    // Reply :N ---> :1 Board
    // 늦게 초기화 하겠다. 사용하는 시점에 로드하겠다
    @ManyToOne(fetch = FetchType.LAZY) 
    private Board board;
    
    private String replyText; //댓글 내용
    private String replyer; // 댓글 작성자

    // Board 객체 수정하기
    public void changeBoard(Board board) {
        this.board = board;
    }
    // 댓글 내용 수정하기
    public void changeReplyText(String text) {
        this.replyText = text;
    }
    // 임시로 , 댓글 작성자 수정 연습해보기.
    public void changeReplyer(String replyer) {
        this.replyer = replyer;
    }
}
