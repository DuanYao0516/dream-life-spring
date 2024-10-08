package com.example.hellospring.service;

import com.example.hellospring.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void registerAndLogin() {
        User user = userService.getUserById(100L);
        assert user == null;
        user = new User();
        user.setUserName("yanzhengma");
        user.setPassword("123456");
        user.setNickname("newUser");
        assert !userService.register(user);
        user.setUserName("newUser");
        user.setEmail("1336211227@qq.com");
        assert !userService.register(user);
        user.setEmail("111@qq.com");
        assert userService.register(user);
        user = userService.login("newUser", "12345");
        assert user == null;
        user = userService.login("newUser", "123456");
        assert Objects.equals(user.getUserName(), "newUser");
    }

    @Test
    void getUserById() {
        User user = userService.getUserById(4L);
        assert Objects.equals(user.getUserName(), "yanzhengma");
    }

    @Test
    void updateName() {
        assert userService.updateName(4L, "yanzhengma", "linkehua");
    }

    @Test
    void getTopUsersByBlogCount() {
        assert userService.getTopUsersByBlogCount().get(0).getUserName().equals("yanzhengma");
    }
}