package com.lecture.assist.bean;

/**
 * Created by lgx on 2019/4/22.
 */

public class Student {
    public String name;
    public String number;
    public int age;
    public String college_name;

    public String getCollege_name() {
        return college_name;
    }

    public void setCollege_name(String college_name) {
        this.college_name = college_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String toString(){
        return name+" "+number+" "+college_name;
    }
}
