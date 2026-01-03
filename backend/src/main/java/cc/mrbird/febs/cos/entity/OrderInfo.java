package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单信息
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 物件描述
     */
    private String content;

    /**
     * 瑕疵描述
     */
    private String flawContent;

    /**
     * 瑕疵图片
     */
    private String flawImages;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 预估价格
     */
    private BigDecimal forecastPrice;

    /**
     * 视频
     */
    private String video;

    /**
     * 总价格
     */
    private BigDecimal total;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 订单状态（0.等待报价 1.未支付 2.维修回收中 3.已完成）
     */
    private String status;

    /**
     * 分配技师
     */
    private String staffIds;

    /**
     * 下单时间
     */
    private String payDate;

    /**
     * 物品类型
     */
    private String goodsType;

    /**
     * 物品图片
     */
    private String images;

    /**
     * 物品重量
     */
    private BigDecimal weight;

    /**
     * 物品高度
     */
    private BigDecimal height;

    /**
     * 物品宽度
     */
    private BigDecimal width;

    /**
     * 开始地址
     */
    private Integer startAddressId;

    /**
     * 送货地址
     */
    private Integer endAddressId;

    /**
     * 配送时间
     */
    private String deliveryDate;

    /**
     * 物流信息
     */
    private String logisticsInfo;

    /**
     * 修复流程信息
     */
    private String fixProcessInfo;

    /**
     * 完成时间
     */
    private String finishDate;

    /**
     * 公里数
     */
    private BigDecimal kilometre;

    /**
     * 距离价格
     */
    private BigDecimal distributionPrice;

    /**
     * 订单价格
     */
    private BigDecimal orderPrice;

    /**
     * 优惠券ID
     */
    private Integer discountId;

    /**
     * 折扣后价格
     */
    private BigDecimal afterOrderPrice;

    /**
     * 订单积分
     */
    private BigDecimal integral;

    /**
     * 订单类型（1.维修 2.回收）
     */
    private String orderType;

    /**
     * 订单方式（1.上门维修回收 2.邮寄）
     */
    private String orderMethod;

    /**
     * 修复难度（1.轻度、2.中度、3.复杂）
     */
    private String fixDifficulty;

    /**
     * 物件标签
     */
    private String tagList;

    /**
     * 报价ID
     */
    private Integer quotationId;

    /**
     * 是否使用优惠券
     */
    @TableField(exist = false)
    private boolean useDiscount;

    /**
     * 联系方式
     */
    @TableField(exist = false)
    private String phone;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String staffFlag;

    @TableField(exist = false)
    private List<DiscountInfo> discountInfos;
}
