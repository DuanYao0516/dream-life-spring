package com.example.hellospring.service.impl;

import com.example.hellospring.entity.FollowRelation;
import com.example.hellospring.entity.vo.FollowListVO;
import com.example.hellospring.mapper.FollowMapper;
import com.example.hellospring.mapper.UserMapper;
import com.example.hellospring.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FollowMapper followMapper;

    @Override
    public boolean addFollow(Long fromUserId, Long toUserId) {
        return followMapper.insert(new FollowRelation(fromUserId, toUserId)) > 0;
    }

    @Override
    public int deleteFollow(Long fromUserId, Long toUserId) {
        return followMapper.deleteFollow(new FollowRelation(fromUserId, toUserId));
    }

    @Override
    public List<FollowRelation> getFollows(Long userId) {
        return followMapper.selectFollowByFromUserId(userId);
    }

    @Override
    public List<FollowRelation> getFollowers(Long userId) {
        return followMapper.selectFollowByToUserId(userId);
    }

    @Override
    public List<FollowListVO> getFollowListVOsByFollows(List<FollowRelation> relations) {
        List<FollowListVO> nicknames = new ArrayList<>();
        for (FollowRelation relation : relations) {
            nicknames.add(new FollowListVO(relation.getToUserId(),
                    relation.getToUserId(),
                    userMapper.getNicknameByUId(relation.getToUserId()),
                    relation.getCreateAt()
                    ));
        }
        return nicknames;
    }

    @Override
    public List<FollowListVO> getFollowListVOsByFollowers(List<FollowRelation> relations) {
        List<FollowListVO> nicknames = new ArrayList<>();
        for (FollowRelation relation : relations) {
            nicknames.add(new FollowListVO(relation.getFromUserId(),
                    relation.getToUserId(),
                    userMapper.getNicknameByUId(relation.getFromUserId()),
                    relation.getCreateAt()
                    ));
        }
        return nicknames;
    }

    @Override
    public boolean hasFollowed(Long loginUserId, Long userToVisit) {
        Integer count = followMapper.selectByFromUserIdAndToUserId(loginUserId, userToVisit);
        return count != null && count > 0;
    }

    @Override
    public List<FollowListVO> fuzzySearchFollows(Long userId, String query) {
        List<FollowRelation> followRelations = followMapper.fuzzySearchFollowByFromUserIdAndNickname(userId, query);
        List<FollowListVO> result = new ArrayList<>();
        for (FollowRelation relation : followRelations) {
            String nickname = userMapper.getNicknameByUId(relation.getToUserId());
            result.add(new FollowListVO(relation.getToUserId(),
                    relation.getToUserId(),
                    nickname,
                    relation.getCreateAt()
            ));
        }
        return result;
    }

    @Override
    public List<FollowListVO> fuzzySearchFollowers(Long userId, String query) {
        List<FollowRelation> followerRelations = followMapper.fuzzySearchFollowByToUserIdAndNickname(userId, query);
        List<FollowListVO> result = new ArrayList<>();
        for (FollowRelation relation : followerRelations) {
            String nickname = userMapper.getNicknameByUId(relation.getFromUserId());
            result.add(new FollowListVO(relation.getFromUserId(),
                    relation.getToUserId(),
                    nickname,
                    relation.getCreateAt()
            ));
        }
        return result;
    }
}
