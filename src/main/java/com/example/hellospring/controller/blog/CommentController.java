package com.example.hellospring.controller.blog;

import com.example.hellospring.entity.Comment;
import com.example.hellospring.service.BlogService;
import com.example.hellospring.service.CommentService;
import com.example.hellospring.service.UserService;
import com.example.hellospring.util.PageQueryUtil;
import com.example.hellospring.util.Result;
import com.example.hellospring.util.ResultGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
public class CommentController {
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/user/comments/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Long userId = (Long) request.getSession().getAttribute("loginUserId");
        if (userId == null || userId < 1) {
            return ResultGenerator.genFailResult("用户未登录");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        pageUtil.put("userId", userId);
        return ResultGenerator.genSuccessResult(commentService.getCommentsPage(pageUtil));
    }

    @PostMapping("/blog/article/{blogId}/comment")
    @ResponseBody
    public Result comment(@PathVariable("blogId") Long blogId,
                          @RequestParam("commentBody") String commentBody,
                          HttpServletRequest request
                          ) {
        Long userId = (Long) request.getSession().getAttribute("loginUserId");
        if (userId == null || userId < 1) {
            return ResultGenerator.genFailResult("用户未登录");
        }
        else {
            Comment comment = new Comment();
            comment.setBlogId(blogId);
            comment.setUserId(userId);
            comment.setContent(commentBody);
            if (commentService.addComment(comment)) {
                return ResultGenerator.genSuccessResult();
            }
            else {
                return ResultGenerator.genFailResult("评论失败");
            }
        }
    }

    @GetMapping("/user/comments")
    public String comments(HttpServletRequest request) {
        request.setAttribute("path", "comments");
        return "user/comments";
    }

    @PostMapping("/user/comments/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (commentService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        }
        else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    @PostMapping("/user/comments/checkDone")
    @ResponseBody
    public Result checkDone(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (commentService.checkDone(ids)) {
            return ResultGenerator.genSuccessResult();
        }
        else {
            return ResultGenerator.genFailResult("审核失败，选中的评论可能均已通过审核");
        }
    }

    @PostMapping("/user/comments/reply")
    @ResponseBody
    public Result reply(@RequestParam("commentId") Long commentId,
                        @RequestParam("replyBody") String replyBody) {
        if (StringUtils.isEmpty(replyBody)) {
            return ResultGenerator.genFailResult("回复内容不能为空");
        }
        else if (commentService.reply(commentId, replyBody)) {
            return ResultGenerator.genSuccessResult();
        }
        else {
            return ResultGenerator.genFailResult("回复失败");
        }
    }
}
