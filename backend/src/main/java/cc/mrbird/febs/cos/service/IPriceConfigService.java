package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.PriceConfig;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IPriceConfigService extends IService<PriceConfig> {

    /**
     * 分页获取品类信息
     *
     * @param page        分页对象
     * @param priceConfig 品类信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPriceConfigPage(Page<PriceConfig> page, PriceConfig priceConfig);
}
