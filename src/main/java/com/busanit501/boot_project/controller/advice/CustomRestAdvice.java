package com.busanit501.boot_project.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {

    // 어떤 예외를 처리할 지 종류 지정.
    @ExceptionHandler(BindException.class)// 예외를 처리할 종류를 알려주기
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)//응답코드, 예외 실패. 메세지 코드 전달.
    public ResponseEntity<Map<String,String>> handleException(BindException e){
        log.error("CustomRestAdvice 클래스에서, 레스트 처리시, 유효성 체크 작업중.",e);

        // 예외 종류와, 메세지 담아 둘 임시 객체,
        Map<String,String> errorMap = new HashMap<>();
        if(e.hasErrors()){ // 댓글 작성중, 오류가 있다면, 맵에 담아서 전달 할게,
            // bindingResult, 오류 사전에서, 문제점 꺼내서, 맵에 담기.
            BindingResult bindingResult = e.getBindingResult();
            bindingResult.getFieldErrors().forEach((fieldError)->{
                errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());
            });
        }
        // 서버 -> 화면으로 , 준비물 전달 : 1) 상태 코드 400 badrequest , 너가 잘못된 형식으로 보낸거야,
        // 2) body(errorMap); 데이터 전달, 구체적인 오류의 내용들.
        return ResponseEntity.badRequest().body(errorMap);
    }

    // 잘못된 값이 들어오는 경우의 예외처리.
    // 예) 게시글이 110번이 없는데, 110번 게시글에 댓글을 달기 위한 액션한다.
    // 데이터 무결성 참조 오류
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String,String>> handleFKException(DataIntegrityViolationException e){
        log.error(e);

        Map<String,String> errorMap = new HashMap<>();

        errorMap.put("time","" +System.currentTimeMillis());
        errorMap.put("msg",e.getMessage());
        // ResponseEntity 이용해서, 1) 상태코드 400(잘못된 요청) 2) 오류 원인 메세지
        // 서버 -> 화면, 응답한다.
        return ResponseEntity.badRequest().body(errorMap);

    }

    // NoSuchElement 예외처리,
    @ExceptionHandler({NoSuchElementException.class,
    EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String,String>> handleNoSuchElementException(Exception e){
        log.error(e);
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("time","" +System.currentTimeMillis());
        errorMap.put("msg",e.getMessage());
        return ResponseEntity.badRequest().body(errorMap);
    }



}
