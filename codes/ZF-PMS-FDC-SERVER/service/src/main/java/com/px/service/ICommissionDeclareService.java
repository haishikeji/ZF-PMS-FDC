package com.px.service;
import com.px.dto.CommissionDeclareDTO;
import com.px.entity.CommissionDeclare;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.CommissionDeclareDetailVO;
import com.px.vo.CommissionDeclareTodoCheckVO;
import com.px.vo.CommissionDeclareVO;

import java.util.List;

/**
 * <p>
 * 提成申报 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-05-15
 */
public interface ICommissionDeclareService extends IService<CommissionDeclare> {


    Boolean delete(Long id);

    Page<CommissionDeclareVO> majorMarketDeclarePage(Page page, CommissionDeclareVO commissionDeclare);

    Page<CommissionDeclareVO> majorEvaluateDeclarePage(Page page, CommissionDeclareVO commissionDeclare);

    Long commissionDeclare(CommissionDeclareDTO declareDTO);

    CommissionDeclareDetailVO detail(Long id);

    Page<CommissionDeclareTodoCheckVO> todoCheckPage(Page page, CommissionDeclareTodoCheckVO checkVO);

    List<CommissionDeclareTodoCheckVO> todoCheckList();

    /**
     * 获取资产评估人员提成申报列表
     * @param page 分页参数
     * @param commissionDeclare 查询参数
     * @return Page<CommissionDeclareVO>
     */
    Page<CommissionDeclareVO> assetsEvaluateDeclarePage(Page page, CommissionDeclareVO commissionDeclare);

    /**
     * 资产业务新增绩效分配申报
     * @param declareDTO dto
     * @return Long
     */
    Long assetsCommissionDeclare(CommissionDeclareDTO declareDTO);

    /**
     * 获取资产市场人员提成申报列表
     * @param page 分页参数
     * @param commissionDeclare 查询参数
     * @return Page<CommissionDeclareVO>
     */
    Page<CommissionDeclareVO> assetsMarketDeclarePage(Page page, CommissionDeclareVO commissionDeclare);

    /**
     * 资产待办绩效申报列表
     * @param page 分页参数
     * @param checkVO 查询参数
     * @return Page<CommissionDeclareTodoCheckVO>
     */
    Page<CommissionDeclareTodoCheckVO> assetsTodoCheckPage(Page page, CommissionDeclareTodoCheckVO checkVO);

    /**
     * 资产待办绩效申报集合
     * @return List<CommissionDeclareTodoCheckVO>
     */
    List<CommissionDeclareTodoCheckVO> assetsTodoCheckList();

    /**
     * 更新资产绩效申报提成比列与下限信息
     * @param declareDTO dto
     * @return Boolean
     */
    Boolean updateAssetsCommissionDeclare(CommissionDeclareDTO declareDTO);
}
