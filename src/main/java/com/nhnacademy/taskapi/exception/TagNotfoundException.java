package com.nhnacademy.taskapi.exception;

public class TagNotfoundException extends IllegalArgumentException {

    public TagNotfoundException() {
        super("태그를 찾을 수 없습니다.");
    }
}
