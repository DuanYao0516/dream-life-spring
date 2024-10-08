package com.example.hellospring.mapper;

import com.example.hellospring.entity.FollowRelation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FollowMapper {
    @Insert("insert into follow(from_user_id, to_user_id) values(#{fromUserId}, #{toUserId})")
    int insert(FollowRelation record);

    @Delete("delete from follow where from_user_id = #{fromUserId} and to_user_id = #{toUserId}")
    int deleteFollow(FollowRelation record);

    @Select("SELECT * FROM follow WHERE from_user_id = #{fromUserId}")
    @Results(id = "followRelationMap", value = {
            @Result(property = "fromUserId", column = "from_user_id"),
            @Result(property = "toUserId", column = "to_user_id"),
            @Result(property = "createAt", column = "create_at")
        }
    )
    List<FollowRelation> selectFollowByFromUserId(Long fromUserId);

    @Select("SELECT * FROM follow WHERE to_user_id = #{toUserId}")
    @ResultMap("followRelationMap")
    List<FollowRelation> selectFollowByToUserId(Long toUserId);

    @Select("SELECT * FROM follow WHERE from_user_id = #{fromUserId} AND to_user_id = #{toUserId}")
    Integer selectByFromUserIdAndToUserId(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    // 模糊搜索方法
    @Select("SELECT * FROM follow WHERE from_user_id = #{fromUserId} AND to_user_id IN (SELECT user_id FROM users WHERE nickname LIKE CONCAT('%', #{nickname}, '%'))")
    @ResultMap("followRelationMap")
    List<FollowRelation> fuzzySearchFollowByFromUserIdAndNickname(@Param("fromUserId") Long fromUserId, @Param("nickname") String nickname);

    @Select("SELECT * FROM follow WHERE to_user_id = #{toUserId} AND from_user_id IN (SELECT user_id FROM users WHERE nickname LIKE CONCAT('%', #{nickname}, '%'))")
    @ResultMap("followRelationMap")
    List<FollowRelation> fuzzySearchFollowByToUserIdAndNickname(@Param("toUserId") Long toUserId, @Param("nickname") String nickname);
}
