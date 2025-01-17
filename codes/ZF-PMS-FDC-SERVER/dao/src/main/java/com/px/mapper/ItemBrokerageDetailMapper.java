package com.px.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.entity.ItemBrokerageDetail;
import com.px.dao.CustomBaseMapper;
import com.px.vo.BrokerageDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 项目提成明细 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-03
 */
public interface ItemBrokerageDetailMapper extends CustomBaseMapper<ItemBrokerageDetail> {

    Page getPage(Page page, @Param("vo") BrokerageDetailVO brokerageDetailVO);

    List<BrokerageDetailVO> xList( @Param("vo") BrokerageDetailVO brokerageDetailVO);

    List<ItemBrokerageDetail> getDetailForMarketer(@Param("list") List<Long> list);
}
