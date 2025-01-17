package com.px.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.entity.*;
import com.px.enums.GlobalConfigEnum;
import com.px.enums.MainBusinessEnum;
import com.px.mapper.*;
import com.px.service.*;
import com.px.utils.LoginContext;
import com.px.vo.AssetsRepertoryVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import cn.hutool.core.util.ObjectUtil;
import com.px.common.BaseEntity;
import com.px.dto.*;
import com.px.exception.ErrorCode;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.service.workflow.IWorkFlowService;
import com.px.vo.AssetsBusinessMember;
import com.px.vo.AssetsOrderFundInvoiceVO;
import com.px.vo.AssetsVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.px.common.Constants.BY_TURNS;
import static com.px.enums.MainBusinessEnum.ASSET_BUSINESS;
import static com.px.enums.ProductionEnum.*;

/**
 * <p>
 * 资产业务订单表 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-28
 */
@DS("mbs")
@Service
public class AssetsServiceImpl extends ServiceImpl<AssetsMapper, Assets> implements IAssetsService {

    @Autowired
    private IBusinessNumberService businessNumberService;

    @Autowired
    private IWorkFlowService workFlowService;

    @Autowired
    private AssetsMapper assetsMapper;

    @Autowired
    private AssetsProductionMapper assetsProductionMapper;

    @Autowired
    private AssetsEvaluationTargetMapper assetsEvaluationTargetMapper;

    @Autowired
    private IOrderFundService orderFundService;

    @Autowired
    private IBusinessProductionChoiceLogService choiceLogService;

    @Autowired
    private IGlobalConfigService globalConfigService;

    @Autowired
    private ISortedDepartmentService sortedDepartmentService;

    @Autowired
    private IFinanceClaimService financeClaimService;

    @Autowired
    private OrderFundMapper orderFundMapper;

    @Autowired
    private IFinanceInvoiceService financeInvoiceService;

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 条件查询资产任务列表
     *
     * @param page            分页条件
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    @Override
    public Page<AssetsVO> selectPage(Page page, AssetsSelectDTO assetsSelectDTO) {
        // 判断是否要根据部门id集合查询(部门待办查询)
        if (ObjectUtil.isNotNull(assetsSelectDTO.getSelectByDepartment()) && assetsSelectDTO.getSelectByDepartment()){
            assetsSelectDTO.setDepartmentIdList(departmentMapper.getDepIdList(LoginContext.getLoginCacheUserBO().getDepartmentList().get(0).getId()));
        }
        // 判断是否要查询资产列表,资产列表只能查询包含本人及其所在部门的人员为项目负责人或项目经理的流程(资产列表查询)
        if (ObjectUtil.isNotNull(assetsSelectDTO.getSelectByAll()) && assetsSelectDTO.getSelectByAll()){
            assetsSelectDTO.setDepartmentIdList(departmentMapper.getDepIdList(LoginContext.getLoginCacheUserBO().getDepartmentList().get(0).getId()));
        }
        // 此处判断handlerId是否为空只是为了确保前端查询条件有handlerId,并不直接信任
        if (ObjectUtil.isNotNull(assetsSelectDTO) && ObjectUtil.isNotNull(assetsSelectDTO.getHandlerId())){
            assetsSelectDTO.setHandlerId(LoginContext.getCurrentUserId());
        }
        return assetsMapper.selectPage(page, assetsSelectDTO);
    }

