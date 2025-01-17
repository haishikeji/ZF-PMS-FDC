package com.px.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.entity.Department;
import com.px.dao.CustomBaseMapper;
import com.px.vo.DepartmentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 机构 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2022-11-29
 */
public interface DepartmentMapper extends CustomBaseMapper<Department> {

    Page<DepartmentVO> xPage(@Param("department") Department department, Page page);

    Page<DepartmentVO> getPage(Page page, @Param("department") Department department);

    /**
     * 根据业务code获取接单部门
     * @param businessCode 业务code
     * @return List<Department>
     */
    List<Department> getAllotDepartmentByBusiness(@Param("businessCode") String businessCode);

    /**
     * 通过用户id获取部门信息
     * @param userId 用户id
     * @return Long 下单部门id
     */
    Long getDepartmentByUserId(@Param("userId") Long userId);

    /**
     * 根据部门id获取部门及其下属部门的id集合
     * @param depId 部门id
     * @return List<Long>
     */
    List<Long> getDepIdList(@Param("depId") Long depId);
}
