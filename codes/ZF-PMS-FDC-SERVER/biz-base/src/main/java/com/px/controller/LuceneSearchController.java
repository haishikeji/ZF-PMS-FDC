package com.px.controller;


import com.px.common.RestResponse;
import com.px.exception.ErrorCode;
import com.px.service.ILuceneSearchService;
import com.px.vo.LuceneSearchVO;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Lucene 全文搜索
 *
 * @author 品讯科技
 * @since 2023-01-10
 */
@RestController
@RequestMapping("global/search")
@Slf4j
public class LuceneSearchController extends BaseController {

    @Autowired
    private ILuceneSearchService luceneSearchService;



    @GetMapping("/{businessType}/{keyword}")
    public RestResponse<List<LuceneSearchVO>> globalSearch(@PathVariable("businessType") String businessType,@PathVariable("keyword") String keyword) {

        try {
            List<LuceneSearchVO> result = luceneSearchService.globalSearch(businessType,keyword);
            return RestResponse.data(result);
        } catch (Exception e) {
            return RestResponse.error(ErrorCode.DEFAULTERROR.getErrorMsg(),400);
        }
    }

}

