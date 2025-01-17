package com.px.service;
import com.px.bo.LeaderRatioBO;
import com.px.bo.PrivilegeTreeBO;
import com.px.common.PullDownModel;
import com.px.condition.LoginCondition;
import com.px.dto.LoginDTO;
import com.px.dto.UpdatePasswordDTO;
import com.px.dto.UserBaseDetailDTO;
import com.px.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.DepartmentUserVO;
import com.px.vo.IdNameVO;
import com.px.vo.UserVO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 基础用户 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2022-11-29
 */
public interface IUserService extends IService<User> {

        Page<User> selectPage(Page page,User user);

        UserVO detail(Long id);

        Boolean add(User user);

        Boolean update(User user);

        Boolean delete(Long id);

        XSSFWorkbook generateImportTemplate();

        void importExcel(MultipartFile file);

        XSSFWorkbook exportExcel(User user);

        LoginDTO login(LoginCondition loginCondition);

        UserBaseDetailDTO baseInfo(String token);

        Set<PrivilegeTreeBO> privilegesList(String token);

        Set<PrivilegeTreeBO> privilegesTree(String token);

    void saveOrUpdateById(User user);

        Page<UserVO> xPage(Page page, UserVO userVO);

    Boolean xUpdate(UserVO user);

    Boolean xSave(UserVO user);

    Boolean restPassword(Long id);

    Boolean updatePassword(UpdatePasswordDTO passwordDTO);

    Set<Long> getJuniorUserId(Long userId);

    Set<LeaderRatioBO> getLeaderRatioBO(Long userId);

    Set<Long> getLeaderPostInDepartment(Long postId,Long departmentId,Set<Long> parentPostIds);

    Set<Long> getDepartmentIdByUserId(Long userId);

    List<User> simplePostName(String postName);

    List<UserVO> listPostName(String postName);

    Set<LeaderRatioBO> getLeaderRatioBO(Set<Long> leaderIds);

    Set<LeaderRatioBO> marketManagerRatioBO();

    List<IdNameVO> usersByDepartment(String departmentName);

    Boolean resetSettlePassword(Long id);

    Boolean updateSettlePassword(UpdatePasswordDTO passwordDTO);

    Boolean validSettlePassword(String settlePassword);


    List<IdNameVO> usersByDepartments(List<String> departmentNames);

    /**
     * 当前操作人的上级领导
     * @return
     */
    Long getLeaderByCurrentUser();

    List<User> byDepartmentId(Long id);

    List<PullDownModel> allotDepartmentUsers(String businessType);

    List<User> infoPostName(String postName);

    List<DepartmentUserVO> departmentUsers();

    /**
     * 获取资产评估师用户
     * @return List<User>
     */
    List<User> byAssetEvaluator();

    /**
     * 根据部门名称获取部门及其下辖部门人员
     * @param depName 部门
     * @return List<User>
     */
    List<User> byDepartmentName(String depName);

    Boolean notRemind();
}
