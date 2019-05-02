package com.bing.webchat.entity;

import java.util.Date;

public class Group {
    private String id;

    private String groupName;

    private String groupIntroduction;

    private Integer groupUserCount;

    private String createBy;

    private Date createTime;

    private String udateBy;

    private Date updateTime;

    private String delFlag;

    private String groupMembers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public String getGroupIntroduction() {
        return groupIntroduction;
    }

    public void setGroupIntroduction(String groupIntroduction) {
        this.groupIntroduction = groupIntroduction == null ? null : groupIntroduction.trim();
    }

    public Integer getGroupUserCount() {
        return groupUserCount;
    }

    public void setGroupUserCount(Integer groupUserCount) {
        this.groupUserCount = groupUserCount;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUdateBy() {
        return udateBy;
    }

    public void setUdateBy(String udateBy) {
        this.udateBy = udateBy == null ? null : udateBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }

    public String getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(String groupMembers) {
        this.groupMembers = groupMembers == null ? null : groupMembers.trim();
    }
}