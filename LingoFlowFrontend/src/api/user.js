import request from './request'

// 1. 登录接口
export const loginApi = (data) => {
  return request({ url: '/api/user/login', method: 'post', data })
}

// 2. 注册接口 【新增】
export const registerApi = (data) => {
  return request({
    url: '/api/user/register',
    method: 'post',
    data 
  })
}

// 3. 找回密码 (预留)
export const forgotPasswordApi = (email) => {
  return request({ url: '/api/user/forgot-password', method: 'post', data: { email } })
}

// 4. 修改密码 (预留)
export const changePasswordApi = (data) => {
  return request({ url: '/api/user/change-password', method: 'put', data })
}