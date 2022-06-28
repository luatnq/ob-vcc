package com.vcc.ob.data.dto.response;

import com.vcc.ob.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDTO {
    private String userId;

    private String name;

    private String address;

    private int age;

    public UserResponseDTO(String userId, String name, String address, int age) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public UserResponseDTO(User user){
        this.userId = user.getUserId();
        this.name = user.getName();
        this.address = user.getAddress();
        this.age = user.getAge();
    }
}
