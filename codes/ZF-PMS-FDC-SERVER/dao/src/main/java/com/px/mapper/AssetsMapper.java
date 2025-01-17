package com.px.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.dto.AssetsBusinessMembersDTO;
import com.px.dto.AssetsSelectDTO;
import com.px.entity.Assets;
import com.px.dao.CustomBaseMapper;
import com.px.entity.BusinessNumber;
import com.px.entity.Department;
import com.px.vo.AssetsBusinessMember;
import com.px.vo.AssetsOrderFundInvoiceVO;
import com.px.vo.AssetsRepertoryVo;
import com.px.vo.AssetsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资产业务订单表 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-28
 */
public interface AssetsMapper extends CustomBaseMapper<Assets> {

    /**
     * 条件查询资产任务列表
     * @param page 分页条件
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    Page<AssetsVO> selectPage(@Param("page") Page page, @Param("assetsSelectDTO") AssetsSelectDTO assetsSelectDTO);

    /**
     * 条件查询我的资产订单列表
     * @param page 分页条件
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    Page<AssetsVO> selectMyOrderPage(@Param("page") Page page, @Param("assetsSelectDTO") AssetsSelectDTO assetsSelectDTO);

    /**
     * 导出我的资产订单列表
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    List<AssetsVO> exportMyOrder(@Param("assetsSelectDTO") AssetsSelectDTO assetsSelectDTO);

    /**
     * 条件查询资产已归档列表
     * @param page 分页条件
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    Page<AssetsVO> selectArchivedPage(@Param("page") Page page, @Param("assetsSelectDTO") AssetsSelectDTO assetsSelectDTO);

    /**
     * 条件查询资产已终止列表
     * @param page 分页条件
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    Page<AssetsVO> selectTerminatedPage(@Param("page") Page page, @Param("assetsSelectDTO") AssetsSelectDTO assetsSelectDTO);


    /**
     * 条件查询资产入库统计列表(分页查询)
     * @param page 分页条件
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    Page<AssetsRepertoryVo> selectRepertoryPage(@Param("page") Page page, @Param("assetsSelectDTO") AssetsSelectDTO assetsSelectDTO);

    /**
     * 条件查询资产入库统计集合(用于导出)
     * @param assetsSelectDTO 查询条件
     * @return Page<AssetsVO>
     */
    List<AssetsRepertoryVo> selectRepertoryList(@Param("assetsSelectDTO") AssetsSelectDTO assetsSelectDTO);

    /**
     * 根据业务id获取资产业务详情
     * @param id 业务id
     * @return AssetsVO
     */
    AssetsVO getAssetsDetailById(@Param("id") Long id);

    /**
     * 根据业务id获取资产业务款项信息
     * @param id 业务id
     * @return AssetsOrderFundInvoiceVO
     */
    AssetsOrderFundInvoiceVO getOrderFundInvoiceById(@Param("id") Long id);

    /**
     * 根据业务类型与取号类型获取业务取号信息
     * @param takeType 取号类型
     * @param businessType 业务类型
     * @return BusinessNumber
     */
    BusinessNumber getBusinessNum(@Param("takeType") String takeType, @Param("businessType") String businessType, @Param("year") Integer year);

    /**
     * 新增取号信息
     * @param businessNumber 取号信息
     * @return Boolean
     */
    Boolean addBusinessNum(BusinessNumber businessNumber);

    /**
     * 更新取号信息
     * @param businessNumber 取号信息
     * @return Boolean
     */
    Boolean updateBusinessNum(BusinessNumber businessNumber);

    /**
     * 获取项目参与人id
     * @param assetsBusinessMembersDTO 查询dto
     * @return List<AssetsBusinessMembers>
     */
    Assets getBusinessMembers(AssetsBusinessMembersDTO assetsBusinessMembersDTO);

    /**
     * 获取报告签字师id
     * @param assetsBusinessMembersDTO 查询dto
     * @return String
     */
    String getSignatorys(AssetsBusinessMembersDTO assetsBusinessMembersDTO);

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return AssetsBusinessMember
     */
    AssetsBusinessMember getAssetsBusinessMember(@Param("userId") Long userId);

    /**
     * 通过客户经理id获取下单部门
     * @param managerId 客户经理id
     * @return Long 下单部门id
     */
    Long getManagerDepId(@Param("managerId") Long managerId);

    /**
     * 获取资产下单部门
     * @return List<Department> 下单部门id
     */
    List<Department> getMarkeDepartment();

    /**
     * 更新资产订单基本信息
     * @param assets 更新信息
     * @return Boolean
     */
    Boolean updateOrderBaseInfo(@Param("assets") Assets assets);
}
