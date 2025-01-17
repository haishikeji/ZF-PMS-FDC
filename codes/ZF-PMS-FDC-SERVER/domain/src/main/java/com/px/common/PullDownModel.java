package com.px.common;

import lombok.Data;

/**
 * 简单下拉列表
 */
@Data
public class PullDownModel implements SimpleModel<Long, String> {
    /**
     * id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;

    @Override
    public Long getKey() {
        return id;
    }

    @Override
    public String getValue() {
        return name;
    }
}
