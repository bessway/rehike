<template>
  <div class="main-editor">
    <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" ref='topbutton' @click="debug">JSON格式化</el-button><br/>
    <el-input type="textarea" v-model="content.paraValue"/>
  </div>
</template>

<style lang="scss">
.main-editor {
  height: 100%;
  .el-textarea__inner {
    min-height: 350px !important;
  }
}
</style>

<script>
import { mapMutations, mapGetters } from 'vuex'
export default {
  computed: {
    content: function () {
      return this.getActiveEditor()
    }
  },
  methods: {
    ...mapMutations(['setActiveEditor']),
    ...mapGetters(['getActiveEditor']),
    debug () {
      console.log(this.content)
    }
  }
}
</script>
