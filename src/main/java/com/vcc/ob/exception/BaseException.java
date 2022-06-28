package com.vcc.ob.exception;

import lombok.Data;

@Data
public class BaseException extends RuntimeException{

    private int code;
    private String message;
}
