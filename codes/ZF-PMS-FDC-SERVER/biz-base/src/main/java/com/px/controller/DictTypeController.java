package com.px.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IDictTypeService;
import com.px.entity.DictType;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;
import com.px.common.PullDownModel;
import com.px.utils.ConvertUtil;
import com.px.utils.HttpKit;
import com.px.exception.ErrorCode;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.multipart.MultipartFile;
/**
 * 字典类型
 *
 * @author 品讯科技
 * @since 2022-11-29
 */
@RestController
@RequestMapping("dictType")
@Slf4j
public class DictTypeController extends BaseController {
    @Autowired
    private IDictTypeService dictTypeService;

    /**
    * 字典类型列表
    */
    @GetMapping("")
    public RestResponse<Page<DictType>> page(DictType dictType, Page page){
        Page<DictType> pages=dictTypeService.selectPage(page,dictType);
        return RestResponse.data(pages);
    }

    /**
     * 字典类型详情
     */
    @GetMapping("/{id}")
    public RestResponse<DictType> detail(@PathVariable Long id){
        DictType xDictType =dictTypeService.detail(id);
        return RestResponse.data(xDictType);
     }

    /**
     * 字典类型新增
     */
    @PostMapping("")
    public RestResponse<Boolean> save(@RequestBody DictType dictType) {
        Boolean ret = dictTypeService.add(dictType);
        return RestResponse.data(ret);
    }

    /**
     * 字典类型更新
     */
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody DictType dictType) {
        Boolean ret = dictTypeService.update(dictType);
        return RestResponse.data(ret);
    }

    /**
     * 字典类型删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = dictTypeService.delete(id);
        return RestResponse.data(ret);
    }


    /**
     * 字典类型下拉列表
     */
    @GetMapping("/simpleAll")
    public RestResponse<List<PullDownModel>> simpleAll(){
        LambdaQueryWrapper<DictType> lambdaQueryWrapper = new LambdaQueryWrapper<DictType>()
        .select(DictType::getId,DictType::getName)
        .eq(DictType::getDeleted,false);
        List<DictType> list= dictTypeService.list(lambdaQueryWrapper);
        List<PullDownModel> ret=ConvertUtil.copyList(list, PullDownModel.class);
        return RestResponse.data(ret);
    }
    /**
     * 字典类型模板下载
     */
    @GetMapping(value = "/download/importTemplate")
    public void importTemplate() {
        try {
            XSSFWorkbook wb = dictTypeService.generateImportTemplate();

            String fileName = "字典类型导入模板" + System.currentTimeMillis() + ".xlsx";

            writeXss2Response(HttpKit.getRequest(), HttpKit.getResponse(), wb, fileName);
        } catch (Exception e) {
            log.error("生成字典类型导入模板异常", e);
            ErrorCode.throwBusinessException(ErrorCode.DEFAULTERROR);
        }
    }

    /**
     * 字典类型导入
     */
    @PostMapping("/importExcel")
    public RestResponse<Boolean> importExcel(@RequestPart("file") MultipartFile file) {
        dictTypeService.importExcel(file);
        return RestResponse.data(true);
    }

    /**
     * 字典类型导出
     */
    @GetMapping(value = "/download/exportExcel")
    public void export(DictType dictType) {
        try {
            XSSFWorkbook wb =  dictTypeService.exportExcel(dictType);

            String fileName = "字典类型" + System.currentTimeMillis() + ".xlsx";

            writeXss2Response(HttpKit.getRequest(), HttpKit.getResponse(), wb, fileName);
        } catch (Exception e) {
            log.error("导出字典类型", e);
            ErrorCode.throwBusinessException(ErrorCode.DEFAULTERROR);
        }

    }



}

