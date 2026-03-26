// src/api/vocabulary.js
import request from './request'

// 1. 获取当前用户的生词列表
export const getVocabularyListApi = () => {
  return request({
    url: '/api/vocabulary/list',
    method: 'get'
  })
}

// 2. 移除生词
export const deleteVocabularyApi = (id) => {
  return request({
    url: `/api/vocabulary/delete/${id}`,
    method: 'delete'
  })
}

// 3. (预留) 更新生词状态，比如标为“已掌握”
export const updateVocabularyStatusApi = (id, status) => {
  return request({
    url: '/api/vocabulary/update',
    method: 'put',
    data: { id, status }
  })
}