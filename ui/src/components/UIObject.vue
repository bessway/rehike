<template>
  <div class="objects-editor">
    <div class="object">
      <el-select placeholder="选择页面" :disabled="!editable"
        v-model="keyPage"
        @change="loadObjectsByPage">
        <el-option
        v-for="item in getUIObjPages()"
        :key="item.label"
        :label="item.label"
        :value="item.label">
      </el-option>
      </el-select>
      <el-select placeholder="选择类型" :disabled="!editable"
        v-model="keyType">
      </el-select>
      <el-select placeholder="选择名称" :disabled="!editable"
        v-model="keyName">
      </el-select>
      <el-autocomplete class="xpath" placeholder="搜索xpath" :disabled="!editable"
        :trigger-on-focus="false"
        :fetch-suggestions="searchUIObjectByXpath">
      </el-autocomplete>
    </div>
    <div class="object">
      <el-input placeholder="页面" v-model="localUIobject.uiObjectPage" :disabled="!editable"></el-input>
      <el-input placeholder="类型" v-model="localUIobject.uiObjectType" :disabled="!editable"></el-input>
      <el-input placeholder="名称" v-model="localUIobject.uiObjectName" :disabled="!editable"></el-input>
      <el-input class="xpath" placeholder="xpath" v-model="localUIobject.uiObjectPath" :disabled="!editable"></el-input><br/>
      <el-button size="mini" @click="createUIObject" :disabled="!editable">添加</el-button><br/>
      <el-button size="mini" @click="createUIObject" :disabled="!editable">修改</el-button><br/>
    </div>
  </div>
</template>

<style lang="scss">
.object {
  display: flex;
  justify-content: left;
  align-items: left;
  .el-button {
    margin: 0px;
    padding: 3px;
  }
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
  props: ['uiobject', 'editable'],
  data () {
    return {
      keyPage: '',
      keyType: '',
      keyName: '',
      pageobjects: {},
      pathobjects: [],
      localUIobject: this.uiobject,
      searchProps: {
        value: 'label',
        children: 'objects'
      }
    }
  },
  watch: {
    uiobject: function () {
      this.localUIobject = this.uiobject
    }
  },
  methods: {
    ...mapGetters(['getUIObjPages']),
    async searchUIObjectByXpath () {
      // 不能直接修改props
      this.pathobjects = await this.API.getUIObjectByXpath(this.localUIobject.uiObjectPath)
    },
    async createUIObject () {
      if (this.uiobject.uiObjectPage === '' ||
      this.uiobject.uiObjectType === '' ||
      this.uiobject.uiObjectName === '' ||
      this.uiobject.uiObjectPath === '') {
        Message.error({message: 'page type name xpath不能为空!'})
      } else {
        var strUIObj = JSON.stringify(this.uiobject)
        var newUIObj = JSON.parse(strUIObj)
        delete newUIObj.uiObjectId
        this.localUIobject = await this.API.createUIObject(newUIObj)
      }
    },
    async loadObjectsByPage (val) {
      if (!this.pageobjects[val]) {
        var res = await this.API.getUIObjectsByPage(val)
        var result = {}
        for (var obj in res) {
          this.addToStrctureObject(result, obj['uiObjectPage'], obj['uiObjectType'], obj['uiObjectName'], obj['uiObjectPath'], obj['uiObjectId'])
        }
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
    }
  }
}
</script>
