package com.example.hellospring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TagServiceTest {

   @Autowired
   private TagService tagService; // 假设已经通过Spring配置正确注入

   @Test
   public void testGetTagCountByUserId() {
       // 准备测试数据
       Long userId = 2L; // 假设用户ID为1

       // 执行方法
       int tagCount = tagService.getTagCountByUserId(userId);// 没有这个方法

       // 验证预期输出
       Assertions.assertEquals(10, tagCount); // 假设预期用户ID为1的tag数量为11
   }
}