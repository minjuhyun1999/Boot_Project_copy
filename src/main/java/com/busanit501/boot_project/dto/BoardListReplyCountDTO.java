package com.busanit501.boot_project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {
    // 게시글의 목록의 표시 내용
    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;

    // 게시글 목록에, 댓글의 갯수를 표시하기.
    private  Long replyCount;
}
