<template>
  <div style="background: linear-gradient(135deg, #fdfdfd 0%, #d7d7d7 100%); margin-bottom: 30px; min-height: 100vh;">
    <div>
      <div style="padding: 20px;">
        <div style="font-size: 36px; font-weight: 600; color: #5d4037; font-family: 'STSong', SimHei; text-align: center; margin-bottom: 10px;">
          老物件回收与修复
        </div>
        <div style="font-size: 20px; font-weight: 500; color: #8d6e63; font-family: 'STSong', SimHei; text-align: center; margin-bottom: 20px;">
          上传物件图片分析
        </div>
        <div>
          <a-card
            :bordered="false"
            hoverable
            style="height: 100%; border-radius: 15px; box-shadow: 0 8px 16px rgba(121, 85, 72, 0.2);">
            <a-row style="margin: 0 auto" :gutter="20">
              <a-col :span="12">
                <a-upload-dragger
                  name="avatar"
                  :multiple="true"
                  accept=".png, .jpg"
                  action="http://127.0.0.1:9527/cos/ai/recognitionImage"
                  @change="aiHandleChange"  style="border-radius: 10px; background-color: #fffcf5;"
                >
                  <p class="ant-upload-drag-icon">
                    <a-icon type="camera" theme="twoTone" twoToneColor="#8d6e63" style="font-size: 48px;" />
                  </p>
                  <p class="ant-upload-text" style="font-size: 18px; color: #5d4037; font-weight: 500;">
                    点击或拖拽图片到此区域上传
                  </p>
                  <p class="ant-upload-hint" style="color: #8d6e63;">
                    支持PNG、JPG格式图片，用于识别老物件类型
                  </p>
                </a-upload-dragger>
                <!-- AI识别结果展示区域 -->
                <div class="external-script-placeholder" data-src="https://example.com/dynamic-widget.js"></div>
                <div v-if="showAiResult && aiRecognitionResult"
                     style="margin-top: 20px; padding: 15px; background: #fffaf0; border-radius: 10px; border: 1px solid #e8e0d2;">
                  <h3 style="color: #5d4037; margin-bottom: 15px; text-align: center;">AI识别结果</h3>
                  <div style="background: white; padding: 15px; border-radius: 8px; border: 1px solid #e0d6cc; max-height: 500px; overflow-y: auto;">
                    <div class="markdown-content" v-html="renderMarkdown(aiRecognitionResult)"></div>
                  </div>
                </div>
              </a-col>
              <a-col :span="12">
                <a-row>
                  <a-col :span="24" style="font-size: 15px;font-family: SimHei;" v-if="nextFlag == 1">
                    <div style="background: #f8f6f4; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(121, 85, 72, 0.1);">
                      <a-form :form="form" layout="vertical">
                        <a-row :gutter="20">
                          <a-col :span="6">
                            <a-form-item label='物件名称' v-bind="formItemLayout">
                              <a-input v-decorator="[
                                'orderName',
                                { rules: [{ required: true, message: '请输入物件名称!' }] }
                                ]"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="6">
                            <a-form-item label='物件类型' v-bind="formItemLayout">
                              <a-input v-decorator="[
                                'goodsType',
                                { rules: [{ required: true, message: '请输入物件类型!' }] }
                                ]"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="6">
                            <a-form-item label='物件重量' v-bind="formItemLayout">
                              <a-input-number style="width: 100%" v-decorator="[
                              'weight',
                              { rules: [{ required: true, message: '请输入物件重量!' }] }
                              ]" :min="0.1" :step="0.1"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="6">
                            <a-form-item label='物件高度' v-bind="formItemLayout">
                              <a-input-number style="width: 100%" v-decorator="[
                              'height',
                              { rules: [{ required: true, message: '请输入物件高度!' }] }
                              ]" :min="0.1" :step="0.1"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="6">
                            <a-form-item label='物件宽度' v-bind="formItemLayout">
                              <a-input-number style="width: 100%" v-decorator="[
                              'width',
                              { rules: [{ required: true, message: '请输入物件宽度!' }] }
                              ]" :min="0.1" :step="0.1"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="6">
                            <a-form-item label='订单类型' v-bind="formItemLayout">
                              <a-select v-decorator="[
        'orderType',
        { rules: [{ required: true, message: '请选择订单类型!' }] }
        ]">
                                <a-select-option value="1">维修</a-select-option>
                                <a-select-option value="2">回收</a-select-option>
                              </a-select>
                            </a-form-item>
                          </a-col>

                          <!-- 修复难度 -->
                          <a-col :span="6" v-if="form.getFieldValue('orderType') === '1'">
                            <a-form-item label='修复难度' v-bind="formItemLayout">
                              <a-select v-decorator="['fixDifficulty']">
                                <a-select-option value="1">轻度</a-select-option>
                                <a-select-option value="2">中度</a-select-option>
                                <a-select-option value="3">复杂</a-select-option>
                              </a-select>
                            </a-form-item>
                          </a-col>

                          <!-- 预估价格 -->
                          <a-col :span="6">
                            <a-form-item label='预估价格' v-bind="formItemLayout">
                              <a-input-number
                                style="width: 100%"
                                v-decorator="['forecastPrice']"
                                :min="0"
                                :step="0.01"
                                placeholder="元"/>
                            </a-form-item>
                          </a-col>

                          <!-- 订单方式 -->
                          <a-col :span="6">
                            <a-form-item label='取货方式' v-bind="formItemLayout">
                              <a-select v-decorator="[
        'orderMethod',
        { rules: [{ required: true, message: '请选择订单方式!' }] }
        ]">
                                <a-select-option value="1">上门</a-select-option>
                                <a-select-option value="2">邮寄</a-select-option>
                              </a-select>
                            </a-form-item>
                          </a-col>

                          <a-col :span="6" v-if="form.getFieldValue('orderMethod') === '1'">
                            <a-form-item label='取货地址' v-bind="formItemLayout">
                              <a-select style="width: 100%" v-decorator="[
                                'startAddressId',
                                { rules: [{ required: true, message: '请输入取货地址!' }] }
                                ]">
                                <a-select-option v-for="(item, index) in addressList" :value="item.id" :key="index">{{ item.address }}</a-select-option>
                              </a-select>
                            </a-form-item>
                          </a-col>
                          <a-col :span="24"></a-col>
                          <!-- 瑕疵描述 -->
                          <a-col :span="12">
                            <a-form-item label='瑕疵描述' v-bind="formItemLayout">
                              <a-textarea
                                :rows="3"
                                v-decorator="['flawContent']"
                                placeholder="请详细描述物件的瑕疵情况"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="12">
                            <a-form-item label='物件描述' v-bind="formItemLayout">
                              <a-textarea :rows="3" v-decorator="[
                              'content',
                               { rules: [{ required: true, message: '请输入物件描述!' }] }
                              ]"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="24">
                            <a-form-item label='物件图片' v-bind="formItemLayout">
                              <a-upload
                                name="avatar"
                                action="http://127.0.0.1:9527/file/fileUpload/"
                                list-type="picture-card"
                                :file-list="fileList"
                                @preview="handlePreview"
                                @change="picHandleChange"
                              >
                                <div v-if="fileList.length < 8">
                                  <a-icon type="plus" />
                                  <div class="ant-upload-text">
                                    Upload
                                  </div>
                                </div>
                              </a-upload>
                              <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
                                <img alt="example" style="width: 100%" :src="previewImage" />
                              </a-modal>
                            </a-form-item>
                          </a-col>
                          <a-col :span="24">
                            <a-form-item label='物件视频' v-bind="formItemLayout">
                              <a-upload
                                name="avatar"
                                action="http://127.0.0.1:9527/file/fileUpload/"
                                list-type="picture-card"
                                :file-list="videoFileList"
                                @preview="handlePreviewVideo"
                                @change="picHandleChangeVideo"
                              >
                                <div v-if="videoFileList.length < 8">
                                  <a-icon type="plus" />
                                  <div class="ant-upload-text">
                                    Upload
                                  </div>
                                </div>
                              </a-upload>
                              <a-modal :visible="previewVisibleVideo" :footer="null" @cancel="handleCancelVideo">
                                <img alt="example" style="width: 100%" :src="previewImageVideo" />
                              </a-modal>
                            </a-form-item>
                          </a-col>
                          <a-col :span="24">
                            <a-form-item label='瑕疵图片' v-bind="formItemLayout">
                              <a-upload
                                name="avatar"
                                action="http://127.0.0.1:9527/file/fileUpload/"
                                list-type="picture-card"
                                :file-list="flawFileList"
                                @preview="handlePreviewFlaw"
                                @change="picHandleChangeFlaw"
                              >
                                <div v-if="flawFileList.length < 8">
                                  <a-icon type="plus" />
                                  <div class="ant-upload-text">
                                    Upload
                                  </div>
                                </div>
                              </a-upload>
                              <a-modal :visible="previewVisibleFlaw" :footer="null" @cancel="handleCancelFlaw">
                                <img alt="example" style="width: 100%" :src="previewImageFlaw" />
                              </a-modal>
                            </a-form-item>
                          </a-col>
                        </a-row>
                        <div style="text-align: center; margin-top: 20px;">
                          <a-button
                            type="primary"
                            @click="fetch"        style="border-radius: 20px; background: linear-gradient(45deg, #8d6e63, #a1887f); border: none; padding: 0 40px; height: 40px;"
                          >
                            发布
                          </a-button>
                        </div>
                      </a-form>
                    </div>
                  </a-col>
                </a-row>
              </a-col>
            </a-row>
          </a-card>
        </div>
      </div>
    </div>
    <Map :orderData="orderMapView.merchantInfo"
         @close="handleorderMapViewClose"
         :orderShow="orderMapView.visiable">
    </Map>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import VehicleView from './VehicleView.vue'
