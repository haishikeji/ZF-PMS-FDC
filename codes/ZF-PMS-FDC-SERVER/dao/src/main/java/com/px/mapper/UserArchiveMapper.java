package com.px.mapper;

import com.px.entity.UserArchive;
import com.px.dao.CustomBaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 员工档案 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2024-09-02
 */
public interface UserArchiveMapper extends CustomBaseMapper<UserArchive> {

    UserArchive getRegistrationNoByUserId(@Param("userId") Long userId);
}
