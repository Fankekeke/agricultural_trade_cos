<template>
  <div :class="[multipage === true ? 'multi-page':'single-page', 'not-menu-page', 'home-page']">
    <a-row :gutter="8" class="head-info">
      <a-card class="head-info-card">
        <a-col :span="12">
<!--          <div class="head-info-avatar">-->
<!--            <img alt="头像" :src="avatar">-->
<!--          </div>-->
          <div class="head-info-count">
            <div class="head-info-welcome">
              {{welcomeMessage}}
            </div>
            <div class="head-info-desc">
              <p>{{user.roleName ? user.roleName : '暂无角色'}}</p>
            </div>
            <div class="head-info-time">上次登录时间：{{user.lastLoginTime ? user.lastLoginTime : '第一次访问系统'}}</div>
          </div>
        </a-col>
        <a-col :span="24" v-if="user.roleId == 75" style="margin-top: 15px">
          <a-row :gutter="25">
<!--            <a-col :span="10">-->
<!--              <div class="scenic-carousel-wrapper">-->
<!--                <a-carousel autoplay>-->
<!--                  <div class="carousel-slide">-->
<!--                    <img src="http://127.0.0.1:9527/imagesWeb/SA1648175183138.jpg" alt="景区风光" class="carousel-image" />-->
<!--                    <div class="slide-overlay">-->
<!--                      <div class="slide-content">-->
<!--                        <h2 class="slide-title">欢迎来到美丽景区</h2>-->
<!--                        <p class="slide-description">探索自然与文化的完美融合，享受难忘的旅行体验</p>-->
<!--                        <a-button type="primary" class="explore-btn">立即探索</a-button>-->
<!--                      </div>-->
<!--                    </div>-->
<!--                  </div>-->
<!--                  <div class="carousel-slide">-->
<!--                    <img src="http://127.0.0.1:9527/imagesWeb/SA1648524716556.jpg" alt="特色景点" class="carousel-image" />-->
<!--                    <div class="slide-overlay">-->
<!--                      <div class="slide-content">-->
<!--                        <h2 class="slide-title">特色景点推荐</h2>-->
<!--                        <p class="slide-description">发现隐藏的自然宝藏，感受独特的风景魅力</p>-->
<!--                        <a-button type="primary" class="explore-btn">查看详情</a-button>-->
<!--                      </div>-->
<!--                    </div>-->
<!--                  </div>-->
<!--                  <div class="carousel-slide">-->
<!--                    <img src="http://127.0.0.1:9527/imagesWeb/SA1648524713924.jpg" alt="最佳季节" class="carousel-image" />-->
<!--                    <div class="slide-overlay">-->
<!--                      <div class="slide-content">-->
<!--                        <h2 class="slide-title">最佳旅游季节</h2>-->
<!--                        <p class="slide-description">四季皆有不同的美景，为您提供最佳游览建议</p>-->
<!--                        <a-button type="primary" class="explore-btn">查看攻略</a-button>-->
<!--                      </div>-->
<!--                    </div>-->
<!--                  </div>-->
<!--                </a-carousel>-->
<!--              </div>-->
<!--            </a-col>-->
            <a-col :span="24">
              <div>
                <work></work>
              </div>
            </a-col>
          </a-row>
        </a-col>
        <a-col :span="12">
          <div>
            <a-row class="more-info" v-if="user.roleId == 74">
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4">
                <head-info title="用户数量" :content="titleAdminData.merchantNum" :center="false" :bordered="false"/>
              </a-col>
              <a-col :span="4">
                <head-info title="技师数量" :content="titleAdminData.staffNum" :center="false" :bordered="false"/>
              </a-col>
              <a-col :span="4">
                <head-info title="总收益" :content="titleAdminData.totalPrice" :center="false" :bordered="false"/>
              </a-col>
              <a-col :span="4">
                <head-info title="总订单数" :content="titleAdminData.totalNum" :center="false"/>
              </a-col>
            </a-row>
            <a-row class="more-info" v-if="user.roleId == 76">
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4">
                <head-info title="总收益" :content="titleData.totalPrice" :center="false" :bordered="false"/>
              </a-col>
              <a-col :span="4">
                <head-info title="总订单数" :content="titleData.totalNum" :center="false"/>
              </a-col>
            </a-row>
          </div>
        </a-col>
      </a-card>
    </a-row>
    <home @setTitle="setTitleData" @setAdminTitle="setAdminTitle"></home>
    <a-row :gutter="8" class="count-info" style="margin-top: 15px" v-show="user.roleId == 74">
      <a-col :span="12" class="visit-count-wrapper">
        <a-card class="visit-count" hoverable>
          <apexchart ref="count" type=bar height=300 :options="chartOptions" :series="series" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import HeadInfo from '@/views/common/HeadInfo'
