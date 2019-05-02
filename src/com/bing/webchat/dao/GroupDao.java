package com.bing.webchat.dao;

import com.bing.webchat.entity.Group;
import com.bing.webchat.entity.GroupExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GroupDao {
    int countByExample(GroupExample example);

    int deleteByExample(GroupExample example);

    int insert(Group record);

    int insertSelective(Group record);

    List<Group> selectByExampleWithBLOBs(GroupExample example);

    List<Group> selectByExample(GroupExample example);

    int updateByExampleSelective(@Param("record") Group record, @Param("example") GroupExample example);

    int updateByExampleWithBLOBs(@Param("record") Group record, @Param("example") GroupExample example);

    int updateByExample(@Param("record") Group record, @Param("example") GroupExample example);
}