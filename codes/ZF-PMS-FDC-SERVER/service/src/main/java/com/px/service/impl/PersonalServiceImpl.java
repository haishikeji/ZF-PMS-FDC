package com.px.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.common.BaseEntity;
import com.px.dto.*;
import com.px.entity.*;
import com.px.enums.CertificateEnum;
import com.px.enums.GlobalConfigEnum;
import com.px.exception.ErrorCode;
import com.px.mapper.*;
import com.px.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.service.workflow.IWorkFlowNodeInstanceService;
import com.px.service.workflow.IWorkFlowService;
import com.px.utils.LoginContext;
import com.px.vo.PersonalVO;
import com.px.vo.TaskTodoVO;
import com.px.workflow.handler.PersonalNodeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import static com.px.common.Constants.OUTWARD_TO_PRICE;
import static com.px.common.Constants.PERSONAL_INWARD;
import static com.px.enums.MainBusinessEnum.PERSONAL_BUSINESS;
import static com.px.enums.workflow.NodeLogEnum.PASS;
import static com.px.enums.workflow.NodeStateEnum.PENDING;

/**
 * <p>
 * 个贷业务订单 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-01
 */
@DS("mbs")
@Service
public class PersonalServiceImpl extends ServiceImpl<PersonalMapper, Personal> implements IPersonalService {

    @Autowired
    private PersonalMapper personalMapper;

    @Autowired
    private IBusinessNumberService businessNumberService;

    @Autowired
    private IWorkFlowService workFlowService;

    @Autowired
    private IUserService userService;
    
    @Autowired
    private IGlobalConfigService globalConfigService;

    @Autowired
    private IPersonalTargetService personalTargetService;

    @Autowired
    private IWorkFlowNodeInstanceService workFlowNodeInstanceService;

    @Autowired
    private WorkTaskRecordMapper workTaskRecordMapper;

    @Autowired
    private WorkFlowNodeInstanceMapper workFlowNodeInstanceMapper;

    @Autowired
     private WorkFlowLogMapper workFlowLogMapper;

    @Autowired
    private IPersonalService personalService;

    @Autowired
    private IFinanceClaimService financeClaimService;

    @Autowired
    private IOrderFundService orderFundService;

    @Autowired
    private OrderFundMapper orderFundMapper;


    @Autowired
    private IBusinessProductionService businessProductionService;

    @Autowired
    private PersonalNodeHandler personalNodeHandler;

    @Autowired
    private IDictDataService dictDataService;


    @Override
    @SuppressWarnings("unchecked")
    public Page<PersonalVO> selectPage(Page page, PersonalVO personal,String keyword){
        Page<PersonalVO> pages = personalMapper.xSelectPage(page,personal,keyword);
        return pages;
    }


    @Override
    public PersonalVO detail(Long id){
        return personalMapper.getDetail(id);
    }

    @Transactional
    @Override
    public WorkNodeCommit add(Personal personal){
        personal.setOrderId(businessNumberService.takeOrderNumber());
        //确定内业人员
        //下单时暂不确定内业人员，客户经理报价反馈后再确定。否则会有内业轮空。
        //personal.setInwardStaff(nextInwardStaff());
        //确定定价内业人员
        //下单时暂不确定定价内业，需要分单完成，由外业人员确定定价内业
        //personal.setPricingStaff(aimPriceStaff());
        this.save(personal);
        //创建估价对象
        PersonalTarget pt = new PersonalTarget();
        pt.setPersonalId(personal.getId());
        pt.setLocation(personal.getLocation());
        //生成出入库二维码
        pt.setQrCode(businessProductionService.doCreateNormalCode(null,personal.getOrderId()));
        //生成验证二维码
        pt.setValidateCode(businessProductionService.doCreateValidateCode(PERSONAL_BUSINESS, personal.getId(), personal.getOrderId()));
        personalTargetService.save(pt);
        //个贷下单
        return workFlowService.openingOrder(PERSONAL_BUSINESS,personal.getId(),personal.getOrderId(),personal.getRemark());
    }

