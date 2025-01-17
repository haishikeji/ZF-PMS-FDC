package com.px.aspect;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.annotation.DataPermission;
import com.px.bo.SimpleParentModel;
import com.px.common.BaseEntity;
import com.px.common.SimpleListModel;
import com.px.dto.SimplePostModel;
import com.px.entity.Team;
import com.px.entity.TeamMember;
import com.px.service.*;
import com.px.utils.LoginContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.px.common.Constants.MARKET_MANAGER;
import static com.px.common.Constants.SUPERVISOR;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/2/13
 * created with IntelliJ IDEA.
 */

@Slf4j
@Component
@Aspect
public class DataPermissionAop {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPostService postService;

    @Autowired
    private ITeamMemberService teamMemberService;

    @Autowired
    private IUserPostService userPostService;

    @Autowired
    private ITeamService teamService;

    @Pointcut("@annotation(com.px.annotation.DataPermission)")
    public void dataPermissionPointcut(){

    }

    @Around("dataPermissionPointcut() && @annotation(dataPermission)")
    public Object  beforeHandle(ProceedingJoinPoint joinPoint, DataPermission dataPermission) throws Throwable {

        Object arg = joinPoint.getArgs()[0];
        List<SimpleParentModel> departmentList = LoginContext.getLoginCacheUserBO().getDepartmentList();
        List<String> departmentNames = departmentList.stream().map(SimpleListModel::getName).collect(Collectors.toList());
        List<SimplePostModel> postList = LoginContext.getLoginCacheUserBO().getPostList();
        Long userId = LoginContext.getUserId();

        List<String> ignores = Arrays.asList(dataPermission.ignoreDepartment());
        if (!CollectionUtils.isEmpty(ignores) && !Collections.disjoint(departmentNames,ignores)){
            return joinPoint.proceed();
        }

        if (dataPermission.department()){
            //部门内部最高岗位拥有该部门最高数据权限，否则只有登录人参与的项目的数据权限
            List<Long> dIds = new ArrayList<>();
            Set<Long> users = new HashSet<>();
            for (SimplePostModel post : postList){
                if (postService.mostHighPost(post)){
                    dIds.add(post.getDepartmentId());
                }else {
                    users.add(LoginContext.getUserId());
                }
            }

            Field departments = arg.getClass().getDeclaredField("departmentIds");
            departments.setAccessible(Boolean.TRUE);
            departments.set(arg,dIds);
            Field userIds = arg.getClass().getDeclaredField("userIds");
            userIds.setAccessible(Boolean.TRUE);
            userIds.set(arg,users);
        }

        if (dataPermission.postChild()){
            Set<Long> users = new HashSet<>();
            //根据当前登录人获取下属员工id
            users = userService.getJuniorUserId(userId);
            users.add(userId);
            Field userIds = arg.getClass().getDeclaredField("userIds");
            userIds.setAccessible(Boolean.TRUE);
            userIds.set(arg,users);
        }
        else if(dataPermission.teamData()){
            Set<Long> users = new HashSet<>();
            SimplePostModel postModel = userPostService.getMarketPostList(userId);
            if (postModel.getName().equals(SUPERVISOR)){
                Team team = teamService.getOne(new LambdaQueryWrapper<Team>().eq(Team::getSupervisorId, userId).eq(BaseEntity::getDeleted, Boolean.FALSE));
                if (team!=null){
                    Long teamId = team.getId();
                    users = teamMemberService.list(new LambdaQueryWrapper<TeamMember>()
                            .eq(TeamMember::getTeamId,teamId).eq(BaseEntity::getDeleted,Boolean.FALSE)).stream().map(TeamMember::getMemberId).collect(Collectors.toSet());
                }
                else {
                    users.add(userId);
                }
            }
            else if (postModel.getName().equals(MARKET_MANAGER)){
                users = userService.getJuniorUserId(userId);
                users.add(userId);

            }else {
                users.add(userId);
            }
            Field userIds = arg.getClass().getDeclaredField("userIds");
            userIds.setAccessible(Boolean.TRUE);
            userIds.set(arg,users);
        }
        else {
            Set<Long> users = new HashSet<>();
            users.add(userId);
            Field userIds = arg.getClass().getDeclaredField("userIds");
            userIds.setAccessible(Boolean.TRUE);
            userIds.set(arg,users);
        }

        return joinPoint.proceed();

    }
}
