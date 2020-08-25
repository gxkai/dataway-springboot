package com.dataway.cn.test;

/**
 * @author phil
 * @date 2020/08/24 10:05
 */
public class StudentA {
   StudentB studentB;
   String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StudentB getStudentB() {
        return studentB;
    }

    public void setStudentB(StudentB studentB) {
        this.studentB = studentB;
    }
}
