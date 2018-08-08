import Vue from 'vue'
import Router from 'vue-router'
import CaseDetail from '@/components/CaseDetail'
// import test from '@/components/MainEditor'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'casemanager',
      component: CaseDetail
    }
  ]
})
