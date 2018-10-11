<template>
  <div>
    <uiobject/>
    <paras/>
    <!--el-button ref='topbutton'>格式化</el-button><br/>
    <el-input type="textarea"/-->
    <maineditor/>
  </div>
</template>

<style lang="scss">

</style>

<script>
import uiobject from './UIObject.vue'
import paras from './Parameters.vue'
import maineditor from './MainEditor.vue'

export default {
  components: {uiobject, paras, maineditor}
}
</script>
