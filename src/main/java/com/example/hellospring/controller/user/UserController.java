package com.example.hellospring.controller.user;

import com.example.hellospring.entity.User;
import com.example.hellospring.entity.Blog;
import com.example.hellospring.service.BlogService;
import com.example.hellospring.service.CommentService;
import com.example.hellospring.service.FollowService;
import com.example.hellospring.service.UserService;
import com.example.hellospring.service.TagService;
import com.example.hellospring.service.MailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;

import com.example.hellospring.util.PageQueryUtil;
import com.example.hellospring.util.Result;
import com.example.hellospring.util.ResultGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FollowService followService;
    @Autowired
    private TagService tagService;
    @Autowired
    private MailService mailService;

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        if (request.getSession().getAttribute("loginUserId") != null) {
            return "redirect:/user/home";
        }
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session,
                        Model model
    ) {
    if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
        model.addAttribute("errorMsg", "用户名或密码不能为空");
        return "user/login";
    }
     String kaptchaCode = session.getAttribute("verifyCode") + "";
    if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
        model.addAttribute("errorMsg", "验证码错误");
        return "user/login";
    }

    User user = userService.login(userName, password);
    if (user != null) {
        session.setAttribute("loginUser", user.getNickname());
        session.setAttribute("loginUserId", user.getUserId());
        session.setMaxInactiveInterval(60 * 60 * 5);
        return "redirect:/user/home";
    } else {
        model.addAttribute("errorMsg", "用户名或密码错误");
        return "user/login";
    }
}

    @GetMapping("/register")
    public String register(@ModelAttribute("user") User user) {
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("userName") String userName,
                           @RequestParam("password") String password,
                           @RequestParam("nickname") String nickname,
                           @RequestParam("email") String email,
                           @RequestParam("verifyCode") String verifyCode,
                           Model model,
                           HttpServletRequest request
                           ) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(email)
                || StringUtils.isEmpty(verifyCode)) {
            model.addAttribute("errorMsg", "请填写完整信息");
            return "user/register";
        }
        // System.out.println("userName: " + userName + ", password: " + password + ", nickname: " + nickname);
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setEmail(email);

        String checkCode = (String) request.getSession().getAttribute("checkCode");
        if (checkCode == null || !checkCode.equals(verifyCode)) {
            model.addAttribute("errorMsg", "验证码错误");
            return "user/register";
        } else if ((Long) request.getSession().getAttribute("ExpirationTime") < System.currentTimeMillis()) {
            model.addAttribute("errorMsg", "验证码过期");
            return "user/register";
        }

        if(userService.register(user)) {
            // 成功注册后，使用注册时填写的用户名和密码发送登录请求
            return "redirect:/user/login";
        }
        else {
            model.addAttribute("errorMsg", "用户名或邮箱已存在");
            return "user/register";
        }
    }

    @GetMapping("/home")
    public String home(HttpServletRequest request, @RequestParam(value = "userToVisit", required = false) Long userToVisit) {
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");

        if (userToVisit == null) {
            if (loginUserId == null) {
                return "redirect:/user/login";
            }
            request.setAttribute("isLogin", true);
            userToVisit = loginUserId;
        }
        else {
            if (loginUserId == null || !loginUserId.equals(userToVisit)) {
                request.setAttribute("isLogin", false);
                if (loginUserId != null) {
                    request.setAttribute("hasFollowed", followService.hasFollowed(loginUserId, userToVisit));
                }
            }
            else {
                request.setAttribute("isLogin", true);
            }
        }

        request.setAttribute("path", "home");

        User user = userService.getUserById(userToVisit);
        request.setAttribute("user", user);
        request.setAttribute("blogCount", blogService.getTotalBlogsByUserId(userToVisit));
        request.setAttribute("commentCount", commentService.getTotalCommentsByUserId(userToVisit));

        // 计算并添加标签总量到请求属性中
        int tagCount = tagService.getTagCountByUserId(userToVisit);
//        System.out.println(tagCount);
        request.setAttribute("tagCount", tagCount);

        return "user/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginUser");
        session.removeAttribute("loginUserId");
        return "redirect:/user/login";
    }

    @GetMapping("/blog")
    public String blogPage(HttpServletRequest request) {
        request.setAttribute("path", "blog");

        return "user/blog";
    }

    @GetMapping("/tags")
    public String tagPage(HttpServletRequest request, Model model) {
        request.setAttribute("path", "tags");
//        // 如果有登录用户，则获取该用户的标签列表，否则获取所有公开标签
//        // 获取登录用户ID
//        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
//        List<Tag> tags;
//
//        model.addAttribute("tags", tags);
        return "user/tags";
    }

    // 处理获取标签列表的AJAX请求
    @GetMapping("/tags/list")
    @ResponseBody
    public Result listTags(HttpServletRequest request, @RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("limit")) || StringUtils.isEmpty(params.get("page"))) {
            return ResultGenerator.genFailResult("参数异常");
        }

        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        if (loginUserId == null || loginUserId < 1) {
            return ResultGenerator.genFailResult("未登录");
        }

        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(tagService.getTagsPage(pageUtil, loginUserId));
    }

    @GetMapping("/tags/{tagName}")
    public String getBlogsByTagName(@PathVariable("tagName") String tagName, HttpServletRequest request, Model model) {
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        if (loginUserId == null) {
            return "redirect:/user/login";
        }
        List<Blog> blogs = tagService.getBlogsByTagNameAndUserId(tagName, loginUserId);
        model.addAttribute("tagName", tagName);
        model.addAttribute("blogs", blogs);
        return "user/tag-blogs";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        request.setAttribute("path", "profile");
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        User adminUser = userService.getUserById(loginUserId);
        if (adminUser == null) {
            return "user/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", adminUser.getUserName());
        request.setAttribute("nickName", adminUser.getNickname());
        return "user/profile";
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName) {
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        if (userService.updateName(loginUserId, loginUserName, nickName)) {
            request.getSession().setAttribute("loginUser", nickName);
            return "success";
        } else {
            return "修改失败";
        }
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空";
        }
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        if (userService.updatePassword(loginUserId, originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return "success";
        } else {
            return "修改失败";
        }
    }

    @GetMapping("/retrievePwd")
    public String retrievePwdPage() {
        return "user/retrievePwd";
    }

    @PostMapping("/retrievePwd")
    public String retrievePwd(@RequestParam("email") String email,
                              @RequestParam("verifyCode") String verifyCode,
                              HttpSession session,
                              Model model) {
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            model.addAttribute("errorMsg", "验证码错误");
            return "user/retrievePwd";
        }

        Long userId = userService.getUserIdByEmail(email);
        if (userId == null) {
            model.addAttribute("errorMsg", "该邮箱未注册");
            return "user/retrievePwd";
        }

        // 生成随机密码或密码重置链接
        String newPassword = generateRandomPassword();
        userService.retrievePassword(userId, newPassword);

        // 发送邮件
        mailService.sendSimpleMail(email, "DreamLife Blog 找回密码", "您的新密码是：" + newPassword);

        model.addAttribute("successMsg", "新密码已发送至您的邮箱，请查收，请尽快更新您的密码");
        return "user/retrievePwd";
    }

    private String generateRandomPassword() {
        int length = 8;
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(charSet.charAt(random.nextInt(charSet.length())));
        }
        return sb.toString();
    }

    @PostMapping("/md/uploadfile")
    @ResponseBody
    public Object uploadImage(@RequestParam("editormd-image-file") MultipartFile file,
                              HttpServletRequest request) {
        if (!file.isEmpty()) {
            try {
                String path = ResourceUtils.getURL("classpath:").getPath() + "static/md/img";
                // String path = "D:/jetbra/hellospring/target/classes/static/md/img";
                // System.out.println(path);
                // 获取文件原始名称
                String originalFilename = file.getOriginalFilename();
                // 使用UUID重命名文件
                String newFileName = UUID.randomUUID() + "." + getExtension(Objects.requireNonNull(originalFilename));

                // 定义图片保存路径
                Path uploadPath = Paths.get(path);
                System.out.println("uploadPath: " + uploadPath);

                Files.createDirectories(uploadPath);
                Files.copy(file.getInputStream(), uploadPath.resolve(newFileName));
                // 返回给 editor.md 的JSON响应
                return "{\"success\": 1, \"message\": \"\", \"url\": \"/static/md/img/" + newFileName + "\"}";
            } catch (IOException e) {
                e.printStackTrace();
                return "{\"success\": 0, \"message\": \"上传失败\"}";
            }
        } else {
            return "{\"success\": 0, \"message\": \"文件为空\"}";
        }
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

}
