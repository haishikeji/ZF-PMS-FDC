package com.px.mapper;

import com.px.dto.ProductionDTO;
import com.px.entity.PersonalProduction;
import com.px.dao.CustomBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 个贷产品 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-18
 */
public interface PersonalProductionMapper extends CustomBaseMapper<PersonalProduction> {

    List<ProductionDTO> getProductions(@Param("id") Long id);
}
