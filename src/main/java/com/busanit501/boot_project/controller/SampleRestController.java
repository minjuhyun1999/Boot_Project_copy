package com.busanit501.boot_project.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 서버가 데이터만 전달함. http, body에 데이터를 담아서 전달함.
@RestController
@Log4j2
// 컨트롤러 작업이 좀더 하고나서, 뒤에서가서, 추가해서 같이 설명.
public class SampleRestController {

    @GetMapping("/hiStr")
    public String[] hiStr() {
        log.info("SampleRestController에서 작업중. ");
        return new String[]{"aaa","bbb","ccc","ddd"};
    }
}
