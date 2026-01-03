<template>
  <div style="background:#ECECEC; padding:30px;width: 100%">
    <a-row :gutter="20">
      <!-- 替换空状态部分 -->
      <div style="text-align: center; padding: 60px 0;" v-if="orderList.length === 0">
        <a-icon type="inbox" theme="twoTone" style="font-size: 72px; margin-bottom: 16px;"/>
        <p style="font-size: 20px; color: #8c8c8c; margin-bottom: 8px;">暂无订单</p>
        <p style="font-size: 14px; color: #bfbfbf;">当前没有可接的订单，请稍后再试</p>
      </div>
      <a-col :span="6" v-for="(item, index) in orderList" :key="index" style="margin-bottom: 30px">
        <a-card hoverable style="width: 100%; border-radius: 5px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.1);" @click="orderMapOpen(item)">
<!--          <template slot="actions" class="ant-card-actions">-->
<!--            <a-icon key="pushpin" type="pushpin" @click="checkOrder(item.id)" style="font-size: 16px; color: #1890ff;"/>-->
<!--            <a-icon key="ellipsis" type="ellipsis"  style="font-size: 16px; color: #1890ff;"/>-->
<!--          </template>-->
          <a-carousel autoplay style="height: 180px; border-radius: 8px 8px 0 0;" v-if="item.images !== undefined && item.images !== ''">
            <div style="width: 100%; height: 180px; display: flex; align-items: center; justify-content: center;"
                 v-for="(img, index) in item.images.split(',')" :key="index">
              <img :src="'http://127.0.0.1:9527/imagesWeb/'+img"
                   style="width: 100%; height: 100%; object-fit: cover;">
            </div>
          </a-carousel>
          <a-card-meta :title="item.orderName" style="padding: 16px;">
            <div slot="description">
              <div style="display: flex; align-items: center; margin-bottom: 12px;">
                <a-avatar
                  shape="circle"
                  size="small"
                  :src="'http://127.0.0.1:9527/imagesWeb/' + item.userImages"          style="margin-right: 8px;"
                />
                <div>
                  <div style="font-weight: 500;">{{ item.userName }}</div>
                  <div style="font-size: 12px; color: #8c8c8c;">
                    <a-icon type="phone" style="font-size: 12px;"/>
                    {{ item.phone ? item.phone : ' - - ' }}
                  </div>
                </div>
              </div>

              <div style="margin-bottom: 8px; display: flex; flex-wrap: wrap; gap: 8px;">
                <a-tag color="blue">{{ item.goodsType }}</a-tag>
                <a-tag v-if="item.orderType == 1" color="orange">维修</a-tag>
                <a-tag v-if="item.orderType == 2" color="purple">回收</a-tag>
                <a-tag v-if="item.orderMethod == 1" color="cyan">上门</a-tag>
                <a-tag v-if="item.orderMethod == 2" color="volcano">邮寄</a-tag>
              </div>

              <div style="margin-bottom: 8px;">
                <a-icon type="clock-circle" style="color: #8c8c8c;"/>
                <span style="font-size: 12px; color: #8c8c8c; margin-left: 4px;">
                  {{ moment(item.createDate).format('YYYY-MM-DD HH:mm') }}
                </span>
              </div>

              <div v-if="item.content" style="margin: 12px 0;">
                <div :style="{
    fontSize: '13px',
    lineHeight: '1.6',
    color: '#595959',
    maxHeight: expandedItems[item.id] ? 'none' : '60px',
    overflow: 'hidden',
    position: 'relative'
  }">
                  {{ item.content }}
                </div>
<!--                <a v-if="item.content && item.content.length > 100"-->
<!--                   @click="toggleContent(item.id)"     style="font-size: 12px; color: #1890ff; margin-top: 4px; display: inline-block;">-->
<!--                  {{ expandedItems[item.id] ? '收起' : '展开' }}-->
<!--                </a>-->
              </div>

              <div style="margin-top: 12px; padding-top: 12px; border-top: 1px dashed #f0f0f0;">
                <div style="display: flex; justify-content: space-between; align-items: center;">
                  <div>
                    <div style="font-size: 12px; color: #8c8c8c;">期望价格</div>
                    <div style="color: #ff4d4f; font-weight: 500;font-size: 15px">¥{{ (item.forecastPrice).toFixed(2) }}</div>
                  </div>
                  <div v-if="item.orderType == 1">
                    <div style="font-size: 12px; color: #8c8c8c;">维修难度</div>
                    <a-tag :color="item.fixDifficulty == 1 ? 'green' : item.fixDifficulty == 2 ? 'orange' : 'red'">
                      <span v-if="item.fixDifficulty == 1">轻度</span>
                      <span v-if="item.fixDifficulty == 2">中度</span>
                      <span v-if="item.fixDifficulty == 3">复杂</span>
                    </a-tag>
                  </div>
                </div>
              </div>
            </div>
          </a-card-meta>
        </a-card>
      </a-col>
    </a-row>
    <MapView
      @close="handleorderMapViewClose"
      :orderShow="orderMapView.visiable"
      :orderData="orderMapView.data">
    </MapView>
    <MapRecycleView
      @close="handleorderRecycleMapViewClose"
      :orderShow="orderRecycleMapView.visiable"
      :orderData="orderRecycleMapView.data">
    </MapRecycleView>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import moment from 'moment'
