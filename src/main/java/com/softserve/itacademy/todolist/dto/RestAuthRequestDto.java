package com.softserve.itacademy.todolist.dto;

public class RestAuthRequestDto {
    private String username;
    private String password;



    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
