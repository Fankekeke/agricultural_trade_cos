package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderQuotation;
import cc.mrbird.febs.cos.entity.StaffInfo;
import cc.mrbird.febs.cos.service.IOrderQuotationService;
import cc.mrbird.febs.cos.service.IStaffInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/order-quotation")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderQuotationController {

    private final IOrderQuotationService orderQuotationService;

    private final IStaffInfoService staffInfoService;

    /**
     * 分页获取订单报价信息
     *
     * @param page           分页对象
     * @param orderQuotation 订单报价信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OrderQuotation> page, OrderQuotation orderQuotation) {
        return R.ok(orderQuotationService.queryQuotation(page, orderQuotation));
    }

    /**
     * 根据订单ID获取报价信息
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @GetMapping("/queryQuotationByOrder")
    public R queryQuotationByOrder(@RequestParam Integer orderId) {
        return R.ok(orderQuotationService.queryQuotationByOrder(orderId));
    }

    /**
     * 获取ID获取订单报价详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderQuotationService.getById(id));
    }

    /**
     * 获取订单报价信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderQuotationService.list());
    }

    /**
     * 获取报价信息
     *
     * @param orderId 订单ID
     * @param staffId 批发商ID
     * @return 结果
     */
    @GetMapping("/queryQuotationByStaff")
    public R queryQuotationByStaff(@RequestParam Integer orderId, @RequestParam Integer staffId) {
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, staffId));
        return R.ok(orderQuotationService.getOne(Wrappers.<OrderQuotation>lambdaQuery().eq(OrderQuotation::getOrderId, orderId).eq(OrderQuotation::getStaffId, staffInfo.getId())));
    }

    /**
     * 新增订单报价信息
     *
     * @param orderQuotation 订单报价信息
     * @return 结果
     */
    @PostMapping
    public R save(OrderQuotation orderQuotation) {
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, orderQuotation.getStaffId()));
        if (staffInfo != null) {
            orderQuotation.setStaffId(staffInfo.getId());
        }
        orderQuotation.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(orderQuotationService.save(orderQuotation));
    }

    /**
     * 修改订单报价信息
     *
     * @param orderQuotation 订单报价信息
     * @return 结果
     */
    @PutMapping
    public R edit(OrderQuotation orderQuotation) {
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, orderQuotation.getStaffId()));
        if (staffInfo != null) {
            orderQuotation.setStaffId(staffInfo.getId());
        }
        return R.ok(orderQuotationService.updateById(orderQuotation));
    }

    /**
     * 删除订单报价信息
     *
     * @param ids ids
     * @return 订单报价信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderQuotationService.removeByIds(ids));
    }
}
