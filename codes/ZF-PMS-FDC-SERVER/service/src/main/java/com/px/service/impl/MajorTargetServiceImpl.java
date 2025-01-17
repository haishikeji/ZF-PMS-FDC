package com.px.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.px.common.BaseEntity;
import com.px.dto.MajorTargetMergeNumberDTO;
import com.px.dto.TakeNumberDTO;
import com.px.dto.TaskRecordDTO;
import com.px.entity.MajorProduction;
import com.px.entity.MajorTarget;
import com.px.enums.ProductionEnum;
import com.px.enums.TakeNumberEnum;
import com.px.mapper.MajorProductionMapper;
import com.px.mapper.MajorTargetMapper;
import com.px.service.IBusinessNumberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.service.IMajorProductionService;
import com.px.service.IMajorTargetService;
import com.px.vo.IdNameVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.px.enums.ProductionEnum.*;

/**
 * <p>
 * 大中型项目评估对象 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-11-03
 */
@DS("mbs")
@Service
public class MajorTargetServiceImpl extends ServiceImpl<MajorTargetMapper, MajorTarget> implements IMajorTargetService {

    @Autowired
    private IBusinessNumberService businessNumberService;

    @Autowired
    private MajorTargetMapper majorTargetMapper;

    @Autowired
    private IMajorTargetService majorTargetService;
    @Autowired
    private MajorProductionMapper majorProductionMapper;

    @Autowired
    private IMajorProductionService majorProductionService;

    @Override
    public List<MajorTarget> getList(MajorTarget majorTarget){
//        return this.list(new LambdaQueryWrapper<MajorTarget>().eq(MajorTarget::getMajorId,majorTarget.getMajorId())
//                        .eq(StrUtil.isNotEmpty(majorTarget.getStatementNo()), MajorTarget::getStatementNo,majorTarget.getStatementNo())
//                        .eq(StrUtil.isNotEmpty(majorTarget.getReportNo()), MajorTarget::getReportNo,majorTarget.getReportNo())
//                        .eq(StrUtil.isNotEmpty(majorTarget.getLetterNo()), MajorTarget::getLetterNo,majorTarget.getLetterNo())
//                        .eq(BaseEntity::getDeleted,Boolean.FALSE)
//                .orderByAsc(BaseEntity::getCreated));
        List<MajorTarget> list = majorTargetMapper.getList(majorTarget);
        return list;
    }


    @Override
    public MajorTarget detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(TaskRecordDTO<MajorTarget> majorTarget){
        MajorTarget taskData = majorTarget.getTaskData();
        taskData.setSiteIds(JSON.toJSONString(taskData.getSites()));
        return  this.save(taskData);
    }

    @Override
    public Boolean update(TaskRecordDTO<MajorTarget> majorTarget){
        MajorTarget taskData = majorTarget.getTaskData();
        taskData.setSiteIds(JSON.toJSONString(taskData.getSites()));
        return  this.updateById(taskData);
    }

    @Override
    public Boolean xUpdate(MajorTarget majorTarget) {
        return  this.updateById(majorTarget);
    }

    @Override
    public Boolean delete(TaskRecordDTO<Long> majorTarget){
        Long id = majorTarget.getTaskData();
        //逻辑删除
        return this.removeById(id);
    }


    @Override
    public synchronized String takeNumber(TakeNumberDTO takeNumberDTO) {
        ProductionEnum productionType = takeNumberDTO.getProductionType();
        if (productionType.equals(STATEMENT)){
            return majorTargetService.majorTakeStatementNo(takeNumberDTO);
        }
        if (productionType.equals(ProductionEnum.REPORT)){
            return majorTargetService.majorTakeReportNo(takeNumberDTO);
        }
        if (productionType.equals(LETTER)){
            return  majorTargetService.majorTakeLetterNo(takeNumberDTO);
        }
        return null;
    }

