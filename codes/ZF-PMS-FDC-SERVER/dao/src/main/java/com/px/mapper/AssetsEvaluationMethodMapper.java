package com.px.mapper;

import com.px.entity.AssetsEvaluationMethod;
import com.px.dao.CustomBaseMapper;

import java.util.List;

/**
 * <p>
 * 资产评估方法表 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2024-04-07
 */
public interface AssetsEvaluationMethodMapper extends CustomBaseMapper<AssetsEvaluationMethod> {

    /**
     * 获取所有评估方法集合
     * @return List<AssetsEvaluationMethod>
     */
    List<AssetsEvaluationMethod> getAssetsEvaluationMethod();

}
