package com.px.service;
import com.px.entity.BrokerageMarketerRatio;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.vo.MarketerRatioVO;

/**
 * <p>
 * 项目提成比例（市场人员） 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-03
 */
public interface IBrokerageMarketerRatioService extends IService<BrokerageMarketerRatio> {

        Page<MarketerRatioVO> selectPage(Page page, BrokerageMarketerRatio brokerageMarketerRatio);

        BrokerageMarketerRatio detail(Long id);

        Boolean add(BrokerageMarketerRatio brokerageMarketerRatio);

        Boolean update(BrokerageMarketerRatio brokerageMarketerRatio);

        Boolean delete(Long id);

        void cacheMarketerRatio();

}
