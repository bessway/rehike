<template>
  <div class="main-editor">
    <uiobject/>
    <paras/>
    <!--el-button ref='topbutton'>格式化</el-button><br/>
    <el-input type="textarea"/-->
    <maineditor/>
  </div>
</template>
<script>
import uiobject from './Object.vue'
import paras from './Parameters.vue'
import maineditor from './MainEditor.vue'
export default {
  components: {uiobject, paras, maineditor}
}
</script>
<style lang="scss">
.main-editor {
  height: 100%;
  .el-textarea__inner {
    min-height: 400px !important;
  }
}
</style>
