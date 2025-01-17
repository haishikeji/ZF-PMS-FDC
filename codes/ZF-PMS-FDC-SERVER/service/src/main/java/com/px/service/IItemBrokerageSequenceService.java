package com.px.service;
import com.px.dto.DeductionDTO;
import com.px.entity.ItemBrokerageSequence;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.entity.ItemBrokerageTechnicist;
import com.px.vo.BrokerageSequenceVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 项目提成流水 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-03
 */
public interface IItemBrokerageSequenceService extends IService<ItemBrokerageSequence> {

        Page<BrokerageSequenceVO> selectPage(Page page, BrokerageSequenceVO brokerageSequenceVO);

        ItemBrokerageSequence detail(Long id);

        Boolean add(ItemBrokerageSequence itemBrokerageSequence);

        Boolean update(ItemBrokerageSequence itemBrokerageSequence);

        Boolean delete(Long id);

        Set<Long> doNonMarketerAheadSettle();

        Set<Long> doAbleItemBrokerageSettle();

        List<BrokerageSequenceVO> deduction(BrokerageSequenceVO brokerageSequenceVO);

        List<BrokerageSequenceVO> xList(BrokerageSequenceVO brokerageSequenceVO);

        void deduction(List<ItemBrokerageTechnicist> itemBrokerages, List<DeductionDTO> deductionDTOS);

        void extracted(BigDecimal defaultAmount, ItemBrokerageSequence defaultSequence, List<ItemBrokerageTechnicist> deductionAble);

}
