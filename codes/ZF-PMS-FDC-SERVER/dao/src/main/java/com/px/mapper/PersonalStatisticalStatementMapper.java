package com.px.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.dto.PersonalStatisticalSelectDTO;
import com.px.vo.PersonalEfficiencyDetailVO;
import com.px.vo.PersonalDepEfficiencyVO;
import com.px.vo.PersonalInwardEfficiencyVO;
import com.px.vo.PersonalOutwardEfficiencyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PersonalStatisticalStatementMapper {

    /**
     * 个贷部门效率值查询
     * @param dto 查询dto
     * @return List<PersonalDepEfficiencyVO>
     */
    List<PersonalDepEfficiencyVO> getPersonalDepEfficiencyVO(@Param("dto") PersonalStatisticalSelectDTO dto);

    /**
     * 获取效率值相关个贷订单列表
     * @param page 分页
     * @param dto 查询dto
     * @return Page<PersonalEfficiencyDetailVO>
     */
    Page<PersonalEfficiencyDetailVO> getPersonalEfficiencyDetailVO(@Param("page") Page page, @Param("dto") PersonalStatisticalSelectDTO dto);

    /**
     * 导出效率值相关个贷订单列表
     * @param dto 查询dto
     * @return List<PersonalEfficiencyDetailVO>
     */
    List<PersonalEfficiencyDetailVO> exportPersonalEfficiencyDetailVO(@Param("dto") PersonalStatisticalSelectDTO dto);

    /**
     * 个贷外业效率值查询
     * @param dto 查询dto
     * @return List<PersonalOutwardEfficiencyVO>
     */
    List<PersonalOutwardEfficiencyVO> getPersonalOutwardEfficiencyVO(@Param("dto") PersonalStatisticalSelectDTO dto);

    /**
     * 个贷内业效率值查询
     * @param dto 查询dto
     * @return List<PersonalInwardEfficiencyVO>
     */
    List<PersonalInwardEfficiencyVO> getPersonalInwardEfficiencyVO(@Param("dto") PersonalStatisticalSelectDTO dto);

}
