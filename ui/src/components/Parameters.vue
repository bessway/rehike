<template>
  <div class="stepparas">
    <div class="para" v-if="editable">
      <el-autocomplete
        placeholder="参数1"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p1.paraName"
        @focus="clickPara">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数2"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p2.paraName"
        @focus="clickPara">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数3"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p3.paraName"
        @focus="clickPara">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数4"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p4.paraName"
        @focus="clickPara">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数5"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p5.paraName"
        @focus="clickPara">
      </el-autocomplete>
    </div>
    <div class="para" v-if="editable">
      <el-autocomplete
        placeholder="返回值"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.response.paraName"
        @focus="clickPara">
      </el-autocomplete>
      <el-button size="small" @click="debug">Debug</el-button>
    </div>
    <div class="para" v-if="!editable">
      <el-input
        v-model="localParas.p1.paraName" :disabled=!editable
        @focus="clickPara">
      </el-input>
      <el-input
        v-model="localParas.p2.paraName" :disabled=!editable
        @focus="clickPara">
      </el-input>
      <el-input
        v-model="localParas.p3.paraName" :disabled=!editable
        @focus="clickPara">
      </el-input>
      <el-input
        v-model="localParas.p4.paraName" :disabled=!editable
        @focus="clickPara">
      </el-input>
      <el-input
        v-model="localParas.p5.paraName" :disabled=!editable
        @focus="clickPara">
      </el-input>
    </div>
    <div class="para" v-if="!editable">
      <el-input
        v-model="localParas.response.paraName" :disabled=!editable>
      </el-input>
      <el-button size="small" @click="debug">Debug</el-button>
    </div>
  </div>
</template>

<style lang="scss">
.para {
  display: flex;
  justify-content: left;
  align-items: left;
  .el-input {
    font-size: 12px;
    min-width: 150px;
    padding-left: 5px;
    .el-input__inner {
      height: 28px;
      margin-top: 0px;
      padding: 0px;
      font-size: 12px;
    }
  }
  label {
    font-size: 12px;
    height: 30px;
    margin-top: 5px;
    margin-left: 10px;
    font-weight: bold;
  }
}
.stepparas {
  margin-top: 3px;
}
</style>

<script>
import { mapGetters } from 'vuex'
export default {
  props: ['paras', 'editable'],
  data () {
    return {
      localParas: this.paras
    }
  },
  watch: {
    paras: function () {
      this.localParas = this.paras
    }
  },
  methods: {
    ...mapGetters(['getTestParas']),

    paraSearch (queryString, callback) {
      var results = queryString ? this.getTestParas().filter(this.createFilter(queryString)) : this.getTestParas()
      // 调用 callback 返回建议列表的数据
      callback(results)
    },
    createFilter (queryString) {
      return (para) => {
        return (para.paraName.toLowerCase().indexOf(queryString.toLowerCase()) > 0)
      }
    },
    clickPara () {

    },
    debug () {
      console.log(this.editable)
    }
  }
}
</script>
