package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.PriceConfig;
import cc.mrbird.febs.cos.service.IPriceConfigService;
import cn.hutool.core.date.DateUtil;
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
@RequestMapping("/cos/price-config")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PriceConfigController {

    private final IPriceConfigService priceConfigService;

    /**
     * 分页获取品类信息
     *
     * @param page        分页对象
     * @param priceConfig 品类信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PriceConfig> page, PriceConfig priceConfig) {
        return R.ok(priceConfigService.selectPriceConfigPage(page, priceConfig));
    }

    /**
     * 获取ID获取品类详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(priceConfigService.getById(id));
    }

    /**
     * 获取品类信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(priceConfigService.list());
    }

    /**
     * 新增品类信息
     *
     * @param priceConfig 品类信息
     * @return 结果
     */
    @PostMapping
    public R save(PriceConfig priceConfig) {
        return R.ok(priceConfigService.save(priceConfig));
    }

    /**
     * 修改品类信息
     *
     * @param priceConfig 品类信息
     * @return 结果
     */
    @PutMapping
    public R edit(PriceConfig priceConfig) {
        return R.ok(priceConfigService.updateById(priceConfig));
    }

    /**
     * 删除品类信息
     *
     * @param ids ids
     * @return 品类信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(priceConfigService.removeByIds(ids));
    }
}
