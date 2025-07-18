package com.busanit501.boot_project.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
public class SampleController {

    // 내부 클래스 만들기, 임시로, 원래는 분리해서 사용하지만,
    // 지금은 테스트 용도, 1회용으로 사용해서,
    class SampleDTO {
        private String p1,p2,p3;

        public  String getP1(){
            return p1;
        }
        public  String getP2(){
            return p2;
        }
        public  String getP3(){
            return p3;
        }
    }

    @GetMapping("/hello")
    public void hello(Model model) {
        log.info("SampleController에서,/hello:  hello");
        model.addAttribute("msg", "Hello World!");
    }

    @GetMapping("/ex/ex1")
    public void ex1(Model model) {
        // 화면에 전달할 샘플 데이터 배열
        List<String> list = Arrays.asList("aaa","bbb","ccc","ddd");
        // 서버 -> 화면, 데이터 전달.
        model.addAttribute("list", list);
    }

    @GetMapping("/ex/ex2")
    public void ex2(Model model) {

        log.info("SampleController에서, /ex/ex2 확인 중");
        // 서버에서 , 화면으로 데이터 전달하는 예시,
        // html 파일 화면에서는 넘어온 데이터 확인 하는 예시,
        // 전달 데이터 1
        List<String> strList = IntStream.range(1,10).mapToObj(i ->"Data" + i)
                .collect(Collectors.toList());
        model.addAttribute("list", strList);
        // 전달 데이터 2
        Map<String, String> map = new HashMap<>();
        map.put("a", "aaaa");
        map.put("b", "bbbb");
        model.addAttribute("map", map);
        // 전달 데이터 3
        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.p1 = "p1 ----";
        sampleDTO.p2 = "p2 ----";
        sampleDTO.p3 = "p3 ----";
        model.addAttribute("dto", sampleDTO);
    }

    @GetMapping("/ex/ex3")
    public void ex3(Model model) {
        model.addAttribute("list", Arrays.asList("aaa","bbb","ccc","ddd"));
    }
    
} //SampleController 닫는 블록
