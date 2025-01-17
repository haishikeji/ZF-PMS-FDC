package com.px.service.impl;

import com.px.brokerage.annotation.DoBrokerage;
import com.px.brokerage.constants.BrokerageRule;
import com.px.common.BaseEntity;
import com.px.dto.PaymentDetailDTO;
import com.px.dto.PaymentExportDTO;
import com.px.entity.PaymentCollection;
import com.px.enums.OperationTypeEnum;
import com.px.exception.ErrorCode;
import com.px.mapper.PaymentCollectionMapper;
import com.px.service.IItemService;
import com.px.service.IPaymentCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.utils.DateUtils;
import com.px.vo.ItemPaymentVO;
import com.px.vo.PaymentCollectionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 回款记录 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-01-10
 */
@Service
public class PaymentCollectionServiceImpl extends ServiceImpl<PaymentCollectionMapper, PaymentCollection> implements IPaymentCollectionService {

    @Autowired
    private PaymentCollectionMapper paymentCollectionMapper;

    @Autowired
    private IItemService iItemService;

    @Override
    @SuppressWarnings("unchecked")
    public Page<PaymentCollectionVO> selectPage(Page page, PaymentCollectionVO paymentCollection){
        if (paymentCollection.getIsCurMonth()){
            paymentCollection.setLastMonth23(DateUtils.getLastMonth23(LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1));
        }
        return paymentCollectionMapper.page(page, paymentCollection);
    }

    /**
     * 回款记录导出
     */
    @Override
    public List<PaymentCollectionVO> exportPaymentCollection(PaymentCollectionVO paymentCollection) {
        if (paymentCollection.getIsCurMonth()){
            paymentCollection.setLastMonth23(DateUtils.getLastMonth23(LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1));
        }
        return paymentCollectionMapper.exportPaymentCollection(paymentCollection);
    }


    @Override
    public PaymentCollection detail(Long id){
        return this.getById(id);
    }

    @DoBrokerage(operation = OperationTypeEnum.SETTLE,rule = BrokerageRule.LAND_MARKETER_RULE)
    @Override
    public Boolean add(PaymentCollection paymentCollection){
        Double amount = paymentCollection.getAmount();
        if (amount==null || amount<0){
            ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR,"抱歉！回款金额不能小于0");
        }
        BigDecimal totalAmount = iItemService.getById(paymentCollection.getItemId()).getAmount();
        if (totalAmount==null){
            ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR,"抱歉！该项目合同金额未填写，请先完善合同金额再操作。");
        }
        List<Double> doneAmount = this.list(new LambdaQueryWrapper<PaymentCollection>()
                .select(PaymentCollection::getAmount)
                .eq(PaymentCollection::getItemId, paymentCollection.getItemId())
                .eq(BaseEntity::getDeleted, Boolean.FALSE)).stream().map(PaymentCollection::getAmount).collect(Collectors.toList());


        BigDecimal sum = BigDecimal.valueOf(doneAmount.stream().reduce(0D, Double::sum));

        BigDecimal payment = BigDecimal.valueOf(amount);
        BigDecimal add = sum.add(payment);
        if (add.compareTo(totalAmount)==1){
            ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR,"新增回款金额已大于合同签订金额，请检查.");
        }
        return this.save(paymentCollection);
    }

    @Override
    public Boolean update(PaymentCollection paymentCollection){
        return  this.updateById(paymentCollection);
    }
    @DoBrokerage(operation = OperationTypeEnum.SETTLE,rule = BrokerageRule.LAND_MARKETER_RULE)
    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public IPage<ItemPaymentVO> getItemPayment(Page page,PaymentCollectionVO paymentCollectionVO) {
        IPage<ItemPaymentVO> result = paymentCollectionMapper.getItemPayment(page,paymentCollectionVO);
        return result;
    }

    @Override
    public List<PaymentDetailDTO> getPaymentDetailListByItemId(Long id,Long userId) {
        List<PaymentDetailDTO> list = paymentCollectionMapper.getPaymentDetailListByItemId(id,userId);
        return list;
    }

    @Override
    public List<PaymentExportDTO> getList(PaymentCollectionVO paymentCollectionVO) {
        List<PaymentExportDTO> list = paymentCollectionMapper.getList(paymentCollectionVO);
        return list;
    }
}
