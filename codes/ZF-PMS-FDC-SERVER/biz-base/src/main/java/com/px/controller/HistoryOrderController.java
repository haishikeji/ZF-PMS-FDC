package com.px.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.common.RestResponse;
import com.px.dto.history.HisAssetsOrder;
import com.px.dto.history.HisMajorOrder;
import com.px.dto.history.HisOrderParam;
import com.px.dyoa.IDyoaHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类说明：原系统订单查询
 *
 * @author: wucl
 * @since: 2024/5/8
 * created with IntelliJ IDEA.
 */
@RestController
@RequestMapping("dyoa")
@Slf4j
public class HistoryOrderController {

    @Autowired
    private IDyoaHistoryService dyoaHistoryService;

    /**
     * 获取原系统cookie
     * @return
     */
    @GetMapping("/cookie")
    public RestResponse<String> getEverCookie(){
        String cookie = dyoaHistoryService.getEverCookie();
        return RestResponse.data(cookie);
    }

    /**
     * 原系统大中型订单
     * @param hisOrderParam
     * @return
     */
    @GetMapping("/major/page")
    public RestResponse<Page<HisMajorOrder>> page(HisOrderParam hisOrderParam){
        Page<HisMajorOrder> pages = dyoaHistoryService.hisMajorPage(hisOrderParam);
        return RestResponse.data(pages);
    }

    /**
     * 获取老系统资产订单
     * @param hisOrderParam 老系统查询参数
     * @return Page<HisAssetsOrder>
     */
    @GetMapping("/assets/page")
    public RestResponse<Page<HisAssetsOrder>> hisAssetsPage(HisOrderParam hisOrderParam){
        Page<HisAssetsOrder> pages = dyoaHistoryService.hisAssetsPage(hisOrderParam);
        return RestResponse.data(pages);
    }
}
