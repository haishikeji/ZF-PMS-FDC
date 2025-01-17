package com.px.mapper;

import com.px.entity.WorkFlowLog;
import com.px.dao.CustomBaseMapper;
import com.px.vo.WorkflowLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 实例节点流水表 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-10-17
 */
public interface WorkFlowLogMapper extends CustomBaseMapper<WorkFlowLog> {

    List<WorkflowLogVO> getList(@Param("businessId") Long businessId, @Param("businessSubId") String businessSubId,
                                @Param("businessMinId") String businessMinId,@Param("businessType") String businessType);

}
