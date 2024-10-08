package com.example.hellospring.service;

import com.example.hellospring.entity.Tag;
import com.example.hellospring.entity.Blog;
import com.example.hellospring.mapper.TagMapper;
import com.example.hellospring.util.PageQueryUtil;
import com.example.hellospring.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();
    void addTag(Tag tag);
    void deleteTag(Long tagId);
    List<Tag> getAllTagsWithBlogCount();
    PageResult getTagsPage(PageQueryUtil pageUtil, Long loginUserId);
    int getTagCountByUserId(Long userId);

    List<Tag> getTagsByUserId(Long userId);

    // 新增方法获取热门标签
    List<Tag> getHotTags();

    List<Blog> getBlogsByTagName(String tagName);

    List<Blog> getBlogsByTagNameAndUserId(String tagName, Long userId);

     /**
     * 根据blogId和标签名称集合更新或创建标签，并维护blog_tag关系，主要用于 updateBlog 与 saveBlog 方法
     * @param blogId 博客ID
     * @param tagNames 标签名的集合，以逗号分隔的字符串
     */
//    void updateOrCreateTagsForBlog(Long blogId, String tagNames);

    void updateTagsForBlog(Long blogId, String oldTagNames, String tagNames);

    void createTagsForBlog(Long blogId, String tagNames);

}
