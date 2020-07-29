package com.holelin.sundry.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Employee implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private Department department;
}
@Data
@ToString
class Department{
    private long id;
    private String firstName;
    private String lastName;
}
