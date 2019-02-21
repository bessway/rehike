<template>
  <div class="stepparas">
    <div class="para" v-if="editable">
      <el-autocomplete
        :placeholder="getPlaceHolder(0)"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="name1"
        :disabled="!isParaAvailable(0)"
        @select="selectP1">
      </el-autocomplete>
      <el-autocomplete
        :placeholder="getPlaceHolder(1)"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="name2"
        :disabled="!isParaAvailable(1)"
        @select="selectP2">
      </el-autocomplete>
      <el-autocomplete
        :placeholder="getPlaceHolder(2)"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="name3"
        :disabled="!isParaAvailable(2)"
        @select="selectP3">
      </el-autocomplete>
      <el-autocomplete
        :placeholder="getPlaceHolder(3)"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="name4"
        :disabled="!isParaAvailable(3)"
        @select="selectP4">
      </el-autocomplete>
      <el-autocomplete
        :placeholder="getPlaceHolder(4)"
        value-key='paraName'
        :fetch-suggestions="paraSearch"
        :trigger-on-focus="false"
        v-model="name5"
        :disabled="!isParaAvailable(4)"
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
    <div v-if="!editable">
      <!--el-input
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
      </el-input-->
      <label v-if="isParaAvailable(0)">{{this.action.actionParas[0].paraDesc + ": " + localParas.p1.paraName+" = "}}</label>
      <label v-if="isParaAvailable(0)">{{localParas.p1.paraValue}}</label><br/>
      <label v-if="isParaAvailable(1)">{{this.action.actionParas[1].paraDesc + ": " + localParas.p2.paraName+" = "}}</label>
      <label v-if="isParaAvailable(1)">{{localParas.p2.paraValue}}</label><br/>
      <label v-if="isParaAvailable(2)">{{this.action.actionParas[2].paraDesc + ": " + localParas.p3.paraName+" = "}}</label>
      <label v-if="isParaAvailable(2)">{{localParas.p3.paraValue}}</label><br/>
      <label v-if="isParaAvailable(3)">{{this.action.actionParas[3].paraDesc + ": " + localParas.p4.paraName+" = "}}</label>
      <label v-if="isParaAvailable(3)">{{localParas.p4.paraValue}}</label><br/>
      <label v-if="isParaAvailable(4)">{{this.action.actionParas[4].paraDesc + ": " + localParas.p5.paraName+" = "}}</label>
      <label v-if="isParaAvailable(4)">{{localParas.p5.paraValue}}</label>
      <label>{{"response: " + localParas.response.paraName}}</label>
    </div>
    <!--div class="para" v-if="!editable">
      <el-input
        v-model="localParas.response.paraName" :disabled=!editable>
      </el-input>
      <el-button size="small" @click="debug">Debug</el-button>
    </div-->
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
// import { mapGetters } from 'vuex'
import { Message } from 'element-ui'
export default {
  props: ['step', 'testParas', 'action', 'editable'],
  data () {
    return {
      name1: '',
      name2: '',
      name3: '',
      name4: '',
      name5: '',
      localParas: {p1: {paraName: ''}, p2: {paraName: ''}, p3: {paraName: ''}, p4: {paraName: ''}, p5: {paraName: ''}, response: {paraName: ''}}
    }
  },
  created: function () {
    this.setLocalParas(this.findParas())
  },
  watch: {
    testParas: function () {
      if (this.testParas === undefined || this.testParas.p1 === null) {
        this.setLocalParas({p1: {paraName: ''}, p2: {paraName: ''}, p3: {paraName: ''}, p4: {paraName: ''}, p5: {paraName: ''}, response: {paraName: ''}})
      } else {
        this.setLocalParas(this.findParas())
      }
    },
    action: function () {
      if (this.testParas === undefined || this.testParas.p1 === null) {
        this.setLocalParas({p1: {paraName: ''}, p2: {paraName: ''}, p3: {paraName: ''}, p4: {paraName: ''}, p5: {paraName: ''}, response: {paraName: ''}})
      } else {
        this.setLocalParas(this.findParas())
      }
    }
  },
  methods: {
    // ...mapGetters(['getTestParas']),

    paraSearch (queryString, callback) {
      var results = queryString ? this.testParas.filter(this.createFilter(queryString)) : this.testParas
      // 调用 callback 返回建议列表的数据
      callback(results)
    },
    createFilter (queryString) {
      return (para) => {
        return (para.paraName.toLowerCase().indexOf(queryString.toLowerCase()) > 0 && (para.refTestId === undefined || para.refTestId === null))
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
        if (this.step.resParaId !== undefined && this.step.resParaId !== null) {
          ids.push(this.step.resParaId)
        }
        ids.forEach((paraId, index) => {
          for (var i = 0; i < this.testParas.length; i++) {
            if (paraId === this.testParas[i].paraId) {
              temp = this.testParas[i]
              break
            }
          }
          if (Object.keys(temp).length === 0 && this.action.actionId !== '') {
            temp = {paraName: '', paraId: ''}
            Message.error({message: '找不到' + paraId + '对应的参数!'})
          }
          // response放在最后一个
          if (index === ids.length - 1 && this.step.resParaId !== undefined && this.step.resParaId !== null) {
            paras['response'] = temp
          } else if (this.step.paras !== undefined && this.step.paras !== null && this.step.paras.length > 0) {
            paras['p' + (index + 1)] = temp
          }
        })
      }
      this.name1 = paras.p1.paraName
      return paras
    },
    setLocalParas (paras) {
      this.localParas = paras
      this.name1 = this.localParas.p1.paraName
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
    getPlaceHolder (index) {
      if (this.action !== undefined && this.action !== null && this.action.actionParas !== undefined && this.action.actionParas.length > index) {
        return this.action.actionParas[index].paraDesc
      } else {
        return ''
      }
    },
    isParaAvailable (index) {
      if (this.action !== undefined && this.action !== null && this.action.actionParas !== undefined && this.action.actionParas.length > index) {
        return true
      } else {
        return false
      }
    },
    debug () {
      console.log(this.testParas)
      console.log(this.localParas)
      console.log(this.step.paras)
      console.log(this.name1)
    }
  }
}
</script>
