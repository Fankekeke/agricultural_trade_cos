package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.PriceTrends;
import cc.mrbird.febs.cos.dao.PriceTrendsMapper;
import cc.mrbird.febs.cos.service.IPriceTrendsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class PriceTrendsServiceImpl extends ServiceImpl<PriceTrendsMapper, PriceTrends> implements IPriceTrendsService {

    /**
     * 分页获取价格记录信息
     *
     * @param page        分页对象
     * @param priceTrends 价格记录信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPriceTrendsPage(Page<PriceTrends> page, PriceTrends priceTrends) {
        return baseMapper.queryPriceTrendsPage(page, priceTrends);
    }
}
