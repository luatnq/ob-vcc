package com.vcc.ob.data.dto.request;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Data
public class UserRequestDTO {
    @Min(value = 1, message = "Age is number bigger 1")
    @Max(value = 100, message = "Age is number ")
    private int age;

    @NotEmpty(message = "Name not blank")
    private String name;

    @NotEmpty(message = "Address not empty")
    private String address;

}
