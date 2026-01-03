<template>
  <a-drawer
    placement="right"
    width="100%"
    :closable="false"
    :visible="orderShow"
    destroyOnClose
    wrapClassName="aa"
    :getContainer="false"
  >
    <div style="width: 100%">
      <a-icon type="arrow-left" style="position: absolute;z-index: 999;color: red;font-size: 20px;margin: 15px" @click="home"/>
      <a-row style="height:100vh;font-family: SimHei">
        <a-col :span="9" style="height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff">
          <div>
            <div class="scenicInfo" style="height: 100vh; overflow-y: auto;overflow-x: hidden">
              <div v-if="orderInfo != null">
                <a-carousel autoplay style="height: 300px;" v-if="orderInfo.images !== null && orderInfo.images !== ''">
                  <div style="width: 100%;height: 300px" v-for="(item, index) in orderInfo.images.split(',')" :key="index">
                    <img :src="'http://127.0.0.1:9527/imagesWeb/' + item" style="width: 100%;height: 100%">
                  </div>
                </a-carousel>
                <a-card :title="orderInfo.orderName" :bordered="false">
                  <div style="text-align: center;margin-top: 20px;font-family: SimHei;">
                    <a-icon type="smile" theme="twoTone" style="font-size: 80px"/>
                    <p v-if="orderInfo.status == 0" style="font-size: 20px;margin-top: 15px;margin-bottom: 15px">等待报价中！</p>
                    <p v-if="orderInfo.status == 1" style="font-size: 20px;margin-top: 15px;margin-bottom: 15px">已确认，等待订单支付！</p>
                    <p v-if="orderInfo.status == 2" style="font-size: 20px;margin-top: 15px;margin-bottom: 15px">维修回收中！</p>
                    <p v-if="orderInfo.status == 3" style="font-size: 20px;margin-top: 15px;margin-bottom: 15px">订单已完成！</p>
                  </div>
                  <div style="padding-left: 24px;padding-right: 24px;margin-bottom: 50px;margin-top: 50px" v-if="orderInfo != null">
                    <a-steps :current="orderInfo.status" progress-dot size="small">
                      <a-step title="等待报价" />
                      <a-step title="未支付" />
                      <a-step title="维修回收中" />
                      <a-step title="已完成" />
                    </a-steps>
                  </div>
                  <div v-if="orderData.status == 2 || orderData.status == 3">
                    <h3 style="font-size: 18px; font-weight: 650; color: #000c17; margin-bottom: 20px; border-left: 4px solid #1890ff; padding-left: 10px;">地址</h3>
                    <div id="areas" style="width: 100%;height: 350px;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);background:#ec9e3c;color:#fff"></div>
                  </div>
                </a-card>
              </div>
              <div style="font-size: 12px;font-family: SimHei;color: #404040;margin-top: 15px">
                <div style="font-size: 12px;font-family: SimHei" v-if="userInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">用户信息</span></a-col>
                    <a-col :span="8"><b>用户编号：</b>
                      {{ userInfo.code }}
                    </a-col>
                    <a-col :span="8"><b>用户姓名：</b>
                      {{ userInfo.name ? userInfo.name : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>邮箱地址：</b>
                      {{ userInfo.mail ? userInfo.mail : '- -' }}
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="8"><b>联系电话：</b>
                      {{ userInfo.phone }}
                    </a-col>
                  </a-row>
                </div>
                <br/>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="orderInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单信息</span></a-col>
                    <a-col :span="8"><b>订单编号：</b>
                      {{ orderInfo.code }}
                    </a-col>
                    <a-col :span="8"><b>订单名称：</b>
                      {{ orderInfo.orderName ? orderInfo.orderName : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>预估价格：</b>
                      {{ orderInfo.forecastPrice ? orderInfo.forecastPrice + '元' : '- -' }}
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="8"><b>总价格：</b>
                      {{ orderInfo.orderPrice ? orderInfo.orderPrice + '元' : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>创建时间：</b>
                      {{ orderInfo.createDate }}
                    </a-col>
                    <a-col :span="8"><b>付款时间：</b>
                      {{ orderInfo.payDate }}
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="8"><b>折扣后价格：</b>
                      {{ orderInfo.afterOrderPrice ? orderInfo.afterOrderPrice : '- -' }}元
                    </a-col>
                    <a-col :span="8" v-if="orderData.orderType == 1"><b>维修难度：</b>
                      <span v-if="orderData.fixDifficulty == 1">轻度</span>
                      <span v-if="orderData.fixDifficulty == 2">中度</span>
                      <span v-if="orderData.fixDifficulty == 3">复杂</span>
                    </a-col>
                    <a-col :span="8"><b>订单类型：</b>
                      <span v-if="orderData.orderType == 1">维修</span>
                      <span v-if="orderData.orderType == 2">回收</span>
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="8"><b>订单方式：</b>
                      <span v-if="orderData.orderMethod == 1">上门</span>
                      <span v-if="orderData.orderMethod == 2">邮寄</span>
                    </a-col>
                    <a-col :span="8"><b>物件类型：</b>
                      {{ orderInfo.goodsType ? orderInfo.goodsType : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>物件重量：</b>
                      {{ orderInfo.weight ? orderInfo.weight : '- -' }}KG
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="8"><b>物件高度：</b>
                      {{ orderInfo.height ? orderInfo.height : '- -' }}厘米
                    </a-col>
                    <a-col :span="8"><b>物件宽度：</b>
                      {{ orderInfo.width ? orderInfo.width : '- -' }}厘米
                    </a-col>
                  </a-row>
                </div>
                <br/>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">物件描述</span></a-col>
                  <a-col :span="24">
                    {{ orderData.content ? orderData.content : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">物件瑕疵</span></a-col>
                  <a-col :span="24">
                    {{ orderData.flawContent ? orderData.flawContent : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">物件图册</span></a-col>
                  <a-col :span="24">
                    <a-upload
                      name="avatar"
                      action="http://127.0.0.1:9527/file/fileUpload/"
                      list-type="picture-card"
                      :file-list="fileList"
                      @preview="handlePreview"
                      @change="picHandleChange"
                    >
                    </a-upload>
                    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
                      <img alt="example" style="width: 100%" :src="previewImage" />
                    </a-modal>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">瑕疵图册</span></a-col>
                  <a-col :span="24">
                    <a-upload
                      name="avatar"
                      action="http://127.0.0.1:9527/file/fileUpload/"
                      list-type="picture-card"
                      :file-list="flawFileList"
                      @preview="handlePreviewFlaw"
                      @change="picHandleChangeFlaw"
                    >
                    </a-upload>
                    <a-modal :visible="previewVisibleFlaw" :footer="null" @cancel="handleCancelFlaw">
                      <img alt="example" style="width: 100%" :src="previewImageFlaw" />
                    </a-modal>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="orderData && orderData.video != null">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">视频</span></a-col>
                  <a-col :span="24">
                    <video width="100%" height="100%" controls>
                      <source :src="'http://127.0.0.1:9527/imagesWeb/' + orderData.video" type="video/mp4">
                    </video>
                  </a-col>
                </a-row>
                <br/>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="startAddressInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">物件地址</span></a-col>
                    <a-col :span="24"><b>详细地址：</b>
                      {{ startAddressInfo.address }}
                    </a-col>
                    <br/>
                    <br/>
                    <a-col :span="8"><b>联系人：</b>
                      {{ startAddressInfo.contactPerson ? startAddressInfo.contactPerson : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>联系方式：</b>
                      {{ startAddressInfo.contactMethod ? startAddressInfo.contactMethod : '- -' }}
                    </a-col>
                  </a-row>
                  <br/>
                </div>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="endAddressInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">送寄地址</span></a-col>
                    <a-col :span="24"><b>详细地址：</b>
                      {{ endAddressInfo.address }}
                    </a-col>
                    <br/>
                    <br/>
                    <a-col :span="8"><b>联系人：</b>
                      {{ endAddressInfo.contactPerson ? endAddressInfo.contactPerson : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>联系方式：</b>
                      {{ endAddressInfo.contactMethod ? endAddressInfo.contactMethod : '- -' }}
                    </a-col>
                  </a-row>
                  <br/>
                </div>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="discountInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">优惠信息</span></a-col>
                    <a-col :span="8"><b>优惠券编号：</b>
                      {{ discountInfo.code }}
                    </a-col>
                    <a-col :span="8"><b>优惠券名称：</b>
                      {{ discountInfo.couponName ? discountInfo.couponName : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>优惠券类型：</b>
                      <span v-if="discountInfo.type == 1">满减</span>
                      <span v-if="discountInfo.type == 2">折扣</span>
                    </a-col>
                    <a-col :span="8"><b>发放时间：</b>
                      {{ discountInfo.createDate }}
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;" v-if="discountInfo.type == 1">
                    <a-col :span="8"><b>满减金额：</b>
                      {{ discountInfo.discountPrice }} 元
                    </a-col>
                    <a-col :span="8"><b>门槛金额：</b>
                      {{ discountInfo.threshold }} 元
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;" v-if="discountInfo.type == 2">
                    <a-col :span="8"><b>折扣：</b>
                      {{ discountInfo.rebate }} 折
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="24"><b>备注：</b>
                      {{ discountInfo.content }}
                    </a-col>
                  </a-row>
                </div>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="staffInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">技师信息</span></a-col>
                    <a-col :span="8"><b>技师姓名：</b>
                      {{ staffInfo.name }}
                    </a-col>
                    <a-col :span="8"><b>性别：</b>
                      <span v-if="staffInfo.sex == '1'">男</span>
                      <span v-if="staffInfo.sex == '2'">女</span>
                    </a-col>
                    <a-col :span="8"><b>技师工号：</b>
                      {{ staffInfo.code }}
                    </a-col>
                  </a-row>
                  <br/>
                </div>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="evaluateInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单评价</span></a-col>
                    <a-col :span="8"><b>评价分数：</b>
                      <a-rate :default-value="evaluateInfo.score" disabled />
                    </a-col>
                    <a-col :span="8"><b>评价内容：</b>
                      <a-tooltip>
                        <template slot="title">
                          {{ evaluateInfo.content}}
                        </template>
                        {{ evaluateInfo.content.slice(0, 8) }} ...
                      </a-tooltip>
                    </a-col>
                    <a-col :span="8"><b>评价时间：</b>
                      {{ evaluateInfo.createDate ? evaluateInfo.createDate : '- -' }}
                    </a-col>
                  </a-row>
                  <br/>
                </div>
              </div>
            </div>
          </div>
        </a-col>
        <a-col :span="15" style="height: 100%;background: #f8f8f8;height: 100vh; overflow-y: auto;overflow-x: hidden">
          <a-row :gutter="15" style="padding: 20px" v-if="orderData != null">
            <a-col :span="24" style="margin-top: 15px;background: #fff;padding: 20px">
              <div style="display: flex; justify-content: flex-end; margin-bottom: 10px;">
                <a-button type="primary" icon="message" @click="openChat">
                  在线沟通
                </a-button>
              </div>
            </a-col>
            <a-col :span="24" style="margin-top: 15px;background: #fff;padding: 20px">
              <div style="font-size: 13px;font-family: SimHei">
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px">
                    <h3 style="font-size: 18px; font-weight: 650; color: #000c17; margin-bottom: 20px; border-left: 4px solid #1890ff; padding-left: 10px;">
                      报价信息
                    </h3>
                  </a-col>
                  <a-col :span="24">
                    <a-form :form="quoteForm" layout="vertical">
                      <a-row :gutter="16">
                        <a-col :span="8" v-if="orderData.orderMethod == 2">
                          <a-form-item label="选择地址">
                            <a-select
                              :disabled="orderData && orderData.status > 0"
                              v-decorator="['addressId', { rules: [{ required: true, message: '请选择地址' }] }]"
                              placeholder="请选择地址"
                            >
                              <a-select-option
                                v-for="address in addressList"
                                :key="address.id"
                                :value="address.id"
                              >
                                {{ address.address }}
                              </a-select-option>
                            </a-select>
                          </a-form-item>
                        </a-col>
                        <a-col :span="8">
                          <a-form-item label="报价价格(元)">
                            <a-input-number
                              style="width: 100%"
                              :disabled="orderData && orderData.status > 0"
                              v-decorator="['price', { rules: [{ required: true, message: '请输入报价价格' }] }]"
                              placeholder="请输入报价价格"
                              :min="0"
                            />
                          </a-form-item>
                        </a-col>
                      </a-row>
                      <a-row :gutter="16">
                        <a-col :span="24">
                          <a-form-item label="报价描述">
                            <a-textarea
                              :disabled="orderData && orderData.status > 0"
                              v-decorator="['content', { rules: [{ required: true, message: '请输入报价描述' }] }]"
                              placeholder="请输入报价描述"
                              :rows="6"
                            />
                          </a-form-item>
                        </a-col>
                      </a-row>
                      <a-row :gutter="16" v-if="orderData.status == 0">
                        <a-col :span="24" style="text-align: right">
                          <a-button type="primary" @click="submitQuote">提交报价</a-button>
                        </a-col>
                      </a-row>
                    </a-form>
                  </a-col>
                </a-row>
              </div>
            </a-col>
            <a-col :span="24" v-if="orderData.deliveryDate == null && orderData.logisticsInfo != null" style="margin-top: 15px;background: #fff;padding: 20px">
              <h3 style="font-size: 18px; font-weight: 650; color: #000c17; margin-bottom: 20px; border-left: 4px solid #1890ff; padding-left: 10px;">
                物流信息
              </h3>
              <div v-if="orderData.logisticsInfo" style="padding: 15px; background: #f5f5f5; border-radius: 4px; margin-bottom: 20px;">
                <a-row :gutter="16">
                  <a-col :span="8"><b>物流公司：</b>{{ JSON.parse(orderData.logisticsInfo).company }}</a-col>
                  <a-col :span="8"><b>物流单号：</b>{{ JSON.parse(orderData.logisticsInfo).trackingNumber }}</a-col>
                  <a-col :span="8"><b>备注信息：</b>{{ JSON.parse(orderData.logisticsInfo).remark }}</a-col>
                </a-row>
              </div>
              <a-button type="primary" @click="confirmReceipt">确认收货</a-button>
            </a-col>
<!--            <a-col :span="24" v-else style="margin-top: 15px;background: #fff;padding: 20px">-->
<!--              <h3 style="font-size: 18px; font-weight: 650; color: #000c17; margin-bottom: 20px; border-left: 4px solid #1890ff; padding-left: 10px;">-->
<!--                修复流程-->
<!--              </h3>-->
<!--              <a-timeline style="margin-top: 20px;">-->
<!--                <a-timeline-item-->
<!--                  v-for="(step, index) in repairSteps"-->
<!--                  :key="step.id"-->
<!--                  :color="getStepColor(step.status)">-->
<!--                  <p style="font-size: 14px; margin-bottom: 5px;">{{ step.time }}</p>-->
<!--                  <p style="font-size: 16px; font-weight: 500; color: #000c17;">{{ step.title }}</p>-->
<!--                  <p style="font-size: 13px; color: #8c8c8c;">{{ step.description }}</p>-->
<!--                </a-timeline-item>-->
<!--              </a-timeline>-->
<!--              <div style="margin-top: 20px; text-align: right;" v-if="orderData.finishDate == null">-->
<!--                <a-button type="primary" v-if="repairSteps.length > 0" @click="completeRepair">-->
<!--                  维修完成-->
<!--                </a-button>-->
<!--                <a-button type="primary" icon="plus" @click="showAddStepForm = true">-->
<!--                  添加步骤-->
<!--                </a-button>-->
<!--              </div>-->

<!--              <a-modal-->
<!--                title="添加维修步骤"-->
<!--                :visible="showAddStepForm"-->
<!--                @ok="addRepairStep"-->
<!--                @cancel="showAddStepForm = false"-->
<!--                width="600px">-->
<!--                <a-form :form="stepForm" layout="vertical">-->
<!--                  <a-form-item label="步骤标题">-->
<!--                    <a-input-->
<!--                      v-decorator="['title', { rules: [{ required: true, message: '请输入步骤标题' }] }]"-->
<!--                      placeholder="请输入步骤标题" />-->
<!--                  </a-form-item>-->
<!--                  <a-form-item label="步骤描述">-->
<!--                    <a-textarea-->
<!--                      v-decorator="['description', { rules: [{ required: true, message: '请输入步骤描述' }] }]"-->
<!--                      placeholder="请输入步骤描述"-->
<!--                      :rows="3" />-->
<!--                  </a-form-item>-->
<!--                  <a-form-item label="状态">-->
<!--                    <a-select-->
<!--                      v-decorator="['status', { rules: [{ required: true, message: '请选择状态' }], initialValue: 'pending' }]">-->
<!--                      <a-select-option value="completed">已完成</a-select-option>-->
<!--                      <a-select-option value="in-progress">进行中</a-select-option>-->
<!--                      <a-select-option value="pending">待处理</a-select-option>-->
<!--                    </a-select>-->
<!--                  </a-form-item>-->
<!--                </a-form>-->
<!--              </a-modal>-->
<!--            </a-col>-->
          </a-row>
        </a-col>
      </a-row>
    </div>
  </a-drawer>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'Map',
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Object
    }
  },
  data () {
    return {
      repairSteps1: [
        {
          id: 1,
          time: '2025-11-20 09:30',
          title: '订单确认',
          description: '技师已确认订单，准备开始维修工作',
          status: 'completed' // completed, in-progress, pending
        },
        {
          id: 2,
          time: '202511-20 14:15',
          title: '检测评估',
          description: '完成物品检测，制定维修方案',
          status: 'completed'
        }
      ],
      repairSteps: [],
      showAddStepForm: false,
      stepForm: this.$form.createForm(this),
      rowId: null,
      quoteForm: this.$form.createForm(this),
      addressList: [],
      fileList: [],
      flawFileList: [],
      previewVisible: false,
      previewImage: '',
      previewVisibleFlaw: false,
      previewImageFlaw: '',
      userInfo: null,
      orderInfo: null,
      startAddressInfo: null,
      endAddressInfo: null,
      discountInfo: null,
      staffInfo: null,
      evaluateInfo: null,
      communityRent: 0,
      rentShow: false,
      communityShow: false,
      communityData: null,
      checkedList: ['Apple', 'Orange'],
      indeterminate: true,
      checkAll: false,
      plainOptions: ['Apple', 'Pear', 'Orange'],
      visible: false,
      rentList: [],
      communityList: [],
      community: null,
      nowPoint: null,
      roadData: [],
      checkLoading: false,
      echartsShow: false,
      getShop: null,
      putShop: null,
      series: [{
        name: '得分',
        data: []
      }],
      chartOptions: {
        chart: {
          height: 380,
          type: 'radar'
        },
        title: {
          text: ''
        },
        xaxis: {
          categories: ['交付得分', '价格得分', '质量得分', '准时得分', '服务得分', '综合得分']
        }
      }
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  watch: {
    'orderShow': function (value) {
      if (value) {
        this.dataInit(this.orderData.id)
        this.queryStaffAddress()
      }
    }
  },
  methods: {
    completeRepair () {
      this.$confirm({
        title: '确认维修完成',
        content: '确定要标记此维修订单为完成状态吗？',
        okText: '确认',
        cancelText: '取消',
        onOk: () => {
          this.$get(`/cos/order-info/complete/${this.orderInfo.id}`).then((r) => {
            this.$message.success('维修已完成')
            this.$emit('close')
          }).catch((e) => {
            this.$message.error('操作失败')
          })
        }
      })
    },
    getStepColor (status) {
      switch (status) {
        case 'completed':
          return 'green'
        case 'in-progress':
          return 'blue'
        case 'pending':
          return 'gray'
        default:
          return 'gray'
      }
    },
    queryRepairStep (orderId) {
      this.$get(`/cos/order-info/queryRepairStep/${orderId}`).then((r) => {
        if (r.data.msg) {
          let repairStep = JSON.parse(r.data.msg)
          this.repairSteps = repairStep
        }
      })
    },
    addRepairStep () {
      this.stepForm.validateFields((err, values) => {
        if (!err) {
          const newStep = {
            id: this.repairSteps.length + 1,
            time: this.formatDate(new Date()),
            title: values.title,
            description: values.description,
            status: values.status
          }
          this.repairSteps.push(newStep)

          this.$put(`/cos/order-info/setRepairStep`, {
            id: this.orderInfo.id,
            fixProcessInfo: JSON.stringify(this.repairSteps)
          }).then((r) => {
            this.queryRepairStep(this.orderInfo.id)
            this.stepForm.resetFields()
            this.showAddStepForm = false
            this.$message.success('步骤添加成功')
          })
        }
      })
    },

    formatDate (date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}`
    },
    confirmReceipt () {
      this.$confirm({
        title: '确认收货',
        content: '确定要确认收货吗？此操作不可撤销。',
        okText: '确认',
        cancelText: '取消',
        onOk: () => {
          this.$get(`/cos/order-info/confirmReceipt/${this.orderInfo.id}`).then(response => {
            this.$message.success('收货确认成功')
            this.$emit('close')
          }).catch(error => {
            this.$message.error('收货确认失败: ' + (error.message || '系统错误'))
          })
        }
      })
    },
    openChat () {
      this.$post('/cos/chat-record/defaultStaffChat', {
        staffId: this.currentUser.userId,
        userId: this.orderInfo.userId,
        senderType: 1,
        content: '你好'
      }).then((r) => {
        // 跳转到聊天页面，并传递默认消息
        this.$router.push({
          path: '/work/chat'
        })
      })
    },
    submitQuote () {
      this.quoteForm.validateFields((err, values) => {
        if (!err) {
          const quoteData = {
            orderId: this.orderInfo.id,
            addressId: values.addressId,
            price: values.price,
            workHour: values.workHour,
            content: values.content,
            staffId: this.currentUser.userId
          }

          if (this.rowId != null) {
            quoteData.id = this.rowId
            this.$put('/cos/order-quotation', quoteData).then(response => {
              this.$message.success('报价提交成功')
              // 可以在此处添加成功后的操作，例如刷新数据或关闭表单
            }).catch(error => {
              this.$message.error('报价提交失败: ' + error.message)
            })
          } else {
            this.$post('/cos/order-quotation', quoteData).then(response => {
              this.$message.success('报价提交成功')
              // 可以在此处添加成功后的操作，例如刷新数据或关闭表单
            }).catch(error => {
              this.$message.error('报价提交失败: ' + error.message)
            })
          }
        }
      })
    },
    queryQuotationByOrder () {
      this.$get('/cos/order-quotation/queryQuotationByStaff', {
        orderId: this.orderInfo.id,
        staffId: this.currentUser.userId
      }).then(response => {
        this.setFormValues(response.data.data)
      })
    },
    setFormValues ({...quotation}) {
      this.rowId = quotation.id
      let fields = ['addressId', 'price', 'workHour', 'content']
      let obj = {}
      Object.keys(quotation).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.quoteForm.getFieldDecorator(key)
          obj[key] = quotation[key]
        }
      })
      this.quoteForm.setFieldsValue(obj)
    },
    queryStaffAddress () {
      this.$get(`/cos/address-info/listByStaffId/${this.currentUser.userId}`).then((r) => {
        this.addressList = r.data.data
      })
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
    flawImagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.flawFileList = imageList
      }
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
    dataInit (orderId) {
      this.$get(`/cos/order-info/${orderId}`).then((r) => {
        this.userInfo = r.data.user
        this.orderInfo = r.data.order
        this.startAddressInfo = r.data.startAddress
        this.endAddressInfo = r.data.endAddress
        this.discountInfo = r.data.discount
        this.staffInfo = r.data.staff
        this.evaluateInfo = r.data.evaluate
        this.imagesInit(this.orderInfo.images)
        this.flawImagesInit(this.orderInfo.flawImages)
        this.queryQuotationByOrder()
        this.queryRepairStep(orderId)
        setTimeout(() => {
          baiduMap.initMap('areas')
          this.getLocal()
          this.navigation(this.startAddressInfo, this.endAddressInfo)
        }, 200)
      })
    },
    navigation (address, merchant) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(merchant.longitude, merchant.latitude), new BMap.Point(address.longitude, address.latitude))
      // this.getRoadData()
    },
    getRoadData () {
      let options = {
        onSearchComplete: results => {
          // eslint-disable-next-line eqeqeq,no-undef
          if (driving.getStatus() == BMAP_STATUS_SUCCESS) {
            // 获取第一条方案
            let plan = results.getPlan(0)
            // 获取方案的驾车线路
            // eslint-disable-next-line no-unused-vars
            let route = plan.getRoute(0)
            // 获取每个关键步骤,并输出到页面
            let s = []
            for (let j = 0; j < plan.getNumRoutes(); j++) {
              let route = plan.getRoute(j)
              for (let i = 0; i < route.getNumSteps(); i++) {
                let step = route.getStep(i)
                s.push((i + 1) + '. ' + step.getDescription())
              }
            }
            this.roadData = s
          }
        }
      }
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), options)
      // eslint-disable-next-line no-undef
      let start = new BMap.Point(this.nowPoint.lng, this.nowPoint.lat)
      let end = null
      if (this.rentShow) {
        end = new BMap.Point(this.rentData.longitude, this.rentData.latitude)
      } else {
        end = new BMap.Point(this.communityData.longitude, this.communityData.latitude)
      }
      // eslint-disable-next-line no-undef
      driving.search(start, end)
    },
    checkEvaluate (score) {
      let data = [score.deliverScore, score.priceScore, score.qualityScore, score.scheduleScore, score.serviceScore, score.overScore, 100]
      this.series[0].data = data
    },
    home () {
      this.$emit('close')
    },
    gisOnChange (e) {
      let data = null
      switch (e.target.value) {
        case '1':
          data = this.getShop
          break
        case '2':
          data = this.putShop
          break
      }
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(this.nowPoint.lng, this.nowPoint.lat), new BMap.Point(data.longitude, data.latitude))
    },
    getLocal () {
      // eslint-disable-next-line no-undef
      let geolocation = new BMap.Geolocation()
      geolocation.getCurrentPosition(r => {
        this.nowPoint = r.point
      }, {enableHighAccuracy: true})
    }
  }
}
</script>

<style scoped>
  >>> .ant-drawer-body {
    padding: 0 !important;
  }
  >>> .ant-card-meta-title {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-card-meta-description {
    font-size: 13px;
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
  >>> .ant-radio-button-wrapper {
    border-radius: 0;
  }
</style>
