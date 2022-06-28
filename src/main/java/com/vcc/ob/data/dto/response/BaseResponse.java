package com.vcc.ob.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private int status;

    private String message;

    private int code;

    private Object data;

    public BaseResponse() {
    }

    public BaseResponse(int status, String message, int code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public BaseResponse(String message, Object data) {
        this.status = 1;
        this.code = 200;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(String message) {
        this.status = 1;
        this.code = 200;
        this.message = message;
    }
}
