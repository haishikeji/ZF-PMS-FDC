package com.px.service;
import com.px.entity.ReportSectionTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.HeadlineSectionVO;

/**
 * <p>
 * 报告段落模板 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-08-25
 */
public interface IReportSectionTemplateService extends IService<ReportSectionTemplate> {

        Page<HeadlineSectionVO> getPage(Page page, HeadlineSectionVO reportSection);

        ReportSectionTemplate detail(Long id);

        Boolean add(ReportSectionTemplate reportSectionTemplate);

        Boolean update(ReportSectionTemplate reportSectionTemplate);

        Boolean delete(Long id);

}
