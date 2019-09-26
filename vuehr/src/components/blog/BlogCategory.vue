<template>
  <el-container>
    <el-header style="padding: 0px;display:flex;justify-content:space-between;align-items: center">
      <el-input
        placeholder="请输入栏目名称"
        v-model="cateName" style="width: 200px;display: none;">
      </el-input>
      <el-button type="primary" size="mini" style="margin-left: 10px" @click="addNewCate">新增栏目</el-button>
    </el-header>
    <el-main style="padding-left: 0px;padding-top: 0px">
      <el-table
        ref="multipleTable"
        :data="categories"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange" v-loading="loading">
        <el-table-column
          type="selection"
          width="55" align="left">
        </el-table-column>
        <el-table-column
          label="编号"
          prop="id"
          width="120" align="left">
        </el-table-column>
        <el-table-column
          label="栏目名称"
          prop="cateName"
          width="120" align="left">
        </el-table-column>
        <el-table-column
          prop="date"
          label="启用时间" align="left">
          <template slot-scope="scope">{{ scope.row.date | formatDate}}</template>
        </el-table-column>
        <el-table-column label="操作" align="left">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="editShow(scope.row)">修改
            </el-button>
            <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex;justify-content: space-between;margin: 2px">
        <el-button type="danger" size="mini" :disabled="this.selItems.length==0" style="margin-top: 10px;width: 100px;"
                   @click="deleteAll" v-if="this.categories.length>0">批量删除
        </el-button>
      </div>
      <!--弹窗数据-->
      <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogFormVisible">
        <el-form :model="editObj">
          <!--          <el-form-item label="编号">
                      <el-input v-model="editObj.id" auto-complete="off"></el-input>
                    </el-form-item>-->
          <el-form-item label="栏目名称">
            <el-input v-model="editObj.cateName" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="启用时间">
            <el-date-picker type="date"  v-model="editObj.date" placeholder="选择时间" @change="selectDate"
                            format="yyyy年MM月dd日" value-formate="yyyy-MM-dd" :picker-options="pickerOptions"
                             style="width: 100%;"></el-date-picker>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="editDo">确 定</el-button>
        </div>
      </el-dialog>
    </el-main>
  </el-container>
