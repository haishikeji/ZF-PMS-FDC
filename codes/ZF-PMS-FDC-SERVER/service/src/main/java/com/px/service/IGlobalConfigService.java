package com.px.service;
import com.px.entity.GlobalConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 全局配置 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2022-12-07
 */
public interface IGlobalConfigService extends IService<GlobalConfig> {

        Page<GlobalConfig> selectPage(Page page,GlobalConfig globalConfig);

        GlobalConfig detail(Long id);

        Boolean add(GlobalConfig globalConfig);

        Boolean update(GlobalConfig globalConfig);

        Boolean delete(Long id);

        String getValue(String key,String field);

        BigDecimal getMathValue(String key, String field);

        BigDecimal getValueByFiled(String filed);

        GlobalConfig getGlobalConfig(String key, String field);

        List<GlobalConfig> getConfigByKey(String key);
}
