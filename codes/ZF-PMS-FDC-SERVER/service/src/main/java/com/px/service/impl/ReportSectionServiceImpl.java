package com.px.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.entity.ReportCalculateParam;
import com.px.entity.ReportCalculateTable;
import com.px.entity.ReportSection;
import com.px.mapper.ReportSectionMapper;
import com.px.report.ActionConvertHandler;
import com.px.report.calculation.cityPrice.annotation.TableHead;
import com.px.report.dto.CalculateDataRequestDTO;
import com.px.report.dto.InputData;
import com.px.report.dto.InputTableData;
import com.px.report.dto.Params;
import com.px.report.format.CalculateDataFormatUtil;
import com.px.service.IReportCalculateParamService;
import com.px.service.IReportCalculateTableService;
import com.px.service.IReportSectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.utils.ConvertUtil;
import com.px.vo.ReportSectionTreeVO;
import org.apache.commons.compress.utils.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.px.report.html.TableHTML.*;


/**
 * <p>
 * 报告段落 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-08-29
 */
@Service
public class ReportSectionServiceImpl extends ServiceImpl<ReportSectionMapper, ReportSection> implements IReportSectionService {

    @Autowired
    private ActionConvertHandler actionConvertHandler;

    @Autowired
    private IReportCalculateParamService reportCalculateParamService;

    @Autowired
    private IReportCalculateTableService reportCalculateTableService;



    @Override
    @SuppressWarnings("unchecked")
    public Page<ReportSection> selectPage(Page page,ReportSection reportSection){
        return this.page(page, new QueryWrapper<ReportSection>(reportSection));
    }


    @Override
    public ReportSection detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(ReportSection reportSection){
        return  this.save(reportSection);
    }