import {mapState} from 'vuex'
import moment from 'moment'
import Home from './manage/component/home/Home'
import Work from './manage/component/user/Work.vue'
import { getUrlKey } from '@/utils/urlKey'
// import Forum from './forum/Forum.vue'
import Post from './forum/Post.vue'
moment.locale('zh-cn')

export default {
  name: 'HomePage',
  components: {Work, Home, Post, HeadInfo},
  data () {
    return {
      titleData: {
        memberNum: 0,
        staffNum: 0,
        totalPrice: 0,
        totalNum: 0
      },
      titleAdminData: {
        merchantNum: 0,
        staffNum: 0,
        totalPrice: 0,
        totalNum: 0
      },
      series: [],
      chartOptions: {
        chart: {
          toolbar: {
            show: false
          }
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '35%'
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: []
        },
        fill: {
          opacity: 1

        }
      },
      todayIp: '',
      todayVisitCount: '',
      totalVisitCount: '',
      userRole: '',
      userDept: '',
      lastLoginTime: '',
      welcomeMessage: '',
      bulletinList: []
    }
  },
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    }),
    avatar () {
      return `static/avatar/${this.user.avatar}`
    }
  },
  methods: {
    cleanMessage (id) {
      this.$get(`/cos/notify-info/setReadStatus/${id}`).then((r) => {
        this.getNewList()
      })
    },
    welcome () {
      const date = new Date()
      const hour = date.getHours()
      let time = hour < 6 ? '早上好' : (hour <= 11 ? '上午好' : (hour <= 13 ? '中午好' : (hour <= 18 ? '下午好' : '晚上好')))
      return `${time}，${this.user.username}`
    },
    setTitleData (titleData) {
      this.titleData = titleData
    },
    setAdminTitle (titleData) {
      this.titleAdminData = titleData
    }
  },
  mounted () {
    if (getUrlKey('out_trade_no') !== null) {
      this.$get('/cos/order-info/orderPay', { orderCode: getUrlKey('out_trade_no') }).then((r) => {
        this.$message.success('支付成功！')
      })
    }
    this.welcomeMessage = this.welcome()
    this.$get(`index/${this.user.username}`).then((r) => {
      let data = r.data.data
      this.todayIp = data.todayIp
      this.todayVisitCount = data.todayVisitCount
      this.totalVisitCount = data.totalVisitCount
      let sevenVisitCount = []
      let dateArr = []
      for (let i = 6; i >= 0; i--) {
        let time = moment().subtract(i, 'days').format('MM-DD')
        let contain = false
        for (let o of data.lastSevenVisitCount) {
          if (o.days === time) {
            contain = true
            sevenVisitCount.push(o.count)
          }
        }
        if (!contain) {
          sevenVisitCount.push(0)
        }
        dateArr.push(time)
      }
      let sevenUserVistCount = []
      for (let i = 6; i >= 0; i--) {
        let time = moment().subtract(i, 'days').format('MM-DD')
        let contain = false
        for (let o of data.lastSevenUserVisitCount) {
          if (o.days === time) {
            contain = true
            sevenUserVistCount.push(o.count)
          }
        }
        if (!contain) {
          sevenUserVistCount.push(0)
        }
      }
      this.$refs.count.updateSeries([
        {
          name: '您',
          data: sevenUserVistCount
        },
        {
          name: '总数',
          data: sevenVisitCount
        }
      ], true)
      this.$refs.count.updateOptions({
        xaxis: {
          categories: dateArr
        },
        title: {
          text: '近七日系统访问记录',
          align: 'left'
        }
      }, true, true)
    }).catch((r) => {
      console.error(r)
      this.$message.error('获取首页信息失败')
    })
  }
}
</script>
<style lang="less">
  .home-page {
    .head-info {
      margin-bottom: .5rem;
      .head-info-card {
        padding: .5rem;
        border-color: #f1f1f1;
        .head-info-avatar {
          display: inline-block;
          float: left;
          margin-right: 1rem;
          img {
            width: 5rem;
            border-radius: 2px;
          }
        }
        .head-info-count {
          display: inline-block;
          float: left;
          .head-info-welcome {
            font-size: 1.05rem;
            margin-bottom: .1rem;
          }
          .head-info-desc {
            color: rgba(0, 0, 0, 0.45);
            font-size: .8rem;
            padding: .2rem 0;
            p {
              margin-bottom: 0;
            }
          }
          .head-info-time {
            color: rgba(0, 0, 0, 0.45);
            font-size: .8rem;
            padding: .2rem 0;
          }
        }
      }
    }
    .count-info {
      .visit-count-wrapper {
        padding-left: 0 !important;
        .visit-count {
          padding: .5rem;
          border-color: #f1f1f1;
          .ant-card-body {
            padding: .5rem 1rem !important;
          }
        }
      }
      .project-wrapper {
        padding-right: 0 !important;
        .project-card {
          border: none !important;
          .ant-card-head {
            border-left: 1px solid #f1f1f1 !important;
            border-top: 1px solid #f1f1f1 !important;
            border-right: 1px solid #f1f1f1 !important;
          }
          .ant-card-body {
            padding: 0 !important;
            table {
              width: 100%;
              td {
                width: 50%;
                border: 1px solid #f1f1f1;
                padding: .6rem;
                .project-avatar-wrapper {
                  display:inline-block;
                  float:left;
                  margin-right:.7rem;
                  .project-avatar {
                    color: #42b983;
                    background-color: #d6f8b8;
                  }
                }
              }
            }
          }
          .project-detail {
            display:inline-block;
            float:left;
            text-align:left;
            width: 78%;
            .project-name {
              font-size:.9rem;
              margin-top:-2px;
              font-weight:600;
            }
            .project-desc {
              color:rgba(0, 0, 0, 0.45);
              p {
                margin-bottom:0;
                font-size:.6rem;
                white-space:normal;
              }
            }
          }
        }
      }
    }
  }
