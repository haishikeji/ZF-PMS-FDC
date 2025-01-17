package com.px.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IAssetsValueTypeService;
import com.px.entity.AssetsValueType;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 价值类型表
 *
 * @author 品讯科技
 * @since 2024-04-07
 */
@RestController
@RequestMapping("assetsValueType")
@Slf4j
public class AssetsValueTypeController extends BaseController {
    @Autowired
    private IAssetsValueTypeService assetsValueTypeService;

    /**
     * 获取所有资产价值类型
     * @return List<AssetsValueType>
     */
    @GetMapping("/getAssetsValueType")
    public RestResponse<List<AssetsValueType>> getAssetsValueType() {
        return RestResponse.data(assetsValueTypeService.getAssetsValueType());
    }

    /**
    * 价值类型表列表
    */
    @GetMapping("")
    public RestResponse<Page<AssetsValueType>> page(AssetsValueType assetsValueType, Page page){
        Page<AssetsValueType> pages=assetsValueTypeService.selectPage(page,assetsValueType);
        return RestResponse.data(pages);
    }

    /**
     * 价值类型表详情
     */
    @GetMapping("/{id}")
    public RestResponse<AssetsValueType> detail(@PathVariable Long id){
        AssetsValueType xAssetsValueType =assetsValueTypeService.detail(id);
        return RestResponse.data(xAssetsValueType);
     }

    /**
     * 价值类型表新增
     */
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody AssetsValueType assetsValueType) {
        Boolean ret = assetsValueTypeService.add(assetsValueType);
        return RestResponse.data(ret);
    }

    /**
     * 价值类型表更新
     */
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody AssetsValueType assetsValueType) {
        Boolean ret = assetsValueTypeService.update(assetsValueType);
        return RestResponse.data(ret);
    }

    /**
     * 价值类型表删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = assetsValueTypeService.delete(id);
        return RestResponse.data(ret);
    }


}

