package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OrderQuotation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface OrderQuotationMapper extends BaseMapper<OrderQuotation> {

    /**
     * 分页获取回收维修报价信息
     *
     * @param page           分页对象
     * @param orderQuotation 回收维修报价信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryQuotation(Page<OrderQuotation> page, @Param("orderQuotation") OrderQuotation orderQuotation);

    /**
     * 根据订单ID获取报价信息
     *
     * @param orderId 订单ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> queryQuotationByOrder(@Param("orderId") Integer orderId);
}
