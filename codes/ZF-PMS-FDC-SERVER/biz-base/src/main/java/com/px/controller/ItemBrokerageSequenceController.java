package com.px.controller;

import com.px.annotation.DataPermission;
import com.px.annotation.IgnoreAuth;
import com.px.vo.BrokerageSequenceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IItemBrokerageSequenceService;
import com.px.entity.ItemBrokerageSequence;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

/**
 * 项目提成流水
 *
 * @author 品讯科技
 * @since 2023-03-03
 */
@RestController
@RequestMapping("itemBrokerageSequence")
@Slf4j
public class ItemBrokerageSequenceController extends BaseController {
    @Autowired
    private IItemBrokerageSequenceService itemBrokerageSequenceService;

    /**
    * 项目提成流水列表
    */
    @DataPermission
    @GetMapping("")
    public RestResponse<Page<BrokerageSequenceVO>> page(BrokerageSequenceVO brokerageSequenceVO, Page page){
        Page<BrokerageSequenceVO> pages=itemBrokerageSequenceService.selectPage(page,brokerageSequenceVO);
        return RestResponse.data(pages);
    }

    /**
     * 项目提成结算抵扣
     */
    @DataPermission
    @GetMapping("/deduction")
    public RestResponse<List<BrokerageSequenceVO>> deduction(BrokerageSequenceVO brokerageSequenceVO){
        List<BrokerageSequenceVO> list = itemBrokerageSequenceService.deduction(brokerageSequenceVO);
        return RestResponse.data(list);
    }
    /**
     * 项目提成流水详情
     */
    @GetMapping("/{id}")
    public RestResponse<ItemBrokerageSequence> detail(@PathVariable Long id){
        ItemBrokerageSequence xItemBrokerageSequence =itemBrokerageSequenceService.detail(id);
        return RestResponse.data(xItemBrokerageSequence);
     }

    /**
     * 项目提成流水新增
     */
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody ItemBrokerageSequence itemBrokerageSequence) {
        Boolean ret = itemBrokerageSequenceService.add(itemBrokerageSequence);
        return RestResponse.data(ret);
    }

    /**
     * 项目提成流水更新
     */
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody ItemBrokerageSequence itemBrokerageSequence) {
        Boolean ret = itemBrokerageSequenceService.update(itemBrokerageSequence);
        return RestResponse.data(ret);
    }

    /**
     * 项目提成流水删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = itemBrokerageSequenceService.delete(id);
        return RestResponse.data(ret);
    }

    /**
     * 提成结算导出
     */
    @DataPermission
    @GetMapping("/export")
    public void exportData(BrokerageSequenceVO brokerageSequenceVO, HttpServletResponse response) throws IOException {
        List<BrokerageSequenceVO> list = itemBrokerageSequenceService.xList(brokerageSequenceVO);
        exportPlus(response,"提成结算",list,BrokerageSequenceVO.class);
    }

    /**
     * 提成结算接口，开发测试用
     * @return
     */
    @IgnoreAuth
    @GetMapping("/settle")
    public RestResponse<Set<Long>> settle(){
        itemBrokerageSequenceService.doAbleItemBrokerageSettle();
        Set<Long> longs = itemBrokerageSequenceService.doNonMarketerAheadSettle();
        return RestResponse.data(longs);
    }

}

