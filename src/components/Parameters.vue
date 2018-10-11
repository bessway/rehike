<template>
  <div class="stepparas">
    <div class="para">
      <el-input placeholder="P1"></el-input>
      <el-input placeholder="P2"></el-input>
      <el-input placeholder="P3"></el-input>
    </div>
    <div class="para">
      <el-input placeholder="P4"></el-input>
      <el-input placeholder="P5"></el-input>
      <el-input placeholder="response"></el-input>
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
}
.stepparas {
  margin-top: 3px;
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
