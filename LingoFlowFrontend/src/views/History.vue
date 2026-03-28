<template>
  <div class="dashboard-layout">
    
    <div class="mobile-header">
      <div class="logo-box">LF</div>
      <span class="mobile-title">阅读历史</span>
    </div>

    <div class="sidebar">
      <div class="top-section">
        <div class="logo-box desktop-only">LF</div>
        <div class="nav-menu">
          <div class="nav-item" @click="goToDashboard">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path></svg>
            <span>工作台</span>
          </div>
          <div class="nav-item" @click="goToVocabulary">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z"></path></svg>
            <span>生词本</span>
          </div>
          <div class="nav-item active">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
            <span>历史</span>
          </div>
        </div>
      </div>
      <div class="bottom-section desktop-only">
        <div class="avatar">👨‍💻</div>
      </div>
    </div>

    <div class="main-content">
      <div class="page-header">
        <div class="title-area">
          <h1 class="page-title">🕰️ 阅读历史</h1>
          <p class="page-subtitle">温故而知新。你一共生成了 <strong>{{ articleList.length }}</strong> 篇 i+1 专属文章。</p>
        </div>
      </div>

      <div v-if="isLoading" class="status-state">
        <div class="spinner"></div>
        <p>正在翻阅历史档案...</p>
      </div>

      <div v-else-if="articleList.length === 0" class="status-state empty-state">
        <div class="empty-icon">📭</div>
        <h2>暂无阅读记录</h2>
        <p>你的学习之旅才刚刚开始，快去生成第一篇文章吧！</p>
        <button class="primary-btn" style="width: auto; padding: 10px 24px; margin-top: 15px;" @click="goToDashboard">去工作台</button>
      </div>

      <div v-else class="history-list">
        <div v-for="article in articleList" :key="article.id" class="history-card">
          
          <div class="card-header">
            <div class="badge-group">
              <span class="difficulty-badge">{{ article.difficultyLevel || '标准难度' }}</span>
              <span class="time-text">{{ formatDate(article.createTime || article.createdAt) }}</span>
            </div>
            <div style="display: flex; gap: 10px;">
              <button class="primary-btn" style="padding: 6px 12px; font-size: 13px;" @click="resumeLearning(article)">
                ✏️ 继续学习
              </button>
              <button class="delete-icon-btn" title="删除记录" @click="removeArticle(article.id)">🗑️</button>
            </div>
          </div>

          <div class="text-comparison">
            <div class="text-box">
              <div class="box-label">原文</div>
              <p class="text-content">{{ article.originalText }}</p>
            </div>
            
            <div class="text-box highlight-box">
              <div class="box-label">i+1 改写版</div>
              <p class="text-content">{{ article.adaptedText }}</p>
            </div>
          </div>

        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getArticleListApi, deleteArticleApi } from '../api/article'

const router = useRouter()
const isLoading = ref(true)
const articleList = ref([])

