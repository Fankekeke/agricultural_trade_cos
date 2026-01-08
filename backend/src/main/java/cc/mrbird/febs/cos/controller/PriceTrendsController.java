package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.PriceTrends;
import cc.mrbird.febs.cos.service.IPriceTrendsService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/price-trends")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PriceTrendsController {

    private final IPriceTrendsService priceTrendsService;

    /**
     * 分页获取价格记录信息
     *
     * @param page        分页对象
     * @param priceTrends 价格记录信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PriceTrends> page, PriceTrends priceTrends) {
        return R.ok(priceTrendsService.queryPriceTrendsPage(page, priceTrends));
    }

    /**
     * 获取ID获取价格记录详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(priceTrendsService.getById(id));
    }

    /**
     * 获取价格记录信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(priceTrendsService.list());
    }

    /**
     * 新增价格记录信息
     *
     * @param priceTrends 价格记录信息
     * @return 结果
     */
    @PostMapping
    public R save(PriceTrends priceTrends) throws FebsException {
        // 校验是否重复记录
        PriceTrends trends = priceTrendsService.getOne(Wrappers.<PriceTrends>lambdaQuery().eq(PriceTrends::getCategory, priceTrends.getCategory()).eq(PriceTrends::getRecordDate, priceTrends.getRecordDate()));
        if (trends != null) {
            throw new FebsException("该品类该时间已记录！");
        }
        return R.ok(priceTrendsService.save(priceTrends));
    }

    /**
     * 根据品类获取当前价格
     *
     * @param category 品类
     * @return 结果
     */
    @GetMapping("/queryCategoryPrice")
    public R queryCategoryPrice(String category) {
        PriceTrends trends = priceTrendsService.getOne(Wrappers.<PriceTrends>lambdaQuery().eq(PriceTrends::getCategory, category).orderByDesc(PriceTrends::getRecordDate).last("limit 1"));
        if (trends != null) {
            return R.ok(trends.getPrice());
        } else {
            return R.ok(0);
        }
    }

    /**
     * 根据品类获取价格走势
     *
     * @param category 品类
     * @return 列表
     */
    public R queryCategoryTrend(String category) {
        return R.ok(priceTrendsService.queryCategoryTrend(category));
    }

    /**
     * 修改价格记录信息
     *
     * @param priceTrends 价格记录信息
     * @return 结果
     */
    @PutMapping
    public R edit(PriceTrends priceTrends) throws FebsException {
        // 校验是否重复记录
        PriceTrends trends = priceTrendsService.getOne(Wrappers.<PriceTrends>lambdaQuery().eq(PriceTrends::getCategory, priceTrends.getCategory()).eq(PriceTrends::getRecordDate, priceTrends.getRecordDate()));
        if (trends != null && !trends.getId().equals(priceTrends.getId())) {
            throw new FebsException("该品类该时间已记录！");
        }
        return R.ok(priceTrendsService.updateById(priceTrends));
    }

    /**
     * 删除价格记录信息
     *
     * @param ids ids
     * @return 价格记录信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(priceTrendsService.removeByIds(ids));
    }
}