    @Override
    public Boolean update(ReportSection reportSection){
        return  this.updateById(reportSection);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    @Override
    public String updateSection(ReportSectionTreeVO treeVO) {
        String xSection = richTextTableFormat(treeVO.getSection());
        ReportSection reportSection = new ReportSection();
        reportSection.setId(treeVO.getSectionId());
        reportSection.setSection(xSection);
        if (this.updateById(reportSection)){
            return xSection;
        }
        return treeVO.getSection();
    }

    @Override
    public String calculate(CalculateDataRequestDTO calculateData) {
        String action = calculateData.getAction();
        switch (action) {
            case "businessCenter":
               return actionConvertHandler.businessCenter54(calculateData.getJsonString());

        }
        return null;
    }

    @Override
    public InputData inputDataStructure(Long id) {
        ReportSection section = this.getById(id);
        if (StrUtil.isNotBlank(section.getCalculateData())){
            return CalculateDataFormatUtil.calculateJsonStringFormat(section.getCalculateData());
        }
        List<ReportCalculateParam> params = reportCalculateParamService.list(new LambdaQueryWrapper<ReportCalculateParam>().eq(ReportCalculateParam::getHeadlineId, section.getHeadlineId()));
        List<ReportCalculateTable> tables = reportCalculateTableService.list(new LambdaQueryWrapper<ReportCalculateTable>().eq(ReportCalculateTable::getHeadlineId, section.getHeadlineId()));
        List<Params> xParams = ConvertUtil.copyList(params, Params.class);
        List<InputTableData> xTables = Lists.newArrayList();
        try {
            for (ReportCalculateTable table : tables){
                InputTableData inputTableData = new InputTableData();
                ConvertUtil.copyProperties(table,inputTableData);
                String tableClass = table.getTableClass();
                List<Field> fields = Arrays.asList(Class.forName(tableClass).getDeclaredFields());
                List<com.px.report.dto.TableHead> heads = Lists.newArrayList();
                for (Field field : fields){
                    field.setAccessible(true);
                    TableHead tableHead = field.getAnnotation(TableHead.class);
                    if (tableHead.isRequest()) {
                        com.px.report.dto.TableHead th = new com.px.report.dto.TableHead();
                        String label = tableHead.name();
                        String prop = field.getName();
                        th.setLabel(label);
                        th.setProp(prop);
                        heads.add(th);
                    }
                }
                inputTableData.setTableHead(heads);
                xTables.add(inputTableData);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        InputData structure = InputData.builder().inputTableData(xTables).params(xParams).build();
        return structure;
    }

    public String richTextTableFormat(String html){
        Document document = Jsoup.parse(html);
        Elements table = document.getElementsByTag("table");
        table.attr("style",TABLE_STYLE);
        Elements td = document.select("table td");
        td.removeAttr("width");
        td.attr("style",TD_STYLE);
        for (Element element : td){
            String text = element.text();
            element.text("");
            element.append(P_SPAN.replace("content",text));
        }
        return document.body().html();
    }

//    public static void main(String[] args) {
//        String html = "<p style=\"text-indent: 37px; line-height: 2em;\"><span style=\"font-family: 宋体; font-size: 18px;\">为了公共利益的需要，根据《中华人民共和国土地管理法》等法律法规的规定，经areaName人民政府研究，决定启动areaName pici建设用地涉及的集体土地征收工作。</span></p><p style=\"text-indent: 2em; line-height: 2em;\"><span style=\"font-family: 宋体, SimSun; font-size: 18px;\"><span style=\"font-family: 宋体;\">根据</span><span style=\"font-family: &quot;Times New Roman&quot;;\"><span style=\"font-family: 宋体;\">《四川省重大决策社会稳定风险评估办法》（川委办〔</span>2022<span style=\"font-family: 宋体;\">〕</span><span style=\"font-family: &quot;Times New Roman&quot;;\">2</span><span style=\"font-family: 宋体;\">号）</span></span><span style=\"font-family: 宋体;\">、《四川省自然资源厅关于做好</span><span style=\"font-family: &quot;Times New Roman&quot;;\">&lt;</span><span style=\"font-family: 宋体;\">土地管理法</span><span style=\"font-family: &quot;Times New Roman&quot;;\">&gt;</span><span style=\"font-family: 宋体;\">（修正案）施行后过渡期征地报批及实施有关工作的通知<span style=\"font-family: 宋体;\">》（川自然资规〔</span><span style=\"font-family: &quot;Times New Roman&quot;;\">2020</span><span style=\"font-family: 宋体;\">〕</span><span style=\"font-family: &quot;Times New Roman&quot;;\">5</span><span style=\"font-family: 宋体;\">号）等相关规定，涉及农村集体土地征收、被征地农民补偿安置和移民安置等方面的重大政策和改革措施的重大行政决策，应当开展社会稳定风险评估。</span></span></span></p><table width=\"768\"><tbody><tr class=\"firstRow\"><td valign=\"middle\" style=\"word-break: break-all; border-color: rgb(221, 221, 221);\" align=\"center\" width=\"192\" class=\"selectTdClass\">1</td><td valign=\"middle\" style=\"word-break: break-all; border-color: rgb(221, 221, 221);\" align=\"center\" width=\"192\" class=\"selectTdClass\">2</td><td valign=\"middle\" style=\"word-break: break-all; border-color: rgb(221, 221, 221);\" align=\"center\" width=\"192\" class=\"selectTdClass\">3</td><td valign=\"middle\" style=\"word-break: break-all; border-color: rgb(221, 221, 221);\" align=\"center\" width=\"192\" class=\"selectTdClass\">4</td></tr><tr><td valign=\"middle\" style=\"word-break: break-all; border-color: rgb(221, 221, 221);\" align=\"center\" width=\"192\" class=\"selectTdClass\">8</td><td valign=\"middle\" style=\"word-break: break-all; border-color: rgb(221, 221, 221);\" align=\"center\" width=\"192\" class=\"selectTdClass\">7</td><td valign=\"middle\" style=\"word-break: break-all; border-color: rgb(221, 221, 221);\" align=\"center\" width=\"192\" class=\"selectTdClass\">6</td><td valign=\"middle\" style=\"word-break: break-all; border-color: rgb(221, 221, 221);\" align=\"center\" width=\"192\" class=\"selectTdClass\">5</td></tr></tbody></table><p style=\"text-indent: 2em; line-height: 2em;\"><span style=\"font-family: 宋体, SimSun; font-size: 18px;\"><span style=\"font-family: 宋体;\"><span style=\"font-family: 宋体;\"></span></span></span><br/></p>";
//        Document document = Jsoup.parse(html);
//        Elements table = document.getElementsByTag("table");
//        table.attr("style",TABLE_STYLE);
//        Elements td = document.select("table td");
//        td.removeAttr("width");
//        td.attr("style",TD_STYLE);
//        for (Element element : td){
//            String text = element.text();
//            element.text("");
//            element.append(P_SPAN.replace("content",text));
//        }
//        System.out.println(document.body().html());
//
//    }
}
