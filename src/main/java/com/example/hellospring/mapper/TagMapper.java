package com.example.hellospring.mapper;

import com.example.hellospring.entity.Tag;
import com.example.hellospring.entity.Blog;
import org.apache.ibatis.annotations.*;
import com.example.hellospring.util.PageQueryUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper // @Mapper注解表示这个接口是一个Mapper接口，会被MyBatis的扫描器扫描到并生成对应的SQL映射文件。
public interface TagMapper {

    @Select("SELECT * FROM tag")
    @Results(id = "tagMap", value = {
            @Result(property = "tagId", column = "tag_id"),
            @Result(property = "tagName", column = "tag_name"),
            @Result(property = "createAt", column = "create_at")
    })
    List<Tag> getAllTags();

    @Insert("INSERT INTO tag(tag_name) VALUES(#{tagName})")
    @Options(useGeneratedKeys = true, keyProperty = "tagId") //使用了@Options注解来指定使用自动生成的主键，并将主键值赋给Tag对象的tagId属性。
    void insertTag(Tag tag);

    @Delete("DELETE FROM tag WHERE tag_id = #{tagId}")
    void deleteTag(Long tagId);

    @Delete("DELETE FROM blog_tag WHERE tag_id = #{tagId}")
    void deleteBlogTagByTagId(Long tagId);

    @Select("SELECT blog_id FROM blog_tag WHERE tag_id = #{tagId}")
    List<Long> getBlogIdsByTagId(Long tagId);

    @Select("SELECT tags FROM blog WHERE blog_id = #{blogId}")
    String getTagsByBlogId(Long blogId);

    @Update("UPDATE blog SET tags = #{tags} WHERE blog_id = #{blogId}")
    void updateTagsByBlogId(@Param("blogId") Long blogId, @Param("tags") String tags);

    @Select("SELECT t.tag_id, t.tag_name, COUNT(bt.blog_id) AS blogCount " +
            "FROM tag t LEFT JOIN blog_tag bt ON t.tag_id = bt.tag_id " +
            "GROUP BY t.tag_id")
    List<Tag> getAllTagsWithBlogCount();

//    分页查询标签列表
    List<Tag> findTagList(PageQueryUtil pageUtil, Long userId);
    // 查询标签总数
    int getTotalTags(PageQueryUtil pageUtil, Long userId);


    @Select("SELECT COUNT(*) FROM tag t JOIN blog_tag bt ON t.tag_id = bt.tag_id WHERE bt.blog_id IN (SELECT blog_id FROM blog WHERE user_id = #{userId})")
    int getTagCountByUserId(@Param("userId") Long userId);

    List<Tag> getTagsByUserId(Long userId);

    // 根据用户ID查询其所有博客ID
    List<Long> getBlogIdsByUserId(Long userId);

    // 根据博客ID列表查询所有关联的标签ID
    List<Long> getTagIdsByBlogIds(List<Long> blogIds);

    // 根据标签ID列表查询标签（注意这里需要支持IN查询）
    List<Tag> getTagsByIds(String tagIdsStr);

    // 新增方法获取热门标签
    List<Tag> getHotTags();

    List<Blog> getBlogsByTagName(@Param("tagName") String tagName);

    List<Blog> getBlogsByTagNameAndUserId(@Param("tagName") String tagName, @Param("userId") Long userId);

    @Select("SELECT * FROM tag WHERE tag_name = #{tagName}")
    @ResultMap("tagMap")
    Tag getTagByName(String tagName);

    @Insert("INSERT INTO blog_tag(blog_id, tag_id) VALUES(#{blogId}, #{tagId})")
    void insertBlogTag(@Param("blogId") Long blogId, @Param("tagId") Long tagId);

    @Delete("DELETE FROM blog_tag WHERE blog_id = #{blogId}")
    void deleteBlogTagsByBlogId(Long blogId);

    @Delete("DELETE FROM blog_tag WHERE blog_id = #{blogId} AND tag_id = #{tagId}")
    void deleteBlogTag(@Param("blogId") Long blogId, @Param("tagId") Long tagId);


}
