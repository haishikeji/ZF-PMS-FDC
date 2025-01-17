package com.px.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IBusinessNumberService;

/**
 * 业务号
 *
 * @author 品讯科技
 * @since 2023-11-06
 */
@RestController
@RequestMapping("businessNumber")
@Slf4j
public class BusinessNumberController extends BaseController {
    @Autowired
    private IBusinessNumberService businessNumberService;


//    /**
//     * 业务取号
//     * @return
//     */
//    @PostMapping("/take")
//    public RestResponse<String> takeNumber(){
//        String ret = businessNumberService.takeOrderNumber();
//        return RestResponse.data(ret);
//    }


}

