<template>
  <div class="dashboard-layout">
    
    <div class="mobile-header">
      <div class="logo-box" @click="router.push('/profile')" title="用户中心">LF</div>
      <span class="mobile-title">我的生词本</span>
    </div>

    <div class="sidebar">
      <div class="top-section">
        <div class="logo-box desktop-only" @click="goToDashboard">LF</div>
        <div class="nav-menu">
          <div class="nav-item" @click="goToDashboard">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path></svg>
            <span>工作台</span>
          </div>
          <div class="nav-item active">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z"></path></svg>
            <span>生词本</span>
          </div>
          <div class="nav-item" @click="router.push('/history')">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
            <span>历史</span>
          </div>
        </div>
      </div>
      <div class="bottom-section desktop-only">
        <div class="avatar" @click="router.push('/profile')" title="用户中心">👨‍💻</div>
      </div>
    </div>

    <div class="main-content">
      <div class="page-header">
        <div class="title-area">
          <h1 class="page-title">📚 核心生词库</h1>
          <p class="page-subtitle">结合原句语境，构建你的专属记忆宫殿。共收录 <strong>{{ total }}</strong> 个单词。</p>
        </div>
      </div>

      <div v-if="isLoading" class="status-state">
        <div class="spinner"></div>
        <p>正在从记忆宫殿中提取生词...</p>
      </div>

      <div v-else-if="vocabList.length === 0" class="status-state empty-state">
        <div class="empty-icon">📭</div>
        <h2>生词本空空如也</h2>
        <button class="primary-btn" style="width: auto; padding: 10px 24px; margin-top: 15px;" @click="goToDashboard">去阅读文章</button>
      </div>

      <div v-else style="display: flex; flex-direction: column; flex: 1;">
        <div class="vocab-grid">
          <div v-for="vocab in vocabList" :key="vocab.id" class="vocab-card" :class="{ 'mastered': vocab.mastered === 1 }">
            <div class="card-top">
              <h2 class="word">{{ vocab.word }}</h2>
            </div>
            <div class="card-translation">{{ vocab.translation }}</div>
            <div class="card-context">
              <span class="context-label">语境回忆：</span>
              <p class="context-sentence">{{ vocab.contextSentence }}</p>
            </div>
            <div class="card-actions">
              <button v-if="vocab.mastered !== 1" class="action-icon-btn check-btn" @click="markMastered(vocab)">✅ 掌握</button>
              <button class="action-icon-btn delete-btn" @click="removeVocab(vocab.id)">🗑️ 移除</button>
            </div>
          </div>
        </div>

        <div class="pagination-bar" v-if="totalPages > 1">
          <button class="page-btn" :disabled="currentPage === 1" @click="changePage(currentPage - 1)">上一页</button>
          <span class="page-info">第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
          <button class="page-btn" :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">下一页</button>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getVocabularyListApi, deleteVocabularyApi, updateVocabularyStatusApi } from '../api/vocabulary'

const router = useRouter()
const isLoading = ref(true)
const vocabList = ref([])

// 分页状态
const currentPage = ref(1)
const pageSize = ref(12) // 一页显示 12 个比较好看
const total = ref(0)
const totalPages = ref(1)

