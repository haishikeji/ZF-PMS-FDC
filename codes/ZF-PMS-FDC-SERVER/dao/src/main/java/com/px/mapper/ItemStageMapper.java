package com.px.mapper;

import com.px.entity.ItemStage;
import com.px.dao.CustomBaseMapper;
import com.px.vo.ItemStageVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 项目阶段表 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2022-12-08
 */
public interface ItemStageMapper extends CustomBaseMapper<ItemStage> {

    List<ItemStageVO> listByItemId(@Param("itemId") Long itemId);

    int checkDate(@Param("id") Long id,@Param("itemId") Long itemId,@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
