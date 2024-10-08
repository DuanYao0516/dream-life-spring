package com.example.hellospring.service;

import com.example.hellospring.entity.User;

import java.util.List;

public interface UserService {
    User login(String username, String password);

    boolean register(User user);

    User getUserById(Long userId);

    // profile
    boolean updateName(Long userId, String loginUserName, String nickName);
    boolean updatePassword(Long userId, String originalPassword, String newPassword);

    List<User> getTopUsersByBlogCount();

//    User findByEmail(String email);

    Long getUserIdByEmail(String email);
    boolean retrievePassword(Long userId, String newPassword);
}
