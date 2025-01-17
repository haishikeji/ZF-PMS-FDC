package com.px.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.px.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;
/**
 * <p>
 * 工作流节点实例
 * </p>
 *
 * @author 品讯科技
 * @since 2023-10-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "工作流节点实例")
public class WorkFlowNodeInstance extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 流程id
     */
    private Long flowId;

    /**
     * 节点id
     */
    private Long nodeId;

    /**
     * 业务主表id
     */
    private Long businessId;

    /**
     * 二级业务id
     */
    private String businessSubId;

    /**
     * 三级业务id
     */
    private String businessMinId;

    /**
     * 业务类型
     */
    @ImportCell
    @ExportCell(columnName = "业务类型")
    private String businessType;

    /**
     * 节点序列
     */
    @ImportCell
    @ExportCell(columnName = "节点序列")
    private Integer sequence;

    /**
     * 节点状态（未开始、进行中、已完成）
     */
    @ImportCell
    @ExportCell(columnName = "节点状态（未开始、进行中、已完成）")
    private String state;

    /**
     * 备注
     */
    @ImportCell
    @ExportCell(columnName = "备注")
    private String comment;

    @TableField(exist = false)
    private String verify;

    @TableField(exist = false)
    private String nodeCode;

}
