<template>
  <div class="dashboard-layout">
    
    <div class="mobile-header">
      <div class="logo-box export-btn" @click="handleExportData">LF</div>
      <span class="mobile-title">LingoFlow 工作台</span>
    </div>

    <div class="sidebar">
      <div class="top-section">
        <div class="logo-box export-btn desktop-only" title="点击导出个人数据" @click="handleExportData">LF</div>
        <div class="nav-menu">
          <div class="nav-item active">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path></svg>
            <span>工作台</span>
          </div>
          <div class="nav-item" @click="goToVocabulary">
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
      
      <div class="editor-section">
        
        <div class="editor-card">
          <div class="card-header flex-between">
            <span class="card-title">1. 输入原文</span>
            
            <div class="rewrite-actions" v-if="!isMobile">
              <select v-model="difficulty" class="mini-select">
                <optgroup label="国内考试">
                  <option value="中考英语水平">中考英语</option>
                  <option value="高考英语水平">高考英语</option>
                  <option value="大学英语四级(CET4)">CET 4 (四级)</option>
                  <option value="大学英语六级(CET6)">CET 6 (六级)</option>
                  <option value="考研英语水平">考研英语</option>
                  <option value="英语专业八级(TEM8)">专八 (TEM8)</option>
                </optgroup>
                <optgroup label="PTE 考试">
                  <option value="PTE 45分水平">PTE 45分 (基础)</option>
                  <option value="PTE 58分水平">PTE 58分 (常用)</option>
                  <option value="PTE 65分水平">PTE 65分 (进阶)</option>
                  <option value="PTE 79分水平">PTE 79分 (高阶)</option>
                  <option value="PTE 90分水平">PTE 90分 (满分)</option>
                </optgroup>
                <optgroup label="雅思 (IELTS)">
                  <option value="雅思 5.5分水平">雅思 5.5分</option>
                  <option value="雅思 6.0分水平">雅思 6.0分</option>
                  <option value="雅思 6.5分水平">雅思 6.5分</option>
                  <option value="雅思 7.0分水平">雅思 7.0分</option>
                  <option value="雅思 8.0分水平">雅思 8.0分</option>
                </optgroup>
                <optgroup label="托福 (TOEFL)">
                  <option value="托福 80分水平">托福 80分</option>
                  <option value="托福 90分水平">托福 90分</option>
                  <option value="托福 100分水平">托福 100分</option>
                  <option value="托福 110分水平">托福 110+分</option>
                </optgroup>
              </select>
              <button class="action-btn" :disabled="isGenerating" @click="handleRewrite">
                {{ isGenerating ? 'AI重写中...' : '开始 i+1 重写' }}
              </button>
            </div>
          </div>

          <div class="mobile-rewrite-actions" v-if="isMobile">
            <select v-model="difficulty" class="mobile-select">
              <optgroup label="国内考试">
                <option value="中考英语水平">中考英语</option>
                <option value="高考英语水平">高考英语</option>
                <option value="大学英语四级(CET4)">CET 4 (四级)</option>
                <option value="大学英语六级(CET6)">CET 6 (六级)</option>
                <option value="考研英语水平">考研英语</option>
                <option value="英语专业八级(TEM8)">专八 (TEM8)</option>
              </optgroup>
              <optgroup label="PTE 考试">
                <option value="PTE 45分水平">PTE 45分 (基础)</option>
                <option value="PTE 58分水平">PTE 58分 (常用)</option>
                <option value="PTE 65分水平">PTE 65分 (进阶)</option>
                <option value="PTE 79分水平">PTE 79分 (高阶)</option>
                <option value="PTE 90分水平">PTE 90分 (满分)</option>
              </optgroup>
              <optgroup label="雅思 (IELTS)">
                <option value="雅思 5.5分水平">雅思 5.5分</option>
                <option value="雅思 6.0分水平">雅思 6.0分</option>
                <option value="雅思 6.5分水平">雅思 6.5分</option>
                <option value="雅思 7.0分水平">雅思 7.0分</option>
                <option value="雅思 8.0分水平">雅思 8.0分</option>
              </optgroup>
              <optgroup label="托福 (TOEFL)">
                <option value="托福 80分水平">托福 80分</option>
                <option value="托福 90分水平">托福 90分</option>
                <option value="托福 100分水平">托福 100分</option>
                <option value="托福 110分水平">托福 110+分</option>
              </optgroup>
            </select>
            <button class="mobile-action-btn" :disabled="isGenerating" @click="handleRewrite">
              {{ isGenerating ? '重写中...' : '开始 i+1' }}
            </button>
          </div>

          <textarea class="textarea-input" v-model="originalText" placeholder="请在此粘贴你需要阅读的英文文章..."></textarea>
        </div>

        <div class="editor-card result-card">
          <div class="card-header">
            <span class="card-title">2. i+1 专属文章 (长按选词翻译)</span>
          </div>
          <textarea class="textarea-input result-area" v-model="resultText" readonly @mouseup="handleTextSelection" @touchend="handleTextSelection" placeholder="文章将展示在这里..."></textarea>
        </div>
        
      </div>

      <div class="tools-section">
        <div class="tools-card">
          
          <div class="tab-header desktop-only">
            <div class="tab-item" :class="{ active: activeTab === 'dict' }" @click="activeTab = 'dict'">划词翻译</div>
            <div class="tab-item" :class="{ active: activeTab === 'quiz' }" @click="activeTab = 'quiz'">AI 出题</div>
          </div>

          <div class="tab-content desktop-only" v-show="activeTab === 'dict'">
            <div v-if="!selectedWord" class="empty-state">
              <div class="empty-icon">🖱️</div>
              <p>请在左侧改写后的文章中<br/>划选任意你不懂的单词</p>
            </div>
            <div v-else class="dict-result">
              <div class="word-header">
                <h2 class="word-text">{{ selectedWord }}</h2>
              </div>
              <div class="translation-box">
                <p class="trans-item" style="white-space: pre-wrap; line-height: 1.6;">
                  {{ isTranslating ? 'AI 翻译中...' : translationResult }}
                </p>
              </div>
              <div class="context-box">
                <span class="context-label">原句：</span>
                <p class="context-text">{{ contextSentence }}</p>
              </div>
              <button class="add-vocab-btn" @click="handleAddVocab" :disabled="isTranslating">➕ 加入生词本</button>
            </div>
          </div>

          <div class="tab-content" v-show="!isMobile || (isMobile && true)" :style="isMobile ? 'padding: 15px;' : ''">
            <h3 v-if="isMobile" class="mobile-section-title">3. 课后自测</h3>
            
            <div v-if="quizList.length === 0" class="empty-state">
              <div class="empty-icon">📝</div>
              <p>阅读完成后生成 3 道理解题</p>
              <button class="primary-btn" @click="generateQuiz" :disabled="isGeneratingQuiz">
                {{ isGeneratingQuiz ? '出题中...' : '生成课后自测' }}
              </button>
            </div>

            <div v-else class="quiz-container">
              <div v-if="quizFeedback" class="score-board">
                <div class="score-number">{{ quizFeedback.score }}</div>
                <div class="score-text">Total Score</div>
              </div>

              <div class="quiz-list">
                <div v-for="(item, index) in quizList" :key="index" class="quiz-item">
                  <p class="question">{{ index + 1 }}. {{ item.question }}</p>
                  <div class="options">
                    <label v-for="(opt, oIndex) in item.options" :key="oIndex" class="option-label" :class="getOptionClass(index, opt)">
                      <input type="radio" :name="'q'+index" :value="opt" v-model="userAnswers[index]" :disabled="quizFeedback !== null" />
                      <span class="opt-text">{{ opt }}</span>
                    </label>
                  </div>
                  <div v-if="quizFeedback" class="feedback-box" :class="quizFeedback.feedbacks[index].isCorrect ? 'is-correct' : 'is-wrong'">
                    <div class="explanation eng">📝 {{ quizFeedback.feedbacks[index].englishExplanation }}</div>
                    <div class="explanation chs">💡 {{ quizFeedback.feedbacks[index].explanation }}</div>
                  </div>
                </div>
              </div>

              <button v-if="!quizFeedback" class="primary-btn submit-btn" :disabled="isGrading" @click="submitQuiz">
                {{ isGrading ? 'AI 批阅中...' : '提交答案' }}
              </button>
              <button v-else class="outline-btn submit-btn" @click="generateQuiz" :disabled="isGeneratingQuiz">
                再来一组新题
              </button>
            </div>
          </div>

        </div>
      </div>
    </div>

    <div v-if="isMobile && showMobileDict" class="mobile-dict-popup" :style="mobilePopupStyle">
      <div class="popup-header">
        <h2 class="word-text">{{ selectedWord }}</h2>
        <button class="close-btn" @click="showMobileDict = false">✖</button>
      </div>
      <div class="translation-box">
        <p class="trans-item" style="white-space: pre-wrap; line-height: 1.6;">
          {{ isTranslating ? 'AI 翻译中...' : translationResult }}
        </p>
      </div>
      <button class="add-vocab-btn mobile-add-btn" @click="handleAddVocab" :disabled="isTranslating">➕ 存入生词本</button>
    </div>
    <div v-if="isMobile && showMobileDict" class="mobile-overlay" @click="showMobileDict = false" @touchstart="showMobileDict = false"></div>

  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { generateArticleApi, translateWordApi, addVocabularyApi } from '../api/article'
