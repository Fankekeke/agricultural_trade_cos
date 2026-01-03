package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.OrderQuotation;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.entity.WithdrawInfo;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cc.mrbird.febs.cos.service.IOrderQuotationService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/order-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoController {

    private final IOrderInfoService orderInfoService;

    private final IOrderQuotationService orderQuotationService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OrderInfo> page, OrderInfo orderInfo) {
        return R.ok(orderInfoService.selectOrderPage(page, orderInfo));
    }

    /**
     * 订单分配
     *
     * @param orderId 订单ID
     * @param staffId 技师ID
     * @return 结果
     */
    @GetMapping("/checkOrder")
    public R checkOrder(@RequestParam("orderId") Integer orderId, @RequestParam("staffId") Integer staffId) {
        return R.ok(orderInfoService.checkOrder(orderId, staffId));
    }

    /**
     * 查询订单维修步骤
     *
     * @param orderId 订单ID
     * @return 订单维修步骤
     */
    @GetMapping("/queryRepairStep/{orderId}")
    public R queryRepairStep(@PathVariable("orderId") Integer orderId) {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        return R.ok(StrUtil.isEmpty(orderInfo.getFixProcessInfo()) ? null : orderInfo.getFixProcessInfo());
    }

    /**
     * 确认报价信息
     *
     * @param quotationId 报价ID
     * @return 结果
     */
    @GetMapping("/checkQuotation")
    public R checkQuotation(@RequestParam("quotationId") Integer quotationId) {
        OrderQuotation quotation = orderQuotationService.getById(quotationId);
        OrderInfo orderInfo = orderInfoService.getById(quotation.getOrderId());
        orderInfo.setStaffIds(quotation.getStaffId().toString());
        orderInfo.setTotal(quotation.getPrice());
        orderInfo.setOrderPrice(quotation.getPrice());
        orderInfo.setStatus("1");
        if ("2".equals(orderInfo.getOrderMethod())) {
            orderInfo.setEndAddressId(quotation.getAddressId());
        }
        orderInfo.setQuotationId(quotationId);
        return R.ok(orderInfoService.updateById(orderInfo));
    }

    /**
     * 获取订单付款信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping("/getPriceTotal")
    public R getPriceTotal(OrderInfo orderInfo) {
        return R.ok(orderInfoService.getPriceTotal(orderInfo));
    }

    /**
     * 查询收益记录详情
     *
     * @param id 记录ID
     * @return 结果
     */
    @GetMapping("/income/{id}")
    public R selectIncomeDetail(@PathVariable("id") Integer id) {
        return R.ok(orderInfoService.selectIncomeDetail(id));
    }

    /**
     * 提现记录详情
     *
     * @param withdrawId 提现记录ID
     * @return 结果
     */
    @GetMapping("/withdraw/{id}")
    public R selectWithdrawDetail(@PathVariable("id") Integer withdrawId) {
        return R.ok(orderInfoService.selectWithdrawDetail(withdrawId));
    }

    /**
     * 订单收货
     *
     * @param orderCode 订单编号
     * @param status    状态
     * @return 结果
     */
    @GetMapping("/auditOrderFinish")
    public R auditOrderFinish(@RequestParam("orderCode") String orderCode, @RequestParam("status") Integer status) {
        return R.ok(orderInfoService.auditOrderFinish(orderCode, status));
    }

    /**
     * 订单支付
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @GetMapping("/orderPay")
    public R orderPay(@RequestParam("orderCode") String orderCode) {
        return R.ok(orderInfoService.orderPay(orderCode));
    }

    /**
     * 维修完成
     *
     * @param orderId 订单编号
     * @return 结果
     */
    @GetMapping("/complete/{orderId}")
    public R orderComplete(@PathVariable("orderId") Integer orderId) {
        return R.ok(orderInfoService.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getFinishDate, DateUtil.formatDateTime(new Date())).eq(OrderInfo::getId, orderId)));
    }

    /**
     * 回收完成
     *
     * @param orderId 订单编号
     * @return 结果
     */
    @GetMapping("/recycleComplete/{orderId}")
    public R orderRecycleComplete(@PathVariable("orderId") Integer orderId) {
        return R.ok(orderInfoService.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getFinishDate, DateUtil.formatDateTime(new Date())).set(OrderInfo::getStatus, "3").eq(OrderInfo::getId, orderId)));
    }

    /**
     * 更新订单维修步骤
     *
     * @param orderInfo 订单信息
     * @return 订单信息
     */
    @PutMapping("/setRepairStep")
    public R setRepairStep(OrderInfo orderInfo) {
        return R.ok(orderInfoService.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getFixProcessInfo, orderInfo.getFixProcessInfo()).eq(OrderInfo::getId, orderInfo.getId())));
    }

    /**
     * 管理员审核提现申请
     *
     * @param withdrawInfo 提现记录
     * @return 结果
     */
    @PostMapping("/auditWithdraw")
    public R auditWithdraw(WithdrawInfo withdrawInfo) throws FebsException {
        return R.ok(orderInfoService.auditWithdraw(withdrawInfo));
    }

    /**
     * 根据用户ID获取优惠券
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/discount/{userId}")
    public R selectDiscountByUser(@PathVariable("userId") Integer userId) {
        return R.ok(orderInfoService.selectDiscountByUser(userId));
    }

    /**
     * 获取ID获取订单详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderInfoService.selectOrderDetail(id));
    }

    /**
     * 修改物流信息
     *
     * @param orderInfo 物流信息
     * @return 结果
     */
    @PutMapping("/updateLogisticsInfo")
    public R updateLogisticsInfo(OrderInfo orderInfo) {
        return R.ok(orderInfoService.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getLogisticsInfo, orderInfo.getLogisticsInfo()).eq(OrderInfo::getId, orderInfo.getId())));
    }

    /**
     * 确认收货
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @GetMapping("/confirmReceipt/{orderId}")
    public R confirmReceipt(@PathVariable("orderId") Integer orderId) {
        return R.ok(orderInfoService.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getDeliveryDate, DateUtil.formatDateTime(new Date())).eq(OrderInfo::getId, orderId)));
    }

    /**
     * 获取ID获取评价详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/evaluate/{id}")
    public R evaluateDetail(@PathVariable("id") Integer id) {
        return R.ok(orderInfoService.selectEvaluate(id));
    }

    /**
     * 获取订单信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderInfoService.list());
    }

    /**
     * 管理员获取主页统计数据
     *
     * @return 结果
     */
    @GetMapping("/admin/homeData")
    public R homeDataByAdmin() {
        return R.ok(orderInfoService.homeDataByAdmin());
    }

    /**
     * 技师获取统计信息
     *
     * @param userId 技师ID
     * @return 结果
     */
    @GetMapping("/homeData")
    public R selectHomeDataByMerchant(@RequestParam("userId") Integer userId) {
        return R.ok(orderInfoService.selectHomeDataByMerchant(userId));
    }

    /**
     * 新增订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping
    public R save(OrderInfo orderInfo) {
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfo.getUserId()));
        if (userInfo != null) {
            orderInfo.setUserId(userInfo.getId());
        }
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        orderInfo.setStatus("0");
        orderInfo.setCode("ORD-" + System.currentTimeMillis());
        // 针对物件名称进行分词
        TokenizerEngine engine = TokenizerUtil.createEngine();
        //解析文本
        Result result = engine.parse(orderInfo.getOrderName());
        String resultStr = CollUtil.join((Iterator<Word>)result, ",");
        orderInfo.setTagList(resultStr);
        return R.ok(orderInfoService.save(orderInfo));
    }

    /**
     * 修改订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PutMapping
    public R edit(OrderInfo orderInfo) {
        return R.ok(orderInfoService.updateById(orderInfo));
    }

    /**
     * 删除订单信息
     *
     * @param ids ids
     * @return 订单信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderInfoService.removeByIds(ids));
    }
}
