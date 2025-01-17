package com.px.processor;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.bo.PrivilegeTreeBO;
import com.px.common.BaseEntity;
import com.px.configuration.AsyncManager;
import com.px.entity.*;
import com.px.enums.SexEnum;
import com.px.service.*;
import com.px.utils.DigestUtils;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.px.common.Constants.*;

/**
 * 类说明：应用启动时自定义执行的方法
 *
 * @author: wucl
 * @since: 2022/11/30
 * created with IntelliJ IDEA.
 */
@Component
@Getter
@Slf4j
public class AppBootProcessor implements BeanPostProcessor, CommandLineRunner {

    @Autowired
    private IPostService postService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IPostPrivilegeService postPrivilegeService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserPostService userPostService;

    @Autowired
    private IBrokerageMarketerRatioService brokerageMarketerRatioService;

    @Autowired
    private IBrokerageBaseAmountService brokerageBaseAmountService;


    @Autowired
    private IGlobalConfigService globalConfigService;

    @Autowired
    private ILuceneSearchService luceneSearchService;


    private Gson gson = new Gson();

    @Transactional(rollbackFor = Exception.class)
    @Order(value = 1)
    @Override
    public void run(String... args) throws Exception {
        log.info("【initialize admin begin...】");
        initializeAdmin();
        loadBrokerageData();
        AsyncManager.me().execute(()->{
            luceneSearchService.initializeIndex();
        });

    }


    private void initializeAdmin() {
        //初始化超级管理员岗位
        Post superAdminPost = new Post();
        superAdminPost.setId(superAdminId);
        superAdminPost.setName("超级管理员岗");
        postService.upsert(superAdminPost);

        List<Menu> menus = menuService.list(new LambdaQueryWrapper<Menu>().eq(BaseEntity::getDeleted, false));

        LambdaQueryWrapper<PostPrivilege> postPrivilegeLambdaQueryWrapper = new LambdaQueryWrapper<PostPrivilege>()
                .eq(PostPrivilege::getPostId, superAdminId);
        PostPrivilege postPrivilege = postPrivilegeService.getOne(postPrivilegeLambdaQueryWrapper);
        Set<PrivilegeTreeBO> allPrivilegeSet = Sets.newHashSet();
        if (postPrivilege != null) {
            //如果已存在则进行合并，只把配置中新增的加进去
            String permissionTree = postPrivilege.getPermissionTree();
            Set<PrivilegeTreeBO> existSet = gson.fromJson(permissionTree, new TypeToken<Set<PrivilegeTreeBO>>() {
            }.getType());
            allPrivilegeSet.addAll(existSet);
        }
        //初始化超级管理员岗位权限
        Set<PrivilegeTreeBO> configPrivilegeSet = menus.stream().map(x -> {
            PrivilegeTreeBO privilegeTreeBO = BeanUtil.copyProperties(x, PrivilegeTreeBO.class);
            privilegeTreeBO.setChecked(true);
            return privilegeTreeBO;
        }).collect(Collectors.toSet());
        //将配置的权限加进去，只有新增的才会被加进去，已有的不会被覆盖
        allPrivilegeSet.addAll(configPrivilegeSet);

        String permissionTree = gson.toJson(allPrivilegeSet, new TypeToken<Set<PrivilegeTreeBO>>() {
        }.getType());
        postPrivilege = new PostPrivilege();
        postPrivilege.setPostId(superAdminId);
        postPrivilege.setPermissionTree(permissionTree);
        postPrivilegeService.saveOrUpdateById(postPrivilege);

        //初始化超级管理员
        User user = new User();
        user.setId(superAdminId);
        user.setName(ADMIN);
        user.setStaffNo(ADMIN_STAFF_NO);
        user.setPassword(DigestUtils.getMD5String(ADMIN_PASSWORD));
        user.setUserType("正式员工");
        user.setSex(SexEnum.MAN.getSex());
        user.setPasswordState(Boolean.FALSE);
        userService.saveOrUpdateById(user);
        //为超级管理员分配超级管理员岗位
        UserPost userPost = new UserPost();
        userPost.setId(superAdminId);
        userPost.setPostId(superAdminId);
        userPost.setUserId(superAdminId);
        userPostService.saveOrUpdateById(userPost);

    }


    private void loadBrokerageData(){
        brokerageMarketerRatioService.cacheMarketerRatio();
        brokerageBaseAmountService.cacheBaseAmount();
    }

}
