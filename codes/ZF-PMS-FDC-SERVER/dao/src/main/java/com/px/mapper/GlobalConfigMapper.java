package com.px.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.entity.GlobalConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 全局配置 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2022-12-07
 */
public interface GlobalConfigMapper extends BaseMapper<GlobalConfig> {

    void insertOrUpdateBatch(@Param("list") List<GlobalConfig> list);

    Boolean updateBatchByKey(@Param("list") List<GlobalConfig> globalConfigs);

    String getValueByKeyFiled(@Param("key") String key, @Param("field") String field);

    String getValueByFiled( @Param("field") String field);

    List<GlobalConfig> getConfigByKey(@Param("key") String key);

    GlobalConfig getGlobalConfig(@Param("key") String key, @Param("field") String field);

}
