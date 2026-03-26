import request from './request'

// 1. 生成 i+1 文章
export const generateArticleApi = (data) => {
  return request({
    url: '/api/article/generate',
    method: 'post',
    data // 包含 difficultyLevel, originalText
  })
}

// 2. 划词翻译
export const translateWordApi = (data) => {
  return request({
    url: '/api/article/translate',
    method: 'post',
    data // 包含 word, contextSentence
  })
}

// 3. (预留) 加入生词本
export const addVocabularyApi = (data) => {
  return request({
    url: '/api/vocabulary/add',
    method: 'post',
    data
  })
}