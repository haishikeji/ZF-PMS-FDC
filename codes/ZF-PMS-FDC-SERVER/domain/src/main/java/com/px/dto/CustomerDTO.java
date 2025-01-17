package com.px.dto;

import com.px.annotation.Excel;
import com.px.entity.Customer;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/1/11
 * created with IntelliJ IDEA.
 */
@Data
public class CustomerDTO extends Customer {

    private List<Long> citys;

    /**
     * 市
     */
    @Excel( name="市")
    private String firstCity;

    /**
     * 区县
     */
    @Excel(name="区县")
    private String secCity;

    /**
     * 所属员工查询条件
     */
    private Set<Long> userIds;

    /**
     * 客户经理
     */
    @Excel(name = "客户经理")
    private String userName;


    private Long originalUserId;
}