    @Transactional
    @Override
    public String majorTakeStatementNo(TakeNumberDTO takeNumberDTO){
        Long businessId = takeNumberDTO.getBusinessId();
        List<IdNameVO> takeNumberTargets = takeNumberDTO.getTargets();
        String orderId = takeNumberDTO.getOrderId();
        TakeNumberEnum takeType = takeNumberDTO.getTakeType();
        List<MajorTarget> targets = this.list(new LambdaQueryWrapper<MajorTarget>().select(BaseEntity::getId, MajorTarget::getStatementNo)
                .eq(MajorTarget::getMajorId, businessId).eq(BaseEntity::getDeleted, Boolean.FALSE));
        List<MajorTarget> taken = targets.stream().filter(x -> StrUtil.isNotEmpty(x.getStatementNo())).collect(Collectors.toList());
        List<Long> takenIds = taken.stream().map(BaseEntity::getId).collect(Collectors.toList());
        //从请求数据中过滤出已取过号的对象
        takeNumberTargets = takeNumberTargets.stream().filter(x->!takenIds.contains(x.getId())).collect(Collectors.toList());
        takeNumberDTO.setTargets(takeNumberTargets);

        if (CollectionUtils.isNotEmpty(takeNumberTargets) && CollectionUtils.isNotEmpty(targets)){
            if (takeNumberTargets.size()<targets.size()){

                List<MajorTarget> sorted = taken.stream().sorted((t1, t2) -> t2.getStatementNo().compareTo(t1.getStatementNo())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(taken)){
                    List<IdNameVO> subNumbers = businessNumberService.takeStatementSubNumber(orderId,takeType,null,takeNumberDTO.getTargets());
                    majorTargetMapper.updateSubStatementNo(subNumbers);
                    return subNumbers.get(0).getName();
                }else {
                    //已取过主号，后续取子号主号都一致。
                    List<IdNameVO> subNumbers = businessNumberService.takeStatementSubNumber(orderId,takeType,sorted.get(0).getStatementNo(),takeNumberDTO.getTargets());
                    majorTargetMapper.updateSubStatementNo(subNumbers);
                    return subNumbers.get(0).getName();
                }
            }else {
                //取主号
                String mainNumber = businessNumberService.takeStatementMainNumber(orderId,takeType);
                majorTargetMapper.updateMainStatementNo(mainNumber,businessId);
                return mainNumber;
            }
        }
        return null;
    }


    @Transactional
    @Override
    public String majorTakeReportNo(TakeNumberDTO takeNumberDTO){
        Long businessId = takeNumberDTO.getBusinessId();
        List<IdNameVO> takeNumberTargets = takeNumberDTO.getTargets();
        String existStatementNo = takeNumberDTO.getStatementNo();
        //如果选择不同的价值意见书号，来取同一个报告号。则不允许，此处selectedTargetStatementNos的size只能是1
        List<String> selectedTargetStatementNos = takeNumberTargets.stream()
                .filter(x->StrUtil.isNotBlank(x.getName())).map(IdNameVO::getName).distinct().collect(Collectors.toList());

        String statementNo = null;
        if (CollectionUtils.isNotEmpty(selectedTargetStatementNos) && selectedTargetStatementNos.size()==1){
            statementNo = selectedTargetStatementNos.get(0);
        }

        List<MajorTarget> targets = this.list(new LambdaQueryWrapper<MajorTarget>().select(BaseEntity::getId, MajorTarget::getReportNo)
                .eq(MajorTarget::getMajorId, businessId)
                .eq(StrUtil.isNotBlank(statementNo)
                        ,MajorTarget::getStatementNo,statementNo)
                .eq(BaseEntity::getDeleted, Boolean.FALSE));
        List<MajorTarget> taken = targets.stream().filter(x -> StrUtil.isNotEmpty(x.getReportNo())).collect(Collectors.toList());
        List<Long> takenIds = taken.stream().map(BaseEntity::getId).collect(Collectors.toList());
        //从请求数据中过滤出已取过好的对象
        takeNumberTargets = takeNumberTargets.stream().filter(x->!takenIds.contains(x.getId())).collect(Collectors.toList());
        takeNumberDTO.setTargets(takeNumberTargets);

        if (CollectionUtils.isNotEmpty(takeNumberTargets) && CollectionUtils.isNotEmpty(targets)){
            if (takeNumberTargets.size()<targets.size()){

                List<MajorTarget> sorted = taken.stream().sorted((t1, t2) -> t2.getReportNo().compareTo(t1.getReportNo())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(taken)){
                    List<IdNameVO> subNumbers = businessNumberService.takeSubNumber(takeNumberDTO,null);
                    majorTargetMapper.updateSubReportNo(subNumbers,existStatementNo);
                    return subNumbers.get(0).getName();
                }else {
                    //已取过主号，后续取子号主号都一致。
                    List<IdNameVO> subNumbers = businessNumberService.takeSubNumber(takeNumberDTO,sorted.get(0).getReportNo());
                    majorTargetMapper.updateSubReportNo(subNumbers,existStatementNo);
                    return subNumbers.get(0).getName();
                }
            }else {
                //取主号
                String mainNumber = businessNumberService.takeMainNumber(takeNumberDTO);
                majorTargetMapper.updateMainReportNo(mainNumber,businessId,statementNo);
                return mainNumber;
            }
        }
        return null;
    }

