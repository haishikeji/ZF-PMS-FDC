package com.px.mapper;

import com.px.dto.WorkNodeDTO;
import com.px.entity.WorkNode;
import com.px.dao.CustomBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工作流节点 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-10-17
 */
public interface WorkNodeMapper extends CustomBaseMapper<WorkNode> {

    List<WorkNodeDTO> getFlowNodes(@Param("flowId") Long flowId,@Param("businessType") String businessType);

    List<WorkNode> getNodesByMainBusiness(@Param("businessType") String businessType);

    WorkNode getUniqueNodeByFlowNameAndNodeName(@Param("businessEnum")String businessEnum, @Param("nodeEnum")String nodeEnum);
}
