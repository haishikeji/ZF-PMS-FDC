package com.px.controller;

import com.px.vo.MarketerRatioVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IBrokerageMarketerRatioService;
import com.px.entity.BrokerageMarketerRatio;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;

/**
 * 项目提成比例（市场人员）
 *
 * @author 品讯科技
 * @since 2023-03-03
 */
@RestController
@RequestMapping("brokerageMarketerRatio")
@Slf4j
public class BrokerageMarketerRatioController extends BaseController {
    @Autowired
    private IBrokerageMarketerRatioService brokerageMarketerRatioService;

    /**
    * 项目提成比例（市场人员）列表
    */
    @GetMapping("")
    public RestResponse<Page<MarketerRatioVO>> page(BrokerageMarketerRatio brokerageMarketerRatio, Page page){
        Page<MarketerRatioVO> pages=brokerageMarketerRatioService.selectPage(page,brokerageMarketerRatio);
        return RestResponse.data(pages);
    }

    /**
     * 项目提成比例（市场人员）详情
     */
    @GetMapping("/{id}")
    public RestResponse<BrokerageMarketerRatio> detail(@PathVariable Long id){
        BrokerageMarketerRatio xBrokerageMarketerRatio =brokerageMarketerRatioService.detail(id);
        return RestResponse.data(xBrokerageMarketerRatio);
     }

    /**
     * 项目提成比例（市场人员）新增
     */
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody BrokerageMarketerRatio brokerageMarketerRatio) {
        Boolean ret = brokerageMarketerRatioService.add(brokerageMarketerRatio);
        return RestResponse.data(ret);
    }

    /**
     * 项目提成比例（市场人员）更新
     */
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody  BrokerageMarketerRatio brokerageMarketerRatio) {
        Boolean ret = brokerageMarketerRatioService.update(brokerageMarketerRatio);
        return RestResponse.data(ret);
    }

    /**
     * 项目提成比例（市场人员）删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = brokerageMarketerRatioService.delete(id);
        return RestResponse.data(ret);
    }


}

