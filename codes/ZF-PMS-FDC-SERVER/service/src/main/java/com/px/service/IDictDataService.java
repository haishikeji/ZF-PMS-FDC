package com.px.service;
import com.px.common.PullDownModel;
import com.px.entity.DictData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 字典数据 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2022-11-29
 */
public interface IDictDataService extends IService<DictData> {

        Page<DictData> selectPage(Page page,DictData dictData);

        DictData detail(Long id);

        Boolean add(DictData dictData);

        Boolean update(DictData dictData);

        Boolean delete(Long id);

        XSSFWorkbook generateImportTemplate();

        void importExcel(MultipartFile file);

        XSSFWorkbook exportExcel(DictData dictData);

    List<PullDownModel> simpleType(String type);

        List<DictData> byCode(String code);

        DictData findByDataTypeAndRemark(String dataType, String remark);
}
