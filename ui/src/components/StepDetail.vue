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
    <div class="para" v-if="step.isRefStep === 1 && index % 2 == 0" :key="index" v-for="(item, index) in step.refParas">
      <label>{{step.refParas[index].name+":"}}</label>
      <el-input v-model="step.refParas[index].value" :disabled="!editable"/>
      <label v-if="index+1 < step.refParas.length">{{step.refParas[index+1].name+":"}}</label>
      <el-input v-if="index+1 < step.refParas.length" v-model="step.refParas[index+1].value" :disabled="!editable"/>
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
    <maineditor v-if="step.isRefStep !== 1 && editable"/>
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
