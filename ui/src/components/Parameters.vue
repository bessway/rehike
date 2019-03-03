<template>
  <div class="stepparas">
    <label v-if="action.hasResponse === 1 || action.actionParas.length > 0"><font color="red">参数:</font></label>
    <div v-if="editable">
      <div class="para" v-if="isParaAvailable(0)">
        <label>{{getPlaceHolder(0)+":&nbsp;"}}</label>
        <el-select
          v-model="localParas.p1.paraName"
          filterable
          clearable
          @change="selectP1">
          <el-option
            v-for="item in testParas"
            :key="item.paraId"
            :label="item.paraName"
            :value="item.paraId">
          </el-option>
        </el-select>
        <el-input v-model=localParas.p1.paraValue />
      </div>
      <div class="para" v-if="isParaAvailable(1)">
        <label>{{getPlaceHolder(1)+":&nbsp;"}}</label>
        <el-select
          v-model="localParas.p2.paraName"
          filterable
          clearable
          @change="selectP2">
          <el-option
            v-for="item in testParas"
            :key="item.paraId"
            :label="item.paraName"
            :value="item.paraId">
          </el-option>
        </el-select>
        <el-input v-model=localParas.p2.paraValue />
      </div>
      <div class="para" v-if="isParaAvailable(2)">
        <label>{{getPlaceHolder(2)+":&nbsp;"}}</label>
        <el-select
          v-model="localParas.p3.paraName"
          filterable
          clearable
          @change="selectP3">
          <el-option
            v-for="item in testParas"
            :key="item.paraId"
            :label="item.paraName"
            :value="item.paraId">
          </el-option>
        </el-select>
        <el-input v-model=localParas.p3.paraValue />
      </div>
      <div class="para" v-if="isParaAvailable(3)">
        <label>{{getPlaceHolder(3)+":&nbsp;"}}</label>
        <el-select
          v-model="localParas.p4.paraName"
          filterable
          clearable
          @change="selectP4">
          <el-option
            v-for="item in testParas"
            :key="item.paraId"
            :label="item.paraName"
            :value="item.paraId">
          </el-option>
        </el-select>
        <el-input v-model=localParas.p4.paraValue />
      </div>
      <div class="para" v-if="isParaAvailable(4)">
        <label>{{getPlaceHolder(4)+":&nbsp;"}}</label>
        <el-select
          v-model="localParas.p5.paraName"
          filterable
          clearable
          @change="selectP5">
          <el-option
            v-for="item in testParas"
            :key="item.paraId"
            :label="item.paraName"
            :value="item.paraId">
          </el-option>
        </el-select>
        <el-input v-model=localParas.p5.paraValue />
      </div>
      <div class="para" v-if="action.hasResponse === 1">
        <!--el-autocomplete
          placeholder="返回值"
          value-key='paraName'
          :fetch-suggestions="paraSearch"
          :trigger-on-focus="false"
          v-model="res"
          @select="selectRes">
        </el-autocomplete-->
        <el-select
          placeholder="返回值"
          v-model="localParas.response.paraName"
          filterable
          clearable
          @change="selectRes">
          <el-option
            v-for="item in testParas"
            :key="item.paraId"
            :label="item.paraName"
            :value="item.paraId">
          </el-option>
        </el-select>
        <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" size="small" @click="debug">Debug</el-button>
      </div>
    </div>
    <div v-if="!editable">
      <div v-if="isParaAvailable(0)">
        <label>{{this.action.actionParas[0].paraDesc + ":&nbsp;" + localParas.p1.paraName+"&nbsp;=&nbsp;"}}</label>
        <label v-if="!isRefFormalPara(0)">{{localParas.p1.paraValue}}</label>
      </div>
      <div v-if="isParaAvailable(1)">
        <label>{{this.action.actionParas[1].paraDesc + ":&nbsp;" + localParas.p2.paraName+"&nbsp;=&nbsp;"}}</label>
        <label v-if="!isRefFormalPara(1)">{{localParas.p2.paraValue}}</label>
      </div>
      <div v-if="isParaAvailable(2)">
        <label>{{this.action.actionParas[2].paraDesc + ":&nbsp;" + localParas.p3.paraName+"&nbsp;=&nbsp;"}}</label>
        <label v-if="!isRefFormalPara(2)">{{localParas.p3.paraValue}}</label>
      </div>
      <div v-if="isParaAvailable(3)">
        <label>{{this.action.actionParas[3].paraDesc + ":&nbsp;" + localParas.p4.paraName+"&nbsp;=&nbsp;"}}</label>
        <label v-if="!isRefFormalPara(3)">{{localParas.p4.paraValue}}</label>
      </div>
      <div v-if="isParaAvailable(4)">
        <label>{{this.action.actionParas[4].paraDesc + ":&nbsp;" + localParas.p5.paraName+"&nbsp;=&nbsp;"}}</label>
        <label v-if="!isRefFormalPara(4)">{{localParas.p5.paraValue}}</label>
      </div>
      <label v-if="this.action.hasResponse === 1">{{"结果变量:&nbsp;" + localParas.response.paraName}}</label>
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
    font-size: 14px;
    height: 30px;
    margin-top: 5px;
    margin-left: 0px;
    font-weight: bold;
    text-align: right;
    white-space:nowrap;
  }
}
.el-table__expanded-cell {
  padding-left: 30px !important;
  padding-right: 20px !important;
}
.stepparas {
  margin-top: 10px;
  label {
    font-size: 14px;
    height: 30px;
    margin-top: 5px;
    margin-left: 0px;
    text-align: right;
    white-space:nowrap;
  }
}
// 因为scoped的原因，需要在这里设置tree的属性
.el-tree-node__children {
  overflow: visible !important;
}
</style>

