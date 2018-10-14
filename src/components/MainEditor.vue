<template>
  <div class="main-editor">
    <el-button ref='topbutton'>JSON格式化</el-button><br/>
    <el-input type="textarea"/>
  </div>
</template>
<script>
export default{
}
</script>
<style lang="scss">
.main-editor {
  height: 100%;
  .el-textarea__inner {
    min-height: 350px !important;
  }
}
</style>