</style>
<style lang="less" scoped>.scenic-carousel-wrapper {
  border-radius: 2px;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  position: relative;
  height: 360px;

  // 轮播图基础样式
  ::v-deep .ant-carousel .slick-slider {
    height: 100%;
  }

  ::v-deep .ant-carousel .slick-list {
    height: 100%;
  }

  ::v-deep .ant-carousel .slick-track {
    height: 100%;
  }

  ::v-deep .ant-carousel .slick-slide {
    height: 360px;
    position: relative;
  }
}

.carousel-slide {
  position: relative;
  height: 100%;

  .carousel-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .slide-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(to right, rgba(0, 0, 0, 0.7) 0%, rgba(0, 0, 0, 0.3) 50%, transparent 100%);

    .slide-content {
      position: absolute;
      top: 50%;
      left: 5%;
      transform: translateY(-50%);
      color: white;
      max-width: 60%;
      z-index: 2;

      .slide-title {
        color: white;
        font-size: 28px;
        font-weight: 700;
        margin-bottom: 12px;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
      }

      .slide-description {
        font-size: 16px;
        margin-bottom: 24px;
        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
        line-height: 1.5;
      }

      .explore-btn {
        border-radius: 24px;
        padding: 0 28px;
        height: 42px;
        font-weight: 500;
        background: rgba(255, 255, 255, 0.2);
        backdrop-filter: blur(10px);
        border: 1px solid rgba(255, 255, 255, 0.3);
        color: white;
        font-size: 15px;
        transition: all 0.3s ease;

        &:hover {
          background: rgba(255, 255, 255, 0.3);
          transform: translateY(-2px);
          box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
        }
      }
    }
  }
}
</style>
