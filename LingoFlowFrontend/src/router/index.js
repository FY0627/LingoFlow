import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'

const routes = [
  {
    path: '/',
    redirect: '/login' // 访问根目录直接跳到登录页
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  }
  // 以后你的注册页、后台主页都在这里往下加
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router