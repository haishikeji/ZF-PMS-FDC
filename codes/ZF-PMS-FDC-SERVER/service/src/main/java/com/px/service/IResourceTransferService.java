package com.px.service;
import com.px.entity.ResourceTransfer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.ResourceTransferVO;

import java.util.List;

/**
 * <p>
 * 业务资源转移表 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-05-31
 */
public interface IResourceTransferService extends IService<ResourceTransfer> {

        Page<ResourceTransfer> selectPage(Page page,ResourceTransfer resourceTransfer);

        ResourceTransferVO detail(Long id);

        Boolean add(ResourceTransfer resourceTransfer);

        Boolean update(ResourceTransfer resourceTransfer);

        Boolean delete(Long id);

        List<ResourceTransferVO> transferHistory(String resourceType, Long resourceId);
}
