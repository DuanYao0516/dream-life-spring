package com.example.hellospring.service;

import com.example.hellospring.entity.BlogTag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogTagServiceTest {

    @Autowired
    private BlogTagService blogTagService;

    @Test
    public void testdeleteByBlogId() {
//        assert Objects.equals(blogTagService.deleteByBlogId(21L), TRUE);
    }
}
