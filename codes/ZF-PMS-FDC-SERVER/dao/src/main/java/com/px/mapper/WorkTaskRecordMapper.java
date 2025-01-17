package com.px.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.dto.AssetsSelectDTO;
import com.px.dto.WorkNodeInstanceTaskDTO;
import com.px.entity.WorkTaskRecord;
import com.px.dao.CustomBaseMapper;
import com.px.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工作流任务记录 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-10-17
 */
public interface WorkTaskRecordMapper extends CustomBaseMapper<WorkTaskRecord> {

    void finishOpening(@Param("id") Long id,@Param("flowId") Long flowId);

    List<WorkNodeInstanceTaskDTO> instanceNodeTasks(@Param("instanceNodeId") Long instanceNodeId);

    List<Long> getNotBusinessHandleTaskId(@Param("instanceNodeId") Long instanceNodeId);

    List<WorkTaskRecordVO> getInstanceTask(@Param("instanceId") Long instanceId);

    Page<TaskTodoVO> taskTodoPage(Page page, @Param("todoVO") TaskTodoVO todoVO, @Param("keyword") String keyword);

    Page<MajorTaskDoneVO> majorTaskDonePage(Page page, @Param("todoVO") MajorTaskDoneVO todoVO, @Param("keyword") String keyword);

    void batchFinishTask(@Param("ids") List<Long> ids);

    Page<PersonalTodoVO> personalTodoPage(Page page, @Param("personal") PersonalTodoVO  personal, @Param("keyword")String keyword);

    Page<PersonalTaskDoneVO> personalTaskDonePage(Page page, @Param("todoVO")PersonalTaskDoneVO doneVO, @Param("keyword")String keyword);

    List<TaskTodoVO> majorTodoSimpleList(@Param("handlerId") Long handlerId);

    List<PersonalTodoVO> personalTodoSimpleList(@Param("handlerId") Long handlerId);

    List<BusinessTodoNum> getTodoTotalNum(@Param("handlerId")Long currentUserId);

    /**
     * 获取资产业务已办列表
     * @param assetsSelectDTO 查询dto
     * @param page 分页
     * @return List<AssetsTaskDoneVO>
     */
    Page<AssetsTaskDoneVO> getAssetsTaskDoneList(@Param("assetsSelectDTO") AssetsSelectDTO assetsSelectDTO, Page page);

    void batchFinishTaskByInstanceId(@Param("list") List<Long> instanceIds);
}
