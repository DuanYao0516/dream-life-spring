package com.example.hellospring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Blog {
    private Long blogId;
    private Long userId;
    private String title;
    private String summary;
    private String tags;
    private Byte status;
    private Long views;
    private Byte enableComment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    private Date createAt;
    private Date updateAt;

    private String content;

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Byte getEnableComment() {
        return enableComment;
    }

    public void setEnableComment(Byte enableComment) {
        this.enableComment = enableComment;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Blog(Long blogId, Long userId, String title, String summary,
                String blogTags, String blogContent, Byte blogStatus,
                Byte enableComment) {
        this.blogId = blogId;
        this.userId = userId;
        this.title = title;
        this.summary = summary;
        this.tags = blogTags;
        this.content = blogContent;
        this.status = blogStatus;
        this.enableComment = enableComment;
        this.createAt = new Date();
        this.updateAt = new Date();
        this.views = 0L;
    }

    public Blog(Long blogId, Long userId,
                String title, String summary,
                String tags, Integer status, Long views,
                Integer enableComment, Date createAt,
                Date updateAt, String content) {
        this.blogId = blogId;
        this.userId = userId;
        this.title = title;
        this.summary = summary;
        this.tags = tags;
        this.status = status.byteValue();
        this.views = views;
        this.enableComment = enableComment.byteValue();
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.content = content;
    }

    public Blog(Long userId, String title,
                String summary, String tags,
                Byte status, Long views,
                Byte enableComment, Date createAt,
                Date updateAt, String content) {
        this.userId = userId;
        this.title = title;
        this.summary = summary;
        this.tags = tags;
        this.status = status;
        this.views = views;
        this.enableComment = enableComment;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.content = content;
    }

    public Blog(Long userId, String title,
                String summary, String tags,
                Integer status, Long views,
                Integer enableComment, Date createAt,
                Date updateAt) {
        this.userId = userId;
        this.title = title;
        this.summary = summary;
        this.tags = tags;
        this.status = status.byteValue();
        this.views = views;
        this.enableComment = enableComment.byteValue();
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.content = content;
    }

    public Blog(Long userId, String title,
                String blogTags, String blogContent, Byte blogStatus,
                Byte enableComment) {
        this.userId = userId;
        this.title = title;
        // this.summary = summary;
        this.tags = blogTags;
        this.content = blogContent;
        this.status = blogStatus;
        this.enableComment = enableComment;
        this.createAt = new Date();
        this.updateAt = new Date();
        this.views = 0L;
    }
}
