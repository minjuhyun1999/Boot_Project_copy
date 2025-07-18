package com.busanit501.boot_project.repository;

import com.busanit501.boot_project.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    @Query("select r from Reply r where r.board.bno = :bno")
    Page<Reply> listOfBoard(Long bno, Pageable pageable);

    // 게시글 번호로, 댓글 삭제하기.
    void deleteByBoard_Bno(Long bno);
}
