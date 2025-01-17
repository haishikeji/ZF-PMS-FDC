package com.px.controller;

import com.px.annotation.DataPermission;
import com.px.annotation.OperLog;
import com.px.dto.PaymentExportDTO;
import com.px.vo.ItemPaymentVO;
import com.px.vo.PaymentCollectionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IPaymentCollectionService;
import com.px.entity.PaymentCollection;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;
import com.px.common.PullDownModel;
import com.px.utils.ConvertUtil;

import java.io.IOException;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import javax.servlet.http.HttpServletResponse;

import static com.px.common.Constants.MANAGER_OFFICE;

/**
 * 回款记录
 *
 * @author 品讯科技
 * @since 2023-01-10
 */
@RestController
@RequestMapping("paymentCollection")
@Slf4j
public class PaymentCollectionController extends BaseController {
    @Autowired
    private IPaymentCollectionService paymentCollectionService;

    /**
    * 回款记录列表
    */
    @DataPermission(teamData = true,postChild = false,ignoreDepartment = {MANAGER_OFFICE})
    @GetMapping("")
    public RestResponse<Page<PaymentCollectionVO>> page(PaymentCollectionVO paymentCollection, Page page){
        Page<PaymentCollectionVO> pages=paymentCollectionService.selectPage(page,paymentCollection);
        return RestResponse.data(pages);
    }

    /**
     * 回款记录导出
     */
    @GetMapping("/export")
    public void exportPaymentCollection(PaymentCollectionVO paymentCollection, HttpServletResponse response ) throws IOException {
        exportPlus(response,"回款记录",paymentCollectionService.exportPaymentCollection(paymentCollection), PaymentCollectionVO.class);
    }

    /**
     * 回款记录详情
     */
    @GetMapping("/{id}")
    public RestResponse<PaymentCollection> detail(@PathVariable Long id){
        PaymentCollection xPaymentCollection =paymentCollectionService.detail(id);
        return RestResponse.data(xPaymentCollection);
     }

    /**
     * 回款记录新增
     */
    @OperLog(title = "新增回款")
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody PaymentCollection paymentCollection) {
        Boolean ret = paymentCollectionService.add(paymentCollection);
        return RestResponse.data(ret);
    }

    /**
     * 回款记录更新
     */
    @OperLog(title = "更新回款记录")
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody PaymentCollection paymentCollection) {
        Boolean ret = paymentCollectionService.update(paymentCollection);
        return RestResponse.data(ret);
    }

    /**
     * 回款记录删除
     */
    @OperLog(title = "删除回款记录")
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = paymentCollectionService.delete(id);
        return RestResponse.data(ret);
    }


    /**
     * 回款记录下拉列表
     */
    @GetMapping("/simpleAll")
    public RestResponse<List<PullDownModel>> simpleAll(){
        LambdaQueryWrapper<PaymentCollection> lambdaQueryWrapper = new LambdaQueryWrapper<PaymentCollection>()
        .select(PaymentCollection::getId,PaymentCollection::getName)
        .eq(PaymentCollection::getDeleted,false);
        List<PaymentCollection> list= paymentCollectionService.list(lambdaQueryWrapper);
        List<PullDownModel> ret=ConvertUtil.copyList(list, PullDownModel.class);
        return RestResponse.data(ret);
    }

    /**
     * 项目回款列表
     * @return
     */
    @DataPermission(teamData = true,postChild = false,ignoreDepartment = {MANAGER_OFFICE})
    @GetMapping("/item")
    public RestResponse<IPage<ItemPaymentVO>> getItemPayment(PaymentCollectionVO paymentCollectionVO,Page page){
        IPage<ItemPaymentVO> result = paymentCollectionService.getItemPayment(page,paymentCollectionVO);
        return RestResponse.data(result);
    }

    /**
     * 项目回款导出
     */
    @DataPermission(teamData = true,postChild = false,ignoreDepartment = {MANAGER_OFFICE})
    @GetMapping("/item/export")
    public void exportData(PaymentCollectionVO paymentCollectionVO, HttpServletResponse response ) throws IOException {
        List<PaymentExportDTO> list = paymentCollectionService.getList(paymentCollectionVO);
        exportPlus(response,"项目回款",list,PaymentExportDTO.class);
    }
}

