package com.px.service;
import com.px.entity.AssetsEvaluationTarget;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.AssetsEvaluationTargetVO;

import java.util.List;

/**
 * <p>
 * 资产评估对象表 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-28
 */
public interface IAssetsEvaluationTargetService extends IService<AssetsEvaluationTarget> {

        /**
         * 根据资产业务id获取资产业务评估对象集合
         * @param id 资产业务id
         * @return List<AssetsEvaluationTarget>
         */
        List<AssetsEvaluationTargetVO> getAETargetListByAssetsId(Long id, String statementNo, String reportNo);

        Page<AssetsEvaluationTarget> selectPage(Page page,AssetsEvaluationTarget assetsEvaluationTarget);

        AssetsEvaluationTarget detail(Long id);

        Boolean add(AssetsEvaluationTarget assetsEvaluationTarget);

        Boolean update(AssetsEvaluationTarget assetsEvaluationTarget);

        Boolean delete(Long id);

        /**
         * 评估对象审核定价
         * @param assetsEvaluationTarget 审核定价信息
         * @return Boolean
         */
        Boolean checkValue(AssetsEvaluationTarget assetsEvaluationTarget);

}
