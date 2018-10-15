<template>
  <div>
    <el-row class='test-desc'>
      <label>步骤描述:</label>
      <el-input v-model="step.stepDesc" :disabled="!editable || step.isRefStep === 1"></el-input>
      <el-select
        placeholder='操作'
        v-model="action.actionName"
        :disabled="!editable || step.isRefStep === 1">
      </el-select>
    </el-row>
    <uiobject
      v-if="step.isRefStep !== 1 && action.hasUIObject === 1"
      :uiobject="uiobject"
      :editable="editable">
    </uiobject>
    <paras
      v-if="step.isRefStep !== 1"
      :paras="paras"
      :editable="editable">
    </paras>
    <maineditor v-if="step.isRefStep !== 1"/>
    <steptable
      v-if="step.isRefStep === 1"
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

export default {
  name: 'stepdetail',
  components: {uiobject, paras, maineditor, steptable},
  props: ['action', 'uiobject', 'step', 'paras', 'refTest', 'editable']
}
</script>
