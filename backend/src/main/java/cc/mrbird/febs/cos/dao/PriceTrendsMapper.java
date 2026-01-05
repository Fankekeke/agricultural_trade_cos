package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.PriceTrends;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface PriceTrendsMapper extends BaseMapper<PriceTrends> {

    /**
     * 分页获取价格记录信息
     *
     * @param page        分页对象
     * @param priceTrends 价格记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPriceTrendsPage(Page<PriceTrends> page, @Param("queryParam") PriceTrends priceTrends);
}
