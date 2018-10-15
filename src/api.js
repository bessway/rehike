import axios from 'axios'
// 1. 不带{}, 引入的是module中export default的函数或变量；
// 2. 带{}，引入的是module中export的函数或变量；
import {Message, Loading} from 'element-ui'

export {
  getChildTests,
  getTestDetail,
  getActions,
  getUIObject,
  getAction,
  getTestParas,
  getUIObjectByXpath,
  createUIObject,
  getUIPages,
  getUIObjectsByPage
}
var loadCount = 0
axios.defaults.baseURL = process.env.API_BASE + '/api/v2'

axios.interceptors.request.use(
  config => {
    console.log(config.url)
    loadCount++
    // Loading.service({ fullscreen: true })
    if (this.loadingInstance === undefined) {
      // 找id=root的节点
      this.loadingInstance = Loading.service({
        fullscreen: true, target: '#root', background: 'rgba(0, 0, 0, 0.7)'
      })
    }
    return config
  },
  err => {
    loadCount--
    if (loadCount < 1) {
      // Loading.service().close()
      this.loadingInstance.close()
      this.loadingInstance = undefined
    }
    Message.error({message: '请求失败!'})
    return Promise.reject(err)
  }
)

axios.interceptors.response.use(
  res => {
    loadCount--
    if (loadCount < 1) {
      // Loading.service().close()
      this.loadingInstance.close()
      this.loadingInstance = undefined
    }
    if (res.status && res.status === 200 && res.data.status === 'failed') {
      Message.error({message: res.data.msg})
      return
    }
    return res.data
  },
  err => {
    loadCount--
    if (loadCount < 1) {
      // Loading.service().close()
      this.loadingInstance.close()
      this.loadingInstance = undefined
    }
    if (err.response && (err.response.status === 504 || err.response.status === 404 || err.response.status === 500)) {
      Message.error({message: '服务器错误'})
    } else if (err.response && err.response.status === 401) {
      Message.error({message: '账号密码不对'})
    } else if (err.response && err.response.status === 403) {
      Message.error({message: '权限不足,请联系管理员'})
    } else {
      Message.error({message: '未知错误!'})
    }
    return Promise.reject(err)
  }
)

const postRequest = (path, params) => {
  return axios({
    method: 'post',
    url: path,
    data: params,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}
/*
const uploadFileRequest = (path, params) => {
  return axios({
    method: 'post',
    url: path,
    data: params,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
const putRequest = (path, params) => {
  return axios({
    method: 'put',
    url: path,
    data: params,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}
const deleteRequest = (path) => {
  return axios({
    method: 'delete',
    url: path
  })
}
*/
const getRequest = (path) => {
  return axios({
    method: 'get',
    url: path
  })
}

function getChildTests (parentId) {
  return getRequest('/tests/' + parentId).then(
    function (data) {
      return data.tests
    }
  )
}

function getTestDetail (testId) {
  return getRequest('/test/' + testId).then(
    function (data) {
      return data
    }
  )
}
function getTestParas (testId) {
  return getRequest('/testparas/' + testId).then(
    function (data) {
      return data.para
    }
  )
}

function getUIObject (uiObjectId) {
  return getRequest('/uiobject/' + uiObjectId).then(
    function (data) {
      return data
    }
  )
}

function getAction (actionId) {
  return getRequest('/action/' + actionId).then(
    function (data) {
      return data
    }
  )
}

function getActions () {
  return getRequest('/actions').then(
    function (data) {
      return data.action
    }
  )
}

function getUIObjectByXpath (xpath) {
  return getRequest('/uiobject/path/' + xpath).then(
    function (data) {
      return data
    }
  )
}

function createUIObject (uiobject) {
  return postRequest('/uiobject', uiobject).then(
    function (data) {
      return data
    }
  )
}

function getUIPages () {
  return getRequest('/uipages').then(
    function (data) {
      return data.page
    }
  )
}

function getUIObjectsByPage (page) {
  return getRequest('/uiobjects/page/' + page).then(
    function (data) {
      return data.uiobject
    }
  )
}
