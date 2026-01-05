package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.PriceConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface PriceConfigMapper extends BaseMapper<PriceConfig> {

    /**
     * 分页获取品类信息
     *
     * @param page        分页对象
     * @param priceConfig 品类信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPriceConfigPage(Page<PriceConfig> page, @Param("queryParam") PriceConfig priceConfig);
}
