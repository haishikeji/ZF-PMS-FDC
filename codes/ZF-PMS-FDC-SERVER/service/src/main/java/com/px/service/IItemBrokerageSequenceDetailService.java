package com.px.service;
import com.px.entity.ItemBrokerageSequenceDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.BrokerageSequenceDetailVO;

import java.util.List;

/**
 * <p>
 * 提成流水明细表 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-03
 */
public interface IItemBrokerageSequenceDetailService extends IService<ItemBrokerageSequenceDetail> {

        Page<BrokerageSequenceDetailVO> selectPage(Page page, BrokerageSequenceDetailVO brokerageSequenceDetailVO);

        ItemBrokerageSequenceDetail detail(Long id);

        Boolean add(ItemBrokerageSequenceDetail itemBrokerageSequenceDetail);

        Boolean update(ItemBrokerageSequenceDetail itemBrokerageSequenceDetail);

        Boolean delete(Long id);

    List<BrokerageSequenceDetailVO> xList(BrokerageSequenceDetailVO brokerageSequenceDetailVO);
}
