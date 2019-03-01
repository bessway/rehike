<template>
  <div class="objects-editor">
    <div class="object">
      <el-select placeholder="选择页面" v-if="editable"
        v-model="keyPage"
        @change="loadObjectsByPage">
        <el-option
          v-for="item in getUIObjPages()"
          :key="item"
          :value="item">
        </el-option>
      </el-select>
      <el-select placeholder="选择类型" v-if="editable"
        v-model="keyType">
        <el-option
          v-for="item in getUIObjTypes()"
          :key="item"
          :value="item">
        </el-option>
      </el-select>
      <el-select placeholder="选择名称" v-if="editable"
        v-model="keyName"
        @change="setSelectedObj">
        <el-option
          v-for="item in getUIObjNames()"
          :key="item"
          :value="item">
        </el-option>
      </el-select>
      <el-autocomplete class="xpath" placeholder="搜索xpath" v-if="editable"
        :trigger-on-focus="false"
        :fetch-suggestions="searchUIObjectByXpath">
      </el-autocomplete>
    </div>
    <div class="object">
      <el-input placeholder="页面" v-model="localUIobject.uiObjectPage" :disabled="!editable"></el-input>
      <el-input placeholder="类型" v-model="localUIobject.uiObjectType" :disabled="!editable"></el-input>
      <el-input placeholder="名称" v-model="localUIobject.uiObjectName" :disabled="!editable"></el-input>
      <el-input class="xpath" placeholder="xpath" v-model="localUIobject.uiObjectPath" :disabled="!editable"></el-input><br/>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" size="mini" @click="createUIObject" :disabled="!editable">添加</el-button><br/>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" size="mini" @click="debug" :disabled="!editable">修改</el-button><br/>
    </div>
  </div>
</template>

<style lang="scss">
.object {
  display: flex;
  justify-content: left;
  align-items: left;
  .el-select {
    width: 70%;
    margin-top: 0px;
    .el-input--suffix {
      padding-right: 0px;
    }
    .el-input__inner {
      height: 28px;
      margin-top: 0px;
      margin-bottom: 3px;
      padding: 0px;
    }
  }
  .el-select-dropdown {
    font-size: 12px;
    width: 100px;
    margin: 0px;
    .el-select-dropdown__item {
      height: 25px;
    }
  }
  .el-popper[x-placement^=bottom] {
    margin: 3px;
  }
  .el-select-dropdown__empty {
    height: 25px;
    font-size: 12px;
    padding: 0px;
    width: 100px;
  }
}
.xpath {
  width: 200%
}
.el-input {
  font-size: 12px;
  .el-input__inner {
    height: 28px;
    margin-top: 0px;
    padding: 0px;
    font-size: 12px;
  }
  .el-input__suffix {
    padding-right: 0px;
    height: 28px;
    line-height: 28px;
    .el-input__icon {
      line-height: 28px;
    }
  }
  .el-input-group__append {
    height: 28px;
    padding: 0px;
    font-size: 12px;
    line-height: 28px;
  }
}
</style>

<script>
import {mapGetters} from 'vuex'
import {Message} from 'element-ui'
export default {
  props: ['step', 'action', 'editable'],
  data () {
    return {
      keyPage: '',
      keyType: '',
      keyName: '',
      pageobjects: {},
      pathobjects: [],
      localUIobject: {}
    }
  },
  created: function () {
    console.log(this.action)
    this.loadUIObject()
  },
  watch: {
    action: function () {
      console.log(this.action)
      this.loadUIObject()
    }
  },
  methods: {
    ...mapGetters(['getUIObjPages']),
    async searchUIObjectByXpath () {
      // 不能直接修改props
      this.pathobjects = await this.API.getUIObjectByXpath(this.localUIobject.uiObjectPath)
    },
    // initObject () {
    //   if (this.uiobject === undefined || this.uiobject === null) {
    //     return {uiObjectId: '', uiObjectPage: '', uiObjectType: '', uiObjectName: '', uiObjectPath: ''}
    //   } else {
    //     return this.uiobject
    //   }
    // },
    async createUIObject () {
      if (this.localUIobject.uiObjectPage === '' ||
      this.localUIobject.uiObjectType === '' ||
      this.localUIobject.uiObjectName === '' ||
      this.localUIobject.uiObjectPath === '') {
        Message.error({message: '页面 类型 名称 地址 不能为空!'})
      } else {
        var strUIObj = JSON.stringify(this.localUIobject)
        var newUIObj = JSON.parse(strUIObj)
        delete newUIObj.uiObjectId
        this.localUIobject = await this.API.createUIObject(newUIObj)
        this.step.uiObjectId = this.localUIobject.uiObjectId
      }
    },
    async loadUIObject () {
      if (this.action.hasUIObject === 1 && this.step.uiObjectId !== undefined && this.step.uiObjectId !== '') {
        this.localUIobject = await this.API.getUIObject(this.step.uiObjectId)
      } else {
        this.localUIobject = {uiObjectId: '', uiObjectPage: '', uiObjectType: '', uiObjectName: '', uiObjectPath: ''}
      }
    },
    async loadObjectsByPage (val) {
      if (!this.pageobjects[val]) {
        var res = await this.API.getUIObjectsByPage(val)
        var result = {}
        res.forEach(obj => {
          this.addToStrctureObject(result, obj['uiObjectPage'], obj['uiObjectType'], obj['uiObjectName'], obj['uiObjectPath'], obj['uiObjectId'])
        })
        this.pageobjects = result
      }
    },
    addToStrctureObject (obj, page, type, name, path, id) {
      var tmp = obj
      if (tmp[page] === undefined) {
        tmp[page] = {}
      }
      tmp = tmp[page]
      if (tmp[type] === undefined) {
        tmp[type] = {}
      }
      tmp = tmp[type]
      tmp[name] = {uiObjectPath: path, uiObjectId: id}
    },
    getUIObjTypes () {
      if (this.pageobjects[this.keyPage] !== undefined) {
        return Object.keys(this.pageobjects[this.keyPage])
      } else {
        return {}
      }
    },
    getUIObjNames () {
      if (this.pageobjects[this.keyPage] !== undefined && this.pageobjects[this.keyPage][this.keyType] !== undefined) {
        return Object.keys(this.pageobjects[this.keyPage][this.keyType])
      } else {
        return {}
      }
    },
    setSelectedObj (val) {
      this.localUIobject = this.pageobjects[this.keyPage][this.keyType][val]
      this.localUIobject['uiObjectPage'] = this.keyPage
      this.localUIobject['uiObjectType'] = this.keyType
      this.localUIobject['uiObjectName'] = val
      this.step.uiObjectId = this.localUIobject.uiObjectId
    },
    debug () {
      console.log(this.localUIobject)
      console.log(this.pageobjects)
    }
  }
}
</script>
