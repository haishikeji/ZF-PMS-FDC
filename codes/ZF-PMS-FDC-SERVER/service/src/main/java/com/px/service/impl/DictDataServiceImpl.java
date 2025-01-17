package com.px.service.impl;

import com.px.common.PullDownModel;
import com.px.entity.DictData;
import com.px.entity.DictType;
import com.px.mapper.DictDataMapper;
import com.px.service.IDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.service.IDictTypeService;
import com.px.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.utils.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import com.px.enums.BatchTaskTypeEnum;

/**
 * <p>
 * 字典数据 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2022-11-29
 */
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements IDictDataService {

    private static final List<String> dictDataExcelTitles= new ArrayList();

    @Autowired
    private IDictTypeService dictTypeService;

    @Autowired
    private DictDataMapper dictDataMapper;

    static{
            dictDataExcelTitles.add("字典排序");
            dictDataExcelTitles.add("字典值");
            dictDataExcelTitles.add("备注");
            dictDataExcelTitles.add("是否可用:1:是 0:否");
    }


    @Override
    @SuppressWarnings("unchecked")
    public Page<DictData> selectPage(Page page,DictData dictData){
        return this.page(page, new QueryWrapper<DictData>(dictData));
    }


    @Override
    public DictData detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(DictData dictData){
        return  this.save(dictData);
    }

    @Override
    public Boolean update(DictData dictData){
        return  this.updateById(dictData);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public XSSFWorkbook generateImportTemplate(){
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建固定列的表头
        createExcelTitle(wb);
        return wb;
    }

    private void createExcelTitle(XSSFWorkbook wb) {
        Sheet sheet = wb.createSheet("字典数据导入模板");
        Row row = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        Cell cell = null;
        for (int i = 0; i < dictDataExcelTitles.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(dictDataExcelTitles.get(i));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(MultipartFile file){
        ExcelUtil.importExcel(DictData.class, new ExcelUtil.ImportListener<DictData>() {
            @Override
            public Boolean handleOneRecord(DictData dictData) {
                return add(dictData);
            }

            @Override
            public void afterImport(ExcelUtil.BatchImportResult batchImportResult) {

            }
        }, file, BatchTaskTypeEnum.DEFAULT,ExcelUtil.BatchImportStrategy.ROLL_BACK);
    }

    @Override
    public XSSFWorkbook exportExcel(DictData dictData) {
        XSSFWorkbook wb = ExcelUtil.export(DictData.class,
        (page, dictDataReq) -> selectPage(page,dictDataReq),
        dictData);
        return wb;
    }

    @Override
    public List<PullDownModel> simpleType(String type) {
        DictType dictType = dictTypeService.getOne(new LambdaQueryWrapper<DictType>().eq(DictType::getName, type));
        LambdaQueryWrapper<DictData> lambdaQueryWrapper = new LambdaQueryWrapper<DictData>()
                .select(DictData::getId,DictData::getName)
                .eq(DictData::getDictTypeId,dictType.getId())
                .eq(DictData::getDeleted,false);
        List<DictData> list= this.list(lambdaQueryWrapper);
        List<PullDownModel> ret= ConvertUtil.copyList(list, PullDownModel.class);
        return ret;
    }

    @Override
    public List<DictData> byCode(String code) {
        DictType dictType = dictTypeService.getOne(new LambdaQueryWrapper<DictType>().eq(DictType::getCode, code));
        LambdaQueryWrapper<DictData> lambdaQueryWrapper = new LambdaQueryWrapper<DictData>()
                .select(DictData::getDictSort, DictData::getName,DictData::getRemark)
                .eq(DictData::getDictTypeId,dictType.getId())
                .eq(DictData::getDeleted,false).orderByAsc(DictData::getDictSort);
        return  this.list(lambdaQueryWrapper);
    }

    @Override
    public DictData findByDataTypeAndRemark(String dataType, String remark) {
        return dictDataMapper.findByDataTypeAndName(dataType,remark);
    }

}
