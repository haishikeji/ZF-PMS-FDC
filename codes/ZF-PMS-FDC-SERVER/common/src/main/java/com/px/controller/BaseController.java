package com.px.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.px.common.SimpleDownloadModel;
import com.px.configuration.DfsConfig;
import com.px.utils.ExcelPlusUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author 品讯科技
 */
@Slf4j
public class BaseController {

    @Autowired
    protected DfsConfig dfsConfig;


    protected void writeXss2Response(HttpServletRequest request, HttpServletResponse response, XSSFWorkbook wb,
                                     String fileName) {
        try {
            OutputStream out = response.getOutputStream();
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//            wb.write(out);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            InputStream is = new ByteArrayInputStream(bos.toByteArray());

            int b = 0;
            byte[] buffer = new byte[1024];
            while (b != -1) {
                b = is.read(buffer);
                if (b != -1) {
                    out.write(buffer, 0, b);
                }
            }
            is.close();

            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
            out.close();
        } catch (Exception e) {
            log.error("生成" + fileName + "xlsx错误", e);
            throw new RuntimeException(e);
        }
    }

    protected void exportPlus(HttpServletResponse response, String filename, List datas, Class clz) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        ExcelPlusUtil<Object> util = new ExcelPlusUtil<Object>(clz);
        String fileName = URLEncoder.encode(filename, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        util.exportExcel2HttpResponse(datas, filename, outputStream);
    }


    protected void exportPlusCombos(HttpServletResponse response, String filename, List datas, Class clz, String[] options) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        ExcelPlusUtil<Object> util = new ExcelPlusUtil<Object>(clz);
        String fileName = URLEncoder.encode(filename, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        util.exportExcel2HttpResponse(datas, filename, outputStream, options);
    }

    protected void packageFile2Zip(String zipName, HttpServletResponse response, List<? extends SimpleDownloadModel> downloadModels)throws IOException {
        String fileName = URLEncoder.encode(zipName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".zip");
        File zipTmpFile = FileUtil.file(dfsConfig.getPath() + "\\" + RandomUtil.randomString(32) + ".zip");
        List<File> fileList = new ArrayList<>();
        for (SimpleDownloadModel simpleDownloadModel : downloadModels) {
            try {
                String url = simpleDownloadModel.getUrl();
                if (StrUtil.isNotBlank(url)) {
                    File old = FileUtil.file("/opt"+url);
//                    File old = FileUtil.file(url);
                    fileList.add(old);
                }
            } catch (Exception e) {
                log.error("导出zip错误 ", e);
            }
        }
        File[] files = ArrayUtil.toArray(fileList, File.class);

        ZipUtil.zip(zipTmpFile, true, files);

        ServletOutputStream outputStream = response.getOutputStream();
        IoUtil.copy(FileUtil.getInputStream(zipTmpFile), outputStream);

        zipTmpFile.delete();
    }
}
