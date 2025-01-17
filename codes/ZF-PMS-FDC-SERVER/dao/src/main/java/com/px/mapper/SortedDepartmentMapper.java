package com.px.mapper;

import com.px.common.PullDownModel;
import com.px.entity.SortedDepartment;
import com.px.dao.CustomBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 轮单部门 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-10-31
 */
public interface SortedDepartmentMapper extends CustomBaseMapper<SortedDepartment> {

    List<PullDownModel> simpleAll(@Param("businessType") String businessType);
}
