<template>
  <div>
    <el-row class='test-desc'>
      <label>步骤描述:</label>
      <el-input v-model="step.stepDesc" :disabled="!editable || step.stepType === 2"></el-input>
      <el-select
        placeholder='操作'
        v-model="step.actionId"
        :disabled="!editable || step.stepType === 2">
        <el-option
          v-for="item in getActions()"
          :key="item.actionId"
          :label="item.actionName"
          :value="item.actionId">
        </el-option>
      </el-select>
      <el-button size="small" type="primary" @click="debug">debug</el-button>
    </el-row>
    <uiobject
      v-if="step.stepType !== 2 && action.hasUIObject === 1"
      :step="step"
      :uiobject="uiobject"
      :editable="editable">
    </uiobject>
    <paras
      v-if="step.stepType !== 2"
      :step="step"
      :testParas="testParas"
      :editable="editable">
    </paras>
    <div v-if="step.stepType === 2">
      <div class="para" :key="index" v-for="(item, index) in referParas">
        <label v-if="index % 2 === 0">{{referParas[index].paraName+":"}}</label>
        <el-autocomplete
          :disabled="!editable"
          v-if="index % 2 === 0"
          value-key='paraName'
          :fetch-suggestions="paraSearch"
          :trigger-on-focus="false"
          v-model="referParas[index].paraValue">
        </el-autocomplete>
        <label v-if="index+1 < referParas.length && index % 2 === 0">{{referParas[index+1].paraName+":"}}</label>
        <el-autocomplete
          :disabled="!editable"
          v-if="index+1 < referParas.length && index % 2 === 0"
          value-key='paraName'
          :fetch-suggestions="paraSearch"
          :trigger-on-focus="false"
          v-model="referParas[index+1].paraValue">
        </el-autocomplete>
      </div>
    </div>
    <maineditor v-if="step.stepType !== 2 && editable"/>
    <steptable
      v-if="step.stepType === 2"
      :currTest="referTest"
      :testParas="testParas"
      :editable="false">
    </steptable>
  </div>
</template>

<style lang="scss">
.test-desc {
  display: flex;
  margin-left: 5px;
  justify-content: left;
  align-items: left;
  .el-input {
    .el-input__inner {
      margin-top: 3px;
      height: 25px;
    }
  }
}
.el-select {
  margin-top: 0px;
  .el-input--suffix {
    padding-right: 0px;
  }
  .el-input__inner {
    height: 25px;
    margin-top: 0px;
    margin-bottom: 3px;
    padding: 0px;
  }
}
.el-select-dropdown {
  font-size: 12px;
  width: 100px;
  margin: 0px;
  .el-select-dropdown__item {
    height: 25px;
  }
}
.el-popper[x-placement^=bottom] {
  margin: 3px;
}
.el-select-dropdown__empty {
  height: 25px;
  font-size: 12px;
  padding: 0px;
  width: 100px;
}
</style>

<script>
import uiobject from './UIObject.vue'
import paras from './Parameters.vue'
import maineditor from './MainEditor.vue'
import steptable from './StepTable.vue'
import { mapGetters } from 'vuex'
import { Message } from 'element-ui'

export default {
  name: 'stepdetail',
  components: {uiobject, paras, maineditor, steptable},
  props: ['step', 'uiobject', 'referTest', 'referParas', 'testParas', 'editable'],
  computed: {
    action: function () {
      return this.findAction(this.step.actionId)
    }
  },
  methods: {
    ...mapGetters(['getActions']),

    findAction (actionId) {
      var defaultAction = {actionId: '', actionName: '', hasUIObject: 1, actionType: 1}
      if (actionId === undefined || actionId === '') {
        return defaultAction
      }
      for (var i = 0; i < this.getActions().length; i++) {
        if (actionId === this.getActions()[i].actionId) {
          return this.getActions()[i]
        }
      }
      Message.error({message: '找不到' + actionId + '对应的操作!'})
      return defaultAction
    },
    // async loadRefTest () {
    //   if (this.step.stepType !== 2 || this.step.refTestId === undefined || this.step.refTestId === '') {
    //     return {}
    //   } else {
    //     var refTestDetail = await this.API.getTestDetail(this.step.refTestId)
    //     return refTestDetail
    //   }
    // },
    paraSearch (queryString, callback) {
      var results = queryString ? this.testParas.filter(this.createFilter(queryString)) : this.testParas
      // 调用 callback 返回建议列表的数据
      callback(results)
    },
    createFilter (queryString) {
      return (para) => {
        return (para.paraName.toLowerCase().indexOf(queryString.toLowerCase()) > 0)
      }
    },
    debug () {
      console.log(this.step)
      console.log(this.referTest)
      console.log(this.referParas)
      console.log(this.testParas)
    }
  }
}
</script>
