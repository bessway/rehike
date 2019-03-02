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
  getTestParasAll,
  getUIObjectByXpath,
  createUIObject,
  getUIPages,
  getUIObjectsByPage,
  createTest,
  copyTest,
  searchTest,
  createTestPara,
  copyFormalParas,
  setFormalParas,
  updateTest,
  setParasValue,
  getRefStepParas,
  getAgents,
  startJob,
  getTasks,
  delStepsFormalParas,
  updateParaName
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
      Message.error({message: res.data.error})
      return
    }
    console.log(res.data.data)
    return res.data.data
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
      return data
    }
  )
}

function getTestDetail (testId) {
  return getRequest('/tests/testdetail/' + testId).then(
    function (data) {
      return data
    }
  )
}
function getTestParasAll (testId, dataVersion) {
  return getRequest('/paras/test/' + testId + '/version/' + dataVersion + '/all').then(
    function (data) {
      return data
    }
  )
}

function getUIObject (uiObjectId) {
  return getRequest('/objects/uiobject/' + uiObjectId).then(
    function (data) {
      return data
    }
  )
}

function getAction (actionId) {
  return getRequest('/actions/' + actionId).then(
    function (data) {
      return data
    }
  )
}

function getActions () {
  return getRequest('/actions/all').then(
    function (data) {
      return data
    }
  )
}

function getUIObjectByXpath (xpath) {
  return postRequest('/objects/path', xpath).then(
    function (data) {
      return data
    }
  )
}

function createTest (test) {
  return postRequest('/tests/test', test).then(
    function (data) {
      return data
    }
  )
}

function createUIObject (uiobject) {
  return postRequest('/objects/uiobject', uiobject).then(
    function (data) {
      return data
    }
  )
}

function getUIPages () {
  return getRequest('/objects/pages').then(
    function (data) {
      return data
    }
  )
}

function getUIObjectsByPage (page) {
  return getRequest('/objects/page/' + page).then(
    function (data) {
      return data
    }
  )
}

function copyTest (testsId) {
  return postRequest('/tests/test/copy', testsId).then(
    function (data) {
      return data
    }
  )
}

function searchTest (testDesc) {
  return postRequest('/tests/public', testDesc).then(
    function (data) {
      return data
    }
  )
}

function createTestPara (newPara) {
  return postRequest('/paras/para', newPara).then(
    function (data) {
      return data
    }
  )
}

function copyFormalParas (srcTestId, tarTestId, stepId) {
  return postRequest('/paras/' + srcTestId + '/formalpara/' + tarTestId + '/step/' + stepId).then(
    function (data) {
      return data
    }
  )
}

function setFormalParas (paras) {
  return putRequest('/paras/formalparas', paras).then(
    function (data) {
      return data
    }
  )
}

function updateTest (test) {
  return putRequest('/tests/testdetail', test).then(
    function (data) {
      return data
    }
  )
}

function setParasValue (paras) {
  return putRequest('/paras/values', paras).then(
    function (data) {
      return data
    }
  )
}

function getRefStepParas (testId, stepId) {
  return getRequest('/paras/test/' + testId + '/step/' + stepId).then(
    function (data) {
      return data
    }
  )
}

function getAgents () {
  return getRequest('/jenkins/agents').then(
    function (data) {
      return data
    }
  )
}

function startJob (tests) {
  return postRequest('/jenkins/job', tests).then(
    function (data) {
      return data
    }
  )
}

function getTasks (cnt) {
  return getRequest('/jenkins/jobs').then(
    function (data) {
      return data
    }
  )
}

function delStepsFormalParas (testId, stepIds) {
  return postRequest('/paras/test/' + testId, stepIds).then(
    function (data) {
      return data
    }
  )
}

function updateParaName (para) {
  return putRequest('/paras/para', para).then(
    function (data) {
      return data
    }
  )
}
