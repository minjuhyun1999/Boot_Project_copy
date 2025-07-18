package com.busanit501.boot_project.service;

import com.busanit501.boot_project.dto.PageRequestDTO;
import com.busanit501.boot_project.dto.PageResponseDTO;
import com.busanit501.boot_project.dto.ReplyDTO;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {
    @Autowired
    private ReplyService replyService;

    @Test
    public void testRegister() {
        // 실제 디비에, 게시글이 존재해야함.
        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("샘플 서비스 테스트, 댓글 작성4")
                .replyer("이상용")
                .bno(108L)
                .build();

        log.info("작성한 댓글 번호 : "+replyService.register(replyDTO));
    }

    @Test
    @Transactional
    public void testFindByRno() {
        // 실제 댓글 rno 번호로 확인.
        ReplyDTO replyDTO = replyService.read(6L);
        log.info("댓글 서비스 테스트 하나 조회 replyDTO : "+replyDTO);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testModify() {
        // 실제 디비를 조회 해서, 수정해보기. 6 댓글로 작성하기.

        // 6번에 대한 댓글의 DTO 조회
        ReplyDTO replyDTO = replyService.read(8L);
        // 임시 데이터로 수정하고,
        replyDTO.setReplyText("수정 테스트777777777 ");

        // 다시 저장하기.
        replyService.modify(replyDTO);

    }

    @Test
    public void testRemove() {
        replyService.remove(8L);
    }

    @Test
    public void testGetPaging() {
        // 페이징 처리를 위한 더미 데이터 준비물 준비 작업,
        Long bno = 108L;
        // 페이징 정보 담기.
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
//                .keyword("")
                .build();
        PageResponseDTO<ReplyDTO> pageResponseDTO = replyService.getListOfBoard(bno,pageRequestDTO);
        log.info("ReplyServiceTests 에서, 페이징 처리 확인 : pageResponseDTO " + pageResponseDTO);
//        pageResponseDTO.getDtoList().forEach(dto -> log.info("dto : "+dto));
    }

}
