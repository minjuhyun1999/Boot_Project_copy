package com.busanit501.boot_project.controller;

import com.busanit501.boot_project.dto.BoardDTO;
import com.busanit501.boot_project.dto.BoardListReplyCountDTO;
import com.busanit501.boot_project.dto.PageRequestDTO;
import com.busanit501.boot_project.dto.PageResponseDTO;
import com.busanit501.boot_project.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Log4j2
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        // 서비스 외주 이용해서, 데이터 가져오기
        // 1, 기존, 페이징 정보와, 검색 정보만 이용한 리스트 목록,
//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        //2. 기존 + 댓글 갯수 포함 목록 정보.
        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);
        log.info("BoardController에서, list, responseDTO : {}", responseDTO);
        // 서버 -> 화면으로 데이터 전달.
        model.addAttribute("responseDTO", responseDTO);
    }

//    등록화면 작업, get
    @GetMapping("/register")
    public void register() {
    }
    // 등록 처리, post
    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        log.info("BoardController 에서, registerPost 작업중");
        if (bindingResult.hasErrors()) {
            log.info("registerPost, 입력 작업중, 유효성 체크에 해당 사항 있음");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/board/register";
        }
        log.info("넘어온 데이터 확인 boardDTO : " + boardDTO);
        Long bno = boardService.register(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno);
        return  "redirect:/board/list";
    }

    // 상세보기 화면, 수정하는 화면 동일.
    // 읽기 전용, 수정이 가능한 input
    @GetMapping({"/read","/update"})// 화면 경로 : /board/read.html 작업함.
    // 예시
    //http://localhost:8080/board/list?type=tcw&keyword=1&page=2
    // type, keyword, page, -> PageRequestDTO의 멤버 이름과 동일함.
    // 그래서, 자동 수집함. !!중요!!
    // 자동 화면으로 전달도 함. !!중요!!
    public void read(Long bno, PageRequestDTO pageRequestDTO,
                     Model model) {
        // 누구에게 외주 줄까요? BoardService  외주,
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info("BoardController 에서, read 작업중 boardDTO: "+ boardDTO);
        // 서버 -> 화면, 데이터 전달,
        model.addAttribute("dto", boardDTO);

    }

    // 수정 처리 post 작업,
    @PostMapping("/update")
    public String update(PageRequestDTO pageRequestDTO,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        //화면에서, 데이터가 잘 전달 되는지 확인.
        log.info("BoardController 에서, update 작업중 boardDTO:" + boardDTO);
        if (bindingResult.hasErrors()) {
            log.info("update, 입력 작업중, 유효성 체크에 해당 사항 있음");
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            // 수정중, 잘못된 오류가 발생시, 현재 수정하는 화면으로 이동하는게 더 좋다.
            // 현재 , 수정하고 있는 게시글의 번호 정보가 필요함.
            // 쿼리 스트링으로 bno 번호는 전달하고,
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/update?"+link;
        }
        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "수정완료");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return  "redirect:/board/read";
    }

    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {
        log.info("BoardController 에서, remove 작업중 , 넘어온 bno 확인: " + bno);
        boardService.remove(bno);
        redirectAttributes.addFlashAttribute("result", "삭제완료!!");
        return "redirect:/board/list";
    }

}
