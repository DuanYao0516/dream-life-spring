package com.example.hellospring.entity.vo;

import java.io.Serializable;
import java.util.Date;

public class FollowListVO implements Serializable {
    private Long userId;
    private Long toUserId;
    private String nickname;
    private Date createAt;

    public FollowListVO(Long userId, Long toUserId, String nicknameByUId, Date createAt) {
        this.userId = userId;
        this.toUserId = toUserId;
        this.nickname = nicknameByUId;
        this.createAt = createAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "FollowListVO{" +
                "userId=" + userId +
                ", toUserId=" + toUserId +
                ", nickname='" + nickname + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
