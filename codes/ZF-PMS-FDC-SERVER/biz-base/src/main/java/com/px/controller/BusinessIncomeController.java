package com.px.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.common.RestResponse;
import com.px.exception.ErrorCode;
import com.px.service.IBusinessIncomeService;
import com.px.vo.UserCommissionProdVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 业务收入
 *
 * @author 品讯科技
 * @since 2023-11-06
 */
@RestController
@RequestMapping("businessIncome")
@Slf4j
public class BusinessIncomeController extends BaseController {


    @Autowired
    private IBusinessIncomeService businessIncomeService;


    /**
     * 我的大中型业务收入列表
     * @param vo
     * @param page
     * @return
     */
    @GetMapping("/my/major")
    public RestResponse<Page<UserCommissionProdVO>> myMajorIncome(UserCommissionProdVO vo,Page page){
        Page<UserCommissionProdVO> ret = businessIncomeService.myMajorIncome(page,vo);
        return RestResponse.data(ret);
    }

    /**
     * 资产业务我的提成
     * @param page 分页信息
     * @param vo 查询条件
     * @return Page<UserCommissionProdVO>
     */
    @GetMapping("/my/assets")
    public RestResponse<Page<UserCommissionProdVO>> myAssetsIncome(Page page, UserCommissionProdVO vo) {
        Page<UserCommissionProdVO> ret = businessIncomeService.myAssetsIncome(page,vo);
        return RestResponse.data(ret);
    }

    /**
     * 资产业务我的提成导出excel
     * @param vo 查询条件
     */
    @GetMapping("/my/assets/export")
    public void myAssetsIncomeExport(HttpServletResponse response, UserCommissionProdVO vo) throws IOException {
        List<UserCommissionProdVO> list = businessIncomeService.myAssetsIncomeExport(vo);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (ObjectUtil.isNull(vo.getStartDate()) || ObjectUtil.isNull(vo.getEndDate())){
            ErrorCode.throwBusinessException("起始时间和结束时间必填！", "500");
        }if (ObjectUtil.isNull(list) || list.isEmpty()){
            ErrorCode.throwBusinessException("条件筛选结果数量为0,不可导出！", "500");
        }else {
            exportPlus(response, "资产我的提成_" + dateFormat.format(new Date()),list, UserCommissionProdVO.class);
        }
    }


}

