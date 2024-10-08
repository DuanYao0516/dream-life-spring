package com.example.hellospring.service.impl;

import com.example.hellospring.entity.Tag;
import com.example.hellospring.entity.Blog;
import com.example.hellospring.exception.TagDeletionException;
import com.example.hellospring.mapper.TagMapper;
import com.example.hellospring.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hellospring.util.PageQueryUtil;
import com.example.hellospring.util.PageResult;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> getAllTags() {
//        System.out.println("访问 tag service getalltags");
        return tagMapper.getAllTags();
    }

    @Override
    public void addTag(Tag tag) {
        tagMapper.insertTag(tag);
    }

    @Override
    public void deleteTag(Long tagId) {
        Tag tag = tagMapper.getAllTags().stream().filter(t -> t.getTagId().equals(tagId)).findFirst().orElse(null);
        if (tag == null) {
            throw new TagDeletionException("can not delete Tag");
        }
        String tagName = tag.getTagName();
        List<Long> blogIds = tagMapper.getBlogIdsByTagId(tagId);
        boolean singleTagFound = false;

        for (Long blogId : blogIds) {
            String tags = tagMapper.getTagsByBlogId(blogId);
            List<String> tagList = new ArrayList<>(Arrays.asList(tags.split(",")));
            if (tagList.size() == 1 && tagList.contains(tagName)) {
                singleTagFound = true;
                break;
            }
        }

        if (singleTagFound) {
            throw new TagDeletionException("Detected blogs with only the tag to be deleted, deletion aborted.");
        } else {
            for (Long blogId : blogIds) {
                String tags = tagMapper.getTagsByBlogId(blogId);
                List<String> tagList = new ArrayList<>(Arrays.asList(tags.split(",")));
                tagList.remove(tagName);
                String updatedTags = String.join(",", tagList);
                tagMapper.updateTagsByBlogId(blogId, updatedTags);
            }

            tagMapper.deleteBlogTagByTagId(tagId);
            tagMapper.deleteTag(tagId);
        }
    }

    @Override
    public List<Tag> getAllTagsWithBlogCount() {
        return tagMapper.getAllTagsWithBlogCount();
    }


    @Override
    public PageResult getTagsPage(PageQueryUtil pageUtil, Long loginUserId) {
        List<Tag> tagList = tagMapper.findTagList(pageUtil, loginUserId);
        int total = tagMapper.getTotalTags(pageUtil, loginUserId);

//        System.out.println("total: " + total);
//        System.out.println("Tag Details:");
        for (Tag tag : tagList) {
            System.out.println("ID: " + tag.getTagId() + ", Name: " + tag.getTagName() + ", Time: " + tag.getCreateAt());
        }

//        System.out.println(pageUtil);
//        System.out.println(loginUserId);

        return new PageResult(total, pageUtil.getLimit(), pageUtil.getPage(), tagList);
    }

    @Override
    public int getTagCountByUserId(Long userId) {
        return tagMapper.getTagCountByUserId(userId);
    }

//    @Override
//    public List<Tag> getTagsByUserId(Long userId) {
//        return tagMapper.getTagsByUserId(userId);
//    }

    @Override
    public List<Tag> getTagsByUserId(Long userId) {
        // 查询该用户的所有博客ID
        List<Long> blogIds = tagMapper.getBlogIdsByUserId(userId);

        // 如果该用户没有博客，则直接返回空列表
        if (CollectionUtils.isEmpty(blogIds)) {
            return Collections.emptyList();
        }

        // 通过博客ID列表查询关联的标签ID
        List<Long> tagIds = tagMapper.getTagIdsByBlogIds(blogIds);

        // 将标签ID列表转换为逗号分隔的字符串，以便于IN查询
        String tagIdsStr = StringUtils.join(tagIds, ",");

        // 通过标签ID列表查询所有标签
        return tagMapper.getTagsByIds(tagIdsStr);
    }

    // 实现获取热门标签的方法
    @Override
    public List<Tag> getHotTags() {
        List<Tag> hotTags = tagMapper.getHotTags();
        return hotTags;
    }

    @Override
    public List<Blog> getBlogsByTagName(String tagName) {
        return tagMapper.getBlogsByTagName(tagName);
    }

    @Override
    public List<Blog> getBlogsByTagNameAndUserId(String tagName, Long userId) {
        return tagMapper.getBlogsByTagNameAndUserId(tagName, userId);
    }

    @Override
    public void updateTagsForBlog(Long blogId, String oldTagNames, String tagNames) {
        List<String> oldTags = oldTagNames != null ?
                Arrays.asList(oldTagNames.split(",")) :
                new ArrayList<>();

        List<String> newTags = Arrays.asList(tagNames.split(","));


        // 找出需要新增和删除的标签
        List<String> tagsToAdd = new ArrayList<>(newTags);
        tagsToAdd.removeAll(oldTags);
//        System.out.println("tagsToAdd: " + tagsToAdd);

        List<String> tagsToRemove = new ArrayList<>(oldTags);
        tagsToRemove.removeAll(newTags);
        System.out.println("tagsToRemove: " + tagsToRemove);

        // 删除需要移除的标签关联
        for (String tagName : tagsToRemove) {
            Tag tag = tagMapper.getTagByName(tagName);
            System.out.println(tag);
            if (tag != null) {
                System.out.println(blogId.toString() + " " + tag.getTagId().toString());
                tagMapper.deleteBlogTag(blogId, tag.getTagId());
            }
        }

        // 新增需要添加的标签关联
        for (String tagName : tagsToAdd) {
            Tag tag = tagMapper.getTagByName(tagName);
            if (tag == null) {
                // 如果标签不存在，创建新标签
                tag = new Tag();
                tag.setTagName(tagName);
                tagMapper.insertTag(tag);
            }
            // 插入 blog_tag 关系
            tagMapper.insertBlogTag(blogId, tag.getTagId());
        }
    }

    @Override
    public void createTagsForBlog(Long blogId, String tagNames) {
//        List<String> oldTags = tagMapper.getTagsByBlogId(blogId) != null ?
//                Arrays.asList(tagMapper.getTagsByBlogId(blogId).split(",")) :
//                new ArrayList<>();

        List<String> newTags = Arrays.asList(tagNames.split(","));

        List<String> tagsToAdd = new ArrayList<>(newTags);
//        tagsToAdd.removeAll(oldTags);

        for (String tagName : tagsToAdd) {
            Tag tag = tagMapper.getTagByName(tagName.trim());
            if (tag == null) {
                tag = new Tag();
                tag.setTagName(tagName.trim());
                tagMapper.insertTag(tag);
            }
            tagMapper.insertBlogTag(blogId, tag.getTagId());
        }
    }
}


