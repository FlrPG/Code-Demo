package com.lmzz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    private Long id;
    private String name;
    private Integer age;
    private String address;
    private Date birthday;
    private Double height;
    private Boolean isMainlandChina;
}
