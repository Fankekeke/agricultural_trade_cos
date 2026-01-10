package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.PriceTrends;
import cc.mrbird.febs.cos.dao.PriceTrendsMapper;
import cc.mrbird.febs.cos.service.IPriceTrendsService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * @author FanK
 */
@Service
public class PriceTrendsServiceImpl extends ServiceImpl<PriceTrendsMapper, PriceTrends> implements IPriceTrendsService {

    /**
     * 分页获取价格记录信息
     *
     * @param page        分页对象
     * @param priceTrends 价格记录信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPriceTrendsPage(Page<PriceTrends> page, PriceTrends priceTrends) {
        return baseMapper.queryPriceTrendsPage(page, priceTrends);
    }

    /**
     * 查询品类价格走势
     *
     * @param category 品类
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> queryCategoryTrend(String category) {
        // 统计前14天价格记录，以及预测后14天的价格走势
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        // 获取前14天的历史价格数据
        List<LinkedHashMap<String, Object>> historyData = baseMapper.getHistoryPriceData(category);

        // 预测后14天的价格走势
        List<LinkedHashMap<String, Object>> forecastData = predictFutureTrend(historyData);

        // 构建返回结果
        result.put("category", category);
        result.put("historyData", historyData);
        result.put("forecastData", forecastData);

        return result;
    }

    /**
     * 预测未来价格趋势
     *
     * @param historyData 历史数据
     * @return 预测数据
     */
    private List<LinkedHashMap<String, Object>> predictFutureTrend(List<LinkedHashMap<String, Object>> historyData) {
        List<LinkedHashMap<String, Object>> forecastList = new ArrayList<>();

        if (historyData == null || historyData.isEmpty()) {
            return forecastList;
        }

        // 计算平均价格和趋势
        double sum = 0;
        for (LinkedHashMap<String, Object> data : historyData) {
            sum += Double.parseDouble(data.get("price").toString());
        }
        double average = Math.round(sum / historyData.size() * 100.0) / 100.0;

        // 获取最新价格作为基准
        LinkedHashMap<String, Object> latest = historyData.get(historyData.size() - 1);
        double latestPrice = Math.round(Double.parseDouble(latest.get("price").toString()) * 100.0) / 100.0;
        String latestDate = latest.get("date").toString();

        // 计算趋势斜率
        double trend = 0;
        if (historyData.size() > 1) {
            LinkedHashMap<String, Object> earliest = historyData.get(0);
            double earliestPrice = Math.round(Double.parseDouble(earliest.get("price").toString()) * 100.0) / 100.0;
            trend = Math.round((latestPrice - earliestPrice) / (historyData.size() - 1) * 100.0) / 100.0;
        }

        // 计算波动因子（基于历史数据的标准差）
        double variance = 0;
        for (LinkedHashMap<String, Object> data : historyData) {
            double price = Double.parseDouble(data.get("price").toString());
            variance += Math.pow(price - average, 2);
        }
        variance = variance / historyData.size();
        // 标准差
        double stdDev = Math.round(Math.sqrt(variance) * 100.0) / 100.0;

        // 预测未来14天的数据
        try {
            Date baseDate = DateUtil.parse(latestDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(baseDate);

            for (int i = 1; i <= 14; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                String forecastDate = DateUtil.formatDate(calendar.getTime());
                // 基于趋势预测价格，加入波动因子
                double forecastPrice = latestPrice + (trend * i);
                // 根据趋势方向添加随机波动
                double fluctuation = generateFluctuation(stdDev, trend, i);
                forecastPrice += fluctuation;

                if (forecastPrice < 0) {
                    forecastPrice = average;
                }

                // 保留两位小数
                forecastPrice = Math.round(forecastPrice * 100.0) / 100.0;

                LinkedHashMap<String, Object> forecastItem = new LinkedHashMap<>();
                forecastItem.put("date", forecastDate);
                forecastItem.put("price", forecastPrice);
                forecastItem.put("type", "forecast");

                // 添加趋势因子信息
                forecastItem.put("trendFactor", trend);
                forecastItem.put("fluctuation", fluctuation);
                forecastItem.put("isRising", trend > 0);

                forecastList.add(forecastItem);
            }
        } catch (Exception e) {
            log.error("日期解析错误", e);
        }
        return forecastList;
    }

    /**
     * 生成价格波动因子
     *
     * @param stdDev 标准差
     * @param trend  趋势
     * @param day    预测天数
     * @return 波动值
     */
    private double generateFluctuation(double stdDev, double trend, int day) {
        // 使用随机数生成波动，考虑趋势方向
        Random random = new Random();
        // 波动幅度基于标准差和天数，天数越远波动越大
        double baseFluctuation = stdDev * 0.5;
        // 随着天数增加，波动幅度逐渐增加
        double dayFactor = 1 + (day * 0.05);
        // 生成-1到1之间的随机数
        double randomFactor = (random.nextDouble() * 2 - 1) * baseFluctuation * dayFactor;
        // 如果趋势为正（上升），略微偏向正向波动；趋势为负（下降），略微偏向负向波动
        if (trend > 0) {
            // 上升趋势，增加一点正向波动
            randomFactor = randomFactor * 0.9 + stdDev * 0.1;
        } else if (trend < 0) {
            // 下降趋势，增加一点负向波动
            randomFactor = randomFactor * 0.9 - stdDev * 0.1;
        }
        // 保留两位小数
        return Math.round(randomFactor * 100.0) / 100.0;
    }
}
