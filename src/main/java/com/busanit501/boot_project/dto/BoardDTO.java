package com.busanit501.boot_project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private Long bno;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String title;

    @NotEmpty
    @Size(min = 3, max = 1000)
    private String content;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String writer;

    // 리스트 화면에서 사용중.
    // 수정 작업시, valid 옵션에서, 유효성 체크에서,
    // 화면에서 넘어오는 날짜 포맷, : 문자열,
    // 서버에서 받는 dto 의 날짜 포맷 : LocalDateTime ,. 형이 안맞음.
    // 서버에서, 문자열을 LocalDateTime 변경 하는 방법 밖에 없음.
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