import { generateQuizApi, submitQuizApi } from '../api/quiz'

const router = useRouter()

const isMobile = ref(false)
const checkMobile = () => { isMobile.value = window.innerWidth <= 768 }

const showMobileDict = ref(false)
const mobilePopupStyle = ref({})

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})
onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})

const activeTab = ref('dict')
const isGenerating = ref(false)
const originalText = ref('')
const resultText = ref('')
const difficulty = ref('PTE 65分水平')
const currentArticleId = ref(null) 

const selectedWord = ref('')
const contextSentence = ref('')
const translationResult = ref('')
const isTranslating = ref(false)

const quizList = ref([])         
const userAnswers = ref({})      
const quizFeedback = ref(null)   
const isGeneratingQuiz = ref(false)
const isGrading = ref(false)

const goToVocabulary = () => {
  router.push('/vocabulary')
}

const handleRewrite = async () => {
  if (!originalText.value.trim()) return alert('请先输入原文！')
  isGenerating.value = true
  resultText.value = '🚀 正在重写...'
  quizList.value = []
  quizFeedback.value = null
  userAnswers.value = {}
  
  try {
    const res = await generateArticleApi({ difficultyLevel: difficulty.value, originalText: originalText.value })
    resultText.value = res.adaptedText
    currentArticleId.value = res.id 
  } catch (error) {
    resultText.value = '生成失败。'
  } finally {
    isGenerating.value = false
  }
}