const fetchVocabularies = async () => {
  isLoading.value = true
  try {
    const res = await getVocabularyListApi(currentPage.value, pageSize.value)
    if (res.records) {
      vocabList.value = res.records
      total.value = res.total
      totalPages.value = Math.ceil(res.total / pageSize.value)
    } else {
      vocabList.value = res || []
      total.value = vocabList.value.length
    }
  } catch (error) {
    console.error('获取失败', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => { fetchVocabularies() })

const changePage = (page) => {
  currentPage.value = page
  fetchVocabularies()
}

const goToDashboard = () => router.push('/dashboard')

const removeVocab = async (id) => {
  if (!confirm('确定移出专属词库吗？')) return
  try {
    await deleteVocabularyApi(id)
    // 删除了要刷新当前页
    fetchVocabularies() 
  } catch (error) {
    alert('移除失败！')
  }
}

const markMastered = async (vocab) => {
  try {
    await updateVocabularyStatusApi(vocab.id, 1)
    vocab.mastered = 1
  } catch (error) {
    alert('状态更新失败！')
  }
}
</script>

<style scoped>
.dashboard-layout { display: flex; height: 100vh; width: 100vw; background-color: #f3f4f6; font-family: -apple-system, sans-serif; overflow: hidden;}
.mobile-header { display: none; }

/* 侧边栏完美间距 */
.sidebar { width: 80px; background-color: #ffffff; border-right: 1px solid #e5e7eb; display: flex; flex-direction: column; justify-content: space-between; align-items: center; padding: 24px 0; flex-shrink: 0; z-index: 10;}
.top-section { display: flex; flex-direction: column; align-items: center; width: 100%; }
.logo-box { width: 40px; height: 40px; background-color: #111827; color: #ffffff; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-weight: 800; cursor: pointer; margin-bottom: 60px; }
.nav-menu { display: flex; flex-direction: column; gap: 36px; width: 100%; }
.nav-item { display: flex; flex-direction: column; align-items: center; gap: 8px; color: #9ca3af; cursor: pointer; transition: 0.2s;}
.nav-icon { width: 24px; height: 24px; }
.nav-item span { font-size: 12px; font-weight: 500; }
.nav-item:hover { color: #4b5563; }
.nav-item.active { color: #111827; }
.avatar { width: 40px; height: 40px; background-color: #f3f4f6; border-radius: 50%; display: flex; align-items: center; justify-content: center; cursor: pointer; transition: 0.2s; border: 2px solid transparent;}
.avatar:hover { border-color: #111827; transform: scale(1.05); }

.main-content { flex: 1; display: flex; flex-direction: column; padding: 30px 40px; overflow-y: auto; }
.page-header { margin-bottom: 30px; }
.page-title { font-size: 28px; font-weight: 800; color: #111827; margin: 0 0 10px 0; }
.page-subtitle { font-size: 15px; color: #6b7280; margin: 0; }

.status-state { display: flex; flex-direction: column; align-items: center; justify-content: center; height: 50vh; color: #6b7280; }
.empty-icon { font-size: 60px; margin-bottom: 20px; }
.spinner { width: 40px; height: 40px; border: 4px solid #e5e7eb; border-top: 4px solid #111827; border-radius: 50%; animation: spin 1s linear infinite; margin-bottom: 20px; }
@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }

.vocab-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 24px; margin-bottom: 30px; flex: 1; align-content: start;}
.vocab-card { background: #ffffff; border-radius: 16px; border: 1px solid #e5e7eb; padding: 20px; display: flex; flex-direction: column; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); transition: transform 0.2s; }
.vocab-card.mastered { opacity: 0.7; background: #f9fafb; border-color: #d1d5db; }
.card-top { margin-bottom: 10px; }
.word { font-size: 24px; font-weight: 800; color: #111827; margin: 0; }
.card-translation { font-size: 15px; color: #4b5563; line-height: 1.5; white-space: pre-wrap; margin-bottom: 15px; flex: 1;}
.card-context { background: #f8fafc; padding: 12px; border-radius: 8px; border-left: 3px solid #cbd5e1; margin-bottom: 15px; }
.context-label { font-size: 12px; color: #64748b; font-weight: bold; margin-bottom: 5px; display: block;}
.context-sentence { font-size: 13.5px; color: #475569; font-style: italic; margin: 0; line-height: 1.5; }
.card-actions { display: flex; gap: 10px; border-top: 1px solid #f3f4f6; padding-top: 15px; }
.action-icon-btn { flex: 1; padding: 8px; border-radius: 8px; font-size: 13px; font-weight: 600; cursor: pointer; border: none; }
.check-btn { background: #f0fdf4; color: #16a34a; }
.delete-btn { background: #fef2f2; color: #dc2626; }
.primary-btn { padding: 12px; background: #111827; color: white; border-radius: 8px; border: none; font-weight: 600; cursor: pointer; }

/* 💥 分页器样式 💥 */
.pagination-bar { display: flex; justify-content: center; align-items: center; gap: 20px; padding: 20px 0; border-top: 1px solid #e5e7eb; margin-top: auto;}
.page-btn { padding: 8px 16px; background: #ffffff; border: 1px solid #d1d5db; border-radius: 8px; font-weight: 600; cursor: pointer; transition: 0.2s;}
.page-btn:hover:not([disabled]) { background: #f3f4f6; border-color: #111827;}
.page-btn[disabled] { opacity: 0.5; cursor: not-allowed; }
.page-info { font-size: 14px; color: #4b5563; font-weight: 500;}

@media (max-width: 768px) {
  .dashboard-layout { flex-direction: column; overflow-y: auto;} 
  .desktop-only { display: none !important; }
  .mobile-header { display: flex; align-items: center; gap: 15px; padding: 15px 20px; background: white; border-bottom: 1px solid #e5e7eb; position: sticky; top: 0; z-index: 20;}
  .mobile-title { font-weight: 700; font-size: 16px;}
  .mobile-header .logo-box { margin: 0; width: 32px; height: 32px; font-size: 14px;}
  .sidebar { width: 100%; height: 65px; padding: 0; flex-direction: row; border-top: 1px solid #e5e7eb; position: fixed; bottom: 0; z-index: 20; background: #ffffff; border-right: none;}
  .top-section { height: 100%; justify-content: center;}
  .nav-menu { flex-direction: row; justify-content: space-between; padding: 0 40px; margin: 0; gap: 0;}
  .nav-item { gap: 4px; flex: 1;}
  .main-content { padding: 20px 15px; padding-bottom: 90px; }
  .vocab-grid { grid-template-columns: 1fr; gap: 15px; }
}
</style>