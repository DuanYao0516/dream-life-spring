package com.example.hellospring.service;

import com.example.hellospring.entity.Comment;
import com.example.hellospring.util.PageQueryUtil;
import com.example.hellospring.util.PageResult;

public interface CommentService {

    Boolean addComment(Comment blogComment);

    Boolean reply(Long commentId, String replyBody);

    PageResult getCommentPageByBlogIdAndPageNum(Long blogId, int page);

    PageResult getCommentsPage(PageQueryUtil pageUtil);

    int getTotalCommentsByUserId(Long userId);

    boolean deleteBatch(Integer[] ids);

    boolean checkDone(Integer[] ids);
}
