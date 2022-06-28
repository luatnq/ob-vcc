package com.vcc.ob.exception;

import com.vcc.ob.constant.ErrorMessageConstant;

public class DataWrongBindingException extends BaseException{
    public DataWrongBindingException(){
        super();
        setCode(900);
        setMessage(ErrorMessageConstant.WRONG_DATA_BINDING);
    }
}
