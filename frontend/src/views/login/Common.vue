<template>
  <div class="container">
    <div class="content-wrapper">
      <div class="card">
        <div class="header-section">
          <div class="header">
            <span class="title">老物件回收与修复系统</span>
          </div>
          <div class="desc">欢迎使用我们的专业回收与修复服务</div>
        </div>
        <component :is="componentName" @regist="handleRegist" class="main-content"></component>
      </div>
    </div>
    <global-footer :copyright="copyright"/>
  </div>
</template>

<script>
import GlobalFooter from '../common/GlobalFooter'
import Login from './Login'
import Regist from './Regist'
import RegistStaff from './RegistStaff'

export default {
  name: 'Common',
  components: {GlobalFooter, Login, Regist, RegistStaff},
  data () {
    return {
      componentName: 'Login'
    }
  },
  computed: {
    systemName () {
      return this.$store.state.setting.systemName
    },
    copyright () {
      return this.$store.state.setting.copyright
    }
  },
  methods: {
    handleRegist (val) {
      this.componentName = val
    }
  }
}
</script>

<style lang="less" scoped>
.container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)),
  url(../../../static/img/house.jpg) no-repeat center center;
  background-size: cover;

  .content-wrapper {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px;

    .card {
      width: 100%;
      max-width: 450px;
      background: rgba(255, 255, 255, 0.95);
      border-radius: 16px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
      padding: 40px 30px;
      margin: 20px 0;

      .header-section {
        text-align: center;
        margin-bottom: 30px;

        .header {
          margin-bottom: 15px;

          .title {
            font-size: 28px;
            color: #333;
            font-weight: 600;
            font-family: 'Microsoft YaHei', sans-serif;
          }
        }

        .desc {
          font-size: 15px;
          color: #666;
        }
      }
    }
  }

  .main-content {
    width: 368px;
    margin: 0 auto;
    @media screen and (max-width: 576px) {
      width: 95%;
    }
    @media screen and (max-width: 320px) {
      .captcha-button {
        font-size: 14px;
      }
    }
  }
}
</style>
