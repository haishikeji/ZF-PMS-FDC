package com.px.controller;

import com.px.annotation.DataPermission;
import com.px.annotation.OperLog;
import com.px.dto.DataPermissionCondition;
import com.px.vo.ItemLogCollectVO;
import com.px.vo.MarketLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IMarketLogService;
import com.px.entity.MarketLog;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.px.common.Constants.MANAGER_OFFICE;

/**
 * 市场人员日志
 *
 * @author 品讯科技
 * @since 2023-01-13
 */
@RestController
@RequestMapping("marketLog")
@Slf4j
public class MarketLogController extends BaseController {
    @Autowired
    private IMarketLogService marketLogService;

    /**
    * 市场人员日志列表
    */
    @GetMapping("")
    public RestResponse<Page<MarketLogVO>> page(MarketLog marketLog, Page page){
        Page<MarketLogVO> pages=marketLogService.selectPage(page,marketLog);
        return RestResponse.data(pages);
    }

    /**
     * 市场人员日志详情
     */
    @GetMapping("/{id}")
    public RestResponse<MarketLog> detail(@PathVariable Long id){
        MarketLog xMarketLog =marketLogService.detail(id);
        return RestResponse.data(xMarketLog);
     }

    /**
     * 市场人员日志新增
     */
    @OperLog(title = "新增市场人员日志")
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody MarketLog marketLog) {
        Boolean ret = marketLogService.add(marketLog);
        return RestResponse.data(ret);
    }

    /**
     * 市场人员日志更新
     */
    @OperLog(title = "更新市场人员日志")
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody MarketLog marketLog) {
        Boolean ret = marketLogService.update(marketLog);
        return RestResponse.data(ret);
    }

    /**
     * 市场人员日志删除
     */
    @OperLog(title = "删除市场人员日志")
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = marketLogService.delete(id);
        return RestResponse.data(ret);
    }

    /**
     * 日志汇总列表
     * @return
     */
    @DataPermission(teamData = true,postChild = false,ignoreDepartment = {MANAGER_OFFICE})
    @GetMapping("/collect")
    public RestResponse<List<ItemLogCollectVO>> logCollect(DataPermissionCondition condition){
        List<ItemLogCollectVO> result = marketLogService.logCollect(condition);
        return RestResponse.data(result);
    }

}

