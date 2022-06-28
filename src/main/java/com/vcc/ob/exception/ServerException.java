package com.vcc.ob.exception;

public class ServerException extends BaseException{
    public ServerException(){
        super();
        setCode(500);
    }
}