    /**
     * 条件查询我的资产订单列表
     *
     * @param page            分页条件
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    @Override
    public Page<AssetsVO> selectMyOrderPage(Page page, AssetsSelectDTO assetsSelectDTO) {
        // 此处判断managerId是否为空只是为了确保前端查询条件有managerId,并不直接信任
        if (ObjectUtil.isNotNull(assetsSelectDTO) && ObjectUtil.isNotNull(assetsSelectDTO.getClientManagerId())){
            assetsSelectDTO.setClientManagerId(LoginContext.getCurrentUserId());
        }
        return assetsMapper.selectMyOrderPage(page, assetsSelectDTO);
    }

    /**
     * 导出我的任务订单列表
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    @Override
    public List<AssetsVO> exportMyOrder(AssetsSelectDTO assetsSelectDTO) {
        // 此处判断managerId是否为空只是为了确保前端查询条件有managerId,并不直接信任
        if (ObjectUtil.isNotNull(assetsSelectDTO) && ObjectUtil.isNotNull(assetsSelectDTO.getClientManagerId())){
            assetsSelectDTO.setClientManagerId(LoginContext.getCurrentUserId());
        }
        return assetsMapper.exportMyOrder(assetsSelectDTO);
    }

    /**
     * 条件查询资产已归档列表
     *
     * @param page            分页条件
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    @Override
    public Page<AssetsVO> selectArchivedPage(Page page, AssetsSelectDTO assetsSelectDTO) {
        return assetsMapper.selectArchivedPage(page, assetsSelectDTO);
    }

    /**
     * 条件查询资产已终止列表
     *
     * @param page            分页条件
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    @Override
    public Page<AssetsVO> selectTerminatedPage(Page page, AssetsSelectDTO assetsSelectDTO) {
        assetsSelectDTO.setHandlerId(LoginContext.getCurrentUserId());
        return assetsMapper.selectTerminatedPage(page, assetsSelectDTO);
    }


    /**
     * 条件查询资产已终止列表
     *
     * @param page            分页条件
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    @Override
    public Page<AssetsRepertoryVo> selectRepertoryPage(Page page, AssetsSelectDTO assetsSelectDTO) {
        return assetsMapper.selectRepertoryPage(page, assetsSelectDTO);
    }

    /**
     * 条件查询资产入库统计集合(用于导出)
     *
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    @Override
    public List<AssetsRepertoryVo> selectRepertoryList(AssetsSelectDTO assetsSelectDTO) {
        return assetsMapper.selectRepertoryList(assetsSelectDTO);
    }

    /**
     * 根据业务id获取资产业务详情
     *
     * @param id 业务id
     * @return AssetsVO
     */
    @Override
    public AssetsVO getAssetsDetailById(Long id) {
        AssetsVO assetsVO = assetsMapper.getAssetsDetailById(id);
        return assetsVO;
    }


