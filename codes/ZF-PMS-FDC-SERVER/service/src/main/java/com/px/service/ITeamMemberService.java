package com.px.service;
import com.px.bo.LeaderRatioBO;
import com.px.entity.TeamMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.TeamVO;

import java.util.Set;

/**
 * <p>
 * 团队成员 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-04-07
 */
public interface ITeamMemberService extends IService<TeamMember> {

        Page<TeamMember> selectPage(Page page,TeamMember teamMember);

        TeamMember detail(Long id);

        Boolean add(TeamVO teamVO);

        Boolean update(TeamMember teamMember);

        Boolean delete(Long id);

        Set<LeaderRatioBO> getLeader(Long userId,String departmentName);

        Set<Long> getJuniorUserIds(Long userId);

}
