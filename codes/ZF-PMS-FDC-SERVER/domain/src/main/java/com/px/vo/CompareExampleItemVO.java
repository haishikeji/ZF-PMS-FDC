package com.px.vo;

import com.px.dto.ItemData;
import lombok.Data;


/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2024/3/7
 * created with IntelliJ IDEA.
 */
@Data
public class CompareExampleItemVO {

    private Integer sorted;

    private String index;

    private ItemData compareA;

    private ItemData compareB;

    private ItemData compareC;

    private ItemData target;


    public CompareExampleItemVO(Integer sorted, String index) {
        this.sorted = sorted;
        this.index = index;
    }

    public CompareExampleItemVO() {
    }
}

