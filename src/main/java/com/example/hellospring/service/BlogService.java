package com.example.hellospring.service;

import com.example.hellospring.entity.Blog;
import com.example.hellospring.entity.vo.BlogDetailVO;
import com.example.hellospring.entity.vo.SimpleBlogListVO;
import com.example.hellospring.util.PageQueryUtil;
import com.example.hellospring.util.PageResult;

import java.util.List;

/*
*   增删改查
* */
public interface BlogService {
    String saveBlog(Blog blog);

    boolean addBlog(Blog blog);

    boolean deleteBlog(Long id);

    String updateBlog(Blog blog);

    Blog getBlogById(Long id);

    PageResult getBlogsPage(PageQueryUtil pageUtil, Long userId);

    BlogDetailVO getBlogDetail(Long blogId);

    BlogDetailVO getBlogDetailVO(Blog blog);

    PageResult getBlogsForArchievePage(int page, Long userId, Byte status);

    List<SimpleBlogListVO> getBlogListForArchievePage(Long userId, int type);

    int getTotalBlogsByUserId(Long loginUserId);

    boolean deleteBatch(Integer[] ids);

    String checkPermisionByBlogIds(Integer[] blogIds, Long userId);

    PageResult searchBlogsPage(PageQueryUtil pageUtil, Long userId, String searchType, String keyword);

    List<Blog> searchBlogs(PageQueryUtil pageUtil, Long userId, String searchType, String keyword);
    PageResult searchBlogsInArchive(PageQueryUtil pageUtil, Long userId, String keyword);
    int getTotalBlogsByKeyword(PageQueryUtil pageUtil, Long userId, String keyword);

    List<Blog> getTopBlogsByViews();
}
