package com.busanit501.boot_project.repository.search;

import com.busanit501.boot_project.domain.Board;
import com.busanit501.boot_project.domain.QBoard;
import com.busanit501.boot_project.domain.QReply;
import com.busanit501.boot_project.dto.BoardListAllDTO;
import com.busanit501.boot_project.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

// 인터페이스이름 + Impl, 이름 규칙, 동일하게 작성,
// QuerydslRepositorySupport : 부모클래스, Querydsl 사용하기 위한 도구함.
@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        // 부모 클래스에서, 엔티티 클래스 전달. 사용할 클래스 지정.
        super(Board.class);
    }

    @Override
    public Page<Board> search(Pageable pageable) {
        // 자바 문법으로만, sql 명령어 전달 하는게 목적.

        // QueryDSL ,사용법,
        // 순서1
        // Q도메인 객체 : 엔티티 클래스 Board, 동적 쿼리 작업 하기 위한
        // 편하게 만든 클래스라고 생각하면됨.
        // 기능이 향상된 버전이다.
        QBoard board = QBoard.board;
        // 순서2
        JPQLQuery<Board> query = from(board); // select .. from board 형식과 동일함.
        // 순서3
        query.where(board.title.contains("1")); // where title like...
        // 순서4
        List<Board> list = query.fetch(); // db에서 데이터 가져오기.
        // 순서5
        long count = query.fetchCount(); // 가져온 디비의 갯수 확인.
        // 순서6
        // 페이징 조건 추가해보기. 검색 쿼리에 , 페이징 조건 탑재
        this.getQuerydsl().applyPagination(pageable,query);
        // 순서7
        // 페이징 조건을 적용하고, 조회하기.
        List<Board> list2 = query.fetch();
        // 순서8
        // 페이징 조건 적용 + 전체 갯수
        long count2 = query.fetchCount();

        return null;
    }

    // 페이징 정보 + 검색 정보를 이용해서,
    // 자바 메서드로 -> sql 전달하기.
    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        // 순서1, 고정
        QBoard board = QBoard.board; // (board)
        // 순서2, 고정
        JPQLQuery<Board> query = from(board); // select .. from board
        // 순서3, 옵션
        // where 조건절 , BooleanBuilder 를 이용해서 조건 추가.
        // select .. from board where ....
        if ((types != null && types.length > 0) && keyword != null) {
            // or , 조건, and 조건을 사용하기 싶다. 묶기도 쉽다.
            BooleanBuilder builder = new BooleanBuilder();
            // types = {"t","w","c"}
            for(String type : types){
                switch (type) {
                    case "t":
                        builder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        builder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        builder.or(board.writer.contains(keyword));
                        break;
                } // end switch
            } // end for
            query.where(builder); // select * from board where like %keyword%
        } //end if
        // bno >0 조건 추가히기.
        query.where(board.bno.gt(0L));

        // paging 조건 추가하기.
        // 순서4, 고정, 페이징 처리시,
        this.getQuerydsl().applyPagination(pageable,query);
        // 순서5, 고정, 데이터 가지고 올때
        // 위의 검색 조건, 페이징 조건으로 sql  전달하기.  데이터 가져오기
        List<Board> list = query.fetch();
        // 순서6, 고정, 데이터 가지고 올때
        long count = query.fetchCount();

        // 순서7, 리턴 타입에 맞추기
        return new PageImpl<>(list,pageable,count);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        // 순서1, 고정
        QBoard board = QBoard.board; // (board)
        QReply reply = QReply.reply; // (reply)
        // 순서2, 고정
        JPQLQuery<Board> query = from(board); // select .. from board

        // 순서3,
        // left join,-> 게시글의 댓글이 없는 경우도 표기해야함. 그래서, 사용함.
        query.leftJoin(reply).on(reply.board.eq(board));
        query.groupBy(board);


        // 순서4, 옵션
        // where 조건절 추가. 위의 내용 재사용.
        // where 조건절 , BooleanBuilder 를 이용해서 조건 추가.
        // select .. from board where ....
        if ((types != null && types.length > 0) && keyword != null) {
            // or , 조건, and 조건을 사용하기 싶다. 묶기도 쉽다.
            BooleanBuilder builder = new BooleanBuilder();
            // types = {"t","w","c"}
            for(String type : types){
                switch (type) {
                    case "t":
                        builder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        builder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        builder.or(board.writer.contains(keyword));
                        break;
                } // end switch
            } // end for
            query.where(builder); // select * from board where like %keyword%
        } //end if
        // bno >0 조건 추가히기.
        query.where(board.bno.gt(0L));

        // 순서5,
        //   Projections.bean 를 이용해서,QueryDSL 사용해서, 자동 형변환하기.
        // DTO <-> Entity(VO), 서비스에서 모델 맵퍼 이용해서, 변환
        // 이번에는 자동으로 변환 해보기.
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(
                Projections.bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount")
                )//bean
        );// select

        // 순서6,
        // 기존의 1)페이징 정보 2) 검색정보 + 3) 댓글의 갯수
        // 페이징 적용하기.
        this.getQuerydsl().applyPagination(pageable,dtoQuery);

        // 순서7,
        // 실제 디비를 가져오기 작업, fetch 작업.
        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList,pageable,count);
    }

    // 1) 페이징 2) 검색 3) 댓글 갯수 4) 첨부이미지들
    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
        // 순서1, 고정
        QBoard board = QBoard.board; // (board)
        QReply reply = QReply.reply; // (reply)
        // 순서2, 고정
        JPQLQuery<Board> boardJPQLQuery = from(board); // select .. from board

        // 순서3,
        // left join,-> 게시글의 댓글이 없는 경우도 표기해야함. 그래서, 사용함.
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));

        // 순서3-2
        boardJPQLQuery.groupBy(board);

        // 순서4,
        // 페이징 적용.
        getQuerydsl().applyPagination(pageable,boardJPQLQuery);

        // boardJPQLQuery -> 기존 board 테이블 , reply 댓글 갯수 붙여 놓은 테이블,
        //예)
        // bno title(댓글 갯수포함) writer  regDate ->
        // 이 내용들 boardJPQLQuery 들어가 있다.

        // 순서 5, 추가 버전. 튜플을 이용해서, 자동 형변환,
        JPQLQuery<Tuple> tupleJPQLQuery =  boardJPQLQuery.select(board,reply.countDistinct());

        // 순서6,
        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        // 엔티티 클래스 -> dto 변환 작업.
        // 변환 작업  1) modelmapper 클래스 map 이용 했고,  수동 작업,
        // 2) Projection bean 메서드 이용해서, 자동으로 형변환
        // 3) Tuple 클래스 이용해서, 자동으로 형변환,

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Board board1 = (Board) tuple.get(board);
            long replyCount = tuple.get(1,Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();
            // 추가로 ,첨부된 이미지 목록을 여기 붙이기 작업 예정.

            return dto;
        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList,pageable,totalCount);


        //순서5, 임시 확인용.
//        List<Board> boardList = boardJPQLQuery.fetch();

        // 확인.
        // 게시글에 첨부된 이미지 확인용
//        boardList.forEach(board1 -> {
//            log.info("===searchWithAll: 레포지토리 작업 중. ===============");
//            log.info(board1.getBno().toString());
//            log.info(board1.getImageSet());
//            log.info("=========================");
//        });


//        return null;
    }
}
