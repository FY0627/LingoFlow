import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Dashboard from '../views/Dashboard.vue'
import Vocabulary from '../views/Vocabulary.vue'
import History from '../views/History.vue' // 【新增】引入历史页

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
  { path: '/vocabulary', name: 'Vocabulary', component: Vocabulary },
  { path: '/history', name: 'History', component: History } // 【新增】路由配置
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router