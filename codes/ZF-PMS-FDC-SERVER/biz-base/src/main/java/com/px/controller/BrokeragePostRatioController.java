package com.px.controller;

import com.px.vo.PostRatioVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IBrokeragePostRatioService;
import com.px.entity.BrokeragePostRatio;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;


/**
 * 领导岗位抽成率
 *
 * @author 品讯科技
 * @since 2023-03-10
 */
@RestController
@RequestMapping("brokeragePostRatio")
@Slf4j
public class BrokeragePostRatioController extends BaseController {
    @Autowired
    private IBrokeragePostRatioService brokeragePostRatioService;

    /**
    * 领导岗位抽成率列表
    */
    @GetMapping("")
    public RestResponse<Page<PostRatioVO>> page(PostRatioVO postRatioVO, Page page){
        Page<PostRatioVO> pages=brokeragePostRatioService.selectPage(page,postRatioVO);
        return RestResponse.data(pages);
    }

    /**
     * 领导岗位抽成率详情
     */
    @GetMapping("/{id}")
    public RestResponse<BrokeragePostRatio> detail(@PathVariable Long id){
        BrokeragePostRatio xBrokeragePostRatio =brokeragePostRatioService.detail(id);
        return RestResponse.data(xBrokeragePostRatio);
     }

    /**
     * 领导岗位抽成率新增
     */
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody BrokeragePostRatio brokeragePostRatio) {
        Boolean ret = brokeragePostRatioService.add(brokeragePostRatio);
        return RestResponse.data(ret);
    }

    /**
     * 领导岗位抽成率更新
     */
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody BrokeragePostRatio brokeragePostRatio) {
        Boolean ret = brokeragePostRatioService.update(brokeragePostRatio);
        return RestResponse.data(ret);
    }

    /**
     * 领导岗位抽成率删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = brokeragePostRatioService.delete(id);
        return RestResponse.data(ret);
    }


}

