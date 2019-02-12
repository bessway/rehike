<template>
  <div class="stepparas">
    <div class="para" v-if="editable">
      <el-autocomplete
        placeholder="参数1"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p1.paraName"
        @select="selectP1">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数2"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p2.paraName"
        @select="selectP2">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数3"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p3.paraName"
        @select="selectP3">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数4"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p4.paraName"
        @select="selectP4">
      </el-autocomplete>
      <el-autocomplete
        placeholder="参数5"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.p5.paraName"
        @select="selectP5">
      </el-autocomplete>
    </div>
    <div class="para" v-if="editable">
      <el-autocomplete
        placeholder="返回值"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="localParas.response.paraName"
        @select="selectRes">
      </el-autocomplete>
      <el-button size="small" @click="debug">Debug</el-button>
    </div>
    <div class="para" v-if="!editable">
      <el-input
        v-model="localParas.p1.paraName" :disabled=!editable>
      </el-input>
      <el-input
        v-model="localParas.p2.paraName" :disabled=!editable>
      </el-input>
      <el-input
        v-model="localParas.p3.paraName" :disabled=!editable>
      </el-input>
      <el-input
        v-model="localParas.p4.paraName" :disabled=!editable>
      </el-input>
      <el-input
        v-model="localParas.p5.paraName" :disabled=!editable>
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
import { Message } from 'element-ui'
export default {
  props: ['step', 'editable'],
  data () {
    return {
      localParas: this.findParas()
    }
  },
  // watch: {
  //   paras: function () {
  //     this.localParas = this.paras
  //   }
  // },
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
    findParas () {
      var paras = {p1: {}, p2: {}, p3: {}, p4: {}, p5: {}, response: {}}
      var temp = {}
      if (this.step.paras !== undefined) {
        var ids = []
        if (this.step.paras.length > 0) {
          ids = this.step.paras.slice(0)
        }
        if (this.step.resParaId !== undefined) {
          ids.push(this.step.resParaId)
        }
        ids.forEach((paraId, index) => {
          for (var i = 0; i < this.getTestParas().length; i++) {
            if (paraId === this.getTestParas()[i].paraId) {
              temp = this.getTestParas()[i]
              break
            }
          }
          if (temp.length === 0) {
            temp = {paraName: '', paraId: ''}
            Message.error({message: '找不到' + paraId + '对应的参数!'})
          }
          if (index === ids.length - 1) {
            paras['response'] = temp
          } else {
            paras['p' + (index + 1)] = temp
          }
        })
      }
      console.log(paras)
      return paras
    },
    selectP1 (para) {
      this.step.paras[0] = para.paraId
    },
    selectP2 (para) {
      this.step.paras[1] = para.paraId
    },
    selectP3 (para) {
      this.step.paras[2] = para.paraId
    },
    selectP4 (para) {
      this.step.paras[3] = para.paraId
    },
    selectP5 (para) {
      this.step.paras[4] = para.paraId
    },
    selectRes (para) {
      this.step.resParaId = para.paraId
    },
    debug () {
      console.log(this.localParas)
    }
  }
}
</script>
