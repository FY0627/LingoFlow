import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Dashboard from '../views/Dashboard.vue'
import Vocabulary from '../views/Vocabulary.vue'
import History from '../views/History.vue' // 【新增】引入历史页
import Profile from '../views/Profile.vue'
import AdminCorrection from '../views/AdminCorrection.vue'
import MessageCenter from '../views/MessageCenter.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
  { path: '/vocabulary', name: 'Vocabulary', component: Vocabulary },
  { path: '/history', name: 'History', component: History },
  { path: '/profile', name: 'Profile', component: Profile },
  { path: '/message', name: 'MessageCenter', component: MessageCenter },
  { path: '/admin/correction', name: 'AdminCorrection', component: AdminCorrection }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router