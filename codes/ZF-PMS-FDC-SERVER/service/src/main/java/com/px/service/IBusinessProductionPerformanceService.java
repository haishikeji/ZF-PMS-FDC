package com.px.service;

import com.px.dto.TaskRecordDTO;
import com.px.entity.BusinessProductionPerformance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.BusinessProductionPerformanceVO;

/**
 * <p>
 * 业务产品绩效分配 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-11-07
 */
public interface IBusinessProductionPerformanceService extends IService<BusinessProductionPerformance> {

    Page<BusinessProductionPerformance> selectPage(Page page, BusinessProductionPerformance businessProductionPerformance);

    BusinessProductionPerformance detail(Long id);

    Boolean add(TaskRecordDTO<BusinessProductionPerformance> taskRecordDTO);

    Boolean update(TaskRecordDTO<BusinessProductionPerformance> businessProductionPerformance);

    Boolean delete(Long id);

    BusinessProductionPerformanceVO getDetail(BusinessProductionPerformanceVO performance);

    /**
     * 根据业务类型,产品id及checkLoop获取质检信息
     * @param businessProductionPerformance 查询条件
     * @return BusinessProductionPerformance
     */
    BusinessProductionPerformance getProductionPerformance(BusinessProductionPerformance businessProductionPerformance);
}
