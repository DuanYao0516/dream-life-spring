package com.example.hellospring.entity;

import java.util.List;

public class User {
    private Long userId;
    private String userName;
    private String password;
    private String nickname;
    private String email;

    // 为了index页面添加
    private Integer blogCount; // 添加blogCount属性

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBlogCount() {
       return blogCount;
   }

   public void setBlogCount(Integer blogCount) {
       this.blogCount = blogCount;
   }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
//                ", blogCount=" + blogCount +
                '}';
    }
}
