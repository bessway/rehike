<template>
  <div>
    <paras/>
    <!--el-button ref='topbutton'>格式化</el-button><br/>
    <el-input type="textarea"/-->
    <maineditor/>
  </div>
</template>
<script>
import paras from './Parameters.vue'
import maineditor from './MainEditor.vue'
export default {
  components: {paras, maineditor}
}
</script>
<style lang="scss">

</style>
