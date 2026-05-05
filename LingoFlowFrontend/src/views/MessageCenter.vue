<template>
  <div class="dashboard-layout">
    <!-- Sidebar (Same as Dashboard) -->
    <div class="sidebar">
      <div class="top-section">
        <div class="logo-box" @click="router.push('/dashboard')">LF</div>
        <div class="nav-menu">
          <div class="nav-item" @click="router.push('/dashboard')">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path></svg>
            <span>工作台</span>
          </div>
          <div class="nav-item" @click="router.push('/vocabulary')">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z"></path></svg>
            <span>生词本</span>
          </div>
          <div class="nav-item" @click="router.push('/history')">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
            <span>历史</span>
          </div>
          <div class="nav-item active">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"></path></svg>
            <span>消息</span>
          </div>
        </div>
      </div>
      <div class="bottom-section">
        <div class="avatar" @click="router.push('/profile')" title="用户中心">👨‍💻</div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <div class="page-header">
        <h1 class="page-title">系统通知中心</h1>
        <p class="page-subtitle">及时掌握纠错审核反馈与系统动态。</p>
      </div>

      <div v-if="isLoading" class="loading-state">
        <div class="spinner"></div>
        <p>正在拉取消息...</p>
      </div>

      <div v-else-if="messageList.length === 0" class="empty-state">
        <div class="empty-icon">🔔</div>
        <h2>暂无系统消息</h2>
        <p>当您的纠错反馈被处理后，这里会收到通知。</p>
      </div>

      <div v-else class="message-list">
        <div v-for="msg in messageList" :key="msg.id" class="message-card" :class="{ 'unread': msg.isRead === 0 }" @click="handleRead(msg)">
          <div class="message-header">
            <div class="status-dot" v-if="msg.isRead === 0"></div>
            <span class="message-time">{{ formatDate(msg.createTime) }}</span>
          </div>
          <div class="message-body">
            {{ msg.content }}
          </div>
        </div>

        <div class="pagination" v-if="totalPages > 1">
          <button :disabled="page <= 1" @click="page--; fetchMessages()">上一页</button>
          <span class="page-info">第 {{ page }} / {{ totalPages }} 页</span>
          <button :disabled="page >= totalPages" @click="page++; fetchMessages()">下一页</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMessageListApi, markAsReadApi } from '../api/message'

const router = useRouter()
const messageList = ref([])
const isLoading = ref(true)
const page = ref(1)
const size = ref(10)
const totalPages = ref(1)

const fetchMessages = async (silent = false) => {
  if (!silent) isLoading.value = true
  try {
    const res = await getMessageListApi(page.value, size.value)
    messageList.value = res.records
    totalPages.value = res.pages
  } catch (err) {
    console.error(err)
  } finally {
    if (!silent) isLoading.value = false
  }
}

let timer = null

onMounted(() => {
  fetchMessages()
  timer = setInterval(() => {
    fetchMessages(true)
  }, 10000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})

const handleRead = async (msg) => {
  if (msg.isRead === 1) return
  try {
    await markAsReadApi(msg.id)
    msg.isRead = 1
  } catch (err) {
    console.error(err)
  }
}

const formatDate = (dateArr) => {
    if (!dateArr) return '-'
    if (Array.isArray(dateArr)) {
        return `${dateArr[0]}-${String(dateArr[1]).padStart(2, '0')}-${String(dateArr[2]).padStart(2, '0')} ${String(dateArr[3]).padStart(2, '0')}:${String(dateArr[4]).padStart(2, '0')}`
    }
    return dateArr
}

onMounted(fetchMessages)
</script>

<style scoped>
.dashboard-layout {
  display: flex;
  height: 100vh;
  width: 100vw;
  background-color: #f3f4f6;
  overflow: hidden;
}

/* Sidebar Styling (Reuse) */
.sidebar { width: 80px; background-color: #ffffff; border-right: 1px solid #e5e7eb; display: flex; flex-direction: column; justify-content: space-between; align-items: center; padding: 24px 0; flex-shrink: 0; }
.logo-box { width: 40px; height: 40px; background-color: #111827; color: #ffffff; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-weight: 800; cursor: pointer; margin-bottom: 60px; }
.nav-menu { display: flex; flex-direction: column; gap: 36px; width: 100%; }
.nav-item { display: flex; flex-direction: column; align-items: center; gap: 8px; color: #9ca3af; cursor: pointer; transition: 0.2s;}
.nav-icon { width: 24px; height: 24px; }
.nav-item span { font-size: 12px; font-weight: 500; }
.nav-item:hover { color: #4b5563; }
.nav-item.active { color: #111827; }
.avatar { width: 40px; height: 40px; background-color: #f3f4f6; border-radius: 50%; display: flex; align-items: center; justify-content: center; cursor: pointer; transition: 0.2s; border: 2px solid transparent;}

/* Main Content */
.main-content {
  flex: 1;
  padding: 40px 60px;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.page-header { margin-bottom: 40px; }
.page-title { font-size: 32px; font-weight: 800; color: #111827; margin: 0 0 10px 0; }
.page-subtitle { font-size: 15px; color: #6b7280; margin: 0; }

.message-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  max-width: 800px;
}

.message-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  border: 1px solid #e5e7eb;
  transition: 0.2s;
  cursor: pointer;
  position: relative;
}

.message-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
}

.message-card.unread {
  border-left: 4px solid #111827;
}

.status-dot {
  width: 8px;
  height: 8px;
  background: #111827;
  border-radius: 50%;
  position: absolute;
  top: 20px;
  left: 8px;
}

.message-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 8px;
}

.message-time {
  font-size: 12px;
  color: #9ca3af;
}

.message-body {
  font-size: 15px;
  color: #374151;
  line-height: 1.6;
}

.loading-state, .empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 100px;
  color: #9ca3af;
}

.empty-icon { font-size: 48px; margin-bottom: 16px; }

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #e5e7eb;
  border-top: 4px solid #111827;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 30px;
}

.pagination button {
  padding: 8px 16px;
  background: white;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  cursor: pointer;
}

.pagination button:disabled { opacity: 0.5; cursor: not-allowed; }
</style>