<script>
import { Message } from 'element-ui'
export default {
  props: ['step', 'testParas', 'action', 'editable'],
  data () {
    return {
      // name1: '',
      // name2: '',
      // name3: '',
      // name4: '',
      // name5: '',
      // res: '',
      localParas: {p1: {paraName: ''}, p2: {paraName: ''}, p3: {paraName: ''}, p4: {paraName: ''}, p5: {paraName: ''}, response: {paraName: ''}}
    }
  },
  created: function () {
    console.log(this.testParas)
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
    step: function () {
      if (this.testParas === undefined || this.testParas.p1 === null) {
        this.setLocalParas({p1: {paraName: ''}, p2: {paraName: ''}, p3: {paraName: ''}, p4: {paraName: ''}, p5: {paraName: ''}, response: {paraName: ''}})
      } else {
        this.setLocalParas(this.findParas())
      }
      console.log(this.localParas)
    }
  },
  methods: {
    // paraSearch (queryString, callback) {
    //   var results = queryString ? this.testParas.filter(this.createFilter(queryString)) : this.testParas
    //   // 调用 callback 返回建议列表的数据
    //   callback(results)
    // },
    // createFilter (queryString) {
    //   return (para) => {
    //     return (para.paraName.toLowerCase().indexOf(queryString.toLowerCase()) > 0 && (para.refTestId === undefined || para.refTestId === null))
    //   }
    // },
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
          if (paraId === null) {
            temp = {paraName: '', paraId: ''}
            paras['p' + (index + 1)] = temp
          } else {
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
          }
        })
      }
      // this.name1 = paras.p1.paraName
      // this.name2 = paras.p2.paraName
      // this.name3 = paras.p3.paraName
      // this.name4 = paras.p4.paraName
      // this.name5 = paras.p5.paraName
      // this.res = paras.response.paraName
      return paras
    },
    setLocalParas (paras) {
      this.localParas = paras
      // this.name1 = this.localParas.p1.paraName
      // this.name2 = this.localParas.p2.paraName
      // this.name3 = this.localParas.p3.paraName
      // this.name4 = this.localParas.p4.paraName
      // this.name5 = this.localParas.p5.paraName
      // this.res = this.localParas.response.paraName
    },
    selectP1 (paraId) {
      // this.step.paras[0] = para.paraId
      this.step.paras[0] = paraId
    },
    selectP2 (paraId) {
      // this.step.paras[1] = para.paraId
      this.step.paras[1] = paraId
    },
    selectP3 (paraId) {
      // this.step.paras[2] = para.paraId
      this.step.paras[2] = paraId
    },
    selectP4 (paraId) {
      // this.step.paras[3] = para.paraId
      this.step.paras[3] = paraId
    },
    selectP5 (paraId) {
      // this.step.paras[4] = para.paraId
      this.step.paras[4] = paraId
    },
    selectRes (paraId) {
      // this.step.resParaId = para.paraId
      this.step.resParaId = paraId
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
    isRefFormalPara (index) {
      var nameIndex = index + 1
      if (this.localParas['p' + nameIndex].isFormalPara === 0) {
        return false
      } else {
        return true
      }
    },
    debug () {
      console.log(this.testParas)
      console.log(this.localParas)
      console.log(this.step.paras)
      console.log(this.step.resParaId)
    }
  }
}
</script>
