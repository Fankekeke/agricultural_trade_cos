package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.PriceConfig;
import cc.mrbird.febs.cos.dao.PriceConfigMapper;
import cc.mrbird.febs.cos.service.IPriceConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class PriceConfigServiceImpl extends ServiceImpl<PriceConfigMapper, PriceConfig> implements IPriceConfigService {

    /**
     * 分页获取品类信息
     *
     * @param page        分页对象
     * @param priceConfig 品类信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectPriceConfigPage(Page<PriceConfig> page, PriceConfig priceConfig) {
        return baseMapper.selectPriceConfigPage(page, priceConfig);
    }
}