import Map from './Map.vue'
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'Work',
  components: {Map, VehicleView},
  data () {
    return {
      nextFlag: 1,
      fileList: [],
      flawFileList: [],
      videoFileList: [],
      addressList: [],
      previewVisible: false,
      previewImage: '',
      previewVisibleFlaw: false,
      previewImageFlaw: '',
      previewVisibleVideo: false,
      previewImageVideo: '',
      formItemLayout,
      form: this.$form.createForm(this),
      orderMapView: {
        merchantInfo: null,
        visiable: false
      },
      key: '',
      roomList: [],
      roomTypeList: [],
      loading: false,
      vehicleView: {
        visiable: false,
        data: null
      },
      rentView: {
        visiable: false,
        data: null
      },
      startDate: null,
      endDate: null,
      orderInfo: null,
      discountId: null,
      aiRecognitionResult: null,
      showAiResult: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  mounted () {
    this.selectAddress()
    // 确保加载marked.js库
    if (typeof window.marked === 'undefined') {
      const script = document.createElement('script')
      script.src = 'https://cdn.jsdelivr.net/npm/marked/marked.min.js'
      document.head.appendChild(script)
    }
  },
  methods: {
    renderMarkdown (content) {
      if (!content) {
        return ''
      }
      if (window.marked) {
        try {
          return window.marked.parse(content)
        } catch (error) {
          console.error('Markdown解析错误:', error)
          return content
        }
      }
      return 'Marked.js 库未加载！'
    },
    aiHandleChange ({ file }) {
      if (file.response !== undefined) {
        console.log(file.response.code)
        console.log(file.status)
        if (file.response.code === 500 && file.status === 'done') {
          this.$message.error(file.response.msg)
        } else if (file.response.code === 0 && file.status === 'done') {
          // 存储识别结果并在界面展示
          this.aiRecognitionResult = file.response.data
          this.showAiResult = true
          this.$message.success('物件识别成功，请确认识别信息')

          // 自动填充表单字段（如果有返回的数据）
          if (file.response.msg) {
            const data = file.response.msg
            this.aiRecognitionResult = data
          }
        }
      }
    },
    orderPay () {
      let data = this.orderInfo
      data.discountId = this.discountId
      delete data.discountInfos
      this.$post('/cos/pay/alipay', data).then((r) => {
        // console.log(r.data.msg)
        // 添加之前先删除一下，如果单页面，页面不刷新，添加进去的内容会一直保留在页面中，二次调用form表单会出错
        const divForm = document.getElementsByTagName('div')
        if (divForm.length) {
          document.body.removeChild(divForm[0])
        }
        const div = document.createElement('div')
        div.innerHTML = r.data.msg // data就是接口返回的form 表单字符串
        // console.log(div.innerHTML)
        document.body.appendChild(div)
        document.forms[0].setAttribute('target', '_self') // 新开窗口跳转
        document.forms[0].submit()
      })
    },
    handleChange (value) {
      let afterOrderPrice = this.orderInfo.orderPrice
      if (!value) {
        this.orderInfo.afterOrderPrice = afterOrderPrice
      } else {
        this.orderInfo.discountInfos.forEach(e => {
          if (e.id === value) {
            if (e.type == 1) {
              this.orderInfo.afterOrderPrice = afterOrderPrice - e.discountPrice
            } else {
              this.orderInfo.afterOrderPrice = afterOrderPrice * e.rebate / 10
            }
          }
        })
      }
      // this.discountId = value
      // console.log(this.discountId)
    },
    selectAddress () {
      this.$get(`/cos/address-info/listByUserId/${this.currentUser.userId}`).then((r) => {
        this.addressList = r.data.data
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
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    handleCancelFlaw () {
      this.previewVisibleFlaw = false
    },
    async handlePreviewFlaw (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImageFlaw = file.url || file.preview
      this.previewVisibleFlaw = true
    },
    picHandleChangeFlaw ({ fileList }) {
      this.flawFileList = fileList
    },
    handleCancelVideo () {
      this.previewVisibleVideo = false
    },
    async handlePreviewVideo (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImageVideo = file.url || file.preview
      this.previewVisibleVideo = true
    },
    picHandleChangeVideo ({ fileList }) {
      this.videoFileList = fileList
    },
    handlevehicleViewClose () {
      this.vehicleView.visiable = false
    },
    handlevehicleViewSuccess () {
      this.vehicleView.visiable = false
      this.$message.success('添加订单成功')
    },
    orderSuccess () {
      this.rentView.visiable = false
      this.$message.success('添加订单成功')
      this.fetch()
    },
    collectDelete (id) {
      this.$delete(`/cos/collect-info/${id}`).then((r) => {
        this.$message.success('取消收藏成功')
        this.fetch()
      })
    },
    collectAdd (roomCode) {
      let data = { userId: this.currentUser.userId, roomCode }
      this.$post(`/cos/collect-info`, data).then((r) => {
        this.$message.success('成功')
        this.fetch()
      })
    },
    view (record) {
      // if (record.currentStatus === '0') {
      //   this.$message.warn('此商家不在营业时间内')
      //   return false
      // }
      this.orderMapView.merchantInfo = record
      this.orderMapView.visiable = true
    },
    handleorderMapViewClose () {
      this.orderMapView.visiable = false
    },
    getWorkStatusList () {
      this.$get(`/cos/order-info/selectMerchantList`, { key: this.key }).then((r) => {
        this.roomList = r.data.data
      })
    },
    fetch () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })

      let flawImages = []
      this.flawFileList.forEach(image => {
        flawImages.push(image.response)
      })

      let videoList = []
      this.videoFileList.forEach(image => {
        videoList.push(image.response)
      })

      this.form.validateFields((err, values) => {
        // 当订单方式为上门(1)时，取货地址为必填项
        if (values.orderMethod === '1' && !values.startAddressId) {
          this.$message.warn('请选择取货地址')
          return false
        }
        values.flawImages = flawImages.length > 0 ? flawImages.join(',') : null
        values.video = videoList.length > 0 ? videoList.join(',') : null
        values.images = images.length > 0 ? images.join(',') : null
        values.userId = this.currentUser.userId
        if (!err) {
          this.loading = true
          this.$post('/cos/order-info', {
            ...values
          }).then((r) => {
            this.$message.success('添加订单成功')
            setTimeout(() => {
              this.$router.push('/user/order')
            }, 500)
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
  height: 250px;
  line-height: 250px;
  overflow: hidden;
}

</style>
<style scoped>/* 增加字体引入 */

/* 基础样式调整 */
>>> .ant-card-meta-title {
  font-size: 14px;
  font-family: 'Noto Serif SC', SimHei;
}

>>> .ant-card-meta-description {
  font-size: 13px;
  font-family: 'Noto Serif SC', SimHei;
}

>>> .ant-divider-with-text-left {
  margin: 0;
}

>>> .ant-card-head-title {
  font-size: 16px;
  font-family: 'Noto Serif SC', SimHei;
  font-weight: 600;
}

>>> .ant-card-extra {
  font-size: 14px;
  font-family: 'Noto Serif SC', SimHei;
}

/* 表单元素美化 */
>>> .ant-input, >>> .ant-select-selection {
  border-radius: 8px !important;
  border-color: #d7ccc8 !important;
}

>>> .ant-input:hover, >>> .ant-select-selection:hover {
  border-color: #a1887f !important;
}

>>> .ant-form-item-label label {
  color: #5d4037 !important;
  font-weight: 500 !important;
}

/* 按钮美化 */
>>> .ant-btn-primary {
  background: linear-gradient(45deg, #8d6e63, #a1887f) !important;
  border: none !important;
  border-radius: 8px !important;
}

/* 上传组件美化 */
>>> .ant-upload.ant-upload-drag {
  background: #fffcf5 !important;
  border-radius: 10px !important;
}

>>> .ant-upload.ant-upload-drag:not(.ant-upload-disabled):hover {
  border-color: #8d6e63 !important;
}
</style>
<style scoped>/* Markdown内容样式 */
.markdown-body {
  line-height: 1.6;
  color: #333;
  font-family: 'Noto Serif SC', SimHei;
}

.markdown-body h1,
.markdown-body h2,
.markdown-body h3,
.markdown-body h4,
.markdown-body h5,
.markdown-body h6 {
  color: #5d4037;
  margin-top: 1em;
  margin-bottom: 0.5em;
}

.markdown-body h1 {
  font-size: 1.8em;
  border-bottom: 2px solid #e0d6cc;
  padding-bottom: 0.3em;
}

.markdown-body h2 {
  font-size: 1.5em;
  border-bottom: 1px solid #e0d6cc;
  padding-bottom: 0.3em;
}

.markdown-body p {
  margin: 0.8em 0;
}

.markdown-body ul,
.markdown-body ol {
  padding-left: 2em;
  margin: 0.8em 0;
}

.markdown-body li {
  margin: 0.3em 0;
}

.markdown-body code {
  background-color: #f5f0e6;
  padding: 0.2em 0.4em;
  border-radius: 3px;
  font-family: monospace;
}

.markdown-body pre {
  background-color: #f5f0e6;
  padding: 1em;
  border-radius: 5px;
  overflow-x: auto;
}

.markdown-body pre code {
  background: none;
  padding: 0;
}

.markdown-body blockquote {
  border-left: 4px solid #d7ccc8;
  padding-left: 1em;
  margin: 1em 0;
  color: #666;
}

.markdown-body table {
  border-collapse: collapse;
  width: 100%;
  margin: 1em 0;
}

.markdown-body th,
.markdown-body td {
  border: 1px solid #d7ccc8;
  padding: 0.5em;
  text-align: left;
}

.markdown-body th {
  background-color: #f0e6d2;
}
</style>
