
<template>
  <a-modal
    title="修改报价"
    :visible="exchangeShow"
    @cancel="handleCancel"
    @ok="handleOk"
    width="600px">
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="24">
          <a-form-item label='报价金额'>
            <a-input-number :disabled="exchangeData != null && exchangeData.status != 0" style="width: 100%" v-decorator="[
                'price',
                { rules: [{ required: true, message: '请输入报价金额!' }] }
              ]"
                                         :min="0"
                                         :step="0.01"
                                         placeholder="请输入报价金额"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='报价内容'>
            <a-textarea
              :disabled="exchangeData != null && exchangeData.status != 0"
              :rows="4"
              v-decorator="[
                'content',
                { rules: [{ required: true, message: '请输入报价内容!' }] }
              ]"
              placeholder="请输入报价内容"/>
          </a-form-item>
        </a-col>
        <a-col :span="24" v-if="exchangeData != null && exchangeData.status != 0">
          <a-result title="此订单报价已选定，无法修改报价信息">
          </a-result>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>export default {
  name: 'QuotationEdit',
  props: {
    exchangeShow: {
      type: Boolean,
      default: false
    },
    exchangeData: {
      type: Object,
      default: null
    }
  },
  data () {
    return {
      form: this.$form.createForm(this)
    }
  },
  watch: {
    exchangeData: {
      handler (val) {
        if (val) {
          this.$nextTick(() => {
            this.form.setFieldsValue({
              price: val.price,
              content: val.content
            })
          })
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    handleCancel () {
      this.form.resetFields()
      this.$emit('close')
    },
    handleOk () {
      this.form.validateFields((err, values) => {
        values.id = this.exchangeData.id
        if (!err) {
          this.$put(`/cos/order-quotation`, values).then((r) => {
            this.$emit('success')
            this.form.resetFields()
          })
        }
      })
    }
  }
}
</script>
