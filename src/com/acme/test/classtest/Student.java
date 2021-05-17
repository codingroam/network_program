package com.acme.test.classtest;

public class Student extends Person{
    public Student(){
        this("str");
        System.out.println("student");
    }
    public Student(String str){
        //super(str);
        System.out.println("555");
    }
    public static void main(String[] args) {
        final Student student = new Student();
    }
}
