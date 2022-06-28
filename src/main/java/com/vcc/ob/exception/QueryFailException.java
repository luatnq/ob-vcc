package com.vcc.ob.exception;

import com.vcc.ob.constant.ErrorMessageConstant;

public class QueryFailException extends BaseException{
    public QueryFailException(){
        super();
        setCode(901);
        setMessage(ErrorMessageConstant.QUERY_DATABASE_FAIL);
    }
}