import MapView from '../../manage/map/Map.vue'
import MapRecycleView from '../../manage/map/MapRecycle.vue'
import OrderView from './OrderView.vue'

moment.locale('zh-cn')

function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

const formItemLayout = {
  labelCol: {span: 24},
  wrapperCol: {span: 24}
}
export default {
  name: 'User',
  components: {MapView, OrderView, MapRecycleView},
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  data () {
    return {
      expandedItems: {},
      form: this.$form.createForm(this),
      formItemLayout,
      loading: false,
      courseInfo: [],
      dataLoading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      expertInfo: null,
      price: 0,
      orderList: [],
      orderRecycleMapView: {
        visiable: false,
        data: null
      },
      orderMapView: {
        visiable: false,
        data: null
      }
    }
  },
  mounted () {
    this.getExpertInfo(this.currentUser.userId)
  },
  methods: {
    moment,
    orderMapOpen (row) {
      if (row.orderType == 1) {
        this.orderMapView.data = row
        this.orderMapView.visiable = true
      } else {
        this.orderRecycleMapView.data = row
        this.orderRecycleMapView.visiable = true
      }
    },
    toggleContent(orderId) {
      this.$set(this.expandedItems, orderId, !this.expandedItems[orderId]);
    },
    checkOrder (orderId) {
      this.$get(`/cos/order-info/checkOrder`, {orderId, staffId: this.currentUser.userId}).then((r) => {
        this.$message.success('接单成功！请在订单中心查看')
        this.getExpertInfo(this.currentUser.userId)
      })
    },
    isDuringDate (beginDateStr, endDateStr, curDataStr) {
      let curDate = new Date(curDataStr)
      let beginDate = new Date(beginDateStr)
      let endDate = new Date(endDateStr)
      if (curDate >= beginDate && curDate <= endDate) {
        return true
      }
      return false
    },
    handleorderMapViewClose () {
      this.orderMapView.visiable = false
    },
    handleorderRecycleMapViewClose () {
      this.orderRecycleMapView.visiable = false
    },
    getListData (value) {
      let listData = []
      this.courseInfo.forEach(item => {
        if ((moment(value).format('YYYY-MM-DD')) === (moment(item.createDate).format('YYYY-MM-DD'))) {
          listData.push({type: 'success', content: item.code})
        }
      })
      return listData || []
    },
    getExpertInfo (userId) {
      this.$get(`/cos/order-info/page`, {staffFlag: 0}).then((r) => {
        this.orderList = r.data.data.records
      })
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({fileList}) {
      this.fileList = fileList
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    setFormValues ({...expert}) {
      this.rowId = expert.id
      let fields = ['code', 'name', 'createDate', 'sex']
      let obj = {}
      Object.keys(expert).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(expert['images'])
        }
        if (key === 'sex') {
          expert[key] = expert[key].toString()
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = expert[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$put('/cos/staff-info', {
            ...values
          }).then((r) => {
            this.$message.success('更新成功')
            this.loading = false
            this.getExpertInfo(this.currentUser.userId)
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
>>> .ant-card-meta-title {
  font-size: 13px;
  font-family: SimHei;
}

>>> .ant-card-meta-description {
  font-size: 12px;
  font-family: SimHei;
}

>>> .ant-divider-with-text-left {
  margin: 0;
}

>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}

>>> .ant-card-extra {
  font-size: 13px;
  font-family: SimHei;
}

.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 150px;
  line-height: 150px;
  overflow: hidden;
}

>>> .ant-card {
  transition: all 0.3s ease;
}

>>> .ant-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

>>> .ant-card-actions {
  display: flex;
  justify-content: center;
  align-items: center;
}

>>> .ant-card-actions > li {
  margin: 0 12px;
}

>>> .ant-card-meta-title {
  font-size: 16px;
  font-weight: 600;
  color: #262626;
}

.ant-carousel >>> .slick-dots li button {
  background: rgba(255, 255, 255, 0.5);
}

.ant-carousel >>> .slick-dots li.slick-active button {
  background: #1890ff;
}

>>> .ant-tag {
  margin: 0;
  font-size: 10px;
}
</style>
