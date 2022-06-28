package com.vcc.ob.exception;

import com.vcc.ob.constant.ErrorMessageConstant;

public class EntityExistedException extends BaseException{

    public EntityExistedException(){
        super();
        setCode(902);
        setMessage(ErrorMessageConstant.USER_EXISTED);
    }
}
