package com.example.hellospring.service;

public interface BlogTagService {
    boolean deleteByBlogId(Long blogId);
    boolean addBlogTag(Long blogId, Long tagId);
}
