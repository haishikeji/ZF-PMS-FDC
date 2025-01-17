package com.px.service;
import com.px.entity.Districts;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.DistrictsTreeVO;

import java.util.List;

/**
 * <p>
 * 地区 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-01-10
 */
public interface IDistrictsService extends IService<Districts> {

        Page<Districts> selectPage(Page page,Districts districts);

        Districts detail(Long id);

    List<DistrictsTreeVO> getDistrictsTree();

    String getDistIds(String city, String county);
}
