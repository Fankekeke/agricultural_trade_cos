package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DiscountInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IDiscountInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/discount-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscountInfoController {

    private final IDiscountInfoService discountInfoService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取优惠券信息
     *
     * @param page         分页对象
     * @param discountInfo 优惠券信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DiscountInfo> page, DiscountInfo discountInfo) {
        return R.ok(discountInfoService.selectDiscountPage(page, discountInfo));
    }

    /**
     * 查询用户可用的优惠券
     *
     * @param userId       用户ID
     * @param orderPrice   订单价格
     * @return 结果
     */
    @GetMapping("/queryDiscountByUser")
    public R queryDiscountByUser(@RequestParam Integer userId, @RequestParam BigDecimal orderPrice) {
        List<DiscountInfo> discountInfos = new ArrayList<>();
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        // 判断是有可用优惠券
        List<DiscountInfo> discountInfoList = discountInfoService.list(Wrappers.<DiscountInfo>lambdaQuery().eq(DiscountInfo::getUserId, userInfo.getId()).eq(DiscountInfo::getStatus, "0"));
        if (CollectionUtil.isNotEmpty(discountInfoList)) {
            List<DiscountInfo> discount1s = discountInfoList.stream().filter(e -> "2".equals(e.getType())).collect(Collectors.toList());
            List<DiscountInfo> discount2s = discountInfoList.stream().filter(e -> "1".equals(e.getType()) && orderPrice.compareTo(e.getThreshold()) >= 0).collect(Collectors.toList());

            discount1s.addAll(discount2s);
            discountInfos = discount1s;
        }
        return R.ok(discountInfos);
    }

    /**
     * 获取ID获取优惠券详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(discountInfoService.getById(id));
    }

    /**
     * 获取优惠券信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(discountInfoService.list());
    }

    /**
     * 新增优惠券信息
     *
     * @param discountInfo 优惠券信息
     * @return 结果
     */
    @PostMapping
    public R save(DiscountInfo discountInfo) {
        discountInfo.setCode("DC-" + System.currentTimeMillis());
        discountInfo.setStatus("0");
        discountInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(discountInfoService.save(discountInfo));
    }

    /**
     * 修改优惠券信息
     *
     * @param discountInfo 优惠券信息
     * @return 结果
     */
    @PutMapping
    public R edit(DiscountInfo discountInfo) {
        return R.ok(discountInfoService.updateById(discountInfo));
    }

    /**
     * 删除优惠券信息
     *
     * @param ids ids
     * @return 优惠券信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(discountInfoService.removeByIds(ids));
    }
}
