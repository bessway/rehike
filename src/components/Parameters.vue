<template>
  <div class="stepparas">
    <div class="para" v-if="editable">
      <el-autocomplete
        placeholder="参数1"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p1.paraName">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数2"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p2.paraName">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数3"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p3.paraName">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数4"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p4.paraName">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数5"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p5.paraName">
      </el-autocomplete>
      <el-button size="small" @click="debug">Debug</el-button>
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
      <el-input placeholder="已上传的文件地址" readonly></el-input>
      <el-upload
        class="upload"
        action="https://jsonplaceholder.typicode.com/posts/"
        :limit="1">
        <el-button size="small" type="primary">上传</el-button>
      </el-upload>
    </div>
    <div class="para" v-if="!editable">
      <el-input
        v-model="localParas.p1.paraName" readonly
        @focus="clickPara">
      </el-input>
      <el-input
        v-model="localParas.p2.paraName" readonly
        @focus="clickPara">
      </el-input>
      <el-input
        v-model="localParas.p3.paraName" readonly
        @focus="clickPara">
      </el-input>
      <el-input
        v-model="localParas.p4.paraName" readonly
        @focus="clickPara">
      </el-input>
      <el-input
        v-model="localParas.p5.paraName" readonly
        @focus="clickPara">
      </el-input>
      <el-button size="small" @click="debug">Debug</el-button>
    </div>
    <div class="para" v-if="!editable">
      <el-input
        v-model="localParas.response.paraName" readonly
        @focus="clickPara">
      </el-input>
      <el-input placeholder="已上传的文件地址" readonly></el-input>
      <el-upload
        class="upload"
        action="https://jsonplaceholder.typicode.com/posts/"
        :limit="1">
        <el-button size="small" type="primary">上传</el-button>
      </el-upload>
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
    .el-input__inner {
      height: 28px;
      margin-top: 0px;
      padding: 0px;
      font-size: 12px;
    }
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
      console.log('test')
    },
    debug () {
      console.log(this.editable)
    }
  }
}
</script>
