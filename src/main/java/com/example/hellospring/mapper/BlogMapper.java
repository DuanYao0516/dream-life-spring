package com.example.hellospring.mapper;

import com.example.hellospring.entity.Blog;
import com.example.hellospring.util.PageQueryUtil;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BlogMapper {
    // 选取处于发布状态的文章
    Blog selectByPrimaryKey(Long id);
    int updateByPrimaryKey(Blog record);
    int updateByPrimaryKeySelective(Blog record);
    int insert(Blog record);
    int insertSelective(Blog record);
    boolean deleteByPrimaryKey(Long id);

    List<Blog> findBlogList(PageQueryUtil pageUtil, Long userId, Byte status);
    int getTotalBlogs(PageQueryUtil pageUtil, Long userId, Byte status);

    List<Blog> findBlogListByType(Long userId, int type, int limit);

    int deleteBatch(Integer[] ids);

    // 添加搜索博客的方法
    List<Blog> searchBlogs(PageQueryUtil pageUtil, Long userId, String searchType, String keyword);
    List<Blog> searchBlogsInArchive(@Param("pageUtil") PageQueryUtil pageUtil, @Param("userId") Long userId, @Param("keyword") String keyword);
    int getTotalBlogsByKeyword(PageQueryUtil pageUtil, Long userId, String keyword);

    List<Blog> getTopBlogsByViews();

    @Select("select count(comment_id) from comment where blog_id = #{blogId}")
    int getCommentCountByBlogId(Long blogId);
}
