package com.px.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.bo.SimpleParentModel;
import com.px.common.PullDownModel;
import com.px.entity.Post;
import com.px.dao.CustomBaseMapper;
import com.px.vo.PostVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 岗位 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2022-11-29
 */
public interface PostMapper extends CustomBaseMapper<Post> {

    void upsert(@Param("post") Post post);

    Page<PostVO> getPage(Page page, @Param("postVO") PostVO postVO);

    List<PullDownModel> xSimpleAll();

    List<SimpleParentModel> getDepartmentList(@Param("postIds") Set<Long> postIds);
}
