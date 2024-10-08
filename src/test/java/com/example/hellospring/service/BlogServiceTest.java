package com.example.hellospring.service;

import com.example.hellospring.entity.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogServiceTest {
    @Autowired
    private BlogService blogService;

    @Test
    void saveBlog() {
        Blog blog = new Blog(4L, "test", "test",
                "test", (byte) 1, 1L, (byte) 1, null, null, "test");
        assert Objects.equals(blogService.saveBlog(blog), "success");
        blog = new Blog(4L, 4L, "test", "test",
                "test", (int) 1, 1L, (int) 1, null, null, "test");
        assert Objects.equals(blogService.saveBlog(blog), "fail");
    }

    @Test
    void updateBlog() {
        Blog blog = blogService.getBlogById(23L);
        blog.setTitle("newTitle");
        blog.setContent("newContent");
        blog.setEnableComment((byte) 0);
        assert Objects.equals(blogService.updateBlog(blog), "success");
    }


    @Test
    void getTotalBlogsByUserId() {
        assert blogService.getTotalBlogsByUserId(4L) == 6;
    }

    @Test
    void deleteBatch() {
        assert blogService.deleteBatch(new Integer[]{23, 24});
        assert !blogService.deleteBatch(new Integer[]{23, 24});
    }

    @Test
    void getTopBlogsByViews() {
        assert blogService.getTopBlogsByViews().get(0).getViews() == 500;
    }
}