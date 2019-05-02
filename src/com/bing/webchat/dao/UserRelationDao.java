package com.bing.webchat.dao;

import com.bing.webchat.entity.UserRelation;
import com.bing.webchat.entity.UserRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserRelationDao {
    int countByExample(UserRelationExample example);

    int deleteByExample(UserRelationExample example);

    int deleteByPrimaryKey(String id);

    int insert(UserRelation record);

    int insertSelective(UserRelation record);

    List<UserRelation> selectByExample(UserRelationExample example);

    UserRelation selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") UserRelation record, @Param("example") UserRelationExample example);

    int updateByExample(@Param("record") UserRelation record, @Param("example") UserRelationExample example);

    int updateByPrimaryKeySelective(UserRelation record);

    int updateByPrimaryKey(UserRelation record);

	List<UserRelation> selectUserRelationByUserId(@Param("userId")String userId);
}