package com.example.hellospring.service;

import jakarta.mail.MessagingException;

public interface MailService {
    void sendSimpleMail(String to,String title,String content);

    boolean isEmailValid(String email);
}
