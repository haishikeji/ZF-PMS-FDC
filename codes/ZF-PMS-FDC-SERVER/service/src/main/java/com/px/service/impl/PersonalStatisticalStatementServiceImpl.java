package com.px.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.dto.PersonalStatisticalSelectDTO;
import com.px.mapper.PersonalStatisticalStatementMapper;
import com.px.service.IPersonalStatisticalStatementService;
import com.px.vo.PersonalDepEfficiencyVO;
import com.px.vo.PersonalEfficiencyDetailVO;
import com.px.vo.PersonalInwardEfficiencyVO;
import com.px.vo.PersonalOutwardEfficiencyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalStatisticalStatementServiceImpl implements IPersonalStatisticalStatementService {

    @Autowired
    private PersonalStatisticalStatementMapper personalStatisticalStatementMapper;

    /**
     * 个贷部门效率值查询
     * @param dto 查询dto
     * @return List<PersonalDepEfficiencyVO>
     */
    @Override
    public List<PersonalDepEfficiencyVO> getPersonalDepEfficiencyVO(PersonalStatisticalSelectDTO dto) {
        List<PersonalDepEfficiencyVO> efficiencyVOList = personalStatisticalStatementMapper.getPersonalDepEfficiencyVO(dto);
        // 循环计算每个评估目的的同比、环比
        for (PersonalDepEfficiencyVO efficiencyVO : efficiencyVOList) {

            /*
             * 计算死单率
             * 死单率(下单数-出报告数) / (下单数)
             * 同比环比同理,公式来源于老系统SQL
             */
            if (efficiencyVO.getCurrentPeriodOrderVolume() > 0 && efficiencyVO.getYoYReportVolume() > 0 && efficiencyVO.getQoQReportVolume() > 0) {
                efficiencyVO.setCurrentPeriodDeadOrderVolume(NumberUtil.round((double) (efficiencyVO.getCurrentPeriodOrderVolume() - efficiencyVO.getCurrentPeriodReportVolume()) / efficiencyVO.getCurrentPeriodOrderVolume() * 100, 2).doubleValue());
                efficiencyVO.setQoQDeadOrderVolume(NumberUtil.round((efficiencyVO.getCurrentPeriodOrderVolume() - efficiencyVO.getYoYReportVolume()) / efficiencyVO.getYoYReportVolume() * 100, 2).doubleValue());
                efficiencyVO.setYoYDeadOrderVolume(NumberUtil.round((efficiencyVO.getCurrentPeriodOrderVolume() - efficiencyVO.getQoQReportVolume()) / efficiencyVO.getQoQReportVolume() * 100, 2).doubleValue());
            } else {
                efficiencyVO.setCurrentPeriodDeadOrderVolume(0.0);
                efficiencyVO.setQoQDeadOrderVolume(0.0);
                efficiencyVO.setYoYDeadOrderVolume(0.0);
            }

            // 计算当期出单率(收费项目数/个贷订单数)
            if (efficiencyVO.getCurrentPeriodRealAmountOrderCount() > 0) {
                efficiencyVO.setCurrentPeriodOrderHasReportVolume((efficiencyVO.getCurrentPeriodRealAmountOrderCount().doubleValue() / efficiencyVO.getCurrentPeriodOrderVolume().doubleValue()));
            } else {
                efficiencyVO.setCurrentPeriodOrderHasReportVolume(0.0);
            }
            // 计算同比出单率
            if (efficiencyVO.getYoYRealAmountOrderCount() > 0) {
                efficiencyVO.setYoYOrderHasReportVolume(NumberUtil.round((efficiencyVO.getCurrentPeriodOrderHasReportVolume() - (efficiencyVO.getYoYRealAmountOrderCount() / efficiencyVO.getYoYOrderVolume())) / (efficiencyVO.getYoYRealAmountOrderCount() / efficiencyVO.getYoYOrderVolume()) * 100, 2).doubleValue());
            } else {
                efficiencyVO.setYoYOrderHasReportVolume(NumberUtil.round(efficiencyVO.getCurrentPeriodOrderHasReportVolume() * 100, 2).doubleValue());
            }
            // 计算环比出单率
            if (efficiencyVO.getQoQRealAmountOrderCount() > 0) {
                efficiencyVO.setQoQOrderHasReportVolume(NumberUtil.round((efficiencyVO.getCurrentPeriodOrderHasReportVolume() - (efficiencyVO.getQoQRealAmountOrderCount() / efficiencyVO.getQoQOrderVolume())) / (efficiencyVO.getQoQRealAmountOrderCount() / efficiencyVO.getQoQOrderVolume()) * 100, 2).doubleValue());
            } else {
                efficiencyVO.setQoQOrderHasReportVolume(NumberUtil.round(efficiencyVO.getCurrentPeriodOrderHasReportVolume() * 100, 2).doubleValue());
            }
            efficiencyVO.setCurrentPeriodOrderHasReportVolume(NumberUtil.round(efficiencyVO.getCurrentPeriodOrderHasReportVolume() * 100, 2).doubleValue());

            // 同比计算(同比增长率 = (当期数据 - 去年同期数据) / 去年同期数据 * 100%) (保留两位小数)
            if (efficiencyVO.getYoYOrderVolume() > 0) {
                efficiencyVO.setYoYOrderVolume(NumberUtil.round((efficiencyVO.getCurrentPeriodOrderVolume() - efficiencyVO.getYoYOrderVolume()) / efficiencyVO.getYoYOrderVolume() * 100, 2).doubleValue());
            } else {
                efficiencyVO.setYoYOrderVolume((double) (efficiencyVO.getCurrentPeriodOrderVolume() * 100));
            }
            if (efficiencyVO.getYoYStatementVolume() > 0) {
                efficiencyVO.setYoYStatementVolume(NumberUtil.round((efficiencyVO.getCurrentPeriodStatementVolume() - efficiencyVO.getYoYStatementVolume()) / efficiencyVO.getYoYStatementVolume() * 100, 2).doubleValue());
            } else {
                efficiencyVO.setYoYStatementVolume((double) (efficiencyVO.getCurrentPeriodStatementVolume() * 100));
            }
            if (efficiencyVO.getYoYReportVolume() > 0) {
                efficiencyVO.setYoYReportVolume(NumberUtil.round((efficiencyVO.getCurrentPeriodReportVolume() - efficiencyVO.getYoYReportVolume()) / efficiencyVO.getYoYReportVolume() * 100, 2).doubleValue());
            } else {
                efficiencyVO.setYoYReportVolume((double) (efficiencyVO.getCurrentPeriodReportVolume() * 100));
            }
            if (efficiencyVO.getYoYShouldAmountSum() > 0) {
                efficiencyVO.setYoYShouldAmountSum(NumberUtil.round((efficiencyVO.getCurrentPeriodShouldAmountSum() - efficiencyVO.getYoYShouldAmountSum()) / efficiencyVO.getYoYShouldAmountSum() * 100, 2).doubleValue());
            } else {
                efficiencyVO.setYoYShouldAmountSum(efficiencyVO.getCurrentPeriodShouldAmountSum() * 100);
            }
            if (efficiencyVO.getYoYRealAmountSum() > 0) {
                efficiencyVO.setYoYRealAmountSum(NumberUtil.round((efficiencyVO.getCurrentPeriodRealAmountSum() - efficiencyVO.getYoYRealAmountSum()) / efficiencyVO.getYoYRealAmountSum() * 100, 2).doubleValue());
            } else {
                efficiencyVO.setYoYRealAmountSum(efficiencyVO.getCurrentPeriodRealAmountSum() * 100);
            }

            // 环比计算(环比增长率 = (当期数据 - 前一期数据) / 前一期数据 * 100%) (保留两位小数)
            if (efficiencyVO.getQoQOrderVolume() > 0) {
                efficiencyVO.setQoQOrderVolume(NumberUtil.round((efficiencyVO.getCurrentPeriodOrderVolume() - efficiencyVO.getQoQOrderVolume()) / efficiencyVO.getQoQOrderVolume() * 100, 2).doubleValue());
            } else {
                efficiencyVO.setQoQOrderVolume((double) (efficiencyVO.getCurrentPeriodOrderVolume() * 100));
            }
            if (efficiencyVO.getQoQStatementVolume() > 0) {
                efficiencyVO.setQoQStatementVolume(NumberUtil.round((efficiencyVO.getCurrentPeriodStatementVolume() - efficiencyVO.getQoQStatementVolume()) / efficiencyVO.getQoQStatementVolume() * 100, 2).doubleValue());
            } else {
                efficiencyVO.setQoQStatementVolume((double) (efficiencyVO.getCurrentPeriodStatementVolume() * 100));
            }
            if (efficiencyVO.getQoQReportVolume() > 0) {
                efficiencyVO.setQoQReportVolume(NumberUtil.round((efficiencyVO.getCurrentPeriodReportVolume() - efficiencyVO.getQoQReportVolume()) / efficiencyVO.getQoQReportVolume() * 100, 2).doubleValue());
            } else {
                efficiencyVO.setQoQReportVolume((double) (efficiencyVO.getCurrentPeriodReportVolume() * 100));
            }
            if (efficiencyVO.getQoQShouldAmountSum() > 0) {
                efficiencyVO.setQoQShouldAmountSum(NumberUtil.round((efficiencyVO.getCurrentPeriodShouldAmountSum() - efficiencyVO.getQoQShouldAmountSum()) / efficiencyVO.getQoQShouldAmountSum() * 100, 2).doubleValue());
            } else {
                efficiencyVO.setQoQShouldAmountSum(efficiencyVO.getCurrentPeriodShouldAmountSum() * 100);
            }
            if (efficiencyVO.getQoQRealAmountSum() > 0) {
                efficiencyVO.setQoQRealAmountSum(NumberUtil.round((efficiencyVO.getCurrentPeriodRealAmountSum() - efficiencyVO.getQoQRealAmountSum()) / efficiencyVO.getQoQRealAmountSum() * 100, 2).doubleValue());
            } else {
                efficiencyVO.setQoQRealAmountSum(efficiencyVO.getCurrentPeriodRealAmountSum() * 100);
            }

        }
        return efficiencyVOList;
    }

    /**
     * 获取效率值相关个贷订单列表
     * @param page 分页
     * @param dto 查询dto
     * @return Page<PersonalEfficiencyDetailVO>
     */
    @Override
    public Page<PersonalEfficiencyDetailVO> getPersonalEfficiencyDetailVO(Page page, PersonalStatisticalSelectDTO dto) {
        Page<PersonalEfficiencyDetailVO> efficiencyDetailVOPage = personalStatisticalStatementMapper.getPersonalEfficiencyDetailVO(page, dto);
        efficiencyDetailVOPage.setRecords(setPurpose(efficiencyDetailVOPage.getRecords()));
        return efficiencyDetailVOPage;
    }

    /**
     * 导出效率值相关个贷订单列表
     * @param dto 查询dto
     * @return List<PersonalEfficiencyDetailVO>
     */
    @Override
    public List<PersonalEfficiencyDetailVO> exportPersonalEfficiencyDetailVO(PersonalStatisticalSelectDTO dto) {
        List<PersonalEfficiencyDetailVO> efficiencyDetailVOList = personalStatisticalStatementMapper.exportPersonalEfficiencyDetailVO(dto);
        return setPurpose(efficiencyDetailVOList);
    }

    /**
     * 个贷外业效率值查询
     * @param dto 查询dto
     * @return List<PersonalOutwardEfficiencyVO>
     */
    @Override
    public List<PersonalOutwardEfficiencyVO> getPersonalOutwardEfficiencyVO(PersonalStatisticalSelectDTO dto) {
        return personalStatisticalStatementMapper.getPersonalOutwardEfficiencyVO(dto);
    }

    /**
     * 个贷内业效率值查询
     * @param dto 查询dto
     * @return List<PersonalInwardEfficiencyVO>
     */
    @Override
    public List<PersonalInwardEfficiencyVO> getPersonalInwardEfficiencyVO(PersonalStatisticalSelectDTO dto) {
        return personalStatisticalStatementMapper.getPersonalInwardEfficiencyVO(dto);
    }

    /**
     * 设置个贷的用途
     * @param detailVOList 订单集合
     * @return List<PersonalEfficiencyDetailVO>
     */
    public List<PersonalEfficiencyDetailVO> setPurpose(List<PersonalEfficiencyDetailVO> detailVOList){
        for (PersonalEfficiencyDetailVO efficiencyVO : detailVOList){
            JSONArray jsonArray = JSONArray.parseArray(efficiencyVO.getPurpose());
            List<String> purposeCodeList = jsonArray.toJavaList(String.class);
            StringBuilder purpose = new StringBuilder();
            int index = 0;
            for (String purposeCode : purposeCodeList){
                switch (purposeCode) {
                    case "HOUSE":
                        purpose.append("住宅");
                        break;
                    case "BUSINESS":
                        purpose.append("商业");
                        break;
                    case "OFFICE":
                        purpose.append("办公");
                        break;
                    case "OTHER":
                        purpose.append("工业");
                        break;
                    default:
                        purpose.append("其他");
                        break;
                }
                index++;
                if (index < purposeCodeList.size()) {
                    purpose.append("，");
                }
            }
            efficiencyVO.setPurpose(String.valueOf(purpose));
        }
        return detailVOList;
    }
}
