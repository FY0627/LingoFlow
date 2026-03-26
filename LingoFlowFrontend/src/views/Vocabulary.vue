<template>
  <div class="dashboard-layout">
    
    <div class="mobile-header">
      <div class="logo-box">LF</div>
      <span class="mobile-title">我的生词本</span>
    </div>

    <div class="sidebar">
      <div class="top-section">
        <div class="logo-box desktop-only">LF</div>
        <div class="nav-menu">
          <div class="nav-item" @click="goToDashboard">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path></svg>
            <span>工作台</span>
          </div>
          <div class="nav-item active">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z"></path></svg>
            <span>生词本</span>
          </div>
          <div class="nav-item">
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
          <h1 class="page-title">📚 核心生词库</h1>
          <p class="page-subtitle">结合原句语境，构建你的专属记忆宫殿。共收录 <strong>{{ vocabList.length }}</strong> 个单词。</p>
        </div>
      </div>

      <div v-if="isLoading" class="status-state">
        <div class="spinner"></div>
        <p>正在从记忆宫殿中提取生词...</p>
      </div>

      <div v-else-if="vocabList.length === 0" class="status-state empty-state">
        <div class="empty-icon">📭</div>
        <h2>生词本空空如也</h2>
        <p>快去工作台中划词翻译，将遇到的拦路虎收入囊中吧！</p>
        <button class="primary-btn" style="width: auto; padding: 10px 24px; margin-top: 15px;" @click="goToDashboard">去阅读文章</button>
      </div>

      <div v-else class="vocab-grid">
        <div v-for="vocab in vocabList" :key="vocab.id" class="vocab-card" :class="{ 'mastered': vocab.status === 'MASTERED' }">
          
          <div class="card-top">
            <h2 class="word">{{ vocab.word }}</h2>
            <span class="phonetic">/{{ vocab.word }}/</span>
          </div>

          <div class="card-translation">
            {{ vocab.translation }}
          </div>

          <div class="card-context">
            <span class="context-label">语境回忆：</span>
            <p class="context-sentence">
              {{ vocab.contextSentence }}
            </p>
          </div>

          <div class="card-actions">
            <button class="action-icon-btn check-btn" title="标为已掌握" @click="markMastered(vocab)">
              ✅ 掌握
            </button>
            <button class="action-icon-btn delete-btn" title="移除生词" @click="removeVocab(vocab.id)">
              🗑️ 移除
            </button>
          </div>

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

// 初始化加载生词列表
const fetchVocabularies = async () => {
  isLoading.value = true
  try {
    const res = await getVocabularyListApi()
    vocabList.value = res || []
  } catch (error) {
    console.error('获取生词本失败', error)
    // 【开发阶段模拟数据】如果你后端还没跑通，取消下面这行的注释看看效果
    // mockData() 
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchVocabularies()
})

// 导航跳转
const goToDashboard = () => {
  router.push('/dashboard')
}

// 移除生词
const removeVocab = async (id) => {
  if (!confirm('确定要将该生词移出你的专属词库吗？')) return
  
  try {
    await deleteVocabularyApi(id)
    // 前端无刷新删除
    vocabList.value = vocabList.value.filter(v => v.id !== id)
  } catch (error) {
    alert('移除失败！')
  }
}

// 标为已掌握
const markMastered = async (vocab) => {
  try {
    // 假设后端接受 status 字段
    await updateVocabularyStatusApi(vocab.id, 'MASTERED')
    vocab.status = 'MASTERED'
  } catch (error) {
    alert('状态更新失败！')
  }
}

// 模拟数据 (如果后端还没准备好，可以解除上方的 mockData() 注释看 UI 效果)
const mockData = () => {
  vocabList.value = [
    { id: 1, word: 'harness', translation: 'v. 利用，控制；给...套上挽具\nn. 马具，挽具', contextSentence: 'Quantum computing is a rapidly-emerging technology that harnesses the laws of quantum mechanics.', status: 'LEARNING' },
    { id: 2, word: 'emerging', translation: 'adj. 新兴的，出现的', contextSentence: 'Quantum computing is a rapidly-emerging technology.', status: 'LEARNING' },
    { id: 3, word: 'mechanics', translation: 'n. 力学，机械学；基本原理', contextSentence: '...harnesses the laws of quantum mechanics to solve problems...', status: 'MASTERED' }
  ]
}
</script>

<style scoped>
/* =========== 基础布局样式 (同 Dashboard) =========== */
.dashboard-layout { display: flex; height: 100vh; width: 100vw; background-color: #f3f4f6; font-family: -apple-system, sans-serif; overflow: hidden;}
.mobile-header { display: none; }
.sidebar { width: 80px; background-color: #ffffff; border-right: 1px solid #e5e7eb; display: flex; flex-direction: column; justify-content: space-between; align-items: center; padding: 24px 0; flex-shrink: 0; z-index: 10;}
.logo-box { width: 40px; height: 40px; background-color: #111827; color: #ffffff; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-weight: 800; cursor: pointer; }
.nav-menu { display: flex; flex-direction: column; gap: 24px; width: 100%; }
.nav-item { display: flex; flex-direction: column; align-items: center; gap: 6px; color: #9ca3af; cursor: pointer; }
.nav-icon { width: 24px; height: 24px; }
.nav-item span { font-size: 12px; font-weight: 500; }
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

/* =========== 网格卡片系统 =========== */
.vocab-grid {
  display: grid;
  /* 响应式网格：最少 300px 宽，自动填充剩余空间 */
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
  padding-bottom: 40px;
}

.vocab-card {
  background: #ffffff;
  border-radius: 16px;
  border: 1px solid #e5e7eb;
  padding: 20px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
  transition: transform 0.2s, box-shadow 0.2s;
}
.vocab-card:hover { transform: translateY(-3px); box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1); }
/* 掌握状态的样式变灰 */
.vocab-card.mastered { opacity: 0.7; background: #f9fafb; border-color: #d1d5db; }

.card-top { display: flex; align-items: baseline; gap: 10px; margin-bottom: 10px; }
.word { font-size: 24px; font-weight: 800; color: #111827; margin: 0; }
.phonetic { font-size: 14px; color: #9ca3af; font-family: monospace; }

.card-translation { font-size: 15px; color: #4b5563; line-height: 1.5; white-space: pre-wrap; margin-bottom: 15px; flex: 1;}

.card-context { background: #f8fafc; padding: 12px; border-radius: 8px; border-left: 3px solid #cbd5e1; margin-bottom: 15px; }
.context-label { font-size: 12px; color: #64748b; font-weight: bold; display: block; margin-bottom: 5px; }
.context-sentence { font-size: 13.5px; color: #475569; font-style: italic; margin: 0; line-height: 1.5; }

.card-actions { display: flex; gap: 10px; border-top: 1px solid #f3f4f6; padding-top: 15px; }
.action-icon-btn { flex: 1; padding: 8px; border-radius: 8px; font-size: 13px; font-weight: 600; cursor: pointer; border: none; transition: 0.2s; display: flex; align-items: center; justify-content: center; gap: 5px;}
.check-btn { background: #f0fdf4; color: #16a34a; }
.check-btn:hover { background: #dcfce7; }
.delete-btn { background: #fef2f2; color: #dc2626; }
.delete-btn:hover { background: #fee2e2; }

/* 全局按钮 */
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
  
  /* 手机端一列展示 */
  .vocab-grid { grid-template-columns: 1fr; gap: 15px; }
}
</style>