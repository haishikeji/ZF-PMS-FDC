package com.px.vo;

import com.px.annotation.Excel;
import lombok.Data;

@Data
public class MajorPerformanceDeductionVO {

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 员工姓名
     */
    @Excel(name = "员工姓名")
    private String userName;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    private String departmentName;

    /**
     * 审核一般错误扣分(初审)
     */
    @Excel(name = "部门审核一般错误")
    private Integer checkNormalMistakeScore;

    /**
     * 审核较大错误扣分(初审)
     */
    @Excel(name = "部门审核较大错误")
    private Integer checkHardMistakeScore;

    /**
     * 审核严重错误扣分(初审)
     */
    @Excel(name = "部门审核严重错误")
    private Integer checkFatalMistakeScore;

    /**
     * 复审一般错误扣分
     */
    @Excel(name = "复审一般错误")
    private Integer reCheckNormalMistakeScore;

    /**
     * 复审较大错误扣分
     */
    @Excel(name = "复审较大错误")
    private Integer reCheckHardMistakeScore;

    /**
     * 复审严重错误扣分
     */
    @Excel(name = "复审严重错误")
    private Integer reCheckFatalMistakeScore;

    /**
     * 外部投诉一般错误扣分
     */
    @Excel(name = "外部投诉一般错误")
    private Integer externalComplaintNormalMistakeScore;

    /**
     * 外部投诉较大错误扣分
     */
    @Excel(name = "外部投诉较大错误")
    private Integer externalComplaintHardMistakeScore;

    /**
     * 外部投诉严重错误扣分
     */
    @Excel(name = "外部投诉严重错误")
    private Integer externalComplaintFatalMistakeScore;

    /**
     * 外部投诉服务态度恶劣
     */
    @Excel(name = "服务态度恶劣")
    private Integer badServiceAttitude;

    /**
     * 内部投诉一般错误扣分
     */
    @Excel(name = "内部投诉一般错误")
    private Integer internalComplaintNormalMistakeScore;

    /**
     * 内部投诉较大错误扣分
     */
    @Excel(name = "内部投诉较大错误")
    private Integer internalComplaintHardMistakeScore;

    /**
     * 内部投诉严重错误扣分
     */
    @Excel(name = "内部投诉严重错误")
    private Integer internalComplaintFatalMistakeScore;

    /**
     * 疑难项目加减分
     */
    @Excel(name = "疑难项目加减分")
    private Integer difficultScore;

    /**
     * 总经理加减分
     */
    @Excel(name = "总经理加减分")
    private Integer generalManagerScore;

    /**
     * 部门综合评分
     */
    @Excel(name = "部门综合评分")
    private Integer integratedScore;

    /**
     * 扣分合计
     */
    @Excel(name = "扣分合计")
    private Integer allMistakeScore;

}
