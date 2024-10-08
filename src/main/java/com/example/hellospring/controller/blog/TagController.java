package com.example.hellospring.controller.blog;

import com.example.hellospring.entity.Tag;
import com.example.hellospring.entity.Blog;
import com.example.hellospring.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public String getAllTags(Model model) {
        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("tags", tags);
        return "tags";
    }

    @GetMapping("/tags-list")
    public String getTags(Model model) {
        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("tags", tags);
        return "tags";
    }

    @PostMapping("/add")
    public String addTag(@ModelAttribute Tag tag) {
        tagService.addTag(tag);
        return "redirect:/tags";
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteTag(@RequestBody Map<String, Long> request) {
        // 这部分代码有问题，前端脚本之间传递参数为空，但是这个功能貌似不需要
        Long tagId = request.get("tagId");
        tagService.deleteTag(tagId);
        return ResponseEntity.ok("Tag deleted successfully");
    }

    @GetMapping("/{tagName}")
    public String getBlogsByTagName(@PathVariable String tagName, Model model, HttpServletRequest request) {
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        request.setAttribute("loginUserId", loginUserId);
        List<Blog> blogs = tagService.getBlogsByTagName(tagName);
        model.addAttribute("blogs", blogs);
        model.addAttribute("tagName", tagName);
        return "tag-blogs";
    }
}
