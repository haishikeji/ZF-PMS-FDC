package com.px.service;
import com.px.common.PullDownModel;
import com.px.entity.SortedDepartment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 轮单部门 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-10-31
 */
public interface ISortedDepartmentService extends IService<SortedDepartment> {

        Page<SortedDepartment> selectPage(Page page,SortedDepartment sortedDepartment);

        SortedDepartment detail(Long id);

        Boolean add(SortedDepartment sortedDepartment);

        Boolean update(SortedDepartment sortedDepartment);

        Boolean delete(Long id);

    List<PullDownModel> simpleAll(String businessType);
}
