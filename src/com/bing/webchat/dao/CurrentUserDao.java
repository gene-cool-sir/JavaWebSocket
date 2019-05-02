package com.bing.webchat.dao;

import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.entity.CurrentUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CurrentUserDao {
    int countByExample(CurrentUserExample example);

    int deleteByExample(CurrentUserExample example);

    int deleteByPrimaryKey(String id);

    int insert(CurrentUser record);

    int insertSelective(CurrentUser record);

    List<CurrentUser> selectByExample(CurrentUserExample example);

    CurrentUser selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") CurrentUser record, @Param("example") CurrentUserExample example);

    int updateByExample(@Param("record") CurrentUser record, @Param("example") CurrentUserExample example);

    int updateByPrimaryKeySelective(CurrentUser record);

    int updateByPrimaryKey(CurrentUser record);
}