    /**
     * 新增资产业务订单
     *
     * @param assets 资产订单信息
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkNodeCommit add(Assets assets) {
        //获取项目编号
        assets.setOrderId(businessNumberService.takeOrderNumber());
        //获取下单部门id
        assets.setMarketDepartmentId(assetsMapper.getManagerDepId(assets.getClientManagerId()));
        // 判断分单类型
        if (BY_TURNS.equals(assets.getAllotType())) {
            //获取轮单部门
            assets.setDepartmentId(getAllotDepartmentId());
        }
        //新增订单信息
        this.save(assets);
        //新增订单收款信息
        OrderFund orderFund = new OrderFund();
        orderFund.setBusinessType("ASSET_BUSINESS");
        orderFund.setBusinessId(assets.getId());
        orderFund.setOrderName(assets.getName());
        orderFund.setOrderId(assets.getOrderId());
        orderFundService.save(orderFund);
        //发起流程
        return workFlowService.openingOrder(ASSET_BUSINESS, assets.getId(), null, assets.getRemark());
    }

    /**
     * 修改资产业务订单项目负责人与项目成员
     *
     * @param taskRecordDTO 修改dto
     * @return Boolean
     */
    @Override
    public Boolean updatePrincipalAndMembers(TaskRecordDTO<AssetsPrincipalMembersDTO> taskRecordDTO) {
        AssetsPrincipalMembersDTO assetsPrincipalMembersDTO = taskRecordDTO.getTaskData();
        if (ObjectUtil.isNull(assetsPrincipalMembersDTO.getPrincipalId()) && ObjectUtil.isNull(assetsPrincipalMembersDTO.getMembers())) {
            ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR, "项目负责人和项目参与人员不能为空。");
        }
        return this.update(new LambdaUpdateWrapper<Assets>()
                .set(Assets::getMembers, assetsPrincipalMembersDTO.getMembers())
                .set(Assets::getPrincipalId, assetsPrincipalMembersDTO.getPrincipalId())
                .set(Assets::getDepartmentId, assetsPrincipalMembersDTO.getDepartmentId())
                .eq(BaseEntity::getId, assetsPrincipalMembersDTO.getId()));
    }

    /**
     * 根据业务id获取资产业务款项信息
     *
     * @param id 业务id
     * @return AssetsOrderFundInvoiceVO
     */
    @Override
    public AssetsOrderFundInvoiceVO getOrderFundInvoiceById(Long id) {
        AssetsOrderFundInvoiceVO assetsOrderFundInvoiceVO = assetsMapper.getOrderFundInvoiceById(id);
        assetsOrderFundInvoiceVO.setFinanceInvoiceVoList(financeInvoiceService.list(new LambdaQueryWrapper<FinanceInvoice>()
                .eq(ObjectUtil.isNotNull(assetsOrderFundInvoiceVO.getId()),FinanceInvoice::getOrderFundId, assetsOrderFundInvoiceVO.getId())
                .eq(BaseEntity::getDeleted, 0)));
        return assetsOrderFundInvoiceVO;
    }

    @Override
    public Boolean update(Assets assets) {
        if (assets.getClienteleType().equals("个人")) {
            assets.setClienteleId(null);
            assets.setClienteleSubId(null);
        }
        if (assets.getTerminalClienteleType().equals("个人")) {
            assets.setTerminalClienteleId(null);
        }
        return this.updateById(assets);
    }

    @Override
    public Boolean delete(Long id) {
        //逻辑删除
        return this.removeById(id);
    }

    /**
     * 资产业务产品取号
     * 格式示例:川友预报字2024第0101-2号
     *
     * @param dto 取号dto
     * @return String
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean takeAssetsProductionNo(TakeAssetsProductionNoDTO dto) {

        dto.setSelectAll(assetsEvaluationTargetMapper.targetCount(dto.getBusinessId(), dto.getStatementNo()) == dto.getTargetIdList().size());

        // 取号时更新产品选择记录状态
        List<BusinessProductionChoiceLog> choiceLogList = new ArrayList<>();
        // 循环取号的评估对象id集合
        for (Long targetId : dto.getTargetIdList()) {
            // 获取每一个取号评估对象的产品选择记录(同一个评估对象不可能同时存在两条状态为未取号的产品选择记录)
            BusinessProductionChoiceLog choiceLog = choiceLogService.getOne(new LambdaQueryWrapper<BusinessProductionChoiceLog>()
                    .eq(BusinessProductionChoiceLog::getTargetId, targetId)
                    .eq(BusinessProductionChoiceLog::getBusinessType, "ASSET_BUSINESS")
                    .eq(BusinessProductionChoiceLog::getBusinessId, dto.getBusinessId())
                    .eq(BusinessProductionChoiceLog::getTakeNoStatus, 0));
            // 已取号的评估对象产品选择记录取号状态改为true
            choiceLog.setTakeNoStatus(true);
            // 获取后将选择记录状态改为true并添加集合用于后续批量更新
            choiceLogList.add(choiceLog);
        }

        // 获取产品选择记录的相关信息,并设置到dto(同一流程的所有评估对象只能选择同一种产品类型,所以此处直接下标获取)
        dto.setPrintCount(choiceLogList.get(0).getPrintCount());
        dto.setProductionType(choiceLogList.get(0).getProductionType());
        String productionNo = genProductionNo(dto);

        //产品信息更新添加产品号
        assetsProductionMapper.updateProductionNo(choiceLogList.get(0).getProductionId(), dto.getBusinessId(), dto.getProductionType(), productionNo);

        //如果还有未取号评估对象
        if (dto.getHasNotTakeNumTargetSelect()) {
            // 有未取号对象,添加一份同类型产品
            AssetsProduction copyProductionInfo = new AssetsProduction();
            copyProductionInfo.setBusinessId(dto.getBusinessId());
            copyProductionInfo.setProductionType(dto.getProductionType());
            copyProductionInfo.setPrintCount(dto.getPrintCount());
            assetsProductionMapper.copyProductionInfo(copyProductionInfo);
            // 获取未取号的评估对象id
            List<Long> unTakeNoTargetIdList = targetIdListDifference(dto.getAllTargetIdList(), dto.getTargetIdList());

            // 将未取号对象的产品选择记录对应的产品id更新
            if (ObjectUtil.isNotNull(unTakeNoTargetIdList) && ObjectUtil.isNotEmpty(unTakeNoTargetIdList)) {
                for (Long targetId : unTakeNoTargetIdList) {
                    // 获取每一个未评估对象的产品选择记录(同一个评估对象不可能同时存在两条状态为未取号的产品选择记录)
                    BusinessProductionChoiceLog choiceLog = choiceLogService.getOne(new LambdaQueryWrapper<BusinessProductionChoiceLog>()
                            .eq(BusinessProductionChoiceLog::getTargetId, targetId)
                            .eq(BusinessProductionChoiceLog::getBusinessType, "ASSET_BUSINESS")
                            .eq(BusinessProductionChoiceLog::getBusinessId, dto.getBusinessId())
                            .eq(BusinessProductionChoiceLog::getTakeNoStatus, 0));
                    // 将未取号的评估对象对应的产品选择记录的产品id改为新的
                    if (ObjectUtil.isNotNull(choiceLog)){
                        choiceLog.setProductionId(copyProductionInfo.getId());
                        // 获取后将选择记录对应的产品id改为前面新添加的产品信息
                        choiceLogList.add(choiceLog);
                    }
                }
            }
        }

        // 批量更新产品选择记录
        choiceLogService.updateBatchById(choiceLogList);

        //评估对象信息更新添加产品号
        for (Long id : dto.getTargetIdList()) {
            //判断取号的产品类型
            if (dto.getProductionType().equals(STATEMENT.getCode())) { //意见书
                assetsEvaluationTargetMapper.updateTargetStatementNo(id, productionNo);
            } else { //报告
                assetsEvaluationTargetMapper.updateTargetReportNo(id, productionNo);
            }

        }
        if (ObjectUtil.isNotNull(productionNo) && ObjectUtil.isNotEmpty(productionNo)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }

    }

    /**
     * 生成资产产品号
     *
     * @param takeAssetsProductionNoDTO 取号dto
     * @return String
     */
    public String genProductionNo(TakeAssetsProductionNoDTO takeAssetsProductionNoDTO) {
        //获取年份
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        String productionNo = "（" + nowYear + "）" + "第";

        //获取资产产品取号信息
        BusinessNumber businessNumber = assetsMapper.getBusinessNum(takeAssetsProductionNoDTO.getProductionType() + "_NUMBER", ASSET_BUSINESS.getCode(), nowYear);

        //获取当前资产业务流程的评估对象已经生成的产品号集合
        List<String> productionNoList = null;
        //判断取号的产品类型
        if (takeAssetsProductionNoDTO.getProductionType().equals(STATEMENT.getCode())) { //意见书
            productionNoList = assetsEvaluationTargetMapper.getStatementNoList(takeAssetsProductionNoDTO.getBusinessId());
        } else { //报告
            productionNoList = assetsEvaluationTargetMapper.getReportNoList(takeAssetsProductionNoDTO.getBusinessId(), takeAssetsProductionNoDTO.getAllTargetIdList());
        }
        //判断该资产业务订单是否有取过号
        if (ObjectUtil.isNull(productionNoList) || productionNoList.isEmpty()) {
            //判断是否有取号信息
            if (ObjectUtil.isNotNull(businessNumber)) { //取号信息不为空组合产品号
                //判断两个进位是否都>9,具有两位数
                if (businessNumber.getMonth() > 9 && businessNumber.getNextNo() > 9) {
                    productionNo = productionNo + businessNumber.getMonth() + businessNumber.getNextNo();
                } else {
                    //第一进位小于10,前面多加一个0组成两位
                    if (businessNumber.getMonth() < 10) {
                        productionNo = productionNo + "0" + businessNumber.getMonth();
                    } else {
                        productionNo = productionNo + businessNumber.getMonth();
                    }
                    //第二进位小于10,前面多加一个0组成两位
                    if (businessNumber.getNextNo() < 10) {
                        productionNo = productionNo + "0" + businessNumber.getNextNo();
                    } else {
                        productionNo = productionNo + businessNumber.getNextNo();
                    }
                }
                //设置下一个产品号取号信息
                if (businessNumber.getNextNo() < 99) {
                    businessNumber.setNextNo(businessNumber.getNextNo() + 1);
                } else {
                    businessNumber.setMonth(businessNumber.getMonth() + 1);
                    businessNumber.setNextNo(1);
                }
                //更新取号信息
                assetsMapper.updateBusinessNum(businessNumber);
            } else { //为空插入取号规则信息,并重新获取
                businessNumber = addBusinessNumber(takeAssetsProductionNoDTO.getProductionType() + "_NUMBER", ASSET_BUSINESS.getCode(), nowYear);
                //判断两个进位是否都>9,具有两位数
                if (businessNumber.getMonth() > 9 && businessNumber.getNextNo() > 9) {
                    productionNo = productionNo + businessNumber.getMonth() + businessNumber.getNextNo();
                } else {
                    //第一进位小于10,前面多加一个0组成两位
                    if (businessNumber.getMonth() < 10) {
                        productionNo = productionNo + "0" + businessNumber.getMonth();
                    } else {
                        productionNo = productionNo + businessNumber.getMonth();
                    }
                    //第二进位小于10,前面多加一个0组成两位
                    if (businessNumber.getNextNo() < 10) {
                        productionNo = productionNo + "0" + businessNumber.getNextNo();
                    } else {
                        productionNo = productionNo + businessNumber.getNextNo();
                    }
                }
                //设置下一个产品号取号信息
                if (businessNumber.getNextNo() < 99) {
                    businessNumber.setNextNo(businessNumber.getNextNo() + 1);
                } else {
                    businessNumber.setMonth(businessNumber.getMonth() + 1);
                    businessNumber.setNextNo(1);
                }
                //更新取号信息
                assetsMapper.updateBusinessNum(businessNumber);
            }

            //判断取号产品类型
            if (takeAssetsProductionNoDTO.getProductionType().equals(STATEMENT.getCode())) { //取号产品为意见书
                productionNo = "川友预报字" + productionNo;
            } else if (takeAssetsProductionNoDTO.getProductionType().equals(REPORT.getCode())) { //取号产品为评估报告
                productionNo = "川友评报字" + productionNo;
            } else if (takeAssetsProductionNoDTO.getProductionType().equals(CONSULT.getCode())) { //取号产品为咨询报告
                productionNo = "川友咨报字" + productionNo;
            } else if (takeAssetsProductionNoDTO.getProductionType().equals(LETTER.getCode())) { //取号产品为意见函
                productionNo = "川友字" + productionNo;
            } else { //都不是返回null
                return null;
            }
        } else { //已经在该子号之前生成过子号则直接将上一个产品的主体拿过来赋值
            int dashIndex = productionNoList.get(0).indexOf('-');
            productionNo = productionNoList.get(0).substring(0, dashIndex);
        }


        // 判断有无子号
        if (takeAssetsProductionNoDTO.getSelectAll()) { //true 则选择了全部评估对象,仅主号无子号
            return productionNo + "号";
        } else { //false 选择了部分评估对象,有子号
            return getProductionSecNum(productionNo, productionNoList);
        }
    }

    /**
     * 获取资产产品子号
     *
     * @return String
     */
    public String getProductionSecNum(String productionNo, List<String> productionNoList) {
        // 判断同一资产业务的同一类型产品是否有生成过子号
        if (ObjectUtil.isNull(productionNoList) || productionNoList.isEmpty()) { //为null还没有生成过子号,生成第一个
            return productionNo + "-1号";
        } else { //不为null则说明已经生成过子号,找到目前最大的子号并生成下一个子号
            // 初始化最大子号为Integer.MIN_VALUE
            int maxNumber = Integer.MIN_VALUE;

            // 正则表达式，用于匹配短横线-后面的数字
            Pattern pattern = Pattern.compile("-(\\d+)");

            // 遍历字符串列表
            for (String str : productionNoList) {
                // 创建匹配器
                Matcher matcher = pattern.matcher(str);
                // 在字符串中查找短横线后面的数字
                while (matcher.find()) {
                    // 将找到的子号从字符串转换为整数
                    int secNum = Integer.parseInt(matcher.group(1));
                    // 更新最大的子号
                    if (secNum > maxNumber) {
                        maxNumber = secNum;
                    }
                }
            }
            // 将找到的最大子号 + 1, 得到下一个子号
            maxNumber = maxNumber + 1;
            return productionNo + "-" + maxNumber + "号";
        }
    }

    /**
     * 新增资产取号信息,并返回
     *
     * @param takeType     取号类型(同产品类型)
     * @param businessType 业务类型
     * @param year         年份
     * @return BusinessNumber
     */
    public BusinessNumber addBusinessNumber(String takeType, String businessType, Integer year) {
        BusinessNumber businessNumber = new BusinessNumber();
        businessNumber.setTakeType(takeType);
        businessNumber.setBusinessType(businessType);
        businessNumber.setYear(year);
        businessNumber.setMonth(1);
        businessNumber.setNextNo(1);
        //新增
        assetsMapper.addBusinessNum(businessNumber);
        //查询
        return assetsMapper.getBusinessNum(takeType, businessType, year);
    }

    /**
     * 获取未取号的资产评估对象id
     *
     * @param allTargetIdList    本次资产流程的所有评估对象
     * @param takeNoTargetIdList 本次资产流程的取号评估对象
     * @return List<Long>
     */
    public List<Long> targetIdListDifference(List<Long> allTargetIdList, List<Long> takeNoTargetIdList) {
        // 将取号评估对象集合转换为Set，以便快速查找
        Set<Long> takeNoTargetIdSet = new HashSet<>(takeNoTargetIdList);

        // 使用Stream API找出只在allTargetIdList中但不在takeNoTargetIdSet中的元素
        return allTargetIdList.stream()
                .filter(value -> !takeNoTargetIdSet.contains(value))
                .collect(Collectors.toList());
    }

    /**
     * 获取资产项目所有参与人
     *
     * @param assetsBusinessMembersDTO 查询dto
     * @return List<AssetsBusinessMembers>
     */
    @Override
    public List<AssetsBusinessMember> getAssetsBusinessMembers(AssetsBusinessMembersDTO assetsBusinessMembersDTO) throws JsonProcessingException {
        // 获取项目参与人id
        Assets businessMember = assetsMapper.getBusinessMembers(assetsBusinessMembersDTO);

        List<AssetsBusinessMember> assetsBusinessMemberList = new ArrayList<>();

        //获取项目负责人信息
        AssetsBusinessMember assetsBusinessMember;
        assetsBusinessMember = assetsMapper.getAssetsBusinessMember(businessMember.getPrincipalId());
        assetsBusinessMember.setBusinessId(assetsBusinessMembersDTO.getBusinessId());
        assetsBusinessMember.setMemberType("项目负责人");
        assetsBusinessMemberList.add(assetsBusinessMember);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> memberIds = objectMapper.readValue(businessMember.getMembers(), new TypeReference<List<Long>>() {
        });


        // 获取参与人信息
        for (Long userId : memberIds) {
            assetsBusinessMember = assetsMapper.getAssetsBusinessMember(userId);
            assetsBusinessMember.setBusinessId(assetsBusinessMembersDTO.getBusinessId());
            assetsBusinessMember.setMemberType("项目参与人");
            assetsBusinessMemberList.add(assetsBusinessMember);
        }

        // 判断报告号是否为空(为空说明没出报告,就没签字师)
        if (ObjectUtil.isNotNull(assetsBusinessMembersDTO.getReportNo()) && ObjectUtil.isNotEmpty(assetsBusinessMembersDTO.getReportNo())) {
            // 获取签字师id
            String signatorys = assetsMapper.getSignatorys(assetsBusinessMembersDTO);
            List<Long> signatoryIds = objectMapper.readValue(signatorys, new TypeReference<List<Long>>() {
            });
            // 获取签字师信息
            for (Long userId : signatoryIds) {
                assetsBusinessMember = assetsMapper.getAssetsBusinessMember(userId);
                assetsBusinessMember.setBusinessId(assetsBusinessMembersDTO.getBusinessId());
                assetsBusinessMember.setMemberType("签字师");
                assetsBusinessMemberList.add(assetsBusinessMember);
            }
        }

        return assetsBusinessMemberList;
    }


    /**
     * 认领实收款到 订单
     *
     * @param claimOrders 认领信息
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean doAllotRealAmountToOrder(List<OrderReportDTO> claimOrders) {
        if (CollectionUtil.isNotEmpty(claimOrders)) {
            //先校验各订单此次认领金额之和是否大于这笔实收款的剩余金额
            Long financeFundId = claimOrders.get(0).getFinanceFundId();
            BigDecimal residueAmount = financeClaimService.checkFinanceFound(financeFundId);
            BigDecimal thisTimeTotalAmount = claimOrders.stream().map(OrderReportDTO::getThisTimeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (thisTimeTotalAmount.compareTo(residueAmount) > 0) {
                ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR, "本次订单认领之和大于实收款剩余金额，请检查后再认领。");
                return Boolean.FALSE;
            }

            for (OrderReportDTO orderReportDTO : claimOrders) {
                BigDecimal thisTimeAmount = orderReportDTO.getThisTimeAmount();
                if (thisTimeAmount == null || thisTimeAmount.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                //更新订单实收款
                Long orderFundId = orderReportDTO.getOrderFundId();
                orderFundMapper.updateRealAmount(orderFundId, thisTimeAmount);
                //更新订单折扣比例
                orderFundMapper.updateDiscountByOrderIdAfter(orderFundId);

                //插入订单认领流水表
                FinanceClaim financeClaim = new FinanceClaim();
                Long userId = LoginContext.getCurrentUserId();
                financeClaim.setClaimUserId(userId);
                financeClaim.setRealFundId(financeFundId);
                financeClaim.setOrderFundId(orderFundId);
                financeClaim.setClaimAmount(thisTimeAmount);
                financeClaim.setClaimDatetime(new Date());
                financeClaimService.add(financeClaim);
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 获取订单详情
     *
     * @param id 订单id
     * @return Assets
     */
    @Override
    public Assets detail(Long id) {
        return this.getById(id);
    }

    /**
     * 更新资产订单基本信息
     *
     * @param assets 更新信息
     * @return Boolean
     */
    @Override
    public Boolean updateOrderBaseInfo(Assets assets) {
        return assetsMapper.updateOrderBaseInfo(assets);
    }

    /**
     * 获取资产市场下单部门
     * @return List<Department>
     */
    @Override
    public List<Department> getAssetsMarketDepartment() {
        return assetsMapper.getMarkeDepartment();
    }

    /**
     * 获取资产轮单部门id
     *
     * @return Long
     */
    private synchronized Long getAllotDepartmentId() {

        List<SortedDepartment> departments = sortedDepartmentService.list(new LambdaQueryWrapper<SortedDepartment>().eq(SortedDepartment::getBusinessType, MainBusinessEnum.ASSET_BUSINESS.getCode()));

        if (CollectionUtil.isEmpty(departments)) {
            ErrorCode.throwBusinessException(ErrorCode.SORTED_DEPARTMENT_NOT_EXISTS, MainBusinessEnum.ASSET_BUSINESS.getMsg());
        }


        GlobalConfig currentSorted = globalConfigService.getGlobalConfig(GlobalConfigEnum.DEPARTMENT_TURNS.name(), GlobalConfigEnum.DepartmentTurns.ASSET_BUSINESS.name());

        Integer currentSortedIndex = Integer.valueOf(StrUtil.isBlank(currentSorted.getValue()) ? "0" : currentSorted.getValue());

        List<Integer> indexes = departments.stream().map(SortedDepartment::getSortedIndex).sorted(Integer::compareTo).collect(Collectors.toList());
        for (int i = 0; i < indexes.size(); i++) {
            if (currentSortedIndex.equals(indexes.get(i))) {
                Integer waitingIndex = indexes.get((i + 1) % indexes.size());
                currentSorted.setValue(String.valueOf(waitingIndex));
                globalConfigService.update(currentSorted);
                break;
            }
        }

        return departments.stream().filter(d -> d.getSortedIndex().equals(currentSortedIndex)).findFirst().get().getDepartmentId();
    }
}