const handleTextSelection = async (e) => {
  const textarea = e.target
  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  
  if (start !== end) {
    const text = textarea.value.substring(start, end).trim()
    if (text && /^[a-zA-Z\s-]+$/.test(text)) {
      selectedWord.value = text
      isTranslating.value = true
      
      const fullText = textarea.value
      const textBefore = fullText.substring(0, start)
      const textAfter = fullText.substring(end)
      const sentenceStart = Math.max(textBefore.lastIndexOf('.'), textBefore.lastIndexOf('?'), textBefore.lastIndexOf('\n')) + 1
      const sentenceEndIdx = textAfter.search(/[\.\?\n]/)
      const sentenceEnd = sentenceEndIdx !== -1 ? end + sentenceEndIdx + 1 : fullText.length
      contextSentence.value = fullText.substring(sentenceStart, sentenceEnd).trim()

      if (isMobile.value) {
        const clientY = e.changedTouches ? e.changedTouches[0].clientY : e.clientY
        const safeY = Math.min(clientY + 10, window.innerHeight - 200)
        mobilePopupStyle.value = { top: `${safeY}px` }
        showMobileDict.value = true
      } else {
        activeTab.value = 'dict'
      }
      
      try {
        const res = await translateWordApi({ word: selectedWord.value, contextSentence: contextSentence.value })
        translationResult.value = res 
      } catch (error) {
        translationResult.value = '翻译请求失败。'
      } finally {
        isTranslating.value = false
      }
    }
  }
}

const handleAddVocab = async () => {
  if (!currentArticleId.value) return alert('请先生成文章ID')
  try {
    await addVocabularyApi({ articleId: currentArticleId.value, word: selectedWord.value, translation: translationResult.value, contextSentence: contextSentence.value })
    alert('🎉 已存入生词本！')
    if(isMobile.value) showMobileDict.value = false 
  } catch (error) {}
}

