package com.px.vo;

import com.px.bo.Tree;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/8/29
 * created with IntelliJ IDEA.
 */
@Data
public class ReportSectionTreeVO implements Tree<Long,String> {

    /**
     * id
     */
    private Long id;

    /**
     * 段落id
     */
    private Long sectionId;

    /**
     * 报告id
     */
    private Long reportId;

    /**
     * 标题
     */
    private String headline;

    /**
     * 父标题id
     */
    private Long parentId;

    /**
     * 根节点id
     */
    private Long rootId;

    /**
     * 序号
     */
    private Integer orderNum;

    /**
     * 段落内容
     */
    private String section;

    /**
     * 上次修改人
     */
    private String preUserName;

    /**
     * 上次修改时间
     */
    private LocalDateTime modified;

    /**
     * 算法名称
     */
    private String action;

    private List<ReportSectionTreeVO> children;

    @Override
    public String getName() {
        return headline;
    }

    @Override
    public int order() {
        return orderNum;
    }

    @Override
    public void setChildren(List<? extends Tree<Long, String>> children) {
        this.children = (List<ReportSectionTreeVO>) children;
    }
}