    @Override
    public Boolean update(Personal personal){
        return  this.updateById(personal);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public Boolean allotOutwardStaff(TaskRecordDTO<Personal> taskRecordDTO) {
        Personal personal = taskRecordDTO.getTaskData();
        Long outwardStaff = personal.getOutwardStaff();
        Long priceStaffId = this.fixationPriceStaff(outwardStaff);
        return this.update(new LambdaUpdateWrapper<Personal>()
                .set(Personal::getOutwardStaff, outwardStaff)
                .set(Personal::getPricingStaff,priceStaffId)
                .eq(BaseEntity::getId,personal.getId()));
    }

    @Override
    public void updateCertificates(Long targetId, CertificateEnum cer,String location) {
        Personal personal = personalMapper.getByTargetId(targetId);
        //更新订单上面的location
        if (StrUtil.isNotBlank(location)){
            personalService.update(new LambdaUpdateWrapper<Personal>().set(Personal::getLocation,location).eq(BaseEntity::getId,personal.getId()));
        }
        if (personal!=null && StrUtil.isNotEmpty(personal.getCredentials())){
            List<String> credentials = JSON.parseArray(personal.getCredentials(), String.class);
            if (!credentials.contains(cer.name())){
                credentials.add(cer.name());
                this.update(new LambdaUpdateWrapper<Personal>().set(Personal::getCredentials,JSON.toJSONString(credentials)).eq(BaseEntity::getId,personal.getId()));
            }
        }
    }

    @Override
    public Boolean doRecall(Long id) {
        List<WorkFlowNodeInstance> exists = workFlowNodeInstanceService.list(new LambdaQueryWrapper<WorkFlowNodeInstance>()
                .eq(WorkFlowNodeInstance::getBusinessType, PERSONAL_BUSINESS.name())
                .eq(WorkFlowNodeInstance::getBusinessId, id)
                .eq(BaseEntity::getDeleted, Boolean.FALSE));
        if (exists.size()>2){
            ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR,"业务部已分单，无法撤回。若需撤回请联系当前节点待办人退回流程。");
            return Boolean.FALSE;
        }else{
            try {
                for (WorkFlowNodeInstance workFlowNodeInstance: exists){
                    if (workFlowNodeInstance.getSequence().equals(0)){
                        workFlowNodeInstance.setState(PENDING.name());
                        workFlowNodeInstanceService.updateById(workFlowNodeInstance);
                    }else {
                        workFlowNodeInstanceService.removeById(workFlowNodeInstance.getId());
                    }
                }
            } catch (Exception e) {
                ErrorCode.throwBusinessException(ErrorCode.DATA_NOT_EXISTS);
            }
            return Boolean.TRUE;
        }
    }

    /**
     * (就是我的订单，我的订单认领实收款无限制)
     * @param page
     * @param personalVO
     * @return
     */
    @Override
    public Page<PersonalVO> waitingClaimOrder(Page page, PersonalVO personalVO,String keyword) {
        personalVO.setClientManagerId(LoginContext.getCurrentUserId());
        Page<PersonalVO> pages = personalMapper.myOrder(page,personalVO,keyword);
        return pages;
    }

    @Override
    public Page<PersonalVO> waitingSaveFile(Page page, PersonalVO personal,String keyword) {
        Page<PersonalVO> pages = personalMapper.waitingSaveFile(page,personal,keyword);
        return pages;
    }

