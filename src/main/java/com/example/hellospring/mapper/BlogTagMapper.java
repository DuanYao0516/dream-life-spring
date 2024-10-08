package com.example.hellospring.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlogTagMapper {
    @Delete("DELETE FROM blog_tag WHERE blog_id = #{blogId}")
    int deleteByBlogId(@Param("blogId") Long blogId);

    @Insert("INSERT INTO blog_tag (blog_id, tag_id) VALUES (#{blogId}, #{tagId})")
    int insertBlogTag(@Param("blogId") Long blogId, @Param("tagId") Long tagId);
}
