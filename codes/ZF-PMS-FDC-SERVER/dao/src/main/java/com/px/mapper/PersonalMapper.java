package com.px.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.entity.Personal;
import com.px.dao.CustomBaseMapper;
import com.px.vo.PersonalVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 个贷业务订单 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2024-03-01
 */
public interface PersonalMapper extends CustomBaseMapper<Personal> {

    Page<PersonalVO> xSelectPage(Page page, @Param("personal") PersonalVO personal,@Param("keyword") String keyword);

    PersonalVO getDetail(@Param("id") Long id);

    Personal getByTargetId(@Param("targetId") Long targetId);

    Page<PersonalVO> waitingSaveFile(Page page, @Param("personal")PersonalVO personal ,@Param("keyword") String keyword);

    void updateSaveFileInfo(@Param("list") List<Long> personalIds);

    Page<PersonalVO> saveFileDone(Page page, @Param("personal")PersonalVO personal ,@Param("keyword") String keyword);

    Page<PersonalVO> myOrder(Page page, @Param("personal")PersonalVO personal ,@Param("keyword") String keyword);

    List<PersonalVO> exportMyOrder(@Param("personal")PersonalVO personal ,@Param("keyword") String keyword);

    Page<PersonalVO> waitingAllotOrderPage(Page page,@Param("keyword") String keyword);

    void batchUpdateOutWardStaffId(@Param("userId") Long userId, @Param("priceStaffId") Long priceStaffId,@Param("list") List<Long> orderIds);
}
