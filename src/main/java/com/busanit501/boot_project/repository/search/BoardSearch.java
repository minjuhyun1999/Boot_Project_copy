package com.busanit501.boot_project.repository.search;

import com.busanit501.boot_project.domain.Board;
import com.busanit501.boot_project.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    // Page, 페이징하기 위한 정보를 담아두는 클래스
    // Pageable, 페이징 조건 : 페이지 번호, 크기, 정렬 조건 담는 클래스
    Page<Board> search(Pageable pageable);

    // 2, 페이징 정보 + 검색 정보 포함하기.
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    // 3, 2번 정보 포함 + 댓글 갯수 추가.
    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);
}
