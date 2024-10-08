package com.example.hellospring.service.impl;

import com.example.hellospring.entity.User;
import com.example.hellospring.mapper.UserMapper;
import com.example.hellospring.service.UserService;
import com.example.hellospring.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    public User login(String username, String password) {
        String passwordMd5 = MD5Util.MD5Encode(password, "utf-8");
        return userMapper.login(username, passwordMd5);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean register(User user) {
        if (userMapper.checkUsernameOrEmail(user.getUserName(), user.getEmail()) > 0) {
            return false;
        }
        user.setPassword(MD5Util.MD5Encode(user.getPassword(), "utf-8"));
        return userMapper.insert(user) >= 1;
    }

    @Override
    public User getUserById(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public boolean updateName(Long userId, String loginUserName, String nickName) {
        return userMapper.updateName(userId, loginUserName, nickName) > 0;
    }

    @Override
    public boolean updatePassword(Long userId, String originalPassword, String newPassword) {
        User user = userMapper.getUserById(userId);
        originalPassword = MD5Util.MD5Encode(originalPassword, "utf-8");
        if (user != null && user.getPassword().equals(originalPassword)) {
            return userMapper.updatePassword(userId, MD5Util.MD5Encode(newPassword, "utf-8")) > 0;
        }
        return false;
    }

    @Override
    public List<User> getTopUsersByBlogCount() {
        return userMapper.getTopUsersByBlogCount();
    }

//    @Override
//    public User findByEmail(String email) {
//        return userMapper.findByEmail(email);
//    }

    @Override
    public Long getUserIdByEmail(String email){
        return userMapper.getUserIdByEmail(email);
    }

    @Override
    public boolean retrievePassword(Long userId, String newPassword) {
        return userMapper.updatePassword(userId, MD5Util.MD5Encode(newPassword, "utf-8")) > 0;
    }
}
