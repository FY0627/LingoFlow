<template>
  <div class="main-viewport">
    <div class="login-container">
      <AnimatedBrand :isPasswordFocused="isPasswordFocused" />

      <div class="right-panel">
        <div class="form-wrapper">
          <div class="mobile-logo">
            <span class="brand-name-mobile">LingoFlow</span>
          </div>

          <div class="header-text">
            <span class="title">欢迎回来！</span>
            <span class="subtitle">请输入您的账号信息进行登录</span>
          </div>

          <div class="form-group">
            <span class="label">账号</span>
            <input class="input-field" v-model="formData.username" type="text" placeholder="你的用户名" />
          </div>

          <div class="form-group">
            <span class="label">密码</span>
            <div class="password-input-wrapper">
              <input 
                class="input-field pr-10" 
                v-model="formData.password" 
                :type="showPassword ? 'text' : 'password'" 
                placeholder="••••••••" 
                @focus="isPasswordFocused = true" 
                @blur="isPasswordFocused = false"
              />
              <div class="eye-icon" @click="showPassword = !showPassword">
                <div class="simple-eye-svg" :class="{ showing: showPassword }"></div>
              </div>
            </div>
          </div>

          <button class="submit-btn" :disabled="isLoading" @click="handleLogin">
            {{ isLoading ? '登录中...' : '登 录' }}
          </button>

          <div class="signup-link">
            <span class="text">还没有账号？</span>
            <span class="link">立即注册</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
// 导入左侧动画组件
import AnimatedBrand from '../components/AnimatedBrand.vue'

const showPassword = ref(false)
const isLoading = ref(false)
const isPasswordFocused = ref(false)

const formData = reactive({
  username: '',
  password: ''
})

const handleLogin = () => {
  // TODO: 这里之后会调用 src/api 里的 axios 接口连接后端
  alert(`提交账号: ${formData.username}, 密码: ${formData.password}`)
}
</script>

<style scoped>
/* 这里只保留右侧表单的样式 */
.main-viewport { width: 100%; height: 100%; display: flex; background-color: #ffffff; }
.login-container { display: flex; width: 100%; height: 100%; }
.right-panel { flex: 1; display: flex; align-items: center; justify-content: center; padding: 2rem; background-color: #ffffff; height: 100%; overflow: hidden;}
.form-wrapper { width: 100%; max-width: 360px; display: flex; flex-direction: column; height: 100%; justify-content: center;}
.mobile-logo { display: flex; justify-content: center; margin-bottom: 2rem; }
@media (min-width: 1024px) { .mobile-logo { display: none; } }
.brand-name-mobile { font-size: 1.5rem; font-weight: bold; color: #111827; }
.header-text { text-align: center; margin-bottom: 2.5rem; }
.title { display: block; font-size: 1.5rem; font-weight: bold; color: #111827; margin-bottom: 0.5rem; }
.subtitle { font-size: 0.875rem; color: #6b7280; }
.form-group { margin-bottom: 1.25rem; width: 100%; }
.label { display: block; font-size: 0.875rem; font-weight: 500; margin-bottom: 0.5rem; color: #374151; }
.input-field { width: 100%; height: 44px; padding: 0 12px; border: 1px solid #e5e7eb; border-radius: 8px; font-size: 0.875rem; background-color: #ffffff; box-sizing: border-box;}
.input-field:focus { border-color: #000000; outline: none;}
.password-input-wrapper { position: relative; width: 100%; }
.eye-icon { position: absolute; right: 12px; top: 50%; transform: translateY(-50%); cursor: pointer; z-index: 10; padding: 5px; color: #9ca3af; }
.simple-eye-svg { width: 18px; height: 18px; position: relative; }
.simple-eye-svg::before { content: ''; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); width: 16px; height: 10px; border-radius: 50px; border: 1.5px solid currentColor; }
.simple-eye-svg::after { content: ''; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); width: 5px; height: 5px; background-color: currentColor; border-radius: 50%; }
.simple-eye-svg.showing::before { content: ''; width: 16px; height: 1.5px; background-color: currentColor; border: none; border-radius: 1px; }
.simple-eye-svg.showing::after { content: none; }
.submit-btn { width: 100%; height: 44px; background-color: #111827; color: white; border-radius: 8px; font-size: 0.875rem; font-weight: 500; display: flex; align-items: center; justify-content: center; border: none; cursor: pointer; margin-top: 10px; }
.submit-btn:active { background-color: #374151; }
.submit-btn[disabled] { opacity: 0.7; }
.signup-link { text-align: center; margin-top: 1.5rem; font-size: 0.875rem; }
.signup-link .text { color: #6b7280; }
.signup-link .link { color: #111827; font-weight: 500; margin-left: 5px; cursor: pointer; text-decoration: none;}
</style>