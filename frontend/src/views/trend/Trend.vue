
<template>
  <div class="trend-container">
    <div class="category-grid">
      <div
        v-for="item in categoryList"
        :key="item.id"
        class="category-card"
        @click="selectCategory(item.category)"
      >
        <div class="category-header">
          <h3 class="category-name">{{ item.category }}</h3>
          <div class="price-change" :class="getChangeClass(item)">
            {{ getChangeText(item) }}
          </div>
        </div>
        <div class="category-body">
          <div class="current-price">¥{{ item.price }}</div>
          <div class="price-info">
            <span class="start-price">起始价: ¥{{ item.startPrice }}</span>
            <span class="last-price">上次: ¥{{ item.lastPrice }}</span>
          </div>
          <div class="price-info">
            <span class="daily-ratio">日比: {{ (item.dailyRatio * 100).toFixed(2) }}%</span>
            <span class="change-amplitude" :class="getChangeClass(item)">幅度: {{ calculateChangeAmplitude(item) }}%</span>
          </div>
        </div>
        <div class="create-date">{{ item.createDate }}</div>
      </div>
    </div>

    <!-- 图表模态框 -->
    <a-modal
      v-model="chartModalVisible"
      :title="selectedCategory + ' - 价格趋势'"
      width="80%"
      :footer="null"
      @cancel="closeChartModal"
    >
      <div v-if="trendList && trendList.historyData && trendList.forecastData">
        <apexchart
          type="line"
          height="400"
          :options="chartOptions"
          :series="chartSeries"
        ></apexchart>
      </div>
      <div v-else-if="chartLoading" class="chart-loading">
        <a-spin size="large" />
      </div>
    </a-modal>
  </div>
</template>

<script>import VueApexCharts from 'vue-apexcharts'

export default {
  name: 'Trend',
  components: {
    apexchart: VueApexCharts
  },
  data () {
    return {
      categoryList: [],
      trendList: null,
      chartModalVisible: false,
      selectedCategory: '',
      chartLoading: false
    }
  },
  computed: {
    chartOptions () {
      return {
        chart: {
          height: 400,
          type: 'line',
          zoom: {
            enabled: true
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: 'smooth',
          width: 2
        },
        title: {
          text: '价格趋势',
          align: 'left'
        },
        markers: {
          size: 4,
          hover: {
            sizeOffset: 6
          }
        },
        xaxis: {
          title: {
            text: '日期'
          },
          type: 'datetime'
        },
        yaxis: {
          title: {
            text: '价格 (元)'
          }
        },
        legend: {
          position: 'top',
          horizontalAlign: 'right'
        },
        tooltip: {
          x: {
            format: 'yyyy-MM-dd'
          },
          y: {
            formatter: function (val) {
              return '¥' + val.toFixed(2)
            }
          }
        },
        grid: {
          borderColor: '#f1f1f1'
        }
      }
    },
    chartSeries() {
      if (!this.trendList || !this.trendList.historyData || !this.trendList.forecastData) {
        return []
      }

      try {
        // 处理历史数据
        const historyData = this.trendList.historyData.map(item => {
          if (!item.date || item.price === undefined) {
            return null
          }
          return {
            x: new Date(item.date).getTime(),
            y: parseFloat(item.price)
          }
        }).filter(Boolean) // 过滤掉无效数据

        // 处理预测数据
        const forecastData = this.trendList.forecastData.map(item => {
          if (!item.date || item.price === undefined) {
            return null
          }
          return {
            x: new Date(item.date).getTime(),
            y: parseFloat(item.price)
          }
        }).filter(Boolean) // 过滤掉无效数据

        return [
          {
            name: '历史价格',
            data: historyData,
            color: '#3b82f6'
          },
          {
            name: '预测价格',
            data: forecastData,
            color: '#ef4444'
          }
        ]
      } catch (error) {
        console.error('处理图表数据时出错:', error)
        return []
      }
    }
  },
  mounted () {
    this.queryAllCategoryPrice()
  },
  methods: {
    queryAllCategoryPrice () {
      this.$get('/cos/price-trends/queryAllCategoryPrice').then((r) => {
        this.categoryList = r.data.data
      })
    },
    queryTrendList (category) {
      this.chartLoading = true
      this.$get(`/cos/price-trends/queryCategoryTrend`, {category})
        .then((response) => {
          // 验证响应数据结构
          if (response && response.data && response.data.historyData && response.data.forecastData) {
            this.trendList = response.data
            this.selectedCategory = category
            this.chartModalVisible = true
          } else {
            console.error('API返回的数据结构不完整:', response)
            this.$message.error('获取数据失败，请稍后重试')
          }
        })
        .catch((error) => {
          console.error('查询趋势数据失败:', error)
          this.$message.error('获取数据失败，请稍后重试')
        })
        .finally(() => {
          this.chartLoading = false
        })
    },
    closeChartModal () {
      this.chartModalVisible = false
      this.trendList = null
      this.selectedCategory = ''
    },
    getChangeClass (item) {
      const change = (item.price - item.lastPrice) / item.lastPrice
      if (change > 0) return 'price-up'
      else if (change < 0) return 'price-down'
      else return 'price-stable'
    },
    getChangeText (item) {
      const change = item.price - item.lastPrice
      const changePercent = ((item.price - item.lastPrice) / item.lastPrice * 100).toFixed(2)
      if (change > 0) {
        return `↑ +${change.toFixed(2)} (+${changePercent}%)`
      } else if (change < 0) {
        return `↓ ${change.toFixed(2)} (${changePercent}%)`
      } else {
        return `→ ${change.toFixed(2)} (${changePercent}%)`
      }
    },
    calculateChangeAmplitude (item) {
      if (!item.lastPrice) return '0.00'
      const changePercent = ((item.price - item.lastPrice) / item.lastPrice * 100).toFixed(2)
      return changePercent
    },
    selectCategory (category) {
      this.queryTrendList(category)
    }
  }
}
</script>

<style scoped>.trend-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.category-card {
  background: white;
  border-radius: 3px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.category-name {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.price-change {
  font-size: 14px;
  font-weight: 500;
  padding: 4px 8px;
  border-radius: 4px;
}

.price-up {
  color: #f5222d;
  background-color: rgba(245, 34, 45, 0.1);
}

.price-down {
  color: #52c41a;
  background-color: rgba(82, 196, 26, 0.1);
}

.price-stable {
  color: #faad14;
  background-color: rgba(250, 173, 20, 0.1);
}

.category-body {
  margin-bottom: 15px;
}

.current-price {
  font-size: 24px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 10px;
}

.price-info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #888;
  margin-bottom: 5px;
}

.start-price,
.last-price,
.daily-ratio,
.change-amplitude {
  display: inline-block;
}

.create-date {
  font-size: 12px;
  color: #aaa;
  text-align: right;
}

.chart-loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}
</style>
