package com.busanit501.boot_project.controller;

import com.busanit501.boot_project.dto.PageRequestDTO;
import com.busanit501.boot_project.dto.PageResponseDTO;
import com.busanit501.boot_project.dto.ReplyDTO;
import com.busanit501.boot_project.service.ReplyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    // JSON 데이터 타입의 기본 모양, { "키" : 값, "키2":값2,... }
    // MIME 타입, -> MediaType.APPLICATION_JSON_VALUE
    //{
    //  "rno": 0,
    //  "bno": 0,
    //  "replyText": "string",
    //  "replyer": "string",
    //  "regDate": "2025-07-11T06:38:49.331Z",
    //  "modDate": "2025-07-11T06:38:49.331Z"
    //}

    // 네트워크 통해서, 실제로 전달시에는 문자열 형태로 전달.

    // @RequestBody ReplyDTO replyDTO
    // 의미 : 화면에서, JSON 데이터(문자열) 전달함
    // -> 서버에서, 객체 형태로 변경해줌. 역직렬화라고 부름.

    //ResponseEntity<Map<String,Long>>
    // 의미 : 서버 -> 화면에게 , 응답하는 타입,
    // 전달 내용.
    // 1) http status code , 200 ok, 2) 데이터 내용
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Map<String,Long>> register( @Valid @RequestBody ReplyDTO replyDTO,
    public Map<String,Long> register( @Valid @RequestBody ReplyDTO replyDTO,
    BindingResult bindingResult) throws BindException
    {
        log.info("ReplyController에서 작업중, 댓글 작성작업");

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        log.info("ReplyController에서 작업중 replyDTO : " + replyDTO);
        // 화면이 없어서, 하드코딩으로 더미 데이터 만들기.
//        Map<String,Long> resultMap = Map.of("rno",100L);
        // 실제로, 데이터를 받아서, 디비에 저장하기.
        Map<String,Long> resultMap = new HashMap<>();
        Long rno = replyService.register(replyDTO);
        resultMap.put("rno",rno);

        return resultMap;
    }

    //댓글 목록 조회
    // http://localhost:8080/replies/list/104
    @Tag(name = "댓글 목록 조회", description = "댓글 목록 조회 레스트 버전 get 방식")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable Long bno, PageRequestDTO pageRequestDTO) {
        log.info("ReplyController에서 작업중, 댓글 목록 조회, 전달받은 bno 확인 :  " + bno);

        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);
        return responseDTO;

    }

    // 댓글 하나 조회
    // http://localhost:8080/replies/104
    @Tag(name = "댓글 하나 조회", description = "댓글 하나 조회 레스트 버전 get 방식")
    @GetMapping(value = "/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable Long rno) {
        log.info("ReplyController에서 작업중,하나 조회 전달받은 rno 확인 :  " + rno);

        ReplyDTO replyDTO = replyService.read(rno);
        return replyDTO;

    }

    // 댓글 삭제
    // http://localhost:8080/replies/612
    @Tag(name = "댓글 삭제", description = "댓글 삭제 레스트 버전 delete 방식")
    @DeleteMapping(value = "/{rno}")
    public Map<String,Long> remove(@PathVariable Long rno) {
        log.info("ReplyController에서 작업중, 삭제, 전달받은 rno 확인 :  " + rno);

        replyService.remove(rno);
        Map<String,Long> resultMap = new HashMap<>();
        resultMap.put("rno",rno);

        return resultMap;

    }

    // 댓글 수정
    // http://localhost:8080/replies/612
    @Tag(name = "댓글 수정", description = "댓글 수정 레스트 버전 put 방식")
    @PutMapping(value = "/{rno}")
    public Map<String,Long> modify(@PathVariable Long rno,
                                   @RequestBody ReplyDTO replyDTO) {
        log.info("ReplyController에서 작업중, 수정, 전달받은 rno 확인 :  " + rno);
        log.info("ReplyController에서 작업중, 수정, 전달받은 replyDTO 확인 :  " + replyDTO);
        replyDTO.setRno(rno);
        replyService.modify(replyDTO);
        Map<String,Long> resultMap = new HashMap<>();
        resultMap.put("rno",rno);

        return resultMap;

    }


}
