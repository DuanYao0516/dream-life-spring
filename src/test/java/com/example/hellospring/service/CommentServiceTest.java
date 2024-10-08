package com.example.hellospring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Test
    void getTotalCommentsByUserId() {
        assertEquals(0, commentService.getTotalCommentsByUserId(3L));
    }
}