package com.example.hellospring.service;

import com.example.hellospring.entity.FollowRelation;
import com.example.hellospring.entity.vo.FollowListVO;

import java.util.List;

public interface FollowService {
    boolean addFollow(Long fromUserId, Long toUserId);

    int deleteFollow(Long fromUserId, Long toUserId);

    List<FollowRelation> getFollows(Long userId);

    List<FollowRelation> getFollowers(Long userId);

    // 获取关注列表的昵称
    List<FollowListVO> getFollowListVOsByFollows(List<FollowRelation> relations);

    // 获取粉丝列表的昵称
    List<FollowListVO> getFollowListVOsByFollowers(List<FollowRelation> relations);

    boolean hasFollowed(Long loginUserId, Long userToVisit);

    // 模糊搜索
    List<FollowListVO> fuzzySearchFollows(Long userId, String query);

    List<FollowListVO> fuzzySearchFollowers(Long userId, String query);
}
