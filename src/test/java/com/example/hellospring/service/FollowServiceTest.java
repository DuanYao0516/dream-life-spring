package com.example.hellospring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FollowServiceTest {
    @Autowired
    private FollowService followService;

    @Test
    void hasFollowed() {
        followService.addFollow(2L, 1L);
        assertTrue(followService.hasFollowed(2L, 1L));
        followService.deleteFollow(2L, 1L);
        assertFalse(followService.hasFollowed(2L, 1L));
    }
}