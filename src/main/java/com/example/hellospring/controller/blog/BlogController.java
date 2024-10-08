package com.example.hellospring.controller.blog;

import com.example.hellospring.entity.Blog;
import com.example.hellospring.entity.vo.BlogDetailVO;
import com.example.hellospring.service.BlogService;
import com.example.hellospring.service.CommentService;
import com.example.hellospring.service.ConfigService;
import com.example.hellospring.service.UserService;
import com.example.hellospring.service.TagService;
import com.example.hellospring.util.PageQueryUtil;
import com.example.hellospring.util.PageResult;
import com.example.hellospring.util.Result;
import com.example.hellospring.util.ResultGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/blog")
public class BlogController {
    private final String theme = "default"; // theme: amaze, default

    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private TagService tagService;

    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum,
                       Long userId) {
        PageResult blogPageResult = blogService.getBlogsForArchievePage(pageNum, userId, (byte) 1);
        if (blogPageResult == null) {
            return "error/404";
        }
//        System.out.println("blogPageResult: " + blogPageResult);
        request.setAttribute("path", "archive");
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("newBlogs", blogService.getBlogListForArchievePage(userId, 1));
        request.setAttribute("hotBlogs", blogService.getBlogListForArchievePage(userId, 0));
//        System.out.println("tagserice:get hot tags start");
        System.out.println(tagService.getHotTags());
        request.setAttribute("hotTags", tagService.getHotTags());
//        System.out.println("tagserice:get hot tags end");
        request.setAttribute("pageName", "首页");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/archive";
    }

    @GetMapping("edit")
    public String edit(HttpServletRequest request) {
        Object userId = request.getSession().getAttribute("loginUserId");
        if (userId == null) {
            return "user/login";
        }
        return "blog/edit";
    }

    @GetMapping("edit/{blogId}")
    public String edit(HttpServletRequest request, @PathVariable Long blogId) {
        request.setAttribute("path", "edit");
        Blog blog = blogService.getBlogById(blogId);
        if (blog != null) {
            request.setAttribute("blog", blog);
        }
        else {
            // deal with error
            return "error/404";
        }
        return "blog/edit";
    }

    @GetMapping("list")
    @ResponseBody
    public Result list(HttpServletRequest request, @RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("limit").toString()) || StringUtils.isEmpty(params.get("page").toString())) {
            return ResultGenerator.genFailResult("参数异常");
        }
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        if (loginUserId == null || loginUserId < 1) {
            return ResultGenerator.genFailResult("未登录");
        }
    
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        String searchType = (String) params.get("searchType");
        String keyword = (String) params.get("keyword");
    
        return ResultGenerator.genSuccessResult(blogService.searchBlogsPage(pageUtil, loginUserId, searchType, keyword));
    }

    @GetMapping({"article/{blogId}", "blog/{blogId}"})
    public String detail(HttpServletRequest request, @PathVariable Long blogId,
                         @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);

        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            // System.out.println("content:" + blogDetailVO.getContent());
            if (blogDetailVO.getEnableComment() == 1) {
                PageResult commentPageResult = commentService.getCommentPageByBlogIdAndPageNum(blogId, commentPage);
                request.setAttribute("commentPageResult", commentPageResult);
            }
        }
        else {
            return "error/404";
        }

        request.setAttribute("pageName", "详情");
        request.setAttribute("configurations", configService.getAllConfigs());

        return "blog/" + theme + "/detail";
    }

    @PostMapping("save")
    @ResponseBody
    public Result save(HttpServletRequest request,
                       @RequestParam("blogTitle") String blogTitle,
                       @RequestParam(name = "blogSummary", required = false) String blogSummary,
                       @RequestParam("blogTags") String blogTags,
                       @RequestParam("blogContent") String blogContent,
                       @RequestParam("blogStatus") Byte blogStatus,
                       @RequestParam("enableComment") Byte enableComment) {
        if (StringUtils.isEmpty(blogTitle)) {
            System.out.println("请输入文章标题");
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            System.out.println("文章标题过长");
            return ResultGenerator.genFailResult("文章标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            System.out.println("请输入文章标签");
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if (StringUtils.isEmpty(blogContent)) {
            System.out.println("请输入文章内容");
            return ResultGenerator.genFailResult("请输入文章内容");
        }

        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        if (loginUserId == null || loginUserId < 1) {
            return ResultGenerator.genFailResult("未登录");
        }

        Blog blog = new Blog(loginUserId, blogTitle, blogTags, blogContent, blogStatus, enableComment);
        blog.setSummary(blogSummary);

        String result = blogService.saveBlog(blog);
        if ("success".equals(result)) {
            return ResultGenerator.genSuccessResult();
        }
        else {
            return ResultGenerator.genFailResult(result);
        }
    }

    @PostMapping("update")
    @ResponseBody
    public Result update(HttpServletRequest request,
                          @RequestParam("blogId") Long blogId,
                         @RequestParam("blogTitle") String blogTitle,
                         @RequestParam(name = "blogSummary", required = false) String blogSummary,
                         @RequestParam("blogTags") String blogTags,
                         @RequestParam("blogContent") String blogContent,
                         @RequestParam("blogStatus") Byte blogStatus,
                         @RequestParam("enableComment") Byte enableComment) {
        if (StringUtils.isEmpty(blogTitle)) {
            System.out.println("请输入文章标题");
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            System.out.println("文章标题过长");
            return ResultGenerator.genFailResult("文章标题过长");

        }
        if (StringUtils.isEmpty(blogTags)) {
            System.out.println("请输入文章标签");
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            System.out.println("文章标签过长");
            return ResultGenerator.genFailResult("文章标签过长");
        }
        if (blogSummary.trim().length() > 375) {
            System.out.println("文章摘要过长");
            return ResultGenerator.genFailResult("文章摘要过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            System.out.println("请输入文章内容");
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (blogTags.trim().length() > 100000) {
            System.out.println("文章内容过长");
            return ResultGenerator.genFailResult("文章内容过长");
        }

        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        if (loginUserId == null || loginUserId < 1) {
            return ResultGenerator.genFailResult("未登录");
        }

        String result = blogService.updateBlog(new Blog(blogId,
                loginUserId, blogTitle, blogSummary, blogTags, blogContent, blogStatus, enableComment));
        if ("success".equals(result)) {
            return ResultGenerator.genSuccessResult();
        }
        else {
            return ResultGenerator.genFailResult(result);
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids, HttpServletRequest request) {
        System.out.println("delete");
        System.out.println(ids);
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }

        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        if (loginUserId == null || loginUserId < 1) {
            return ResultGenerator.genFailResult("未登录");
        }

        String checkResult = blogService.checkPermisionByBlogIds(ids, loginUserId);
        if (!"success".equals(checkResult)) {
            return ResultGenerator.genFailResult(checkResult);
        }

        if (blogService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    @GetMapping("/archive")
    public String archive(HttpServletRequest request,
                          @RequestParam(value="userToVisit", required = false) Long userToVisit,
                          @RequestParam(value="pageNum", required = false) Integer pageNum
                          ) {
        if (userToVisit == null) {
            Long userId = (Long) request.getSession().getAttribute("loginUserId");
            if (userId == null || userId < 1) {
                return "redirect:/user/login";
            }
            userToVisit = userId;
        }
        if (pageNum == null) {
            pageNum = 1;
        }

        request.setAttribute("pageName", "博客归档");
        request.setAttribute("visitingUserId", userToVisit);
        request.setAttribute("visitingNickname", userService.getUserById(userToVisit).getNickname());
        return this.page(request, pageNum, userToVisit);
    }

    // @GetMapping("/search")
    // public String searchBlogsInArchive(@RequestParam Map<String, Object> params, Model model) {
    //     PageQueryUtil pageUtil = new PageQueryUtil(params);
    //     Long userId = Long.parseLong(params.get("userToVisit").toString());
    //     String keyword = params.get("keyword").toString();
    //     PageResult pageResult = blogService.searchBlogsInArchive(pageUtil, userId, keyword);
    
    //     // 设置搜索结果
    //     model.addAttribute("pageResult", pageResult);
    //     model.addAttribute("searchKeyword", keyword);
    //     // 设置页面名称
    //     model.addAttribute("pageName", "搜索结果");
    //     // 设置配置信息
    //     model.addAttribute("configurations", configService.getAllConfigs());
    
    //     return "blog/default/search";
    // }
}
