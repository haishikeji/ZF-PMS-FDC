package com.px.service;
import com.px.entity.ItemStage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.ItemStageVO;

import java.util.List;

/**
 * <p>
 * 项目阶段表 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2022-12-08
 */
public interface IItemStageService extends IService<ItemStage> {

        Page<ItemStage> selectPage(Page page,ItemStage itemStage);

        ItemStage detail(Long id);

    Long add(ItemStageVO itemStageVO);

        Boolean update(ItemStageVO itemStageVO);

        Boolean delete(Long id);

    List<ItemStageVO> listByItemId(Long itemId);

    List<List<ItemStageVO>> stageList(ItemStageVO itemStageVO);

    Boolean confirmation(Long stageId, Boolean overdue);
}
