package com.busanit501.boot_project.repository;

import com.busanit501.boot_project.domain.Board;
import com.busanit501.boot_project.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// 설정한 인터페이스(BoardSearch)를 여기에 적용하기.
public interface BoardRepository extends JpaRepository<Board, Long> , BoardSearch {
//    @Query(value = "select now()", nativeQuery = true)
//    String getTime();
    // 게시글 하나 조회 할 때, 첨부된 첨부 이미지 각각 낱개로 조회.
    // 문제점, 매번 디비 서버 피곤함.
    // 비유) 질문하는데, 모아서, 5개 하는것과,
    // 질문 1개씩, 1개씩, 1개씩,
    // 식후, 그릇 치우기, 쟁반에 담아서 한번에 처리하자.
    // EntityGraph : 같이 로드 해야할 속성을 지정하기.
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(Long bno);

}
