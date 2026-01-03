package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.OrderQuotation;
import cc.mrbird.febs.cos.dao.OrderQuotationMapper;
import cc.mrbird.febs.cos.service.IOrderQuotationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class OrderQuotationServiceImpl extends ServiceImpl<OrderQuotationMapper, OrderQuotation> implements IOrderQuotationService {

    /**
     * 分页获取回收维修报价信息
     *
     * @param page           分页对象
     * @param orderQuotation 回收维修报价信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryQuotation(Page<OrderQuotation> page, OrderQuotation orderQuotation) {
        return baseMapper.queryQuotation(page, orderQuotation);
    }

    /**
     * 根据订单ID获取报价信息
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> queryQuotationByOrder(Integer orderId) {
        return baseMapper.queryQuotationByOrder(orderId);
    }
}
