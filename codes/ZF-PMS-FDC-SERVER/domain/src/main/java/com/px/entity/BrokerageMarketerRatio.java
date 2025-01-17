package com.px.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.px.common.BaseEntity;
import com.px.enums.MergeKeyInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.annotation.ExcelSheet;
import com.px.annotation.ExportCell;
import com.px.annotation.ImportCell;

import java.math.BigDecimal;

import static com.px.common.Constants.MERGE_SYMBOL;

/**
 * <p>
 * 项目提成比例（市场人员）
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelSheet(sheetName = "项目提成比例（市场人员）")
public class BrokerageMarketerRatio extends BaseEntity implements MergeKeyInterface {

    private static final long serialVersionUID=1L;

    /**
     * 项目类型字典id
     */
    private Long itemCate;

    /**
     * 项目来源字典id
     */
    private Long itemSource;

    /**
     * 提成比例
     */
    @ImportCell
    @ExportCell(columnName = "提成比例")
    @TableField(value = "ratio", fill = FieldFill.UPDATE)
    private BigDecimal ratio;

    /**
     * 备注
     */
    @ImportCell
    @ExportCell(columnName = "备注")
    private String remark;

    @Override
    public String mergeKey() {
        return itemCate + MERGE_SYMBOL+ itemSource;
    }
}
