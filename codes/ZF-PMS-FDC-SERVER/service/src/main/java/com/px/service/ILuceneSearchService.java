package com.px.service;

import com.px.vo.LuceneSearchVO;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.IOException;
import java.util.List;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2024/4/26
 * created with IntelliJ IDEA.
 */
public interface ILuceneSearchService {


    /**
     * 初始化Lucene索引
     */
    void initializeIndex();


    /**
     * 搜索
     * @param keyword
     * @return
     */
    List<LuceneSearchVO> globalSearch(String businessType, String keyword) throws IOException, ParseException, InvalidTokenOffsetsException;


    /**
     * 添加索引
     */
    void addIndex();

    /**
     * 删除索引
     */
    void removeIndex();

}
