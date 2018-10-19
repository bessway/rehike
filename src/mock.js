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
    stepDesc: '@csentence(0, 50)',
    uiObjectId: '@string(30)',
    refTestId: '@string(30)',
    'isRefStep|0': 100,
    'stepParaIds|0-5': ['@string(30)'],
    resParaId: '@string(30)'
  }],
  status: 'success'
}

const testParas = {
  'para|0-400': [{
    paraId: '@string(30)',
    paraName: '%%' + '@name(10)' + '%%',
    paraValue: '@string(30)'
  }]
}

const actions = {
  'action|0-400': [{
    actionId: '@string(30)',
    'actionName|+1': 100,
    hasUIObject: 1,
    'actionType|0-1': 100
  }]
}
/*
const action = {
  actionId: '@string(30)',
  actionName: '@name(20)',
  'hasUIObject|0-1': 100,
  'actionType|0-1': 100
}
*/
const uiobjects = {
  'uiobject|0-400': [{
    uiObjectId: '@string(30)',
    uiObjectPage: '@name(20)',
    uiObjectType: '@name(10)',
    uiObjectName: '@name(20)',
    uiObjectPath: '@string(30)'
  }]
}

const uiPages = {
  'page|0-100': [{
    label: '@name(20)'
  }]
}

const uiobject = {
  uiObjectId: '@string(30)',
  uiObjectPage: '@name(20)',
  uiObjectType: '@name(10)',
  uiObjectName: '@name(20)',
  uiObjectPath: '@string(30)'
}

Mock.setup({
  timeout: '500-1000'
})

Mock.mock(process.env.API_BASE + '/api/v2' + '/error', 'get', error)
Mock.mock(RegExp(process.env.API_BASE + '/api/v2' + '/test/' + '[\\s\\S]{30}'), 'get', testDetail)
Mock.mock(RegExp(process.env.API_BASE + '/api/v2' + '/tests/' + '[\\s\\S]{30}'), 'get', tests)
Mock.mock(RegExp(process.env.API_BASE + '/api/v2' + '/testparas/' + '[\\s\\S]{30}'), 'get', testParas)
Mock.mock(process.env.API_BASE + '/api/v2' + '/actions', 'get', actions)
Mock.mock(RegExp(process.env.API_BASE + '/api/v2' + '/uiobject/' + '[\\s\\S]{30}'), 'get', uiobject)
Mock.mock(process.env.API_BASE + '/api/v2' + '/uipages', 'get', uiPages)
Mock.mock(RegExp(process.env.API_BASE + '/api/v2' + '/uiobject/path/' + '[\\s\\S]{30}'), 'get', uiobject)
Mock.mock(process.env.API_BASE + '/api/v2' + '/uiobject', 'post', uiobject)
Mock.mock(RegExp(process.env.API_BASE + '/api/v2' + '/uiobjects/page/' + '[\\s\\S]*'), 'get', uiobjects)
