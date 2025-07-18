package com.busanit501.boot_project.service;

import com.busanit501.boot_project.dto.BoardDTO;
import com.busanit501.boot_project.dto.BoardListReplyCountDTO;
import com.busanit501.boot_project.dto.PageRequestDTO;
import com.busanit501.boot_project.dto.PageResponseDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    //    기존 , 1) 페이징 2) 검색
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    // 기존 , 1) 페이징 2) 검색 3) 댓글 갯수 , 버전으로 목록 출력.
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

}
