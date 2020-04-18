package com.example.examination.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;

    private String username;

    private String nickname;

    private String password;

    private String role;

    private Date createTs;

    private Date updateTs;

    private String phoneNumber;

    private String email;
}