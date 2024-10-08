package com.example.hellospring.mapper;

import com.example.hellospring.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from users")
    List<User> selectAll();

    @Select("select nickname from users where users.user_id = #{uid}")
    String getNicknameByUId(Long uid);

    int insert(User record);

    User login(@Param("userName") String userName, @Param("password") String password);

    User selectByPrimaryKey(Long id);

    // 检查用户名或邮箱是否已存在
    int checkUsernameOrEmail(@Param("userName") String userName, @Param("email") String email);

    @Select("select count(*) from users where users.email = #{email}")
    int checkEmail(@Param("email") String email);

    int updateByPrimaryKey(User record);

    int updateByPrimaryKeySelective(User record);

    User getUserById(Long userId);
    int updateName(@Param("userId") Long userId, @Param("loginUserName") String loginUserName, @Param("nickName") String nickName);
    int updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    List<User> getTopUsersByBlogCount();

    @Select("select user_id from users where users.email = #{email}")
    Long getUserIdByEmail(@Param("email") String email);

//    User findByEmail(@Param("email") String email);
//    void retrievePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);
}
