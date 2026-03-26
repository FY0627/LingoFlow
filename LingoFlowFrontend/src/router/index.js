import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Dashboard from '../views/Dashboard.vue'
import Vocabulary from '../views/Vocabulary.vue' // 【新增】引入生词本

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
  { path: '/vocabulary', name: 'Vocabulary', component: Vocabulary } // 【新增】路由配置
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router