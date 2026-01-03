package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.EvaluateInfo;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.StaffInfo;
import cc.mrbird.febs.cos.service.IEvaluateInfoService;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cc.mrbird.febs.cos.service.IStaffInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/evaluate-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EvaluateInfoController {

    private final IEvaluateInfoService evaluateInfoService;

    private final IStaffInfoService staffInfoService;

    private final IOrderInfoService orderInfoService;

    /**
     * 分页获取订单评价信息
     *
     * @param page         分页对象
     * @param evaluateInfo 订单评价信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<EvaluateInfo> page, EvaluateInfo evaluateInfo) {
        return R.ok(evaluateInfoService.selectEvaluatePage(page, evaluateInfo));
    }

    /**
     * 获取ID获取订单评价详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(evaluateInfoService.getById(id));
    }

    /**
     * 获取订单评价信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(evaluateInfoService.list());
    }

    /**
     * 新增订单评价信息
     *
     * @param evaluateInfo 订单评价信息
     * @return 结果
     */
    @PostMapping
    public R save(EvaluateInfo evaluateInfo) {
        evaluateInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        OrderInfo orderInfo = orderInfoService.getById(evaluateInfo.getOrderId());
        StaffInfo staffInfo = staffInfoService.getById(orderInfo.getStaffIds());
        List<EvaluateInfo> evaluateInfoList = evaluateInfoService.list(Wrappers.<EvaluateInfo>lambdaQuery().eq(EvaluateInfo::getStaffId, staffInfo.getId()));
        if (CollectionUtil.isEmpty(evaluateInfoList)) {
            staffInfo.setScore(new BigDecimal("3.5"));
        } else {
            staffInfo.setScore(evaluateInfoList.stream().map(EvaluateInfo::getScore).reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(evaluateInfoList.size()), 1, RoundingMode.HALF_UP));
        }
        return R.ok(evaluateInfoService.save(evaluateInfo));
    }

    /**
     * 修改订单评价信息
     *
     * @param evaluateInfo 订单评价信息
     * @return 结果
     */
    @PutMapping
    public R edit(EvaluateInfo evaluateInfo) {
        return R.ok(evaluateInfoService.updateById(evaluateInfo));
    }

    /**
     * 删除订单评价信息
     *
     * @param ids ids
     * @return 订单评价信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(evaluateInfoService.removeByIds(ids));
    }
}
