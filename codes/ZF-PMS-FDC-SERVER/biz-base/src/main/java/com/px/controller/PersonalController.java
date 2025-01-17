package com.px.controller;


import com.px.dto.PersonalAllotOrderDTO;
import com.px.dto.TaskRecordDTO;
import com.px.dto.WorkNodeCommit;
import com.px.vo.PersonalVO;
import com.px.vo.TaskTodoVO;
import com.px.workflow.annotation.FinishTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RestController;
import com.px.service.IPersonalService;
import com.px.entity.Personal;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * 个贷业务订单
 *
 * @author 品讯科技
 * @since 2024-03-01
 */
@RestController
@RequestMapping("personal")
@Slf4j
public class PersonalController extends BaseController {
    @Autowired
    private IPersonalService personalService;

    /**
    * 个贷业务订单列表
    */
    @GetMapping("")
    public RestResponse<Page<PersonalVO>> page(PersonalVO personal, Page page,String keyword){
        Page<PersonalVO> pages=personalService.selectPage(page,personal,keyword);
        return RestResponse.data(pages);
    }

    /**
     * 个贷业务我的订单列表
     */
    @GetMapping("/mine")
    public RestResponse<Page<PersonalVO>> mine(PersonalVO personal, Page page,String keyword){
        Page<PersonalVO> pages=personalService.myOrder(page,personal,keyword);
        return RestResponse.data(pages);
    }

    /**
     * 个贷业务我的订单导出
     */
    @GetMapping("/exportMyOrder")
    public void exportMyOrder(PersonalVO personal,String keyword, HttpServletResponse response ) throws IOException {
        exportPlus(response,"个贷我的订单",personalService.exportMyOrder(personal,keyword), PersonalVO.class);
    }

    /**
     * 个贷业务订单详情
     */
    @GetMapping("/{id}")
    public RestResponse<PersonalVO> detail(@PathVariable Long id){
        PersonalVO personalVO =personalService.detail(id);
        return RestResponse.data(personalVO);
     }

    /**
     * 个贷业务订单新增
     */
    @PostMapping("")
    public synchronized RestResponse<WorkNodeCommit> save(@RequestBody Personal personal) {
        WorkNodeCommit ret = personalService.add(personal);
        return RestResponse.data(ret);
    }

    /**
     * 个贷业务订单更新
     */
    @PutMapping("")
    public RestResponse<Boolean> update(@RequestBody Personal personal) {
        Boolean ret = personalService.update(personal);
        return RestResponse.data(ret);
    }

    /**
     * 个贷业务订单删除
     */
    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        Boolean ret = personalService.delete(id);
        return RestResponse.data(ret);
    }

    /**
     * 分配外业人员
     * @param taskRecordDTO
     * @return
     */
    @FinishTask
    @PutMapping("/allot/outward")
    public RestResponse<Boolean> allotOutwardStaff(@RequestBody @Valid TaskRecordDTO<Personal> taskRecordDTO){
        Boolean ret = personalService.allotOutwardStaff(taskRecordDTO);
        return RestResponse.data(ret);
    }

    /**
     * 个贷流程撤回
     */
    @GetMapping("/recall/{id}")
    public RestResponse<Boolean> doRecall(@PathVariable Long id) {
        Boolean ret =personalService.doRecall(id);
        return RestResponse.data(ret);
    }

    /**
     * 我的待认领实收款订单
     * @param page
     * @param personalVO
     * @return
     */
    @GetMapping("/waiting/claim")
    public RestResponse<Page<PersonalVO>> waitingClaimOrder(Page page,PersonalVO personalVO,String keyword){
        Page<PersonalVO> ret = personalService.waitingClaimOrder(page,personalVO, keyword);
        return RestResponse.data(ret);
    }


    /**
     * 个贷业务待归档订单列表
     */
    @GetMapping("/waiting/saveFile")
    public RestResponse<Page<PersonalVO>> waitingSaveFile(PersonalVO personal, Page page,String keyword){
        Page<PersonalVO> pages=personalService.waitingSaveFile(page,personal,keyword);
        return RestResponse.data(pages);
    }

    /**
     * 批量归档
     * @param todoVOList
     * @return
     */
    @PostMapping("/batchSaveFile")
    public RestResponse<Boolean> batchSaveFile(@RequestBody  List<TaskTodoVO> todoVOList){
        Boolean ret = personalService.batchSaveFile(todoVOList);
        return RestResponse.data(ret);
    }

    /**
     * 个贷业务待归档订单列表
     */
    @GetMapping("/saveFile/done")
    public RestResponse<Page<PersonalVO>> saveFileDone(PersonalVO personal, Page page,String keyword){
        Page<PersonalVO> pages=personalService.saveFileDone(page,personal,keyword);
        return RestResponse.data(pages);
    }

    /**
     * 个贷业务待分单订单列表
     * @param page
     * @return
     */
    @GetMapping("/waitingAllot")
    public RestResponse<Page<PersonalVO>> waitingAllotOrderPage(Page page,String keyword){
        Page<PersonalVO> ret = personalService.waitingAllotOrderPage(page,keyword);
        return RestResponse.data(ret);
    }

    /**
     * 批量分单
     * @param dto
     * @return
     */
    @PostMapping("/batchAllot")
    public RestResponse<Boolean> doAllotOrder(@RequestBody @Valid PersonalAllotOrderDTO dto){
        Boolean ret = personalService.doAllotOrder(dto);
        return RestResponse.data(ret);
    }

    /**
     * 换证接口
     * @param personal
     * @return
     */
    @PutMapping("/change")
    public RestResponse<Boolean> changeCredentials(@RequestBody PersonalVO personal){
        Boolean ret = personalService.changeCredentials(personal);
        return RestResponse.data(ret);
    }


}


