package com.example.hellospring.service.impl;

import com.example.hellospring.entity.Blog;
import com.example.hellospring.entity.Tag;
import com.example.hellospring.entity.vo.BlogDetailVO;
import com.example.hellospring.entity.vo.BlogListVO;
import com.example.hellospring.entity.vo.SimpleBlogListVO;
import com.example.hellospring.mapper.BlogMapper;
import com.example.hellospring.service.BlogService;
import com.example.hellospring.service.TagService;
import com.example.hellospring.service.BlogTagService;
import com.example.hellospring.util.MarkDownUtil;
import com.example.hellospring.util.PageQueryUtil;
import com.example.hellospring.util.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogTagService blogTagService;


    @Override
    public String saveBlog(Blog blog) {
        if (blogMapper.insertSelective(blog) > 0) {
            Long blogId = blog.getBlogId();
            if (blogId != null && blog.getTags() != null) {
                tagService.createTagsForBlog(blogId, blog.getTags());
            }
            return "success";
        } else {
            return "fail";
        }
    }

    @Override
    public boolean addBlog(Blog blog) {
        boolean result = blogMapper.insert(blog) > 0;
        if (result) {
            // Update or create tags for the blog
//            tagService.updateOrCreateTagsForBlog(blog.getBlogId(), blog.getTags());
            tagService.createTagsForBlog(blog.getBlogId(), blog.getTags());
        }
        return result;
    }

    @Override
    public boolean deleteBlog(Long id) {
        return blogMapper.deleteByPrimaryKey(id);
    }


    @Override
    public String updateBlog(Blog blog) {
        Blog blogForUpdate = blogMapper.selectByPrimaryKey(blog.getBlogId());
        String oldTagNames = blogForUpdate.getTags();
        if (blogForUpdate != null) {
            blogForUpdate.setTitle(blog.getTitle());
            blogForUpdate.setSummary(blog.getSummary());
            blogForUpdate.setTags(blog.getTags());
            blogForUpdate.setStatus(blog.getStatus());
            blogForUpdate.setViews(blog.getViews());
            blogForUpdate.setEnableComment(blog.getEnableComment());
            blogForUpdate.setUpdateAt(blog.getUpdateAt());
            blogForUpdate.setContent(blog.getContent());
        } else {
            return "不存在的博客，消失的眼角膜";
        }
        if (blogMapper.updateByPrimaryKeySelective(blogForUpdate) > 0) {
            tagService.updateTagsForBlog(blog.getBlogId(), oldTagNames, blog.getTags());
//            tagService.createTagsForBlog(blog.getBlogId(), blog.getTags());
            return "success";
        } else {
            return "fail";
        }
    }

    @Override
    public Blog getBlogById(Long id) {
        if (blogMapper.selectByPrimaryKey(id) != null)
            return blogMapper.selectByPrimaryKey(id);
        return null;
    }

    @Override
    public PageResult getBlogsPage(PageQueryUtil pageUtil, Long userId) {
        List<Blog> blogList = blogMapper.findBlogList(pageUtil, userId, null);
        int total = blogMapper.getTotalBlogs(pageUtil, userId, null);
        return new PageResult(total, pageUtil.getLimit(), pageUtil.getPage(), blogList);
    }

    @Override
    public BlogDetailVO getBlogDetail(Long blogId) {
        Blog blog = blogMapper.selectByPrimaryKey(blogId);
        return getBlogDetailVO(blog);
    }

    @Override
    public BlogDetailVO getBlogDetailVO(Blog blog) {
        if (blog != null && blog.getStatus() == 1) {
            //增加浏览量
            blog.setViews(blog.getViews() + 1);
            blogMapper.updateByPrimaryKey(blog);
            BlogDetailVO blogDetailVO = new BlogDetailVO();
            BeanUtils.copyProperties(blog, blogDetailVO);
            // blogDetailVO.setContent(MarkDownUtil.mdToHtml(blogDetailVO.getContent()));
            blogDetailVO.setContent(blog.getContent());
            blogDetailVO.setCommentCount(blogMapper.getCommentCountByBlogId(blog.getBlogId()));

            if (!StringUtils.isEmpty(blog.getTags())) {
                //标签设置
                List<String> tags = Arrays.asList(blog.getTags().split(","));
                blogDetailVO.setTags(tags);
            }
            //设置评论数
            Map params = new HashMap();
            params.put("blogId", blog.getBlogId());
            params.put("commentStatus", 1);//过滤审核通过的数据
            // blogDetailVO.setCommentCount(blogCommentMapper.getTotalBlogComments(params));
            return blogDetailVO;
        }
        return null;
    }

    @Override
    public PageResult getBlogsForArchievePage(int page, Long userId, Byte status) {
        Map params = new HashMap();
        params.put("page", page);
        //每页8条
        params.put("limit", 8);
        params.put("blogStatus", 1);//过滤发布状态下的数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        List<Blog> blogList = blogMapper.findBlogList(pageUtil, userId, status);
        List<BlogListVO> blogListVOS = getBlogListVOsByBlogs(blogList);
        int total = blogMapper.getTotalBlogs(pageUtil, userId, status);
        return new PageResult(total, pageUtil.getLimit(), pageUtil.getPage(), blogListVOS);
    }

    private List<BlogListVO> getBlogListVOsByBlogs(List<Blog> blogList) {
        List<BlogListVO> blogListVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(blogList)) {
            for (Blog blog : blogList) {
                BlogListVO blogListVO = new BlogListVO();
                BeanUtils.copyProperties(blog, blogListVO);
                blogListVOS.add(blogListVO);
            }
        }
        return blogListVOS;
    }

    /**
     * 获取首页博客列表
     * 该方法用于根据用户ID和博客类型，从数据库中查询相应的博客列表，并将其转换为适用于首页展示的简单博客列表视图对象。
     *
     * @param userId 用户ID，用于查询该用户发布的博客。
     * @param type 博客排序类型，0则按照时间降序排序，1则按照热度降序排序。
     * @return 返回一个SimpleBlogListVO类型的列表，包含查询到的博客的简要信息。
     */
    @Override
    public List<SimpleBlogListVO> getBlogListForArchievePage(Long userId, int type) {
        List<SimpleBlogListVO> simpleBlogListVOS = new ArrayList<>();
        List<Blog> blogs = blogMapper.findBlogListByType(userId, type, 9);
        if (!CollectionUtils.isEmpty(blogs)) {
            for (Blog blog : blogs) {
                SimpleBlogListVO simpleBlogListVO = new SimpleBlogListVO();
                BeanUtils.copyProperties(blog, simpleBlogListVO);
                simpleBlogListVOS.add(simpleBlogListVO);
            }
        }
        return simpleBlogListVOS;
    }

    @Override
    public int getTotalBlogsByUserId(Long loginUserId) {
        return blogMapper.getTotalBlogs(null, loginUserId, null);
    }

    @Override
    public boolean deleteBatch(Integer[] ids) {
        // 首先删除 blog_tag 表中的相关条目
        for (Integer id : ids) {
            blogTagService.deleteByBlogId(id.longValue());
        }
        // 然后删除 blog 表
        return blogMapper.deleteBatch(ids) > 0;
    }
    @Override
    public String checkPermisionByBlogIds(Integer[] blogIds, Long userId) {
        for (Integer blogId : blogIds) {
            Blog blog = blogMapper.selectByPrimaryKey(blogId.longValue());
            if (blog == null) {
                return "博客不存在";
            } else if (!userId.equals(blog.getUserId())) {
                return "存在无权删除的文章";
            }
        }
        return "success";
    }

    @Override
    public PageResult searchBlogsPage(PageQueryUtil pageUtil, Long userId, String searchType, String keyword) {
        List<Blog> blogList = blogMapper.searchBlogs(pageUtil, userId, searchType, keyword);
        int total = blogMapper.getTotalBlogs(pageUtil, userId, null);
        return new PageResult(total, pageUtil.getLimit(), pageUtil.getPage(), blogList);
    }

    @Override
    public List<Blog> searchBlogs(PageQueryUtil pageUtil, Long userId, String searchType, String keyword) {
        return blogMapper.searchBlogs(pageUtil, userId, searchType, keyword);
    }

    @Override
    public PageResult searchBlogsInArchive(PageQueryUtil pageUtil, Long userId, String keyword) {
        List<Blog> blogList = blogMapper.searchBlogsInArchive(pageUtil, userId, keyword);
        int totalBlogs = blogMapper.getTotalBlogsByKeyword(pageUtil, userId, keyword);
        PageResult pageResult = new PageResult(totalBlogs, pageUtil.getLimit(), pageUtil.getPage(), blogList);
        return pageResult;
    }

    @Override
    public int getTotalBlogsByKeyword(PageQueryUtil pageUtil, Long userId, String keyword) {
        return blogMapper.getTotalBlogsByKeyword(pageUtil, userId, keyword);
    }

    @Override
    public List<Blog> getTopBlogsByViews() {
        return blogMapper.getTopBlogsByViews();
    }
}
