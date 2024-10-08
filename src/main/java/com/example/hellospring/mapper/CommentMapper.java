package com.example.hellospring.mapper;

import com.example.hellospring.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {
    Comment selectByPrimaryKey(Long commentId);

    int insert(Comment record);

    int insertSelective(Comment record);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> findCommentListByBlogId(Map map);

    List<Comment> findCommentListByUserId(Map map);

    int getTotalCommentsByBlogId(Map map);

    int getTotalCommentsByUserId(Long userId);

    int checkDone(Integer[] ids);

    int deleteBatch(Integer[] ids);
}
