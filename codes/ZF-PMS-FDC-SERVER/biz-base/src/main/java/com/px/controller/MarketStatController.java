package com.px.controller;

import com.px.annotation.DataPermission;
import com.px.common.RestResponse;
import com.px.dto.MarketStatDTO;
import com.px.service.IMarketStatService;
import com.px.vo.MarketStatVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.px.common.Constants.MANAGER_OFFICE;

/**
 * 全局配置
 *
 * @author 品讯科技
 * @since 2023-01-13
 */
@RestController
@RequestMapping("marketStat")
@Slf4j
public class MarketStatController {

    @Autowired
    private IMarketStatService marketStatService;

    /**
     * 市场首页统计数据
     * @return
     */
    @DataPermission(teamData = true,postChild = false,ignoreDepartment = {MANAGER_OFFICE})
    @GetMapping("")
    public RestResponse<MarketStatVO> marketStat(MarketStatDTO dto){
        MarketStatVO marketStatVO = marketStatService.marketStat(dto);
        return RestResponse.data(marketStatVO);
    }
}
