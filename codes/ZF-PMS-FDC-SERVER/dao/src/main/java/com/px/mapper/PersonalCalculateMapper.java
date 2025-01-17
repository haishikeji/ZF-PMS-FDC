package com.px.mapper;

import com.px.entity.PersonalCalculate;
import com.px.dao.CustomBaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 个贷可比实例算价表 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-01
 */
public interface PersonalCalculateMapper extends CustomBaseMapper<PersonalCalculate> {

    void realDelete(@Param("id") Long id);

    PersonalCalculate getByTargetId(@Param("targetId") Long targetId);
}
