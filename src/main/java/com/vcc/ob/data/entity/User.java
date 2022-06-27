package com.vcc.ob.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

  @Column(name = "name" )
  private String name;

  @Column(name = "address")
  private String address;

  @Column(name = "age")
  @Min(value = 1, message = "Age is number bigger 1")
  @Max(value = 100, message = "Age is number ")
  private int age;
}