</template>
<script>
  // import {postRequest} from '../utils/api'
  // import {putRequest} from '../utils/api'
  // import {deleteRequest} from '../utils/api'
  // import {getRequest} from '../utils/api'

  export default {
    methods: {
      addNewCate() {
        Object.keys(this.editObj).forEach(key => this.editObj[key] = '');
        this.dialogFormVisible = true;
        this.dialogStatus = "addCategory";
        //this.editShow(this.editObj);
        /* if(this.cateName==''){
           this.$message({
             type: 'warn',
             message: '新增栏目不能为空!请输入栏目名称'
           });
           return;
         }
         this.loading = true;
         var _this = this;
         postRequest('/admin/category/', {cateName: this.cateName}).then(resp=> {
           if (resp.status == 200) {
             var json = resp.data;
             _this.$message({type: json.status, message: json.msg});
             _this.cateName = '';
             _this.refresh();
           }
           _this.loading = false;
         }, resp=> {
           if (resp.response.status == 403) {
             _this.$message({
               type: 'error',
               message: resp.response.data
             });
           }
           _this.loading = false;
         });*/
      },
      deleteAll() {
        var _this = this;
        this.$confirm('确认删除这 ' + this.selItems.length + ' 条数据?', '提示', {
          type: 'warning',
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(() => {
          var selItems = _this.selItems;
          var ids = '';
          for (var i = 0; i < selItems.length; i++) {
            ids += selItems[i].id + ",";
          }
          _this.deleteCate(ids.substring(0, ids.length - 1));
        }).catch(() => {
          //取消
          _this.loading = false;
        });
      },
      handleSelectionChange(val) {
        this.selItems = val;
      },
      handleEdit(index, row) {
        var _this = this;
        this.$prompt('请输入新名称', '编辑', {
          confirmButtonText: '更新',
          inputValue: row.cateName,
          cancelButtonText: '取消'
        }).then(({value}) => {
          //value就是输入值
          if (value == null || value.length == 0) {
            _this.$message({
              type: 'info',
              message: '数据不能为空!'
            });
          } else {
            _this.loading = true;
            putRequest("/admin/category/", {id: row.id, cateName: value}).then(resp => {
              var json = resp.data;
              _this.$message({
                type: json.status,
                message: json.msg
              });
              _this.refresh();
            }, resp => {
              if (resp.response.status == 403) {
                _this.$message({
                  type: 'error',
                  message: resp.response.data
                });
              }
              _this.loading = false;
            });
          }
        });
      },
      handleDelete(index, row) {
        let _this = this;
        this.$confirm('确认删除 ' + row.cateName + ' ?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          _this.deleteCate(row.id);
        }).catch(() => {
          //取消
          _this.loading = false;
        });
      },
      deleteCate(ids) {
        var _this = this;
        this.loading = true;
        //删除
        this.deleteRequest("/admin/category/" + ids).then(resp => {
          var json = resp.data;
          _this.$message({
            type: json.status,
            message: json.msg
          });
          _this.refresh();
        }, resp => {
          _this.loading = false;
          if (resp.response.status == 403) {
            _this.$message({
              type: 'error',
              message: resp.response.data
            });
          } else if (resp.response.status == 500) {
            _this.$message({
              type: 'error',
              message: '该栏目下尚有文章，删除失败!'
            });
          }
        })
      },
      refresh() {
        let _this = this;
        this.getRequest("/admin/category/all").then(resp => {
          _this.categories = resp.data;
          _this.loading = false;
        }, resp => {
          if (resp.response.status == 403) {
            _this.$message({
              type: 'error',
              message: resp.response.data
            });
          }
          _this.loading = false;
        });
      },
      editShow(row) {
        this.editObj = JSON.parse(JSON.stringify(row));
        this.dialogStatus = "editCategory";
        this.dialogFormVisible = true;
      },
      editDo() {
        debugger
        this.loading = true;
        var _this = this;
        if (this.dialogStatus == 'addCategory')
          this.postRequest('/admin/category/', this.editObj).then(resp => {
            if (resp.status == 200) {
              var json = resp.data;
              _this.$message({type: json.status, message: json.msg});
              _this.refresh();
            }
            _this.loading = false;
          }, resp => {
            if (resp.response.status == 403) {
              _this.$message({
                type: 'error',
                message: resp.response.data
              });
            }
            _this.loading = false;
          });
        else if (this.dialogStatus == 'editCategory')
          this.putRequest('/admin/category/', this.editObj).then(resp => {
            if (resp.status == 200) {
              var json = resp.data;
              _this.$message({type: json.status, message: json.msg});
              _this.refresh();
            }
            _this.loading = false;
          }, resp => {
            if (resp.response.status == 403) {
              _this.$message({
                type: 'error',
                message: resp.response.data
              });
            }
            _this.loading = false;
          });

        //关闭弹窗
        this.dialogFormVisible = false;
      },
      selectDate(val) {
        var d = new Date(val);
        this.editObj.date = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate();
      }
    },
    mounted: function () {
      debugger
      this.loading = true;
      this.refresh();
    },
    data() {
      return {
        cateName: '',
        selItems: [],
        categories: [],
        loading: false,
        //新增和编辑功能
        dialogFormVisible: false,
        editObj: {
          id: '',
          cateName: '',
          date: '2019-08-28'
        },
        titleMap: {//新增or编辑弹框标题(根据点击的新增or编辑按钮显示不同的标题)
          addCategory: '新增',
          editCategory: "编辑"
        },
        dialogStatus: "",//新增和编辑弹框标题，枚举值：addCategory、editCategory
        pickerOptions: {
          shortcuts: [
            {
              text: '今天',
              onClick(picker) {
                picker.$emit('pick', new Date())
              }
            }, {
              text: '昨天',
              onClick(picker) {
                const date = new Date();
                date.setTime(date.getTime() - 3600 * 1000 * 24);
                picker.$emit('pick', date);
              }
            }, {
              text: '一周前',
              onClick(picker) {
                const date = new Date();
                date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                picker.$emit('pick', date);
              }
            }
          ]
        }
      }
    }
  }
</script>
<style>
  .cate_mana_header {
    background-color: #ececec;
    margin-top: 20px;
    padding-left: 5px;
    display: flex;
    justify-content: flex-start;
  }

  .cate_mana_main {
    /*justify-content: flex-start;*/
    display: flex;
    flex-direction: column;
    padding-left: 5px;
    background-color: #ececec;
    margin-top: 20px;
    padding-top: 10px;
  }
</style>
