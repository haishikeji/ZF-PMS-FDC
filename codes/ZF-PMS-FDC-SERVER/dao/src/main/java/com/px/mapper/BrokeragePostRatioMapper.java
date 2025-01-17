package com.px.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.bo.LeaderRatioBO;
import com.px.entity.BrokeragePostRatio;
import com.px.dao.CustomBaseMapper;
import com.px.vo.PostRatioVO;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 领导岗位抽成率 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-10
 */
public interface BrokeragePostRatioMapper extends CustomBaseMapper<BrokeragePostRatio> {

    Set<LeaderRatioBO> getLeadersRatio(@Param("parentPostIds") Set<Long> parentPostIds,@Param("userId") Long userId);

    Page<PostRatioVO> page(Page page, @Param("ratio") PostRatioVO postRatioVO);

    Set<LeaderRatioBO> getLeaderRatioBO(@Param("leaderIds") Set<Long> leaderIds);

    Set<LeaderRatioBO> marketManagerRatioBO();
}
