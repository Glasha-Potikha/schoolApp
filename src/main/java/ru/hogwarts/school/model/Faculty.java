package ru.hogwarts.school.model;

public class Faculty {
    private Long id;
    private String name;
    private String color;

    public Faculty(Long id, String color, String name) {
        this.id = id;
        this.color = color;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
