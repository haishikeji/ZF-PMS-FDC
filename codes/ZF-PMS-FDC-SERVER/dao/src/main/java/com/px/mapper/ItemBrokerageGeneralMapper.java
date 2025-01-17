package com.px.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.entity.ItemBrokerageGeneral;
import com.px.dao.CustomBaseMapper;
import com.px.vo.BrokerageGeneralVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 项目提成总表 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-03
 */
public interface ItemBrokerageGeneralMapper extends CustomBaseMapper<ItemBrokerageGeneral> {

    Page<BrokerageGeneralVO> aheadPage(Page page, @Param("itemBrokerageGeneral") BrokerageGeneralVO itemBrokerageGeneral);

    ItemBrokerageGeneral getByItemId(@Param("itemId") Long itemId);

    List<BrokerageGeneralVO> xList(@Param("itemBrokerageGeneral") BrokerageGeneralVO itemBrokerageGeneral);
}
