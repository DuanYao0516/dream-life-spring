package com.example.hellospring.service.impl;

import com.example.hellospring.mapper.BlogTagMapper;
import com.example.hellospring.service.BlogTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogTagServiceImpl implements BlogTagService {
    @Autowired
    private BlogTagMapper blogTagMapper;

    @Override
    public boolean deleteByBlogId(Long blogId) {
        return blogTagMapper.deleteByBlogId(blogId) > 0;
    }

    @Override
    public boolean addBlogTag(Long blogId, Long tagId) {
        return blogTagMapper.insertBlogTag(blogId, tagId) > 0;
    }
}
