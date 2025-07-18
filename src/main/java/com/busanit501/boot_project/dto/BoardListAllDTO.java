package com.busanit501.boot_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListAllDTO {
    // 게시글의 목록의 표시 내용
    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;

    // 게시글 목록에, 댓글의 갯수를 표시하기.
    private  Long replyCount;
    // 추가
    // 첨부된 이미지들의 목록
    private List<BoardImageDTO> boardImages;
}
