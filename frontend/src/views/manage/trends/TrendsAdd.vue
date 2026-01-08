<template>
  <a-modal v-model="show" title="新增价格记录" @cancel="onClose" :width="500">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="24">
          <a-form-item label='记录品类' v-bind="formItemLayout">
            <a-select
              v-decorator="['category', { rules: [{ required: true, message: '请选择记录品类' }] }]"
              placeholder="请选择记录品类"
            >
              <a-select-option
                v-for="category in categoryList"
                :key="category.id"
                :value="category.category"
              >
                {{ category.category }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='记录价格' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
             'price',
             { rules: [{ required: true, message: '请输入记录价格!' }] }
             ]" :min="0.1" :step="0.1"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='记录时间' v-bind="formItemLayout">
            <a-date-picker v-decorator="[
             'recordDate',
             { rules: [{ required: true, message: '请选择记录时间!' }] }
             ]" style="width: 100%"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
import moment from 'moment'
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
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'BulletinAdd',
  props: {
    bulletinAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.bulletinAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      categoryList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  mounted() {
    this.queryCategoryList();
  },
  methods: {
    queryCategoryList () {
      this.$get('/cos/price-config/list').then((r) => {
        this.categoryList = r.data.data
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
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          if (values.recordDate) {
            values.recordDate = moment(values.recordDate).format('YYYY-MM-DD')
          }
          this.loading = true
          this.$post('/cos/price-trends', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
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

</style>
