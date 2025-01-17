package com.px.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.entity.MajorProduction;
import com.px.dao.CustomBaseMapper;
import com.px.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 大中型产品 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-11-06
 */
public interface MajorProductionMapper extends CustomBaseMapper<MajorProduction> {

    List<MajorProduction> getList(@Param("production") MajorProduction majorProduction);

    List<MajorProduction> getPendingList(@Param("production") MajorProduction majorProduction);

    Page<MajorProductionSaveDoneVO> saveDonePage(Page page, @Param("done") MajorProductionSaveDoneVO productionDone,@Param("keyword") String keyword);

    Page<MajorProductionVO> getHouse(Page page, @Param("vo") MajorProductionVO vo,@Param("keyword") String keyword);

    Page<MajorOrderVO> myOrder(Page page, @Param("vo") MajorOrderVO myOrder,@Param("keyword") String keyword);

    /**
     * 我的订单导出
     * @param myOrder 查询参数
     * @param keyword 关键字
     * @return List<MajorOrderVO>
     */
    List<MajorOrderVO> exportMyOrder(@Param("vo") MajorOrderVO myOrder,@Param("keyword") String keyword);

    Page<MajorOrderVO> waitingClaim(Page page, @Param("vo") MajorOrderVO myOrder);

    List<MajorProduction> getMajorProductionAboutFundId(@Param("majorId") Long majorId);

    Page<TaskTodoVO> todoSaveFilePage(Page page, @Param("todoVO") TaskTodoVO todoVO,@Param("keyword") String keyword);

    void updateSaveFileInfo(@Param("productionIds") List<Long> productionIds);

    Page<MajorProductionPerformanceVO> performanceAllotPage(Page page, @Param("vo") MajorProductionPerformanceVO vo,@Param("currentUserId") Long currentUserId);

    ProdValidateVO validateProd(@Param("id") Long id);

    Page<MajorProductionVO> waitingClaimProd(Page page, @Param("vo")MajorProductionVO production);

    void cancelDelivery(@Param("businessId")Long businessId, @Param("businessSubId")String businessSubId, @Param("productionType")String productionType );

    MajorProductionVO getMajorProductionById(@Param("productionId") Long productionId);

    List<String> getProductionNosByMajorId(@Param("majorId") Long majorId);

    Long getSecondCheckId( @Param("businessId")Long businessId, @Param("businessMinId")String businessMinId);

    MajorProduction getEvaluateAmountByProductionNo(@Param("toMergeNumber") String toMergeNumber);
}
