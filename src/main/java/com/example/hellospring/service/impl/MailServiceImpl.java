package com.example.hellospring.service.impl;

import com.example.hellospring.mapper.UserMapper;
import com.example.hellospring.service.MailService;
import com.example.hellospring.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MailServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserMapper userMapper;

    public void sendSimpleMail(String to,String title,String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

    public boolean isEmailValid(String email) {
        // 使用正则表达式验证邮箱格式
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches() && userMapper.checkEmail(email) == 0;
    }
}
