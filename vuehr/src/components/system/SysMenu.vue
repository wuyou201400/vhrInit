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
            @click="() => this.add(node, data,event)">
            新增子菜单
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="() => this.remove(node, data,event)">
            删除
          </el-button>
        </span>
      </span>
      </el-tree>
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
      loadTreeData(){
        this.treeData=this.$store.state.routes;
        this.treeLoading = false;
      },
      add(node,data, event) {
      },
      del(node,data, event) {
      }
    }
  }

</script>

<style scoped>

</style>
