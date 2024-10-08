package com.example.hellospring.controller;

import com.example.hellospring.entity.User;
import com.example.hellospring.entity.vo.TopUserVO;
import com.example.hellospring.service.BlogService;
import com.example.hellospring.service.MailService;
import com.example.hellospring.service.UserService;
import com.example.hellospring.service.TagService;
import com.example.hellospring.util.Result;
import com.example.hellospring.util.ResultGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HelloController {
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private MailService mailService;
    @Autowired
    private TagService tagService;
    @GetMapping({"/", "/index"})
    public String SayHello(Model model, HttpServletRequest request) {
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");

        List<TopUserVO> users = userService.getTopUsersByBlogCount().stream().map(user -> {
            TopUserVO userVO = new TopUserVO();
//            System.out.println(user);
            userVO.setUserId(user.getUserId());
            userVO.setUserName(user.getUserName());
            userVO.setNickname(user.getNickname());
            userVO.setEmail(user.getEmail());
            userVO.setBlogCount(blogService.getTotalBlogsByUserId(user.getUserId()));
            return userVO;
        }).collect(Collectors.toList());

        model.addAttribute("topUsers", users);
        model.addAttribute("topBlogs", blogService.getTopBlogsByViews());
        if(loginUserId == null){
            request.setAttribute("loginUserName", null);
            request.setAttribute("loginUserNickName", null);
        } else{
            User adminUser = userService.getUserById(loginUserId);
            request.setAttribute("loginUserName", adminUser.getUserName());
            request.setAttribute("loginUserNickName", adminUser.getNickname());
        }
        request.setAttribute("loginUserId", loginUserId);

        //TopTags
//        System.out.println(tagService.getHotTags());
        request.setAttribute("topTags", tagService.getHotTags());

        return "hello/index";
    }

    @GetMapping("/aboutus")
    public String showAboutUsPage(Model model, HttpServletRequest request) {
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        request.setAttribute("loginUserId", loginUserId);
        // 返回关于我们页面
        return "hello/AboutUs";
    }

    @GetMapping("/feedback")
    public String showFeedbackForm(Model model, HttpServletRequest request) {
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        request.setAttribute("loginUserId", loginUserId);
        // 返回反馈表单页面
        return "hello/feedback";
    }

    @PostMapping("/feedback")
    @ResponseBody
    public Result handleFeedback(@RequestParam("name") String name,
                                 @RequestParam("contact") String contact,
                                 @RequestParam("feedback") String feedback) {
        String subject = "用户反馈";
        String message = "姓名: " + name + "\n" +
                         "联系方式: " + contact + "\n" +
                         "反馈内容: " + feedback;

        try {
            // 尝试发送邮件
            mailService.sendSimpleMail("dreamlife2024@163.com", subject, message);
            return ResultGenerator.genSuccessResult("反馈已提交，谢谢您的意见！");
        } catch (Exception e) {
            // 捕获邮件发送过程中的异常
            // 记录日志，这里仅为示例，实际应用中应使用日志框架记录错误
            System.err.println("发送反馈邮件时发生错误: " + e.getMessage());
            // 返回失败的结果给前端，告知用户反馈提交遇到问题
            return ResultGenerator.genFailResult("反馈提交失败，请稍后重试。");
        }
    }
}
