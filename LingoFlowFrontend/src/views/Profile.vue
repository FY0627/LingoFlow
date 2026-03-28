<template>
  <div class="dashboard-layout">
    
    <div class="mobile-header">
      <div class="logo-box">LF</div>
      <span class="mobile-title">用户中心</span>
    </div>

    <div class="sidebar">
      <div class="top-section">
        <div class="logo-box desktop-only" @click="router.push('/dashboard')">LF</div>
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
        </div>
      </div>
      <div class="bottom-section desktop-only">
        <div class="avatar active">👨‍💻</div>
      </div>
    </div>

    <div class="main-content">
      <div class="page-header">
        <h1 class="page-title">👤 个人中心</h1>
        <p class="page-subtitle">管理你的学习数据资产与账号安全。</p>
      </div>

      <div class="profile-cards-container">
        
        <div class="profile-card">
          <div class="card-title-row">
            <h2>📦 数据资产管理</h2>
          </div>
          <p class="card-desc">数据主权归你所有。你可以随时将本地的生词本、测试记录和阅读历史导出为 JSON 格式备份，或在其他设备上导入恢复。</p>
          
          <div class="action-buttons">
            <button class="primary-btn" @click="handleExportData">📤 一键导出我的数据</button>
            <button class="outline-btn" @click="$refs.fileInput.click()">📥 导入本地 JSON 数据</button>
            <input type="file" ref="fileInput" accept=".json" style="display: none" @change="handleImportData" />
          </div>
        </div>

        <div class="profile-card">
          <div class="card-title-row">
            <h2>🔒 账号安全</h2>
          </div>
          <p class="card-desc">当前账号状态正常。退出登录后，需重新验证密码才能访问你的学习舱。</p>
          
          <div class="action-buttons">
            <button class="danger-btn" @click="handleLogout">退出登录</button>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { importDataApi, downloadUserData } from '../api/user'

const router = useRouter()
const fileInput = ref(null)

// 导出逻辑
const handleExportData = async () => {
  alert('🚀 正在连接记忆宫殿，准备打包您的学习数据...')
  const success = await downloadUserData()
  if (!success) alert('❌ 导出失败，请检查后端服务。')
}

// 导入逻辑
const handleImportData = async (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  const formData = new FormData()
  formData.append('file', file)

  alert('正在将您的记忆档案上传至服务器...')
  try {
    await importDataApi(formData)
    alert('✅ 数据导入成功！')
  } catch (error) {
    alert('❌ 导入失败，请检查文件格式或后端接口。')
  } finally {
    event.target.value = '' // 清空 input
  }
}

// 退出登录逻辑
const handleLogout = () => {
  if (confirm('确定要退出 LingoFlow 吗？')) {
    localStorage.removeItem('lingoflow_token') // 清除登录令牌
    router.push('/login')
  }
}
</script>

<style scoped>
/* =========== 基础布局样式 (与其它页面保持一致) =========== */
.dashboard-layout { display: flex; height: 100vh; width: 100vw; background-color: #f3f4f6; font-family: -apple-system, sans-serif; overflow: hidden;}
.mobile-header { display: none; }

/* 🎯 重点看这里：重构后的侧边栏间距 */
.sidebar { width: 80px; background-color: #ffffff; border-right: 1px solid #e5e7eb; display: flex; flex-direction: column; justify-content: space-between; align-items: center; padding: 24px 0; flex-shrink: 0; z-index: 10;}
.top-section { display: flex; flex-direction: column; align-items: center; width: 100%; }

/* 给 Logo 底部增加大间距，拉开与选项卡的距离 */
.logo-box { width: 40px; height: 40px; background-color: #111827; color: #ffffff; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-weight: 800; cursor: pointer; margin-bottom: 60px; }
/* 增加选项卡之间的 gap */
.nav-menu { display: flex; flex-direction: column; gap: 36px; width: 100%; }

.nav-item { display: flex; flex-direction: column; align-items: center; gap: 8px; color: #9ca3af; cursor: pointer; transition: 0.2s;}
.nav-icon { width: 24px; height: 24px; }
.nav-item span { font-size: 12px; font-weight: 500; }
.nav-item:hover { color: #4b5563; }
.nav-item.active { color: #111827; }

/* 头像加上 hover 动画和激活状态 */
.avatar { width: 40px; height: 40px; background-color: #f3f4f6; border-radius: 50%; display: flex; align-items: center; justify-content: center; cursor: pointer; transition: 0.2s; border: 2px solid transparent;}
.avatar:hover, .avatar.active { border-color: #111827; transform: scale(1.05); }

/* =========== 主内容区：用户中心专属样式 =========== */
.main-content { flex: 1; display: flex; flex-direction: column; padding: 40px 60px; overflow-y: auto; }
.page-header { margin-bottom: 40px; }
.page-title { font-size: 32px; font-weight: 800; color: #111827; margin: 0 0 10px 0; }
.page-subtitle { font-size: 15px; color: #6b7280; margin: 0; }

.profile-cards-container { display: flex; flex-direction: column; gap: 30px; max-width: 800px; }
.profile-card { background: #ffffff; border-radius: 16px; border: 1px solid #e5e7eb; padding: 30px; display: flex; flex-direction: column; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.02); }
.card-title-row h2 { font-size: 20px; font-weight: 700; color: #111827; margin: 0 0 15px 0; }
.card-desc { font-size: 15px; color: #4b5563; line-height: 1.6; margin: 0 0 25px 0; }

.action-buttons { display: flex; gap: 15px; }
.primary-btn { padding: 12px 24px; background: #111827; color: white; border-radius: 8px; border: none; font-weight: 600; cursor: pointer; transition: 0.2s; }
.primary-btn:hover { background: #374151; }
.outline-btn { padding: 12px 24px; background: white; color: #111827; border-radius: 8px; border: 1px solid #111827; font-weight: 600; cursor: pointer; transition: 0.2s; }
.outline-btn:hover { background: #f9fafb; }
.danger-btn { padding: 12px 24px; background: #fef2f2; color: #dc2626; border-radius: 8px; border: 1px solid #fecaca; font-weight: 600; cursor: pointer; transition: 0.2s; }
.danger-btn:hover { background: #fee2e2; border-color: #f87171; }

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

  .main-content { padding: 20px; padding-bottom: 90px; }
  .action-buttons { flex-direction: column; }
  .action-buttons button { width: 100%; }
}
</style>