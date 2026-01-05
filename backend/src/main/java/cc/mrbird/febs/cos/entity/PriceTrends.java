package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 价格记录
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PriceTrends implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录时间
     */
    private LocalDate recordDate;

    /**
     * 品类
     */
    private String category;

    /**
     * 价格
     */
    private BigDecimal price;


}
