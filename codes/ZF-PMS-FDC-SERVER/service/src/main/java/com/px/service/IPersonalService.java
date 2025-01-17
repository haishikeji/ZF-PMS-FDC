package com.px.service;
import com.px.dto.OrderReportDTO;
import com.px.dto.PersonalAllotOrderDTO;
import com.px.dto.TaskRecordDTO;
import com.px.dto.WorkNodeCommit;
import com.px.entity.Personal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.enums.CertificateEnum;
import com.px.vo.PersonalVO;
import com.px.vo.TaskTodoVO;

import java.util.List;

/**
 * <p>
 * 个贷业务订单 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-01
 */
public interface IPersonalService extends IService<Personal> {

        Page<PersonalVO> selectPage(Page page, PersonalVO personal,String keyword);

        PersonalVO detail(Long id);

        WorkNodeCommit add(Personal personal);

        Boolean update(Personal personal);

        Boolean delete(Long id);

    Boolean allotOutwardStaff(TaskRecordDTO<Personal> taskRecordDTO);

    void updateCertificates(Long targetId, CertificateEnum cer,String location);

    Boolean doRecall(Long id);

    Page<PersonalVO> waitingClaimOrder(Page page, PersonalVO personalVO,String keyword);

    Page<PersonalVO> waitingSaveFile(Page page, PersonalVO personal,String keyword);

    Boolean batchSaveFile(List<TaskTodoVO> todoVOList);

    Page<PersonalVO> saveFileDone(Page page, PersonalVO personal,String keyword);

    Page<PersonalVO> myOrder(Page page, PersonalVO personal,String keyword);

    List<PersonalVO> exportMyOrder(PersonalVO personal,String keyword);

    Boolean personalAllotRealAmount(List<OrderReportDTO> claimOrders);

    Boolean doPersonalAllotRealAmount(List<OrderReportDTO> claimOrders);

    Page<PersonalVO> waitingAllotOrderPage(Page page,String keyword);

    Boolean doAllotOrder(PersonalAllotOrderDTO dto);

    Boolean changeCredentials(PersonalVO personal);

    Long nextInwardStaff();

    Long fixationPriceStaff(Long outwardStaffId);

    void fixationInwardStaff(Long personalId);
}
