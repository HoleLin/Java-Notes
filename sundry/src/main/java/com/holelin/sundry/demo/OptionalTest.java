package com.holelin.sundry.demo;


import lombok.Data;

import java.util.Optional;

@Data
class PersonDTO {
    private String dtoName;
    private String dtoAge;
}

@Data
class Person {
    private String name;
    private String age;

    public PersonDTO shouldConvertDTO() {
        PersonDTO personDTO = new PersonDTO();
        Optional.ofNullable(getPerson()).ifPresent(person -> {
            personDTO.setDtoAge(person.getAge());
            personDTO.setDtoName(person.getName());
        });
        return personDTO;
    }

    private Person getPerson() {
        return null;
    }
}

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/7/29 13:28
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/29 13:28
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public class OptionalTest {

    public static void main(String[] args) {
        Person person = new Person();
        person.setAge("1");
        person.setName("12123");
        PersonDTO personDTO = person.shouldConvertDTO();
        Person nullPerson = null;
        PersonDTO nullPersonDTO = nullPerson.shouldConvertDTO();
        System.out.println(personDTO);
        System.out.println(nullPersonDTO);
    }
}
