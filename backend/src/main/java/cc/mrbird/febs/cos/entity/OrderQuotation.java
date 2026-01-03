package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 维修回收报价
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderQuotation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 维修员ID
     */
    private Integer staffId;

    /**
     * 地址ID
     */
    private Integer addressId;

    /**
     * 报价价格
     */
    private BigDecimal price;

    /**
     * 工时
     */
    private Integer workHour;

    /**
     * 报价描述
     */
    private String content;

    /**
     * 报价时间
     */
    private String createDate;

    /**
     * 订单名称
     */
    @TableField(exist = false)
    private String orderName;

    /**
     * 工单名称
     */
    @TableField(exist = false)
    private String staffName;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String userName;


}
