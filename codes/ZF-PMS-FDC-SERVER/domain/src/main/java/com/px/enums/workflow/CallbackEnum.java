package com.px.enums.workflow;

import com.px.enums.MainBusinessEnum;
import com.px.enums.ProductionEnum;
import lombok.Data;

import java.util.*;

import static com.px.enums.workflow.WorkflowNodeEnum.*;

/**
 * 类说明：节点回调校验配置
 *
 * @author: wucl
 * @since: 2023/10/27
 * created with IntelliJ IDEA.
 */
public enum CallbackEnum {

    NONE("NONE"),
    /**
     * 大中型评估对象审核校验
     */
    CHECK_PRODUCTION_CHECK("CHECK_PRODUCTION_CHECK"),

    /**
     * 大中型出具产品类型校验
     */
    CHECK_PRODUCTION_TYPE("CHECK_PRODUCTION_TYPE"),

    /**
     * 大中型产品已收款校验
     */
    CHECK_PRODUCTION_FUND("CHECK_PRODUCTION_FUND"),

    /**
     * 个贷报价反馈节点校验
     */
    CHECK_FEEDBACK("CHECK_FEEDBACK"),
    /**
     * 个贷出具产品类型校验
     */
    CHECK_PERSONAL_PRODUCTION_TYPE("CHECK_PERSONAL_PRODUCTION_TYPE"),

    /**
     *  个贷客户类型校验（如果是银行客户，报告出库无需申请）
     */
    CHECK_PERSONAL_CLIENT_TYPE("CHECK_PERSONAL_CLIENT_TYPE"),

    /**
     * 资产出具产品类型校验
     */
    CHECK_ASSET_PRODUCTION_TYPE("CHECK_ASSET_PRODUCTION_TYPE"),

    /**
     * 资产订单实收款校验
     */
    CHECK_ASSET_ORDER_FUND("CHECK_ASSET_ORDER_FUND"),

    /**
     * 资产报告四审校验
     */
    CHECK_ASSET_FOURTH_CHECK_REPORT("CHECK_ASSET_FOURTH_CHECK_REPORT"),

    /**
     * 大中型土地报告备案校验
     */
    CHECK_LAND_REPORT_IS_RECORD("CHECK_LAND_REPORT_IS_RECORD"),
    ;

    private String code;

    private List<NecessaryNode> necessaryNodes;


