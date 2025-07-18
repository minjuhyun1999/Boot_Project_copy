package com.busanit501.boot_project.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {
    // 이미지 파일명 :
    // 썸네일 이미지 : s_ + uuid랜덤문자열_ +파일원본명
    private String uuid;
    private String fileName;
    private boolean img;
    public String getLink() {
        if(img) {
            return "s_" + uuid + "_" + fileName;
        } else {
            return uuid + "_" + fileName;
        }
    }

}
