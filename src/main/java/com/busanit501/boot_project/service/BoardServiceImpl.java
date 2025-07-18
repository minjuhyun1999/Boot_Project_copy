package com.busanit501.boot_project.service;


import com.busanit501.boot_project.domain.Board;
import com.busanit501.boot_project.dto.*;
import com.busanit501.boot_project.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional()
public class BoardServiceImpl implements BoardService{
    // 화면에서 전달 받은 데이터 DTO를 , 엔티티 클래스 타입으로 변환해서,
    // repository 에게 외주 주는 업무.
    private final ModelMapper modelMapper;// 변환 담당자
    private final BoardRepository boardRepository; // 실제 디비에 쓰는 작업하는 담당자

    @Override
    public Long register(BoardDTO boardDTO) {
        // 변환 먼저하기.
        log.info("보드 서비스 구현체, 등록 과정 중에 변환된 boardDTO 확인 : " + boardDTO);
        Board board = modelMapper.map(boardDTO, Board.class);
        log.info("보드 서비스 구현체, 등록 과정 중에 변환된 board 확인 : " + board);
        // 실제 디비에 쓰기 작업.
        Long bno = boardRepository.save(board).getBno();
        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        // 본인 또 기능 만들어서 구현 하는게 아니라,
        // 다른 누군가 만들어 둔 기능을 이용하기.
        // 외주 주기.->boardRepository
        // 패턴 고정, findById -> 받을 때, Optional 받기
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        // 엔티티 클래스 타입(VO) -> DTO 타입 변환.
        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        // boardDTO : 화면에서 전달받은 수정할 데이터 정보 들어있음.
        // 정보들 중에서, bno 번호를 이용해서, 기존 디비 불러오고,
        // 수정할 데이터로 교체하고,
        // 다시 디비에 저장하기.
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        Board board = result.orElseThrow();
        board.changTitleContent(boardDTO.getTitle(), boardDTO.getContent());
        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        // type = "twc" -> getTypes -> {"t","c","w"}
        // 화면으로 부터 전달 받은,
        // 1)검색 조건과 2)페이징 정보
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        // 준비된 재료로, 서버에서, 데이터 가져오고, 페이징 정보도 가져오기.
        Page<Board> result = boardRepository.searchAll(types,keyword,pageable);
        // Page<Board> result , 들어 있는 정보들 한번더 확인.
        // 1)전체 갯수 2)전체 페이지 3) 현재 페이지 번호
        // 3) 보여줄 사이즈 크기 4) 이전 페이지 유무
        // 5) 다음 페이지 유무
        // 6) 페이징 처리된 실제 Board 내용도 있다.
        // 결과 값, 콘솔에서 확인.
//        log.info("전체 갯수 : total count : " + result.getTotalElements());
//        log.info("전체 페이지 : total pages : " + result.getTotalPages());
//        log.info("현재 페이지 번호 : page number  : " + result.getNumber());
//        log.info("보여줄 사이즈 크기 : page size  : " + result.getSize());
//        log.info("이전 페이지 유무 : " + result.hasPrevious());
//        log.info("다음 페이지 유무 : " + result.hasNext());

        // 6) 페이징 처리된 실제 Board 내용. Board -> BoardDTO 로 변환된 리스트로 변경.
        // result.getContent() -> List<Board>
        //.stream().map() : 리스트의 요소들을 각각 하나씩 순회 하면서, 타입 변환시키고, 전부다 순회
        // board -> modelMapper.map(board, BoardDTO.class)
        //collect(Collectors.toList()); 변환된 DTO를 다시 리스트로 변환 하는 작업.
        // 병렬 처리, 빌더 패턴으로 한번 연쇄 적용하기.
        // 결과는,  BoardDTO 로 변환된 리스트로
        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class)).collect(Collectors.toList());
        // 이 메서드의 최종 반환 타입: PageResponseDTO<BoardDTO>
        // 1) 생성자 호출 2) 생성자를 특정 이름으로 정의 해둔 내용이 있다.

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
        // type = "twc" -> getTypes -> {"t","c","w"}
        // 화면으로 부터 전달 받은,
        // 1)검색 조건과 2)페이징 정보
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        //2) result : 보드 레포지토리 테스트에서, 관련 정보 확인 해주세요.
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types,keyword,pageable);


        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    // 기존 , 1) 페이징 2) 검색 3) 댓글 갯수 , 버전으로 목록 출력. 4) 첨부 이미지들
    // 참고로 위에는 1) ~ 3) 구현이 된 상태임. 4) 번만 추가
    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        return null;
    }
}
