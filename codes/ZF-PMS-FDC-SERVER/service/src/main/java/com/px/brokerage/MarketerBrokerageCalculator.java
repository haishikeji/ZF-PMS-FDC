package com.px.brokerage;

import com.px.bo.BrokerageDetailBO;
import com.px.bo.LeaderRatioBO;
import com.px.entity.BrokerageVariable;
import com.px.entity.ItemBrokerageDetail;
import com.px.entity.PaymentCollection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 类说明：市场人员提成计算器接口
 *
 * @author: wucl
 * @since: 2023/3/3
 * created with IntelliJ IDEA.
 */
public interface MarketerBrokerageCalculator extends BrokerageCalculator {

    /**
     * 预估提成金额(个人计算)
     */
    BrokerageDetailBO predictPersonalAmount(BrokerageDetailBO bo);

    /**
     * 营销主管个人 提成部分 计算
     */
    BrokerageDetailBO supervisorPersonalAmount(BrokerageDetailBO bo);

    /**
     * 营销经理个人 提成部分 计算
     */
    BrokerageDetailBO managerPersonalAmount(BrokerageDetailBO bo);

    /**
     * 预估提成金额(团队抽成计算)
     */
    Set<ItemBrokerageDetail> predictTeamShareAmount(BrokerageDetailBO bo, Set<LeaderRatioBO> leaderRatioBOSet);

    /**
     * 实际提成金额(团队抽成计算)
     */
    BigDecimal actualTeamShareAmount(ItemBrokerageDetail itemBrokerageDetail);



    /**
     * 营销主管团队提成部分 计算 (按回款金额)
     * @param paymentId 回款id
     * @param paymentAmount 回款金额
     * @param userId 提成用户id
     * @param postRatio 岗位提成率
     * @param ratio 项目提成系数
     * @return
     */
    BrokerageVariable supervisorSettle(Long paymentId,BigDecimal paymentAmount, Long userId , BigDecimal postRatio, BigDecimal ratio);


    /**
     * 市场部部门经理提成计算 (按回款金额)
     * @param paymentId 回款id
     * @param paymentAmount 回款金额
     * @param marketerId 客户经理id
     * @param managerId 部门经理id
     * @param postRatio 岗位提成率
     * @return
     */
    BrokerageVariable managerSettle(Long paymentId,BigDecimal paymentAmount,Long marketerId, Long managerId,BigDecimal postRatio);

    /**
     * 市场人员实际提成金额
     */
    BigDecimal actualAmount(BigDecimal predictAmount);

    /**
     * 市场人员预提金额
     * @return
     */
    BigDecimal aheadAmount(BigDecimal predictAmount);

    /**
     * 营销主管团队提成部分 计算 (按合同金额)
     * @param signAmount 合同签约金额
     * @param userId 提成用户id
     * @param postRatio 岗位提成率
     * @param ratio 项目提成系数
     * @return
     */
    BigDecimal supervisorAmount(BigDecimal signAmount, Long userId ,BigDecimal postRatio, BigDecimal ratio);

    /**
     * 市场部部门经理提成计算 (按合同金额)
     * @param signAmount 合同签约金额
     * @param marketerId 客户经理id
     * @param managerId  营销经理id
     * @param postRatio  岗位提成率
     * @return
     */
    BigDecimal managerAmount(BigDecimal signAmount,Long marketerId, Long managerId,BigDecimal postRatio);


    /**
     * 更新市场部领导提成详情
     * @param list
     * @param bo
     * @return
     */
    List<ItemBrokerageDetail> updateTeamShareAmount(List<ItemBrokerageDetail> list,BrokerageDetailBO bo);

    /**
     * 客户经理单笔回款提成计算
     * @param paymentCollection
     * @param ratio
     * @return
     */
    BigDecimal marketerPaymentAmount(PaymentCollection paymentCollection,BigDecimal ratio);
}
