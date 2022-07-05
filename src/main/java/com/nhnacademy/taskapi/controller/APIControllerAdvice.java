package com.nhnacademy.taskapi.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.nhnacademy.taskapi.exception.CommentNotFoundException;
import com.nhnacademy.taskapi.exception.MilestoneNotFoundException;
import com.nhnacademy.taskapi.exception.ProjectNotFoundException;
import com.nhnacademy.taskapi.exception.TagNotfoundException;
import com.nhnacademy.taskapi.exception.TaskNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class APIControllerAdvice {

    @ExceptionHandler(value = {
        CommentNotFoundException.class,
        ProjectNotFoundException.class,
        TagNotfoundException.class,
        TaskNotFoundException.class,
        MilestoneNotFoundException.class
    })
    public ResponseEntity<Map<String, String>> handleException(RuntimeException ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put("type", "not found");
        errors.put("message", ex.getMessage());
        log.error("{}", ex.getMessage());

        return ResponseEntity.status(BAD_REQUEST)
                             .body(errors);
    }

    @ExceptionHandler(value = {
        MethodArgumentNotValidException.class
    })
    public ResponseEntity<Map<String, String>> handleValidationException(
        MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("type", "valid");
        errors.put("message", "Validation Exception");
        ex.getBindingResult()
          .getAllErrors()
          .forEach(c -> {
              errors.put(((FieldError) c).getField(), c.getDefaultMessage());
              log.error("{}", c.getDefaultMessage());
          });

        return ResponseEntity.status(BAD_REQUEST)
                             .body(errors);
    }

    @ExceptionHandler(value = {
        Exception.class
    })
    public ResponseEntity<Map<String, String>> serverError(Exception ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put("message", "SERVER ERROR");

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                             .body(errors);
    }
}
