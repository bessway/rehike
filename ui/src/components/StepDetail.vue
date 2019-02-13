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
      <el-button v-if="step.stepType === 2" size="small" @click="saveRefParaValue">保存</el-button>
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
      :editable="editable">
    </paras>
    <div v-if="step.stepType === 2">
      <div class="para" :key="index" v-for="(item, index) in refParas">
        <label v-if="index % 2 === 0">{{refParas[index].paraName+":"}}</label>
        <!--el-input v-if="index % 2 === 0" v-model="step.refParas[index].value" :disabled="!editable"/-->
        <el-autocomplete
          :disabled="!editable"
          v-if="index % 2 === 0"
          value-key='paraName'
          :fetch-suggestions="paraSearch"
          :trigger-on-focus="false"
          v-model="refParas[index].paraValue">
        </el-autocomplete>
        <label v-if="index+1 < refParas.length && index % 2 === 0">{{refParas[index+1].paraName+":"}}</label>
        <!--el-input v-if="index+1 < step.refParas.length && index % 2 === 0" v-model="step.refParas[index+1].value" :disabled="!editable"/-->
        <el-autocomplete
          :disabled="!editable"
          v-if="index+1 < refParas.length && index % 2 === 0"
          value-key='paraName'
          :fetch-suggestions="paraSearch"
          :trigger-on-focus="false"
          v-model="refParas[index+1].paraValue">
        </el-autocomplete>
      </div>
    </div>
    <div class="para" v-if="editable">
      <el-input placeholder="已上传的文件地址" readonly></el-input>
      <el-upload
        class="upload"
        action="https://jsonplaceholder.typicode.com/posts/"
        :limit="1">
        <el-button size="small" type="primary">上传</el-button>
      </el-upload>
    </div>
    <maineditor v-if="step.stepType !== 2 && editable"/>
    <steptable
      v-if="step.stepType === 2"
      :selectedTest="refTest"
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
  // props: ['action', 'uiobject', 'step', 'paras', 'refTest', 'editable'],
  props: ['step', 'uiobject', 'refTest', 'refParas', 'editable'],
  computed: {
    action: function () {
      return this.findAction(this.step.actionId)
    }
  },
  methods: {
    ...mapGetters(['getTestParas', 'getActions']),

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
      var results = queryString ? this.getTestParas().filter(this.createFilter(queryString)) : this.getTestParas()
      // 调用 callback 返回建议列表的数据
      callback(results)
    },
    createFilter (queryString) {
      return (para) => {
        return (para.paraName.toLowerCase().indexOf(queryString.toLowerCase()) > 0)
      }
    },
    async saveRefParaValue () {
      await this.API.setParasValue(this.refParas)
    },
    debug () {
      console.log(this.refTest)
      console.log(this.refParas)
    }
  }
}
</script>
