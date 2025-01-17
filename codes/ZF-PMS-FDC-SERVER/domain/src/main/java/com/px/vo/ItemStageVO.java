package com.px.vo;

import com.px.entity.ItemStage;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2022/12/8
 * created with IntelliJ IDEA.
 */
@Data
public class ItemStageVO extends ItemStage {

    /**
     * 阶段状态（未开始，进行中，已完成）
     */
    private String state;

    private String dutyer;

    private String itemName;

    private Set<Long> userIds;

    private Set<ParticipatorWeight> participators = new HashSet<>();

    private List<Long> departmentIds;

    private Set<ItemStageUserVO> participated;

    @Data
    public static class ParticipatorWeight{

        private Long userId;

        private BigDecimal weight;

        private String userName;
    }

}
