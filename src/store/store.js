import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
export default new Vuex.Store({
  state: {
    selectedTest: {},
    checkedTests: [],
    testParas: [],
    actions: [],
    uiobjectPages: []
  },
  getters: {
    getSelectedTest: state => {
      return state.selectedTest
    },
    getTestParas: state => {
      return state.testParas
    },
    getActions: state => {
      return state.actions
    },
    getUIObjPages: state => {
      return state.uiobjectPages
    }
  },
  mutations: {
    setSelectedTest (state, test) {
      state.selectedTest = test
      state.testParas = []
    },
    setTestParas (state, paras) {
      state.testParas = paras
    },
    setActions (state, actions) {
      state.actions = actions
    },
    setUIObjectPages (state, pages) {
      state.uiobjectPages = pages
    }
  }
})