    @Transactional
    @Override
    public Boolean batchSaveFile(List<TaskTodoVO> todoVOList) {
        if (CollectionUtil.isNotEmpty(todoVOList)){
            try {
                //1.完成任务记录
                List<Long> taskIds = todoVOList.stream().map(TaskTodoVO::getTaskId).collect(Collectors.toList());
                workTaskRecordMapper.batchFinishTask(taskIds);
                //2.提交节点状态
                List<Long> currentNodeIds = todoVOList.stream().map(TaskTodoVO::getCurrentNodeId).collect(Collectors.toList());
                workFlowNodeInstanceMapper.batchCommitNode(currentNodeIds);
                //3.更新产品归档信息
                List<Long> personalIds = todoVOList.stream().map(TaskTodoVO::getBusinessId).collect(Collectors.toList());
                personalMapper.updateSaveFileInfo(personalIds);
                //4.添加日志
                Long currentUserId = LoginContext.getCurrentUserId();
                List<WorkFlowLog> workFlowLogs = todoVOList.stream().map(x -> {
                    WorkFlowLog log = new WorkFlowLog();
                    log.setBusinessId(x.getBusinessId());
                    log.setBusinessType(PERSONAL_BUSINESS.name());
                    log.setHandingTime(new Date().getTime() - x.getTaskCreated().getTime());
                    log.setHandlerId(currentUserId);
                    log.setState(PASS.name());
                    log.setInstanceId(x.getCurrentNodeId());
                    log.setBusinessSubId(x.getOrderId());
                    return log;
                }).collect(Collectors.toList());
                workFlowLogMapper.insertBatch(workFlowLogs);
                return Boolean.TRUE;
            } catch (Exception e) {
                ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR,"归档数据错误，请联系系统管理员。");
                return Boolean.FALSE;
            }
        }
        ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR,"归档项目为空，请检查归档项目。");
        return Boolean.FALSE;
    }

    @Override
    public Page<PersonalVO> saveFileDone(Page page, PersonalVO personal,String keyword) {
        Page<PersonalVO> pages = personalMapper.saveFileDone(page,personal,keyword);
        return pages;
    }

    @Override
    public Page<PersonalVO> myOrder(Page page, PersonalVO personal,String keyword) {
        personal.setClientManagerId(LoginContext.getCurrentUserId());
        Page<PersonalVO> pages = personalMapper.myOrder(page,personal,keyword);
        return pages;
    }

    public List<PersonalVO> exportMyOrder(PersonalVO personal,String keyword) {
        personal.setClientManagerId(LoginContext.getCurrentUserId());
        return personalMapper.exportMyOrder(personal, keyword);
    }

    /**
     * 确定内业人员
     * @return
     */
    public synchronized Long nextInwardStaff(){
        GlobalConfig inwardStaffRoll = globalConfigService.getGlobalConfig(GlobalConfigEnum.DEPARTMENT_TURNS.getCode(), GlobalConfigEnum.DepartmentTurns.INWARD_STAFF_ROLL.getCode());
        List<User> users = userService.simplePostName(PERSONAL_INWARD);
        if (CollectionUtil.isEmpty(users)){
            ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR,"未配置内业人员，请检查。");
        }
        Long nextStaffId = users.get(0).getId() ;
        if (StrUtil.isNotEmpty(inwardStaffRoll.getValue())){
            for (int i=0;i<users.size();i++){
                if (users.get(i).getId().equals(Long.valueOf(inwardStaffRoll.getValue()))){
                    if (i!=users.size()-1){
                        nextStaffId = users.get(i+1).getId();
                    }else {
                        nextStaffId = users.get(0).getId();
                    }
                }
            }
        }
        inwardStaffRoll.setValue(String.valueOf(nextStaffId));
        globalConfigService.update(inwardStaffRoll);
        return nextStaffId;
    }

    @Transactional
    @Override
    public void fixationInwardStaff(Long personalId) {
        Personal per = this.getOne(new LambdaQueryWrapper<Personal>().select(Personal::getId,Personal::getInwardStaff).eq(BaseEntity::getId, personalId).eq(BaseEntity::getDeleted, Boolean.FALSE));
        if (per.getInwardStaff() == null){
            Long nextStaffId = this.nextInwardStaff();
            this.update(new LambdaUpdateWrapper<Personal>().set(Personal::getInwardStaff,nextStaffId).eq(BaseEntity::getId,personalId));
        }
    }

    /**
     * 由外业人员确定定价内业
     * @return
     */
    @Override
    public Long fixationPriceStaff(Long outwardStaffId){
        try {
            if (outwardStaffId != null){
                DictData dictData = dictDataService.findByDataTypeAndRemark(OUTWARD_TO_PRICE, String.valueOf(outwardStaffId));
                return Long.valueOf(dictData.getName());
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public synchronized Boolean personalAllotRealAmount(List<OrderReportDTO> claimOrders) {
        return personalService.doPersonalAllotRealAmount(claimOrders);
    }

    @Transactional
    @Override
    public Boolean doPersonalAllotRealAmount(List<OrderReportDTO> claimOrders){
        if (CollectionUtil.isNotEmpty(claimOrders)){
            //先校验各订单此次认领金额之和是否大于这笔实收款的剩余金额
            Long financeFundId = claimOrders.get(0).getFinanceFundId();
            BigDecimal residueAmount = financeClaimService.checkFinanceFound(financeFundId);
            BigDecimal thisTimeTotalAmount = claimOrders.stream().map(OrderReportDTO::getThisTimeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (thisTimeTotalAmount.compareTo(residueAmount)>0){
                ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR,"本次订单认领之和大于实收款剩余金额，请检查后再认领。");
            }
            claimOrders.stream().forEach(x->{
                //检查该订单是否创建应收款记录
                OrderFund orderFund = orderFundService.getOne(new LambdaQueryWrapper<OrderFund>().eq(OrderFund::getOrderId, x.getOrderId())
                        .eq(OrderFund::getBusinessId,x.getId()).eq(OrderFund::getBusinessType, PERSONAL_BUSINESS.name())
                        .eq(BaseEntity::getDeleted, Boolean.FALSE));
                if (orderFund==null){
                    orderFund = new OrderFund();
                    orderFund.setOrderId(x.getOrderId());
                    orderFund.setBusinessType(PERSONAL_BUSINESS.name());
                    orderFund.setBusinessId(x.getId());
                    orderFund.setOrderName(x.getOrderName());
                    orderFundService.save(orderFund);
                }
                FinanceClaim financeClaim = new FinanceClaim();
                financeClaim.setRealFundId(financeFundId);
                financeClaim.setOrderFundId(orderFund.getId());
                financeClaim.setClaimUserId(LoginContext.getCurrentUserId());
                financeClaim.setClaimDatetime(new Date());
                financeClaim.setClaimAmount(x.getThisTimeAmount());
                financeClaimService.save(financeClaim);
                //更新订单认领总金额
                orderFundMapper.updateRealAmount(orderFund.getId(),x.getThisTimeAmount());
            });
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Page<PersonalVO> waitingAllotOrderPage(Page page,String keyword) {
        return personalMapper.waitingAllotOrderPage(page,keyword);
    }

    @Transactional
    @Override
    public Boolean doAllotOrder(PersonalAllotOrderDTO dto) {
        List<PersonalVO> orders = dto.getOrders();
        if (CollectionUtil.isNotEmpty(orders)){
            Long userId = dto.getId();
            List<Long> orderIds = orders.stream().map(BaseEntity::getId).collect(Collectors.toList());
            List<Long> instanceIds = orders.stream().map(PersonalVO::getCurrentNodeId).collect(Collectors.toList());

            batchCheckNodeState(instanceIds);
            //确定定价内业
            Long priceStaffId = this.fixationPriceStaff(userId);
            //更新订单业务表的外业人员内业定价人员
            personalMapper.batchUpdateOutWardStaffId(userId,priceStaffId,orderIds);
            //任务状态更新为已完成
            workTaskRecordMapper.batchFinishTaskByInstanceId(instanceIds);
            orders.stream().forEach(x->{
                WorkNodeCommit workNodeCommit = new WorkNodeCommit();
                workNodeCommit.setState(PASS);
                workNodeCommit.setIfCheckTask(true);
                workNodeCommit.setComments("批量分单");
                workNodeCommit.setInstanceNodeId(x.getCurrentNodeId());
                workNodeCommit.setBusinessSubId(x.getOrderId());
                workNodeCommit.setNextHandlerId(userId);
                personalNodeHandler.forward(workNodeCommit);
            });
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    @Transactional
    @Override
    public Boolean changeCredentials(PersonalVO personal) {
        this.update(new LambdaUpdateWrapper<Personal>().eq(BaseEntity::getId,personal.getId())
                .set(Personal::getCredentials,personal.getCredentials()));
        return personalTargetService.update(new LambdaUpdateWrapper<PersonalTarget>().set(PersonalTarget::getHouseCertificate,null)
                .set(PersonalTarget::getLandCertificate,null).set(PersonalTarget::getImmovableCertificate,null).eq(PersonalTarget::getPersonalId,personal.getId()));
    }


    private void batchCheckNodeState(List<Long> instanceIds) {
        List<WorkFlowNodeInstance> list = workFlowNodeInstanceService.list(new LambdaQueryWrapper<WorkFlowNodeInstance>()
                .select(WorkFlowNodeInstance::getState).in(BaseEntity::getId, instanceIds).eq(BaseEntity::getDeleted, Boolean.FALSE));
        for (WorkFlowNodeInstance instance : list) {
            if (!instance.getState().equals(PENDING.name())){
                ErrorCode.throwBusinessException(ErrorCode.DATA_OUT_OFF);
            }
        }
    }
}
