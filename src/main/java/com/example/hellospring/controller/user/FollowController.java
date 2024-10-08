package com.example.hellospring.controller.user;

import com.example.hellospring.entity.vo.FollowListVO;
import com.example.hellospring.service.FollowService;
import com.example.hellospring.util.Result;
import com.example.hellospring.util.ResultGenerator;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {
    @Autowired()
    private FollowService followService;

    @GetMapping("user/social")
    public String social(
            @RequestParam(value = "tab", defaultValue = "follow") String tab,
            HttpServletRequest request,
            Model model) {
        Long userId = (Long) request.getSession().getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/user/login";
        }
    
        model.addAttribute("userId", userId);
        model.addAttribute("path", "social");
        model.addAttribute("query", ""); // 默认搜索字段为空
        model.addAttribute("tab", tab); // 传递当前选项卡
    
        if ("follow".equals(tab)) {
            model.addAttribute("follows", followService.getFollowListVOsByFollows(followService.getFollows(userId)));
            model.addAttribute("followers", new ArrayList<FollowListVO>()); // 空列表
        } else {
            model.addAttribute("followers", followService.getFollowListVOsByFollowers(followService.getFollowers(userId)));
            model.addAttribute("follows", new ArrayList<FollowListVO>()); // 空列表
        }
    
        return "user/social";
    }

    @GetMapping("user/follow")
    @ResponseBody
    public Result follow(@RequestParam("toUserId") Long toUserId, HttpServletRequest request) {
        if (toUserId == null) {
            return ResultGenerator.genFailResult("参数错误");
        }
        Long userId = (Long) request.getSession().getAttribute("loginUserId");

        if (followService.addFollow(userId, toUserId)) {
            return ResultGenerator.genSuccessResult();
        }

        return ResultGenerator.genFailResult("关注失败");
    }

    @GetMapping("user/unfollow")
    @ResponseBody
    public Result unfollow(@RequestParam("toUserId") Long toUserId, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("loginUserId");
        if (userId == null) {
            return ResultGenerator.genFailResult("用户未登录");
        }
        if (followService.deleteFollow(userId, toUserId) > 0) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("取消关注失败");
    }

    @GetMapping("user/social/search")
    public String search(
            @RequestParam("query") String query,
            @RequestParam("tab") String tab,
            HttpServletRequest request,
            Model model) {
        Long userId = (Long) request.getSession().getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("userId", userId);
        model.addAttribute("path", "social");
        model.addAttribute("query", query); // 传递搜索字段
        model.addAttribute("tab", tab); // 传递当前选项卡

        if ("follow".equals(tab)) {
            if (query == null || query.trim().isEmpty()) {
                model.addAttribute("follows", followService.getFollowListVOsByFollows(followService.getFollows(userId)));
            } else {
                model.addAttribute("follows", followService.fuzzySearchFollows(userId, query));
            }
            model.addAttribute("followers", new ArrayList<FollowListVO>()); // 空列表
        } else {
            if (query == null || query.trim().isEmpty()) {
                model.addAttribute("followers", followService.getFollowListVOsByFollowers(followService.getFollowers(userId)));
            } else {
                model.addAttribute("followers", followService.fuzzySearchFollowers(userId, query));
            }
            model.addAttribute("follows", new ArrayList<FollowListVO>()); // 空列表
        }

        return "user/social";
    }
}