const fetchArticles = async () => {
  isLoading.value = true
  try {
    const res = await getArticleListApi()
    // 兼容 Mybatis-Plus 分页 (res.records) 或纯列表 (res)
    articleList.value = res.records ? res.records : (res || [])
  } catch (error) {
    console.error('获取历史记录失败', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchArticles()
})

const goToDashboard = () => router.push('/dashboard')
const goToVocabulary = () => router.push('/vocabulary')

// 新增：继续学习功能
const resumeLearning = (article) => {
  // 把整篇文章的数据压缩存进 localStorage
  localStorage.setItem('lingoflow_resume_article', JSON.stringify(article))
  router.push('/dashboard') // 跳回工作台
}

// 修复：删除功能 (确保后端有 /api/article/delete/{id} 接口)
const removeArticle = async (id) => {
  if (!confirm('确定永久删除这条记录吗？')) return
  try {
    await deleteArticleApi(id)
    articleList.value = articleList.value.filter(a => a.id !== id)
    alert('删除成功！')
  } catch (error) {
    alert('删除失败，请检查后端是否报错。')
  }
}

// 格式化时间字符串
const formatDate = (dateStr) => {
  if (!dateStr) return '未知时间'
  // 将 2026-03-26T18:10:16 转化为 2026-03-26 18:10
  return dateStr.replace('T', ' ').substring(0, 16)
}
</script>

<style scoped>
/* =========== 基础布局样式 (三端统一) =========== */
.dashboard-layout { display: flex; height: 100vh; width: 100vw; background-color: #f3f4f6; font-family: -apple-system, sans-serif; overflow: hidden;}
.mobile-header { display: none; }
.sidebar { width: 80px; background-color: #ffffff; border-right: 1px solid #e5e7eb; display: flex; flex-direction: column; justify-content: space-between; align-items: center; padding: 24px 0; flex-shrink: 0; z-index: 10;}
.logo-box { width: 40px; height: 40px; background-color: #111827; color: #ffffff; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-weight: 800; cursor: pointer; }
.nav-menu { display: flex; flex-direction: column; gap: 24px; width: 100%; }
.nav-item { display: flex; flex-direction: column; align-items: center; gap: 6px; color: #9ca3af; cursor: pointer; transition: 0.2s;}
.nav-icon { width: 24px; height: 24px; }
.nav-item span { font-size: 12px; font-weight: 500; }
.nav-item:hover { color: #4b5563; }
.nav-item.active { color: #111827; }
.avatar { width: 40px; height: 40px; background-color: #f3f4f6; border-radius: 50%; display: flex; align-items: center; justify-content: center; cursor: pointer; }

/* =========== 主内容区 =========== */
.main-content { flex: 1; display: flex; flex-direction: column; padding: 30px 40px; overflow-y: auto; }
.page-header { margin-bottom: 30px; }
.page-title { font-size: 28px; font-weight: 800; color: #111827; margin: 0 0 10px 0; }
.page-subtitle { font-size: 15px; color: #6b7280; margin: 0; }
.page-subtitle strong { color: #111827; }

/* 状态展示 */
.status-state { display: flex; flex-direction: column; align-items: center; justify-content: center; height: 50vh; color: #6b7280; }
.empty-icon { font-size: 60px; margin-bottom: 20px; }
.empty-state h2 { color: #111827; margin-bottom: 10px; }
.spinner { width: 40px; height: 40px; border: 4px solid #e5e7eb; border-top: 4px solid #111827; border-radius: 50%; animation: spin 1s linear infinite; margin-bottom: 20px; }
@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }

/* =========== 历史列表卡片 =========== */
.history-list { display: flex; flex-direction: column; gap: 24px; padding-bottom: 40px; }
.history-card { background: #ffffff; border-radius: 16px; border: 1px solid #e5e7eb; padding: 24px; display: flex; flex-direction: column; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }

.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; border-bottom: 1px solid #f3f4f6; padding-bottom: 15px;}
.badge-group { display: flex; align-items: center; gap: 12px; }
.difficulty-badge { background: #111827; color: #ffffff; padding: 4px 10px; border-radius: 6px; font-size: 12px; font-weight: 600; }
.time-text { font-size: 13px; color: #9ca3af; }
.delete-icon-btn { background: none; border: none; cursor: pointer; font-size: 16px; opacity: 0.5; transition: 0.2s;}
.delete-icon-btn:hover { opacity: 1; transform: scale(1.1);}

/* 双面文本对比布局 */
.text-comparison { display: flex; gap: 20px; }
.text-box { flex: 1; display: flex; flex-direction: column; width: 50%;}
.highlight-box { background: #fafafa; border-radius: 8px; padding: 15px; border: 1px solid #f3f4f6;}
.box-label { font-size: 13px; font-weight: 600; color: #6b7280; margin-bottom: 10px; text-transform: uppercase; letter-spacing: 1px;}
.text-content { font-size: 15px; color: #374151; line-height: 1.6; margin: 0; white-space: pre-wrap; font-family: 'Georgia', serif;}

.primary-btn { padding: 12px; background: #111827; color: white; border-radius: 8px; border: none; font-weight: 600; cursor: pointer; transition: 0.2s; }
.primary-btn:hover { background: #374151; }

/* =========== 移动端适配 =========== */
@media (max-width: 768px) {
  .dashboard-layout { flex-direction: column; overflow-y: auto;} 
  .desktop-only { display: none !important; }
  
  .mobile-header { display: flex; align-items: center; gap: 15px; padding: 15px 20px; background: white; border-bottom: 1px solid #e5e7eb; position: sticky; top: 0; z-index: 20;}
  .mobile-title { font-weight: 700; font-size: 16px;}
  .mobile-header .logo-box { margin: 0; width: 32px; height: 32px; font-size: 14px;}

  .sidebar { width: 100%; height: 65px; padding: 0; flex-direction: row; border-right: none; border-top: 1px solid #e5e7eb; position: fixed; bottom: 0; z-index: 20; background: #ffffff;}
  .top-section { width: 100%; display: flex; align-items: center; justify-content: center; height: 100%; }
  .nav-menu { flex-direction: row; justify-content: space-between; padding: 0 40px; width: 100%; margin: 0; gap: 0; }
  .nav-item { flex-direction: column; gap: 4px; flex: 1; align-items: center; }

  .main-content { padding: 20px 15px; padding-bottom: 80px; }
  .page-title { font-size: 24px; }
  
  /* 手机端对照布局改为上下堆叠 */
  .text-comparison { flex-direction: column; gap: 20px; }
  .text-box { width: 100%; }
  .highlight-box { padding: 10px; }
}
</style>