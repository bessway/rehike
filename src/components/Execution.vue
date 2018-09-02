<template>
  <div class="env">
    <div class="para">
      <el-input placeholder="浏览器"></el-input>
      <el-input placeholder="系统"></el-input>
    </div>
  </div>
</template>
<script>
export default {
}
</script>
<style lang="scss">
.para {
  display: flex;
  justify-content: left;
  align-items: left;
  margin-top: 5px;
}
.el-input {
  font-size: 12px;
  .el-input__inner {
    height: 28px;
    margin-top: 0px;
    padding: 0px;
    font-size: 12px;
  }
}
</style>
