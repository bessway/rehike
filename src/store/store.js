import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
export default new Vuex.Store({
  state: {
    selectedTest: {},
    checkedTests: [],
    testParas: [],
    selectedStep: {},
    actions: [],
    activeUIObject: {}
  },
  getters: {
    getSelectedTest: state => {
      return state.selectedTest
    },
    getSelectedStep: state => {
      return state.selectedStep
    },
    getTestParas: state => {
      return state.testParas
    },
    getActions: state => {
      return state.actions
    },
    getUIObject: state => {
      return state.activeUIObject
    }
  },
  mutations: {
    setSelectedTest (state, test) {
      state.selectedTest = test
      state.selectedStep = {}
    },
    setSelectedStep (state, step) {
      state.selectedStep = step
    },
    setTestParas (state, paras) {
      state.testParas = paras
    },
    setActions (state, actions) {
      state.actions = actions
    },
    setUIObject (state, uiObject) {
      state.activeUIObject = uiObject
    }
  }
})
