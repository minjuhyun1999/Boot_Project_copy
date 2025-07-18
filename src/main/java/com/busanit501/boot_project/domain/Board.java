package com.busanit501.boot_project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

// 엔티티 클래스 이용해서 -> 마리아 디비에 테이블 생성.
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Board extends BaseEntity {

    @Id // DB pk와 같은 역할
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;
    private String content;
    private String writer;

    //연관관계 설정1,
    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL}
    ,fetch = FetchType.LAZY,
    orphanRemoval = true) // BoardImage 의 board 변수를 가리킴
    @Builder.Default
    private Set<BoardImage>  imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName) {
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages() {
        // 자식 테이블 , 첨부 이미지의 부모 테이블 보드, 보드를 null 하게되면,
        // 고아 객체가 되면, 알아서, 자원 자동 수거,
        // 부모가 없으면, 자식 첨부 이미지를 자동 삭제가 된다.
        imageSet.forEach(boardImage -> boardImage.chageBoard(null));
        this.imageSet.clear();
    }

    // 불변성으로, 데이터를 직접 수정하지않고,
    // 변경하려는 필드를 , 따로 메서드로 분리해서 작업함.
    public void changTitleContent(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
