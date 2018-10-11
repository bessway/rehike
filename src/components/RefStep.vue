<template>
  <div class="main-editor">
    <!--el-button ref='topbutton'>格式化</el-button><br/>
    <el-input type="textarea"/-->
    <maineditor/>
  </div>
</template>
<script>
import maineditor from './MainEditor.vue'
export default {
  components: {maineditor}
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
