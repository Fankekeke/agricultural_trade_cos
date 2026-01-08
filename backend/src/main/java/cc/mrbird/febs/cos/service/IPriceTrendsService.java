package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.PriceTrends;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IPriceTrendsService extends IService<PriceTrends> {

    /**
     * 分页获取价格记录信息
     *
     * @param page        分页对象
     * @param priceTrends 价格记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPriceTrendsPage(Page<PriceTrends> page, PriceTrends priceTrends);

    /**
     * 查询品类价格走势
     *
     * @param category 品类
     * @return 结果
     */
    LinkedHashMap<String, Object> queryCategoryTrend(String category);
}
