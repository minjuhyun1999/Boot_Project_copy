package com.busanit501.boot_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type; // 검색의 종류 : t, c, w, tc, twc
    private String keyword; // 검색어
    private String link;//

    // 예) type : "twc" ->  {"t","w","c"}, 문자열 -> 문자열 요소로 가지는 배열로 변환
    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        return type.split(""); // twc -> {"t","w","c"}
    }

    // 페이징 정보, 1) 현재 페이지 2) 1페이징 당 보여줄 갯수 3) 정렬조건, 내림차순
    // ...props , 가변인자, "title". "tno" 번호
    public Pageable getPageable(String... props) {
        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    }

    // 쿼리스트링
    //http://localhost:8080/todo/list
//     ?page=1
//     &size=10
//     &type=twc
//     &keyword=lsy
    // link = "page=1&size=10&type=twc&keyword=lsy"
    public String getLink() {
        if (link == null || link.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if (type != null && !type.isEmpty()) {
                builder.append("&type=" + type);
            }

            if (keyword != null && !keyword.isEmpty()) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (Exception e) {

                }
            }
            link = builder.toString();
        }
        return link;
    }


}
