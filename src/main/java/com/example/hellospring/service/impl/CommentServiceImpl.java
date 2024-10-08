package com.example.hellospring.service.impl;

import com.example.hellospring.entity.Comment;
import com.example.hellospring.entity.vo.CommentListVO;
import com.example.hellospring.mapper.CommentMapper;
import com.example.hellospring.mapper.UserMapper;
import com.example.hellospring.service.CommentService;
import com.example.hellospring.util.PageQueryUtil;
import com.example.hellospring.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentMapper commentMapper;

    @Override
    public Boolean addComment(Comment blogComment) {
        return commentMapper.insertSelective(blogComment) > 0;
    }

    @Override
    public Boolean reply(Long commentId, String replyBody) {
        Comment comment = commentMapper.selectByPrimaryKey(commentId);

        if (comment != null && comment.getStatus().intValue() == 1) {
            comment.setReply(replyBody);
            comment.setReplyAt(new Date());
            return commentMapper.updateByPrimaryKeySelective(comment) > 0;
        }
        return false;
    }

    public List<CommentListVO> getCommentListVOsByComments(List<Comment> comments) {
        List<CommentListVO> commentListVOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(comments)) {
            for (Comment comment : comments) {
                CommentListVO commentListVO = new CommentListVO();
                commentListVO.setCommentId(comment.getCommentId());
                commentListVO.setContent(comment.getContent());
                commentListVO.setNickname(userMapper.getNicknameByUId(comment.getUserId()));
                commentListVO.setCreateAt(comment.getCreateAt());
                commentListVO.setReply(comment.getReply());
                commentListVO.setReplyAt(comment.getReplyAt());
                commentListVO.setStatus(comment.getStatus());
                commentListVOs.add(commentListVO);
           }
        }
        return commentListVOs;
    }

    @Override
    public PageResult getCommentPageByBlogIdAndPageNum(Long blogId, int page) {
        if (page < 1) {
            return null;
        }
        Map params = new HashMap();
        params.put("page", page);
        params.put("limit", 8);
        params.put("blogId", blogId);
        params.put("commentStatus", 1);

        PageQueryUtil pageUtil = new PageQueryUtil(params);
        List<Comment> comments = commentMapper.findCommentListByBlogId(pageUtil);
        List<CommentListVO> commentListVOs = getCommentListVOsByComments(comments);

        if (!CollectionUtils.isEmpty(commentListVOs)) {
            int total = commentMapper.getTotalCommentsByBlogId(pageUtil);
            PageResult pageResult = new PageResult(total, pageUtil.getLimit(), pageUtil.getPage(), commentListVOs);
            return pageResult;
        }
        return null;
    }

    @Override
    public PageResult getCommentsPage(PageQueryUtil pageUtil) {
        List<Comment> comments = commentMapper.findCommentListByUserId(pageUtil);
        if (!CollectionUtils.isEmpty(comments)) {
            List<CommentListVO> commentListVOs = getCommentListVOsByComments(comments);
            PageResult pageResult = new PageResult(commentListVOs.size(), pageUtil.getLimit(), pageUtil.getPage(), commentListVOs);
            return pageResult;
        }
//        int total = commentMapper.getTotalCommentsByUserId((Long) pageUtil.get("userId"));
//        PageResult pageResult = new PageResult(total, pageUtil.getLimit(), pageUtil.getPage(), comments);
        return null;
    }

    @Override
    public int getTotalCommentsByUserId(Long userId) {
        return commentMapper.getTotalCommentsByUserId(userId);
    }

    @Override
    public boolean deleteBatch(Integer[] ids) {
        return commentMapper.deleteBatch(ids) > 0;
    }

    @Override
    public boolean checkDone(Integer[] ids) {
        return commentMapper.checkDone(ids) > 0;
    }
}
