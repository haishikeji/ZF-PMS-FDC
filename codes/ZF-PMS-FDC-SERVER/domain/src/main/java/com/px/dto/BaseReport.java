package com.px.dto;

import com.deepoove.poi.data.DocxRenderData;
import com.deepoove.poi.data.TextRenderData;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/8/9
 * created with IntelliJ IDEA.
 */
@Builder
@Data
public class BaseReport {

    /**
     * 项目名称
     */
    private String itemName;
    /**
     * 报告类型
     */
    private String reportType;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 年月（大写）
     */
    private String yearMonthUppercase;
    /**
     * 项目单位
     */
    private String clientUnit;
    /**
     * 年月 （小写）
     */
    private String yearMonthLowercase;
    /**
     * 项目负责人 + 职位
     */
    private String principalAndTitle;
    /**
     * 编制人员列表 + 职位
     */
    private List<String> memberAndTitles;
    /**
     * 审核人员 + 职位
     */
    private String checkerAndTitle;
    /**
     * 前言
     */
    private String introduction;
    /**
     * 基础内容标签 ，确定了目录结构后会被替换成其他模板标签
     */
    private List<TextRenderData> baseContentLabel;
    /**
     * 评估日期
     */
    private Date evaluateDate;

    /**
     * 目录
     */
    private String catalogue;

    private DocxRenderData titles;
}
