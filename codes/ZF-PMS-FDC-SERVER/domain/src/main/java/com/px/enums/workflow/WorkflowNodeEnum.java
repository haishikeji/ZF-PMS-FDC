package com.px.enums.workflow;

import com.px.enums.CodeMsgEnumInterface;


/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/10/17
 * created with IntelliJ IDEA.
 */
public enum WorkflowNodeEnum implements CodeMsgEnumInterface<String,String>{
        OPENING_ORDER("业务下单","OPENING_ORDER"),
        DEPARTMENT_ALLOCATION("业务部分单","DEPARTMENT_ALLOCATION"),
        CONTACT_DEVELOP_PLAN("联系客户拟定方案","CONTACT_DEVELOP_PLAN"),
        SPOT_RECONNAISSANCE("现场勘查","SPOT_RECONNAISSANCE"),
        INITIAL_PRICE("初步定价","INITIAL_PRICE"),
        REVIEW_QUOTATION("审核报价","REVIEW_QUOTATION"),
        FORMAL_QUOTATION("正式报价","FORMAL_QUOTATION"),
        QUOTATION_FEEDBACK("报价客户反馈","QUOTATION_FEEDBACK"),

        WRITE_STATEMENT("撰写价值意见书","WRITE_STATEMENT"),
        CHECK_STATEMENT("审核价值意见书","CHECK_STATEMENT"),
        RECHECK_STATEMENT("复审价值意见书","RECHECK_STATEMENT"),
        THIRD_CHECK_STATEMENT("三审价值意见书","THIRD_CHECK_STATEMENT"),
        STATEMENT_BOOKBINDING_STAMP("意见书装订盖章","STATEMENT_BOOKBINDING_STAMP"),
        STATEMENT_IN("意见书入库","STATEMENT_IN"),

        STATEMENT_OUT("意见书出库","STATEMENT_OUT"),
        STATEMENT_DELIVERY("意见书送达","STATEMENT_DELIVERY"),
        STATEMENT_FEEDBACK("意见书送达反馈","STATEMENT_FEEDBACK"),
        WRITE_REPORT("撰写报告","WRITE_REPORT"),
        CHECK_REPORT("审核报告","CHECK_REPORT"),
        LAND_REPORT_NOT_RECORD_CHECK("不备案审核","LAND_REPORT_NOT_RECORD_CHECK"),
        RECHECK_REPORT("复审报告","RECHECK_REPORT"),
        THIRD_CHECK_REPORT("三审报告","THIRD_CHECK_REPORT"),
        FOURTH_CHECK_REPORT("四审报告","FOURTH_CHECK_REPORT"),
        REPORT_BOOKBINDING_STAMP("报告装订盖章","REPORT_BOOKBINDING_STAMP"),
        REPORT_IN("报告入库","REPORT_IN"),

        REPORT_OUT_APPLY("报告出库申请","REPORT_OUT_APPLY"),
        REPORT_OUT_DEPARTMENT_CHECK("报告出库部门审核","REPORT_OUT_DEPARTMENT_CHECK"),
        REPORT_OUT_FINANCE_CHECK("报告出库财务审核","REPORT_OUT_FINANCE_CHECK"),
        REPORT_OUT("报告出库","REPORT_OUT"),
        WRITE_LETTER("撰写复评函","WRITE_LETTER"),
        CHECK_LETTER("审核复评函","CHECK_LETTER"),
        RECHECK_LETTER("复审复评函","RECHECK_LETTER"),
        THIRD_CHECK_LETTER("三审复评函","THIRD_CHECK_LETTER"),
        LETTER_BOOKBINDING_STAMP("复评函装订盖章","LETTER_BOOKBINDING_STAMP"),
        LETTER_IN("复评函入库","LETTER_IN"),

        LETTER_OUT_APPLY("复评函出库申请","LETTER_OUT_APPLY"),
        LETTER_OUT_DEPARTMENT_CHECK("复评函出库部门审核","LETTER_OUT_DEPARTMENT_CHECK"),
        LETTER_OUT_FINANCE_CHECK("复评函出库财务审核","LETTER_OUT_FINANCE_CHECK"),
        LETTER_OUT("复评函出库","LETTER_OUT"),

        CHECK_ARCHIVING("归档审核","CHECK_ARCHIVING"),
        RECHECK_ARCHIVING("复审部归档审核","RECHECK_ARCHIVING"),
        BUSINESS_ARCHIVING("归档","BUSINESS_ARCHIVING"),

        DETERMINE_PRICE("定价","DETERMINE_PRICE"),
        GENERATE_STATEMENT("撰写意见书","GENERATE_STATEMENT"),
        REVIEW_STATEMENT("审核意见书","REVIEW_STATEMENT"),
        REEXAMINE_STATEMENT("复审意见书","REEXAMINE_STATEMENT"),
        COMMIT_FILE("提交归档资料","COMMIT_FILE"),
        PRODUCT_CHOICE("产品选择","PRODUCT_CHOICE"),
        SPOT_RECONNAISSANCE_DETERMINE_PRICE("勘察定价","SPOT_RECONNAISSANCE_DETERMINE_PRICE"),
        REPORT_CHOICE("报告选择","REPORT_CHOICE"),
        ASSET_REPORT_TAKE_NO("资产报告取号","ASSET_REPORT_TAKE_NO"),

        DEPARTMENT_LEADER_CHECK("部门经理审批","DEPARTMENT_LEADER_CHECK"),

        VICE_MANAGER_CHECK("分管副总审批","VICE_MANAGER_CHECK"),

        TOP_MANAGER_CHECK("总经理审批","TOP_MANAGER_CHECK"),

        GROUP_LEADER_CHECK("组长审批", "GROUP_LEADER_CHECK"),

        COMMIT_ARCHIVING("提交归档","COMMIT_ARCHIVING");

        ;


        private String name;

        private String code;


    WorkflowNodeEnum(String name, String code) {
            this.name = name;
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getMsg() {
            return name;
        }
    }

