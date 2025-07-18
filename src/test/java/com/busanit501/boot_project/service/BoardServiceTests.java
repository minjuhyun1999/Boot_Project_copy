package com.busanit501.boot_project.service;

import com.busanit501.boot_project.dto.BoardDTO;
import com.busanit501.boot_project.dto.BoardListReplyCountDTO;
import com.busanit501.boot_project.dto.PageRequestDTO;
import com.busanit501.boot_project.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {
        // 입력할 더미 데이터 , 준비물 준비하고,
        BoardDTO boardDTO = BoardDTO.builder()
                .title("서비스 작업 단위 테스트 중")
                .content("서비스 작업 단위 테스트 내용 작성중 ")
                .writer("이상용")
                .build();
        // 실제 서비스 이용해서, 외주 주기.
        boardService.register(boardDTO);
    }

    @Test
    public void testReadOne() {
        // 실제 디비 번호 bno, 각자 디비에 있는 내용으로 조회하기.
        BoardDTO boardDTO = boardService.readOne(102L);
        log.info("서비스 단위테스트에서 하나 조회 boardDTO : " + boardDTO);
    }

    @Test
    public void testModify() {
        // 수정할 실제 데이터 이용, 102L
        BoardDTO boardDTO = boardService.readOne(102L);
        boardDTO.setTitle("수정2 테스트 ");
        boardDTO.setContent("오늘 점심 뭐 먹지 ??");

        boardService.modify(boardDTO);
    }

    @Test
    public void testDelete() {
        boardService.remove(102L);
    }

    @Test
    public void testList() {
        // 화면으로부터 전달 받은 , 페이징 정보, 검색 정보, 더미 데이터 만들기.
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info("서비스 테스트 작업 중, responseDTO : " + responseDTO);
    }

    @Test
    public void testSearchWithReplyCount() {
        // 화면으로부터 전달 받은 , 페이징 정보, 검색 정보, 더미 데이터 만들기.
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("ㅇ")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);
        log.info("서비스 테스트 작업 중2, responseDTO : " + responseDTO);

    }

}
