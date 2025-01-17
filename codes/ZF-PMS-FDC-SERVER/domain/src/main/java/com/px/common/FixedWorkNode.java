package com.px.common;

import com.px.enums.MainBusinessEnum;
import com.px.enums.workflow.WorkflowNodeEnum;

import java.util.Arrays;
import java.util.List;

import static com.px.enums.workflow.WorkflowNodeEnum.*;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/11/17
 * created with IntelliJ IDEA.
 */
public class FixedWorkNode {

    public static final List<WorkflowNodeEnum> MAJOR_FIXED_NODES = Arrays.asList(new WorkflowNodeEnum[]
            {DEPARTMENT_ALLOCATION,CONTACT_DEVELOP_PLAN,SPOT_RECONNAISSANCE,INITIAL_PRICE,REVIEW_QUOTATION,QUOTATION_FEEDBACK,
                    FORMAL_QUOTATION,CHECK_ARCHIVING, RECHECK_ARCHIVING, BUSINESS_ARCHIVING});

    public static final List<WorkflowNodeEnum> MAJOR_NOT_SAVE_FILE_NODES = Arrays.asList(new WorkflowNodeEnum[]
            {CHECK_ARCHIVING,RECHECK_ARCHIVING,BUSINESS_ARCHIVING});

    public static final List<WorkflowNodeEnum> PERSONAL_FIXED_NODES = Arrays.asList(new WorkflowNodeEnum[]
            {DEPARTMENT_ALLOCATION,CHECK_ARCHIVING,SPOT_RECONNAISSANCE,DETERMINE_PRICE,QUOTATION_FEEDBACK,COMMIT_FILE,CHECK_ARCHIVING,BUSINESS_ARCHIVING});

    public static final List<WorkflowNodeEnum> ASSET_FIXED_NODES = Arrays.asList(new WorkflowNodeEnum[]
            {DEPARTMENT_ALLOCATION,SPOT_RECONNAISSANCE_DETERMINE_PRICE,QUOTATION_FEEDBACK,PRODUCT_CHOICE,ASSET_REPORT_TAKE_NO,
                    WRITE_REPORT,CHECK_REPORT,RECHECK_REPORT,REPORT_BOOKBINDING_STAMP,
                    REPORT_IN,REPORT_OUT,COMMIT_ARCHIVING,CHECK_ARCHIVING, BUSINESS_ARCHIVING});

    public static  List<WorkflowNodeEnum> getFixedNodesByBusinessType(String businessType){
        if (MainBusinessEnum.MAJOR_BUSINESS.name().equals(businessType)){
            return MAJOR_FIXED_NODES;
        }
        if (MainBusinessEnum.PERSONAL_BUSINESS.name().equals(businessType)){
            return PERSONAL_FIXED_NODES;
        }
        if (MainBusinessEnum.ASSET_BUSINESS.name().equals(businessType)){
            return ASSET_FIXED_NODES;
        }
        return null;
    }

    public static List<WorkflowNodeEnum> removeSaveFileNode(String businessType){
        if (MainBusinessEnum.MAJOR_BUSINESS.name().equals(businessType)){
            return MAJOR_NOT_SAVE_FILE_NODES;
        }
        return null;
    }

}
