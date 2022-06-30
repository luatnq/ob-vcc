package com.vcc.ob.data.entity;

import com.vcc.ob.data.dto.request.UserRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  @NonNull
  private String userId;

  @Column(name = "name" )
  @NotBlank(message = "Name not blank")
  private String name;

  @Column(name = "address")
  @NotBlank(message = "Address not blank")
  private String address;

  @Column(name = "age")
  @Min(value = 1, message = "Age is number bigger 1")
  @Max(value = 100, message = "Age is number ")
  private int age;

  @Column(name = "is_deleted")
  private boolean isDeleted;

  @Column(name = "money")
  private long money;

  public User(UserRequestDTO userRequestDTO, String userId){
    this.age = userRequestDTO.getAge();
    this.name = userRequestDTO.getName();
    this.address = userRequestDTO.getAddress();
    this.userId = userId;
    this.isDeleted = false;
  }

  public User(Long id, @NonNull String userId, String name, String address, int age, boolean isDeleted) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.address = address;
    this.age = age;
    this.isDeleted = isDeleted;
  }

  public User(User userExist, UserRequestDTO userRequestDTO){
    this.userId = userExist.getUserId();
    this.isDeleted = userExist.isDeleted;
    this.address = userRequestDTO.getAddress();
    this.age = userRequestDTO.getAge();
    this.name = userRequestDTO.getName();
  }
//
//  @NonNull
//  public String getUserId() {
//    return userId;
//  }
}
