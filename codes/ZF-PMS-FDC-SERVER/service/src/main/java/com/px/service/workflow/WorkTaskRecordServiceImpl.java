package com.px.service.workflow;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.px.dto.AssetsSelectDTO;
import com.px.dto.HandlerPermissionDTO;
import com.px.entity.*;
import com.px.exception.ErrorCode;
import com.px.mapper.DepartmentMapper;
import com.px.mapper.WorkTaskRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.utils.DateUtils;
import com.px.utils.LoginContext;
import com.px.vo.*;
import com.px.workflow.config.WorkNodeProcessable;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;


/**
 * <p>
 * 工作流任务记录 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-10-17
 */
@Service
public class WorkTaskRecordServiceImpl extends ServiceImpl<WorkTaskRecordMapper, WorkTaskRecord> implements IWorkTaskRecordService {

    @Autowired
    private  WorkTaskRecordMapper workTaskRecordMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    @SuppressWarnings("unchecked")
    public Page<WorkTaskRecord> selectPage(Page page,WorkTaskRecord workTaskRecord){
        return this.page(page, new QueryWrapper<WorkTaskRecord>(workTaskRecord));
    }


    @Override
    public WorkTaskRecord detail(Long id){
        return this.getById(id);
    }

    @Override
    public Boolean add(WorkTaskRecord workTaskRecord){
        return  this.save(workTaskRecord);
    }

    @Override
    public Boolean update(WorkTaskRecord workTaskRecord){
        return  this.updateById(workTaskRecord);
    }

    @Override
    public Boolean delete(Long id){
        //逻辑删除
        return this.removeById(id);
    }

    /**
     * 任务创建，确定处理人
     * @param tasks
     * @param nextInstanceNode
     * @param handlerId
     * @return
     */
    @Override
    public Boolean doCreateNodeTaskRecord(WorkNodeProcessable workNodeProcessable,List<WorkNodeTask> tasks, WorkFlowNodeInstance nextInstanceNode, Long handlerId) {
        //区分业务类型
        List<WorkTaskRecord> taskRecords = Lists.newArrayList();
        for (WorkNodeTask task : tasks){
            WorkTaskRecord workTaskRecord = new WorkTaskRecord();
            workTaskRecord.setInstanceId(nextInstanceNode.getId());
            workTaskRecord.setTaskId(task.getId());

            //由上个节点直接确定下一个节点审批人
            if (handlerId != null){
                workTaskRecord.setHandlerId(handlerId);
            }else {

                HandlerPermissionDTO handlerPermissionDTO = JSON.parseObject(task.getHandlerPermission(), HandlerPermissionDTO.class);

                if (handlerPermissionDTO.getName() == null){
                    ErrorCode.throwBusinessException(ErrorCode.PERMISSION_IS_NULL, task.getDescription());
                }
                workTaskRecord.setHandlerId(workNodeProcessable.searchHandlerId(nextInstanceNode,handlerPermissionDTO));
            }
            taskRecords.add(workTaskRecord);
        }
        return this.saveBatch(taskRecords);
    }

    @Override
    public List<WorkTaskRecordVO> getInstanceTask(Long instanceId) {
        List<WorkTaskRecordVO> tasks = workTaskRecordMapper.getInstanceTask(instanceId);
        return tasks;
    }

    @Override
    public Page<TaskTodoVO> majorTaskTodoPage(TaskTodoVO todoVO, Page page,String keyword) {
        todoVO.setHandlerId(LoginContext.getCurrentUserId());
        Page<TaskTodoVO> ret = workTaskRecordMapper.taskTodoPage(page,todoVO,keyword);
        return ret;
    }

    @Override
    public Page<MajorTaskDoneVO> majorTaskDonePage(MajorTaskDoneVO doneVO, Page page,String keyword) {
        doneVO.setHandlerId(LoginContext.getCurrentUserId());
        Page<MajorTaskDoneVO> done = workTaskRecordMapper.majorTaskDonePage(page,doneVO,keyword);
        done.getRecords().stream().forEach(x->{
            x.setHandingTime(DateUtils.getTimeLong(x.getHandingTimeLong()==null?0L:x.getHandingTimeLong()));
        });
        return done;
    }

    @Override
    public Page<PersonalTodoVO> personalTodoPage(PersonalTodoVO todoVO, Page page, String keyword) {
        todoVO.setHandlerId(LoginContext.getCurrentUserId());
        Page<PersonalTodoVO> ret = workTaskRecordMapper.personalTodoPage(page,todoVO,keyword);
        return ret;
    }

    @Override
    public Page<PersonalTaskDoneVO> personalTaskDonePage(PersonalTaskDoneVO doneVO, Page page, String keyword) {
        doneVO.setHandlerId(LoginContext.getCurrentUserId());
        Page<PersonalTaskDoneVO> done =workTaskRecordMapper.personalTaskDonePage(page,doneVO,keyword);
        done.getRecords().stream().forEach(x->{
            x.setHandingTime(DateUtils.getTimeLong(x.getHandingTimeLong()==null?0L:x.getHandingTimeLong()));
        });
        return done;
    }

    @Override
    public List<TaskTodoVO> majorTodoSimpleList() {
        return workTaskRecordMapper.majorTodoSimpleList(LoginContext.getCurrentUserId());
    }

    @Override
    public List<PersonalTodoVO> personalTodoSimpleList() {
        return workTaskRecordMapper.personalTodoSimpleList(LoginContext.getCurrentUserId());
    }

    @Override
    public List<BusinessTodoNum> getTodoTotalNum() {
        return workTaskRecordMapper.getTodoTotalNum(LoginContext.getCurrentUserId());
    }

    /**
     * 获取资产业务已办列表
     * @param assetsSelectDTO 查询dto
     * @param page 分页
     * @return List<AssetsTaskDoneVO>
     */
    @Override
    public Page<AssetsTaskDoneVO> getAssetsTaskDoneList(AssetsSelectDTO assetsSelectDTO, Page page) {
        // 判断是否要根据部门id集合查询(部门待办查询)
        if (ObjectUtil.isNotNull(assetsSelectDTO.getSelectByDepartment()) && assetsSelectDTO.getSelectByDepartment()){
            assetsSelectDTO.setDepartmentIdList(departmentMapper.getDepIdList(LoginContext.getLoginCacheUserBO().getDepartmentList().get(0).getId()));
        }else {
            assetsSelectDTO.setHandlerId(LoginContext.getCurrentUserId());
        }
        Page<AssetsTaskDoneVO> assetsTaskDoneVOPage = workTaskRecordMapper.getAssetsTaskDoneList(assetsSelectDTO, page);
        assetsTaskDoneVOPage.getRecords().forEach(x->{
            x.setHandingTime(DateUtils.getTimeLong(x.getHandingTimeLong()==null?0L:x.getHandingTimeLong()));
        });
        return assetsTaskDoneVOPage;
    }

}
