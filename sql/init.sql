DROP TABLE IF EXISTS `users`;

CREATE TABLE `users`
(
    `user_id`   bigint(20)   NOT NULL AUTO_INCREMENT,
    `user_name` varchar(255) NOT NULL,
    `password`  varchar(255) NOT NULL,
    `nickname`  varchar(255) NOT NULL,
    `email`     varchar(255) NOT NULL,
    PRIMARY KEY (`user_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

# INSERT INTO `users` VALUES (1, 'linkehua', '1421', 'lkh', '1336211227@qq.com');
# INSERT INTO `users` VALUES (2, 'duanyao', '1234', 'duany', 'duany@126.com');
# INSERT INTO `users` VALUES (3, 'nnyilun', '123456', 'jyl', 'jyl@126.com');

DROP TABLE IF EXISTS `blog_tag`;
DROP TABLE IF EXISTS `blog`;

CREATE TABLE `blog` (
    `blog_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) NOT NULL,
    `title` varchar(255) NOT NULL,
    `summary` varchar(255) NOT NULL,
    `tags` varchar(255) NOT NULL,
    `status` tinyint(4) NOT NULL DEFAULT 0,
    `views` bigint(20) NOT NULL DEFAULT 0,
    `enable_comment` tinyint(4) NOT NULL DEFAULT 1,
    `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `content` longtext NOT NULL,
    PRIMARY KEY (`blog_id`)
)ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- 插入新的博客
# INSERT INTO `blog` (user_id, title, summary, tags, status, views, enable_comment, content)
# VALUES (2, 'duanyao_test1', 'Summary for duanyao_test1', 'tag1-1,tag1-2', 1, 0, 1, 'Content for duanyao_test1'),
#        (2, 'duanyao_test2', 'Summary for duanyao_test2', 'tag2-1,testend,tag2-3', 1, 0, 1, 'Content for duanyao_test2');




DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
                             `config_name` varchar(100) NOT NULL DEFAULT '' COMMENT '配置项名称',
                             `config_value` varchar(200) NOT NULL DEFAULT '' COMMENT '配置项值',
                             `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                             PRIMARY KEY (`config_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `config` VALUES ('footerAbout', '公共博客', '2024-11-11 20:33:23', '2024-02-26 02:36:45');
INSERT INTO `config` VALUES ('footerCopyRight', '2024 DreamLife', '2024-11-11 20:33:31', '2024-02-26 02:36:45');
INSERT INTO `config` VALUES ('footerICP', '111', '2024-11-11 20:33:27', '2024-02-26 02:36:45');
INSERT INTO `config` VALUES ('footerPoweredBy', 'https://gitee.com/laozibabac', '2024-11-11 20:33:36', '2024-02-26 02:36:45');
INSERT INTO `config` VALUES ('footerPoweredByURL', 'https://gitee.com/laozibabac', '2024-11-11 20:33:39', '2024-02-26 02:36:45');
INSERT INTO `config` VALUES ('websiteDescription', 'DreamLife 是 SpringBoot3 + Thymeleaf + Mybatis 建造的个人博客网站', '2024-11-11 20:33:04', '2024-02-26 02:36:11');
INSERT INTO `config` VALUES ('websiteIcon', '/static/user/dist/img/icon.png', '2024-11-11 20:33:11', '2024-02-26 02:36:11');
INSERT INTO `config` VALUES ('websiteLogo', '/static/user/dist/img/logo.png', '2024-11-11 20:33:08', '2024-02-26 02:36:11');
INSERT INTO `config` VALUES ('websiteName', 'DreamLifeBlog', '2024-11-11 20:33:01', '2024-02-26 02:36:11');
INSERT INTO `config` VALUES ('yourAvatar', '/static/user/dist/img/f.jpg', '2024-11-11 20:33:14', '2024-02-26 02:36:39');
INSERT INTO `config` VALUES ('yourEmail', '1336211227@qq.com', '2024-11-11 20:33:17', '2024-02-26 02:36:39');
INSERT INTO `config` VALUES ('yourName', 'laozibabac', '2024-11-11 20:33:20', '2024-02-26 02:36:39');

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '关联的blog主键',
    `blog_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '关联的blog主键',
    `email` varchar(100) NOT NULL DEFAULT '' COMMENT '评论人的邮箱',
    `content` varchar(200) NOT NULL DEFAULT '' COMMENT '评论内容',
    `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论提交时间',
    `reply` varchar(200) NOT NULL DEFAULT '' COMMENT '回复内容',
    `reply_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否审核通过 0-未审核 1-审核通过',
    PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# INSERT INTO `comment` VALUES ('1', '1', '16', '1336211227@qq.com', '测试评论', '2024-11-11 20:33:23', '测试回复', '2024-02-26 02:36:45', '1');


DROP TABLE IF EXISTS `follow`;

CREATE TABLE `follow` (
    `from_user_id` bigint(20) NOT NULL,
    `to_user_id` bigint(20) NOT NULL,
    `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`from_user_id`, `to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# INSERT INTO `follow` VALUES (1, 1, '2024-11-11 20:33:23');
# INSERT INTO `follow` VALUES (1, 2, '2024-11-11 20:33:23');

DROP TABLE IF EXISTS `tag`;
-- 创建 tag 表
CREATE TABLE tag (
    tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tag_name VARCHAR(255) NOT NULL,
    create_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS `blog_tag`;
-- 创建 blog_tag 中间表
CREATE TABLE blog_tag (
    blog_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (blog_id, tag_id),
    FOREIGN KEY (blog_id) REFERENCES blog(blog_id),
    FOREIGN KEY (tag_id) REFERENCES tag(tag_id)
);


-- 插入 tag 数据
# INSERT INTO tag (tag_name) VALUES
# ('tag1-1'), ('tag1-2'), ('tag2-1'), ('testend'), ('tag2-3');

-- 插入 blog_tag 数据
# INSERT INTO blog_tag (blog_id, tag_id) VALUES
# (16, (SELECT tag_id FROM tag WHERE tag_name='tag1-1')),
# (16, (SELECT tag_id FROM tag WHERE tag_name='tag1-2')),
# (17, (SELECT tag_id FROM tag WHERE tag_name='tag2-1')),
# (17, (SELECT tag_id FROM tag WHERE tag_name='testend')),
# (17, (SELECT tag_id FROM tag WHERE tag_name='tag2-3'));