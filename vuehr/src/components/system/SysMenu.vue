<template>
  <div>
    <div style="text-align: left;margin-top: 20px">
      <el-input
        placeholder="输入菜单名称搜索..."
        style="width: 500px;margin: 0px;padding: 0px;"
        size="mini"
        prefix-icon="el-icon-search"
        v-model="keywords">
      </el-input>
    </div>
    <div>
      <el-tree
        :props="defaultProps"
        :data="treeData"
        ref="tree"
        node-key="id"
        :default-expanded-keys="[1]"
        :filter-node-method="filterNode"
        v-loading="treeLoading"
        style="width: 500px;margin-top: 10px">
        <span slot-scope="{ node, data }"
              style="flex: 1; display: flex; align-items: center; justify-content: space-between; font-size: 14px; padding-right: 8px;">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            type="text"
            size="mini"
            @click="() => add(node, data)">
            新增子菜单
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="() => edit(node, data)">
            编辑
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="() => del(node, data)">
            删除
          </el-button>
        </span>
      </span>
      </el-tree>

      <el-dialog
        :title="dialogTitle"
        :visible.sync="dialogVisible"
        :close-on-click-modal="false"
        width="30%">
        <el-form :model="editMenu" label-width="80px" size="mini" ref="form" :rules="rules">
          <el-form-item label="模糊路径" prop="url">
            <el-input v-model="editMenu.url" placeholder="请输入url,如/system/menu/**"
                      @keyup.enter.native="addOrEditMenu"></el-input>
          </el-form-item>
          <el-form-item label="路径" prop="path">
            <el-input size="mini" v-model="editMenu.path" placeholder="请输入path,如/system/menu"
                      @keyup.enter.native="addOrEditMenu"></el-input>
          </el-form-item>
          <el-form-item label="组件名称" prop="component">
            <el-input v-model="editMenu.component" placeholder="请输入组件名称,如SysMenu"
                      @keyup.enter.native="addOrEditMenu"></el-input>
          </el-form-item>
          <el-form-item label="菜单名称" prop="name">
            <el-input v-model="editMenu.name" placeholder="请输入菜单名称..."
                      @keyup.enter.native="addOrEditMenu"></el-input>
          </el-form-item>
          <el-form-item label="菜单图标" prop="iconCls">
            <el-input v-model="editMenu.iconCls" placeholder="请输入菜单图标..."
                      @keyup.enter.native="addOrEditMenu"></el-input>
          </el-form-item>
          <!--  <el-form-item label="keepAlive">
              <el-switch
                v-model="editMenu.keepAlive"
                active-color="#13ce66"
                inactive-color="#ff4949">
              </el-switch>
            </el-form-item>
            <el-form-item label="requireAuth">
              <el-switch
                v-model="editMenu.requireAuth"
                active-color="#13ce66"
                inactive-color="#ff4949">
              </el-switch>
            </el-form-item>-->
          <el-form-item label="是否启用" prop="enabled">
            <el-switch
              v-model="editMenu.enabled"
              active-color="#13ce66"
              inactive-color="#ff4949">
            </el-switch>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button size="small" @click="dialogVisible = false">取消</el-button>
            <el-button size="small" type="primary" @click="addOrEditMenu">确定</el-button>
          </span>
      </el-dialog>

    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        keywords: '',
        defaultProps: {
          label: 'name',
          children: 'children'
        },
        treeData: [],
        treeLoading: false,

        dialogVisible: false,
        dialogTitle: '',
        editMenu: {
          id: '',
          url: '1',
          path: '1',
          component: '1',
          name: '1',
          iconCls: '',
          meta: {
            keepAlive: false,
            requireAuth: true,
          },
          parentId: '',
          enabled: true,
        },
        rules: {
          url: [{required: true, message: '必填:模糊路径', trigger: 'blur'}],
          path: [{required: true, message: '必填:路径', trigger: 'blur'}],
          component: [{required: true, message: '必填:组件名称', trigger: 'blur'}],
          name: [{required: true, message: '必填:菜单名称', trigger: 'blur'}],
        }
      }
    },
    mounted: function () {
      this.treeLoading = true;
      this.loadTreeData();
    },
    watch: {
      keywords(val) {
        this.$refs.tree.filter(val);
      }
    },
    methods: {
      filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
      },
      loadTreeData() {
        this.treeData = this.$store.state.menus;
        this.treeLoading = false;
      },
      emptyMenu() {
        this.editMenu = {
          id: '',
          url: '',
          path: '',
          component: '',
          name: '',
          iconCls: '',
          meta: {
            keepAlive: false,
            requireAuth: true,
          },
          parentId: '',
          enabled: true,
        };
      },
      add(node, data) {
        this.emptyMenu();
        this.editMenu.parentId = data.id;
        this.dialogTitle = data.name + '》新增子菜单';
        this.dialogVisible = true;
      },
      edit(node, data) {
        this.dialogTitle = '编辑》' + data.name;
        this.dialogVisible = true;
        this.editMenu = data;
      },
      addOrEditMenu() {
        let _this = this;
        this.$refs['form'].validate((valid) => {
          if (valid) {
            if (_this.editMenu.id) {//更新
              _this.treeLoading = true;
              _this.putRequestWithComplexParam("/system/basic/updateMenu", _this.editMenu).then(resp => {
                _this.treeLoading = false;
                if (resp && resp.status == 200) {
                  _this.dialogVisible = false;
                  _this.afterUpdate(_this.editMenu);
                }
              })
            } else {//添加
              _this.treeLoading = true;
              _this.postRequestWithComplexParam("/system/basic/menu", _this.editMenu).then(resp => {
                _this.treeLoading = false;
                if (resp && resp.status == 200) {
                  _this.dialogVisible = false;
                  _this.afterAdd(resp.data.data);
                }
              })
            }
          } else {
            return false;
          }
        });
      },
      afterAdd(menu) {
        for (var i = 0; i < this.treeData.length; i++) {
          if (treeData[i].id == menu.parentId) {
            this.treeData[i].children.push(menu);
            break;
          }
        }
      },
      afterUpdate(menu) {
        for (var i = 0; i < this.treeData.length; i++) {
          if (treeData[i].id == menu.id) {
            Object.assign(this.treeData[i], menu);
            break;
          }
        }
      },
      del(node, data) {
        if (data.children.length > 0) {
          this.$message({
            message: '该菜单[' + data.name + ']有子菜单，请先删除子菜单!',
            type: 'warning'
          });
  /*        this.$alert('该菜单[' + data.name + ']有子菜单，请先删除子菜单!', '友情提示', {
            confirmButtonText: '确定',
            callback: action => {

            }
          });*/
          return;
        }
        var message = '此操作将删除菜单[' + data.name + '], 是否继续?';
        if (data.roles.length > 0) {
          message = '该菜单[' + data.name + ']已被分配给角色[';
          for (var i = 0; i < data.roles.length; i++)
            message += data.roles[i]['nameZh'] + ',';
          message += '],是否继续?'
        }
        this.$confirm(message, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
           this.doDelete(data.id,node);
        }).catch(() => {
        });
      },
      doDelete(id,node) {
        this.treeLoading = true;
        var _this = this;
        this.deleteRequest("/system/basic/menuTree/" + id).then(resp => {
          _this.treeLoading = false;
          if (resp && resp.status == 200) {
            // _this.afterDel(_this.treeData,id);
            const parent = node.parent;
            const children = parent.data.children || parent.data;
            const index = children.findIndex(d => d.id === id);
            children.splice(index, 1);
          }
        })
      },
      afterDel(treeDataArray,id) {
        for (var i = 0; i <treeDataArray.length; i++) {
          if (treeDataArray[i].id == id) {
            treeDataArray.splice(i,1);
            break;
          }
          this.afterDel(treeDataArray[i].children,id)
        }
      }
    }
  }

</script>

<style scoped>

</style>
