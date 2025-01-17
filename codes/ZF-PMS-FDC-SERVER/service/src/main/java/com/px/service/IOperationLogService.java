package com.px.service;
import com.px.entity.OperationLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 系统操作日志 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-01-16
 */
public interface IOperationLogService extends IService<OperationLog> {

        Page<OperationLog> selectPage(Page page,OperationLog operationLog);

        OperationLog detail(Long id);

        Boolean add(OperationLog operationLog);

        Boolean update(OperationLog operationLog);

        Boolean delete(Long id);

}
