package com.vcc.ob.exception;

import com.vcc.ob.constant.ErrorMessageConstant;

public class NotFoundException extends BaseException{

    public NotFoundException(){
        super();
        setCode(404);
        setMessage(ErrorMessageConstant.USER_NOT_FOUND);
    }
}
