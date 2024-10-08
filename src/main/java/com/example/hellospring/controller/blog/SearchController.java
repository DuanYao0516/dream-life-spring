package com.example.hellospring.controller.blog;

import com.example.hellospring.service.BlogService;
import com.example.hellospring.util.PageQueryUtil;
import com.example.hellospring.util.PageResult;
import com.example.hellospring.service.UserService;
import com.example.hellospring.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private ConfigService configService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public String searchBlogsInArchive(@RequestParam Map<String, Object> params, Model model) {
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        Long userId = Long.parseLong(params.get("userToVisit").toString());
        String keyword = params.get("keyword").toString();
        PageResult pageResult = blogService.searchBlogsInArchive(pageUtil, userId, keyword);

        // 设置搜索结果
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("searchKeyword", keyword);
        // 设置页面名称
        model.addAttribute("pageName", "博客搜索结果");
        // 设置当前页面所有者ID
        model.addAttribute("visitingUserId", userId);
        // 设置访问者信息
        model.addAttribute("visitingNickname", userService.getUserById(userId).getNickname());
        // 设置配置信息
        model.addAttribute("configurations", configService.getAllConfigs());

        return "blog/default/search";
    }
}