const generateQuiz = async () => {
  if (!currentArticleId.value) return alert('请先生成文章！')
  isGeneratingQuiz.value = true
  quizList.value = []
  quizFeedback.value = null
  userAnswers.value = {} 
  try {
    const res = await generateQuizApi(currentArticleId.value)
    quizList.value = res 
  } catch (error) { alert('生成题目失败！') } 
  finally { isGeneratingQuiz.value = false }
}

const submitQuiz = async () => {
  if (Object.keys(userAnswers.value).length < quizList.value.length) return alert('请答完所有题！')
  isGrading.value = true
  try {
    const answersArray = quizList.value.map((q, index) => ({
      question: q.question, originalOptions: q.options, selectedOption: userAnswers.value[index]
    }))
    const res = await submitQuizApi({ articleId: currentArticleId.value, userAnswers: answersArray })
    
    const correctCount = res.feedbacks.filter(f => f.isCorrect).length
    res.score = Math.round((correctCount / res.feedbacks.length) * 100)
    quizFeedback.value = res 
  } catch (error) { alert('交卷失败！') } 
  finally { isGrading.value = false }
}

const getOptionClass = (qIndex, optionText) => {
  const isSelected = userAnswers.value[qIndex] === optionText
  if (!quizFeedback.value) return isSelected ? 'selected-choice' : ''
  if (isSelected) return quizFeedback.value.feedbacks[qIndex].isCorrect ? 'correct-choice' : 'wrong-choice'
  return 'disabled-choice'
}

const handleExportData = () => alert('🚀 准备下载...')
</script>

<style scoped>
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