    @Transactional
    @Override
    public String majorTakeLetterNo(TakeNumberDTO takeNumberDTO){
        Long businessId = takeNumberDTO.getBusinessId();
        List<IdNameVO> takeNumberTargets = takeNumberDTO.getTargets();
        String existStatementNo = takeNumberDTO.getStatementNo();
        String orderId = takeNumberDTO.getOrderId();
        TakeNumberEnum takeType = takeNumberDTO.getTakeType();
        //如果选择不同的价值意见书号，来取同一个复评函号。则不允许，此处selectedTargetStatementNos的size只能是1
        List<String> selectedTargetStatementNos = takeNumberTargets.stream()
                .filter(x->StrUtil.isNotBlank(x.getName())).map(IdNameVO::getName).distinct().collect(Collectors.toList());

        String statementNo = null;
        if (CollectionUtils.isNotEmpty(selectedTargetStatementNos) && selectedTargetStatementNos.size()==1){
            statementNo = selectedTargetStatementNos.get(0);
        }
        List<MajorTarget> targets = this.list(new LambdaQueryWrapper<MajorTarget>().select(BaseEntity::getId, MajorTarget::getLetterNo)
                .eq(MajorTarget::getMajorId, businessId)
                .eq(StrUtil.isNotBlank(statementNo)
                        ,MajorTarget::getStatementNo,statementNo)
                .eq(BaseEntity::getDeleted, Boolean.FALSE));

        List<MajorTarget> taken = targets.stream().filter(x -> StrUtil.isNotEmpty(x.getLetterNo())).collect(Collectors.toList());
        List<Long> takenIds = taken.stream().map(BaseEntity::getId).collect(Collectors.toList());
        //从请求数据中过滤出已取过号的对象
        takeNumberTargets = takeNumberTargets.stream().filter(x->!takenIds.contains(x.getId())).collect(Collectors.toList());
        takeNumberDTO.setTargets(takeNumberTargets);

        if (CollectionUtils.isNotEmpty(takeNumberTargets) && CollectionUtils.isNotEmpty(targets)){
            if (takeNumberTargets.size()<targets.size()){

                List<MajorTarget> sorted = taken.stream().sorted((t1, t2) -> t2.getLetterNo().compareTo(t1.getLetterNo())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(taken)){
                    List<IdNameVO> subNumbers = businessNumberService.takeLetterSubNumber(orderId,takeType,null,takeNumberDTO.getTargets());
                    majorTargetMapper.updateSubLetterNo(subNumbers,existStatementNo);
                    return subNumbers.get(0).getName();
                }else {
                    //已取过主号，后续取子号主号都一致。
                    List<IdNameVO> subNumbers = businessNumberService.takeLetterSubNumber(orderId,takeType,sorted.get(0).getLetterNo(),takeNumberDTO.getTargets());
                    majorTargetMapper.updateSubLetterNo(subNumbers,existStatementNo);
                    return subNumbers.get(0).getName();
                }
            }else {
                //取主号
                String mainNumber = businessNumberService.takeLetterMainNumber(orderId,takeType);
                majorTargetMapper.updateMainLetterNo(mainNumber,businessId,statementNo);
                return mainNumber;
            }
        }
        return null;
    }

    @Transactional
    @Override
    public Boolean mergeNumber(MajorTargetMergeNumberDTO mergeDTO) {
        String mergeType = mergeDTO.getMergeType();
        String toMergeNumber = mergeDTO.getToMergeNumber();
        List<Long> targetIds = mergeDTO.getTargetIds();
        MajorProduction majorProduction = majorProductionMapper.getEvaluateAmountByProductionNo(toMergeNumber);

        //更新产品评估价格和是否二三审状态
        BigDecimal evaluateValue = majorTargetMapper.getEvaluateAmountByTargetId(targetIds).multiply(BigDecimal.valueOf(10000L));

        majorProduction.setEvaluateAmount(majorProduction.getEvaluateAmount().add(evaluateValue));

        majorProductionService.updateEvaluateAmountAndCheckState(majorProduction);

        if (mergeType.equals(STATEMENT.name())){
            return this.update(new LambdaUpdateWrapper<MajorTarget>().set(MajorTarget::getStatementNo,toMergeNumber)
                    .in(BaseEntity::getId,targetIds));
        }
        if (mergeType.equals(REPORT.name())){
            MajorTarget one = this.list(new LambdaQueryWrapper<MajorTarget>().eq(MajorTarget::getReportNo, toMergeNumber)).get(0);
            return this.update(new LambdaUpdateWrapper<MajorTarget>().set(MajorTarget::getStatementNo,one.getStatementNo())
                            .set(MajorTarget::getReportNo,toMergeNumber)
                    .in(BaseEntity::getId,targetIds));

        }
        if (mergeType.equals(LETTER.name())){
            MajorTarget one = this.list(new LambdaQueryWrapper<MajorTarget>().eq(MajorTarget::getLetterNo, toMergeNumber)).get(0);
            return this.update(new LambdaUpdateWrapper<MajorTarget>().set(MajorTarget::getStatementNo,one.getStatementNo())
                    .set(MajorTarget::getLetterNo,toMergeNumber)
                    .in(BaseEntity::getId,targetIds));
        }
        return Boolean.FALSE;
    }

}