    CallbackEnum(String code) {
        this.code = code;
        this.necessaryNodes  = new ArrayList<>();
        if ("CHECK_PRODUCTION_TYPE".equals(code)){
            //价值意见书在校验产品类型的节点
            NecessaryNode valueStatement = new NecessaryNode();
            valueStatement.setProduction(ProductionEnum.STATEMENT);
            WorkflowNodeEnum[] workflowNodeEnums = {WRITE_STATEMENT,CHECK_STATEMENT,RECHECK_STATEMENT,THIRD_CHECK_STATEMENT,STATEMENT_BOOKBINDING_STAMP,
                    STATEMENT_IN,STATEMENT_OUT,STATEMENT_DELIVERY,STATEMENT_FEEDBACK};
            valueStatement.setNecessaryNodes(workflowNodeEnums);
            this.necessaryNodes.add(valueStatement);
            //报告在校验产品类型时节点
            NecessaryNode report = new NecessaryNode();
            report.setProduction(ProductionEnum.REPORT);
            WorkflowNodeEnum[] workflowNodeEnums1 = {WRITE_REPORT, CHECK_REPORT,LAND_REPORT_NOT_RECORD_CHECK,RECHECK_REPORT,THIRD_CHECK_REPORT,REPORT_BOOKBINDING_STAMP,REPORT_IN,REPORT_OUT_APPLY,REPORT_OUT_DEPARTMENT_CHECK,REPORT_OUT_FINANCE_CHECK,REPORT_OUT};
            report.setNecessaryNodes(workflowNodeEnums1);
            this.necessaryNodes.add(report);

            //意见函在校验产品类型时的节点
            NecessaryNode letter = new NecessaryNode();
            letter.setProduction(ProductionEnum.LETTER);
            WorkflowNodeEnum[] workflowNodeEnums2 = {WRITE_LETTER, CHECK_LETTER,RECHECK_LETTER,THIRD_CHECK_LETTER,LETTER_BOOKBINDING_STAMP,LETTER_IN,LETTER_OUT_APPLY,LETTER_OUT_DEPARTMENT_CHECK,LETTER_OUT_FINANCE_CHECK,LETTER_OUT};
            letter.setNecessaryNodes(workflowNodeEnums2);
            this.necessaryNodes.add(letter);
        }

        if ("CHECK_PERSONAL_PRODUCTION_TYPE".equals(code)){
            //价值意见书在校验产品类型的节点
            NecessaryNode valueStatement = new NecessaryNode();
            valueStatement.setProduction(ProductionEnum.STATEMENT);
            WorkflowNodeEnum[] workflowNodeEnums = {GENERATE_STATEMENT,REVIEW_STATEMENT,STATEMENT_BOOKBINDING_STAMP,STATEMENT_IN,STATEMENT_OUT,STATEMENT_FEEDBACK};
            valueStatement.setNecessaryNodes(workflowNodeEnums);
            this.necessaryNodes.add(valueStatement);
            //报告在校验产品类型时节点
            NecessaryNode report = new NecessaryNode();
            report.setProduction(ProductionEnum.REPORT);
            WorkflowNodeEnum[] workflowNodeEnums1 = {WRITE_REPORT, CHECK_REPORT,REPORT_BOOKBINDING_STAMP,REPORT_IN,REPORT_OUT_APPLY,REPORT_OUT_DEPARTMENT_CHECK,REPORT_OUT};
            report.setNecessaryNodes(workflowNodeEnums1);
            this.necessaryNodes.add(report);

            //意见函在校验产品类型时的节点
            NecessaryNode letter = new NecessaryNode();
            letter.setProduction(ProductionEnum.LETTER);
            WorkflowNodeEnum[] workflowNodeEnums2 = {WRITE_LETTER, CHECK_LETTER,LETTER_BOOKBINDING_STAMP,LETTER_IN,LETTER_OUT_APPLY,LETTER_OUT_DEPARTMENT_CHECK,LETTER_OUT};
            letter.setNecessaryNodes(workflowNodeEnums2);
            this.necessaryNodes.add(letter);
        }

        if ("CHECK_ASSET_PRODUCTION_TYPE".equals(code)){
            //价值意见书在校验产品类型的节点
            NecessaryNode valueStatement = new NecessaryNode();
            valueStatement.setProduction(ProductionEnum.STATEMENT);
            WorkflowNodeEnum[] statementWorkflowNodeEnums = {GENERATE_STATEMENT,REVIEW_STATEMENT,REEXAMINE_STATEMENT,STATEMENT_BOOKBINDING_STAMP,STATEMENT_IN,STATEMENT_FEEDBACK,STATEMENT_OUT,REPORT_CHOICE};
            valueStatement.setNecessaryNodes(statementWorkflowNodeEnums);
            this.necessaryNodes.add(valueStatement);

            //评估报告在校验产品类型时节点
            NecessaryNode report = new NecessaryNode();
            report.setProduction(ProductionEnum.REPORT);
            WorkflowNodeEnum[] reportWorkflowNodeEnums = {ASSET_REPORT_TAKE_NO,WRITE_REPORT, CHECK_REPORT,RECHECK_REPORT,FOURTH_CHECK_REPORT,REPORT_BOOKBINDING_STAMP,REPORT_IN,REPORT_OUT_APPLY,REPORT_OUT_DEPARTMENT_CHECK,REPORT_OUT,CHECK_ARCHIVING,RECHECK_ARCHIVING,BUSINESS_ARCHIVING};
            report.setNecessaryNodes(reportWorkflowNodeEnums);
            this.necessaryNodes.add(report);

            //咨询报告在校验产品类型时节点
            NecessaryNode consult = new NecessaryNode();
            consult.setProduction(ProductionEnum.CONSULT);
            WorkflowNodeEnum[] consultWorkflowNodeEnums = {ASSET_REPORT_TAKE_NO,WRITE_REPORT, CHECK_REPORT,RECHECK_REPORT,FOURTH_CHECK_REPORT,REPORT_BOOKBINDING_STAMP,REPORT_IN,REPORT_OUT_APPLY,REPORT_OUT_DEPARTMENT_CHECK,REPORT_OUT,CHECK_ARCHIVING,RECHECK_ARCHIVING,BUSINESS_ARCHIVING};
            consult.setNecessaryNodes(consultWorkflowNodeEnums);
            this.necessaryNodes.add(consult);

            //复评函在校验产品类型时的节点
            NecessaryNode letter = new NecessaryNode();
            letter.setProduction(ProductionEnum.LETTER);
            WorkflowNodeEnum[] letterWorkflowNodeEnums = {ASSET_REPORT_TAKE_NO,WRITE_REPORT, CHECK_REPORT,RECHECK_REPORT,FOURTH_CHECK_REPORT,REPORT_BOOKBINDING_STAMP,REPORT_IN,REPORT_OUT_APPLY,REPORT_OUT_DEPARTMENT_CHECK,REPORT_OUT,CHECK_ARCHIVING,RECHECK_ARCHIVING,BUSINESS_ARCHIVING};
            letter.setNecessaryNodes(letterWorkflowNodeEnums);
            this.necessaryNodes.add(letter);
        }
    }

    public String getCode() {
        return code;
    }

    public List<NecessaryNode> getNecessaryNodes(){
        return necessaryNodes;
    }

    @Data
    public static
    class NecessaryNode{

        private ProductionEnum production;

        private WorkflowNodeEnum [] necessaryNodes;


        public static  Map<String,List<WorkflowNodeEnum>> getByProduction(String businessType ,List<ProductionEnum> production){
            Map<String,List<WorkflowNodeEnum>> map = new HashMap<>();
            List<NecessaryNode> necessaryNodes = new ArrayList<>();
            if (MainBusinessEnum.MAJOR_BUSINESS.name().equals(businessType)){
                necessaryNodes = CHECK_PRODUCTION_TYPE.getNecessaryNodes();
            }
            if (MainBusinessEnum.PERSONAL_BUSINESS.name().equals(businessType)){
                necessaryNodes = CHECK_PERSONAL_PRODUCTION_TYPE.getNecessaryNodes();
            }
            if (MainBusinessEnum.ASSET_BUSINESS.name().equals(businessType)){
                necessaryNodes = CHECK_ASSET_PRODUCTION_TYPE.getNecessaryNodes();
            }
            for (NecessaryNode nn : necessaryNodes){
                if (production.contains(nn.getProduction())){
                    map.put(nn.getProduction().name(),Arrays.asList(nn.getNecessaryNodes()));
                }

            }
            return map;
        }
    }
}