.main-content { flex: 1; display: flex; padding: 24px; gap: 24px; overflow: hidden; }
.editor-section { flex: 1; display: flex; flex-direction: column; gap: 24px; min-width: 0; min-height: 0; }
.editor-card { flex: 1; background-color: #ffffff; border-radius: 16px; border: 1px solid #e5e7eb; display: flex; flex-direction: column; overflow: hidden; min-height: 0; }

.result-card { border: 2px solid #e5e7eb; }
.card-header { padding: 16px 20px; border-bottom: 1px solid #f3f4f6; }
.flex-between { display: flex; justify-content: space-between; align-items: center; }
.card-title { font-size: 15px; font-weight: 600; color: #111827; }
.rewrite-actions { display: flex; gap: 8px; }
.mini-select { padding: 6px 10px; border-radius: 6px; border: 1px solid #d1d5db; font-size: 13px; background: #f9fafb; outline:none; max-width: 140px;}
.action-btn { padding: 6px 14px; background: #111827; color: white; border: none; border-radius: 6px; font-size: 13px; font-weight: 600; cursor: pointer; }

.textarea-input { flex: 1; width: 100%; border: none; resize: none; padding: 20px; font-size: 16px; line-height: 1.8; color: #374151; background: transparent; outline: none; box-sizing: border-box; overflow-y: auto; }
.result-area { background-color: #fafafa; font-family: 'Georgia', serif; font-size: 17px; }

.tools-section { width: 360px; flex-shrink: 0; display: flex; flex-direction: column; }
.tools-card { background-color: #ffffff; border-radius: 16px; border: 1px solid #e5e7eb; display: flex; flex-direction: column; height: 100%; overflow: hidden;}
.tab-header { display: flex; border-bottom: 1px solid #e5e7eb; background-color: #f9fafb; }
.tab-item { flex: 1; text-align: center; padding: 16px 0; font-size: 14px; font-weight: 600; color: #6b7280; cursor: pointer; border-bottom: 2px solid transparent; }
.tab-item.active { color: #111827; background-color: #ffffff; border-bottom: 2px solid #111827; }
.tab-content { padding: 24px; display: flex; flex-direction: column; height: 100%; overflow-y: auto; }

.empty-state { display: flex; flex-direction: column; align-items: center; text-align: center; color: #9ca3af; gap: 15px; margin-top: 50px;}
.empty-icon { font-size: 40px; }
.dict-result { display: flex; flex-direction: column; gap: 15px; }
.word-text { font-size: 24px; font-weight: 800; color: #111827; margin: 0; }
.trans-item { font-size: 15px; color: #374151; margin: 5px 0; }
.context-box { background: #f8fafc; padding: 10px; border-radius: 8px; border-left: 3px solid #cbd5e1; }
.context-label { font-size: 12px; color: #64748b; font-weight: bold; }
.context-text { font-size: 13px; color: #475569; font-style: italic; margin: 5px 0 0 0;}
.add-vocab-btn { padding: 10px; border: 1px solid #e5e7eb; background: white; border-radius: 8px; font-weight: 600; cursor: pointer; }

.quiz-container { display: flex; flex-direction: column; gap: 20px; }
.score-board { background: linear-gradient(135deg, #111827, #374151); color: white; border-radius: 12px; padding: 20px; text-align: center; }
.score-number { font-size: 48px; font-weight: 900; margin-bottom: 5px; }
.quiz-list { display: flex; flex-direction: column; gap: 30px; }
.quiz-item { display: flex; flex-direction: column; gap: 10px; }
.question { font-weight: 600; font-size: 15px; color: #111827; margin: 0; line-height: 1.4;}
.options { display: flex; flex-direction: column; gap: 8px; }
.option-label { display: flex; gap: 10px; align-items: flex-start; font-size: 14px; padding: 12px; border: 1px solid #e5e7eb; border-radius: 8px; cursor: pointer; }
.selected-choice { border-color: #111827; background: #f8fafc; color: #111827; font-weight: 500;}
.correct-choice { border-color: #10b981; background: #f0fdf4; color: #047857; font-weight: 600; }
.wrong-choice { border-color: #ef4444; background: #fef2f2; color: #b91c1c; font-weight: 600; }
.feedback-box { padding: 12px; border-radius: 8px; display: flex; flex-direction: column; gap: 8px;}
.feedback-box.is-correct { background-color: #f0fdf4; border-left: 4px solid #10b981; }
.feedback-box.is-wrong { background-color: #fef2f2; border-left: 4px solid #ef4444; }
.explanation { font-size: 13px; line-height: 1.5; }
.explanation.eng { color: #111827; font-weight: 500; font-style: italic;}
.explanation.chs { color: #4b5563; }
.primary-btn { padding: 12px; background: #111827; color: white; border-radius: 8px; border: none; font-weight: 600; cursor: pointer; width: 100%;}
.outline-btn { padding: 12px; background: white; color: #111827; border-radius: 8px; border: 1px solid #111827; font-weight: 600; cursor: pointer; width: 100%;}

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

  .main-content { flex-direction: column; overflow: visible; padding: 15px; padding-bottom: 80px; gap: 15px;}
  
  .mobile-rewrite-actions { display: flex; gap: 10px; padding: 0 15px 15px 15px; border-bottom: 1px solid #f3f4f6;}
  .mobile-select { flex: 2; padding: 12px; border-radius: 8px; border: 1px solid #d1d5db; font-size: 14px; background: #f9fafb; outline:none; }
  .mobile-action-btn { flex: 1; padding: 12px; background: #111827; color: white; border: none; border-radius: 8px; font-size: 14px; font-weight: 600; cursor: pointer; }

  .editor-card { flex: none; min-height: 250px; }
  .textarea-input { padding: 15px; font-size: 15px; }

  .tools-section { width: 100%; }
  .tools-card { border: none; box-shadow: none; background: transparent; }
  .mobile-section-title { font-size: 16px; margin: 10px 0 15px 0; color: #111;}
}

.mobile-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; z-index: 99; }
.mobile-dict-popup { position: fixed; left: 5%; width: 90%; background: white; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,0,0.2); padding: 15px; z-index: 100; border: 1px solid #e5e7eb; animation: slideUp 0.2s ease-out; box-sizing: border-box; }
@keyframes slideUp { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
.popup-header { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #e5e7eb; padding-bottom: 10px; margin-bottom: 10px;}
.popup-header .word-text { font-size: 20px; margin: 0; }
.close-btn { background: none; border: none; font-size: 18px; color: #9ca3af; padding: 0;}
.mobile-add-btn { width: 100%; margin-top: 15px; background: #111; color: white;}
</style>