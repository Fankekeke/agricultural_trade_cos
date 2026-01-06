package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 品类管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PriceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 品类
     */
    private String category;

    /**
     * 起始价格
     */
    private BigDecimal startPrice;

    /**
     * 增长比率
     */
    private BigDecimal dailyRatio;

    /**
     * 创建时间
     */
    private BigDecimal createDate;

}
