package ru.hogwarts.school.model;

public class Student {
    private Long id;
    private String name;
    private int age;

    public Student(int age, String name, Long id) {
        this.age = age;
        this.name = name;
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }


}
