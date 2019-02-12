<template>
  <div class="main-editor">
    <el-button ref='topbutton' @click="debug">JSON格式化</el-button><br/>
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
