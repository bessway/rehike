import Mock from 'mockjs'

const tests = {
  // title: Random.csentence(5, 30), // Random.csentence( min, max )
  // author_name: Random.cname(), // Random.cname() 随机生成一个常见的中文姓名
  // date: Random.date() + ' ' + Random.time(), // Random.date()指示生成的日期字符串的格式,默认为yyyy-MM-dd；Random.time() 返回一个随机的时间字符串
  'tests|0-100': [{
    testId: '@string(30)',
    testDesc: '@csentence(0, 50)',
    parentId: '@string(30)'
  }],
  status: 'success'
}

const error = {
  msg: '@string(30)',
  status: 'failed'
}

const testDetail = {
  testId: '@string(30)',
  testDesc: '@csentence(0, 50)',
  parentId: '@string(30)',
  'steps|0-100': [{
    'id|+1': 100,
    actionId: '@string(30)',
    'stepType|1-3': 100,
    stepDesc: '@csentence(0, 50)',
    uiObjectId: '@string(30)',
    refTestId: '@string(30)',
    'stepParaIds|0-5': ['@string(30)'],
    resParaId: '@string(30)'
  }],
  status: 'success'
}

const testParas = {
  'para|0-400': [{
    paraId: '@string(30)',
    paraName: '%%' + '@name(10)' + '%%'
  }]
}

const actions = {
  'action|0-400': [{
    actionId: '@string(30)',
    actionName: '@name(20)'
  }]
}

const action = {
  actionId: '@string(30)',
  actionName: '@name(20)'
}

const uiobjects = {
  'uiobject|0-400': [{
    uiObjectId: '@string(30)',
    uiObjectPage: '@name(20)',
    uiObjectType: '@name(10)',
    uiObjectName: '@name(20)',
    uiObjectPath: '@string(30)'
  }]
}

const uiobject = {
  uiObjectId: '@string(30)',
  uiObjectPage: '@name(20)',
  uiObjectType: '@name(10)',
  uiObjectName: '@name(20)',
  uiObjectPath: '@string(30)'
}
/*
const stepDetail = {
  actionId: '@string(30)',
  actionName: '@name(20)',
  uiObjectId: '@string(30)',
  uiObjectPage: '@name(20)',
  uiObjectType: '@name(10)',
  uiObjectName: '@name(20)',
  uiObjectPath: '@string(30)',
  resParaId: '@string(30)',
  resParaName: '%%' + '@name(10)' + '%%',
  refCaseId: '@string(30)',
  'refSteps|0-100': [{
    actionId: '@string(30)',
    'stepType|1-4': 100,
    stepDesc: '@csentence(0, 50)',
    uiObjectId: '@string(30)',
    'stepParaIds|0-100': ['@string(30)'],
    resParaId: '@string(30)'
  }]
}
*/
Mock.setup({
  timeout: '500-1000'
})

Mock.mock(process.env.API_BASE + '/api/v2' + '/error', 'get', error)
Mock.mock(RegExp(process.env.API_BASE + '/api/v2' + '/test/' + '[\\s\\S]{30}'), 'get', testDetail)
Mock.mock(RegExp(process.env.API_BASE + '/api/v2' + '/tests/' + '[\\s\\S]{30}'), 'get', tests)
// Mock.mock(process.env.API_BASE + '/api/v2' + '/tests/null', 'get', tests)
Mock.mock(RegExp(process.env.API_BASE + '/api/v2' + '/testparas/' + '[\\s\\S]{30}'), 'get', testParas)
Mock.mock(process.env.API_BASE + '/api/v2' + '/actions', 'get', actions)
Mock.mock(RegExp(process.env.API_BASE + '/api/v2' + '/action/' + '[\\s\\S]{30}'), 'get', action)
Mock.mock(process.env.API_BASE + '/api/v2' + '/uiobjects', 'get', uiobjects)
Mock.mock(RegExp(process.env.API_BASE + '/api/v2' + '/uiobject/' + '[\\s\\S]{30}'), 'get', uiobject)
