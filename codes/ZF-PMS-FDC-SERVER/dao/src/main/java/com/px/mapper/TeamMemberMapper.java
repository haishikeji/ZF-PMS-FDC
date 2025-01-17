package com.px.mapper;

import com.px.entity.Team;
import com.px.entity.TeamMember;
import com.px.dao.CustomBaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 团队成员 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-04-07
 */
public interface TeamMemberMapper extends CustomBaseMapper<TeamMember> {

    Team getLeader(@Param("userId") Long userId, @Param("departmentName")String departmentName);
}
