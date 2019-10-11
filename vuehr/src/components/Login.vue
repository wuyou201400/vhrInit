<template>
  <el-container>
    <el-main>
      <el-form :rules="rules" ref="loginForm" class="login-container" label-position="left" :model="loginForm"
               label-width="0px" v-loading="loading">
        <h3 class="login_title">系统登录</h3>
        <el-form-item prop="username">
          <el-input type="text" v-model="loginForm.username"
                    auto-complete="off" placeholder="账号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="loginForm.password"
                    auto-complete="off" placeholder="密码"></el-input>
        </el-form-item>
        <el-checkbox class="login_remember" v-model="checked"
                     label-position="left">记住密码
        </el-checkbox>&nbsp;&nbsp;&nbsp;&nbsp;
        <el-button plain @click="reg">注册</el-button>
        <el-form-item style="width: 100%">
          <el-button type="primary" style="width: 100%" @click="submitClick">登录</el-button>
        </el-form-item>
      </el-form>

      <!--注册弹窗数据-->
      <el-dialog title="注册" :visible.sync="dialogFormVisible">
        <el-form :model="regForm" :rules="regRules" ref="regForm" label-width="80px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="regForm.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="regForm.password" placeholder="密码"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="password2">
            <el-input type="password" v-model="regForm.password2" placeholder="确认密码"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="regForm.email" placeholder="邮箱"></el-input>
          </el-form-item>
          <el-form-item label="中文名称" prop="name">
            <el-input v-model="regForm.name" placeholder="中文名称"></el-input>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="regForm.phone" placeholder="手机号"></el-input>
          </el-form-item>
          <el-form-item label="座机" prop="telephone">
            <el-input v-model="regForm.telephone" placeholder="座机"></el-input>
          </el-form-item>
          <el-form-item label="地址" prop="address">
            <el-input v-model="regForm.address" placeholder="地址"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="regForm.remark" placeholder="备注"></el-input>
          </el-form-item>
          <el-form-item label="头像" prop="userface">
            <el-input v-model="regForm.userface" placeholder="头像"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="regClick">确 定</el-button>
        </div>
      </el-dialog>

    </el-main>
  </el-container>
</template>
<script>
  export default {
    data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.regForm.password2 !== '') {
            this.$refs.regForm.validateField('password2');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.regForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        rules: {
          username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
          password: [{required: true, message: '请输入密码', trigger: 'blur'}]
        },
        checked: true,
        loginForm: {
          username: 'admin',
          password: '123'
        },
        loading: false,
        //注册相关
        dialogFormVisible: false,
        regForm: {
          id: '',
          name: '',
          phone: '',
          telephone: '',
          address: '',
          username: '',
          password: '',
          password2: '',
          userface: '',
          remark: '',
          email: ''
        },
        regRules: {
          username: [{required: true, message: '请输入用户名', trigger: 'blur'}, {
            min: 6,
            max: 20,
            message: '长度在 6 到 20 个字符',
            trigger: 'blur'
          }],
          password: [{required: true, message: '请输入密码', trigger: 'blur'}, {
            required: true,
            validator: validatePass,
            trigger: 'blur'
          }, {
            pattern: /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_]+$)(?![a-z0-9]+$)(?![a-z\W_]+$)(?![0-9\W_]+$)[a-zA-Z0-9\W_]{6,30}$/,
            message: '密码为数字、字母、特殊符号，至少包含三种，长度为 6 - 30位'
          }],
          password2: [{required: true, validator: validatePass2, trigger: 'blur'}]
        }
      }
    },
    methods: {
      submitClick: function () {
        this.$refs['loginForm'].validate((valid) => {
          if (valid) {
            var _this = this;
            this.loading = true;
            this.postRequest('/login', {
              username: this.loginForm.username,
              password: this.loginForm.password
            }).then(resp => {
              _this.loading = false;
              if (resp && resp.status == 200) {
                var data = resp.data;
                _this.$store.commit('login', data.obj);
                var path = _this.$route.query.redirect;
                _this.$router
                  .replace({path: path == '/' || path == undefined ? '/home' : path});
              }
            });
          } else {
            return false;
          }
        })
      },
      reg: function () {
        this.dialogFormVisible = !this.dialogFormVisible;
      },
      regClick: function () {
        this.$refs['regForm'].validate((valid) => {
          if (valid) {

          } else return false;
        })
      }
    }
  }
</script>
<style>
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }

  .login_remember {
    margin: 0px 0px 35px 0px;
    text-align: left;
  }
</style>
