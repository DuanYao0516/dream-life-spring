package com.example.hellospring.entity;

public class BlogTag {
    private Long blogId;
    private Long tagId;

    // getters and setters
    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
