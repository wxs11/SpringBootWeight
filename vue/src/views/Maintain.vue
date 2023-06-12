<template>
  <div>
    <div style="margin: 10px 0">
      <el-input
        v-model="ppType"
        class="ml-5"
        clearable
        placeholder="请输入料号/PP"
        style="width: 200px"
        @input="change($event)"
      ></el-input>
      <el-button
        icon="el-icon-search"
        style="margin-left: 5px"
        type="primary"
        @click="selectPage"
        >搜索
      </el-button>
      <el-button
        icon="el-icon-refresh"
        style="margin-left: 5px"
        type="primary"
        @click="reset"
        >重置
      </el-button>
    </div>

    <div style="margin: 10px 0">
      <!-- <el-button type="danger" icon="el-icon-delete">批量删除</el-button> -->
      <el-upload
        :on-success="handleExcelImportSuccess"
        :on-error="handleExcelImportError"
        :show-file-list="false"
        accept=".xlsx"
        :action="uploadUrl"
        style="display: inline-block; margin-right: 5px"
      >
        <el-button icon="el-icon-bottom" type="primary">导入</el-button>
      </el-upload>
      <el-button icon="el-icon-top" type="primary" @click="expMsg"
        >导出
      </el-button>
      <el-button icon="el-icon-plus" type="primary" @click="handleSaveOne"
        >新增
      </el-button>
    </div>
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      stripe
      style="width: 100%"
    >
      <el-table-column
        align="center"
        label="序号"
        prop="id"
        type="index"
        width="60"
      >
        <template slot-scope="scope">
          <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>    
        </template>
      </el-table-column>
      <el-table-column align="center" label="类型" prop="type" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.type === '1'">PP</span>
          <span v-if="scope.row.type === '2'">料号</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="PP型号" prop="ppType">
        <template slot-scope="scope">
          <el-popover placement="top" trigger="hover">
            <span v-if="scope.row.type === '1'"
              >含胶量：{{ scope.row.rc }}</span
            >
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.ppType }}</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column align="center" label="尺寸（经*纬）" prop="size">
      </el-table-column>
      <el-table-column align="center" label="最小值" prop="min">
      </el-table-column>
      <el-table-column align="center" label="最大值" prop="max">
      </el-table-column>

      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-edit"
            type="success"
            @click="handleEdit(scope.row)"
            >编辑
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            @confirm="handleDelete(scope.row.id)"
          >
            <el-button
              slot="reference"
              icon="el-icon-delete"
              style="margin-left: 5px"
              type="danger"
              >删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
        :current-page="pageNum"
        :page-size="pageSize"
        :page-sizes="[5, 10, 15, 20]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>

    <!-- 新增 -->
    <el-dialog :visible.sync="dialogFormSave" title="新增数据" width="30%">
      <el-form
        :model="saveForm"
        :rules="rules"
        ref="saveForm"
        label-width="120px"
        size="small"
      >
        <el-form-item label="类型" prop="type">
          <el-select v-model="saveForm.type" style="width: 210px" clearable>
            <el-option label="PP" value="1"></el-option>
            <el-option label="料号" value="2"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="PP型号/料号" prop="ppType">
          <el-input
            v-model="saveForm.ppType"
            autocomplete="off"
            style="width: 210px"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item
          label="尺寸（经*纬）"
          prop="size"
          v-show="this.saveForm.type == 1"
        >
          <el-input
            v-model="saveForm.size"
            autocomplete="off"
            clearable
            style="width: 210px"
          ></el-input>
        </el-form-item>
        <el-form-item label="含胶量" prop="rc" v-show="this.saveForm.type == 1">
          <el-input
            v-model="saveForm.rc"
            autocomplete="off"
            clearable
            style="width: 210px"
          ></el-input>
        </el-form-item>
        <el-form-item label="最小值" prop="min">
          <el-input
            v-model="saveForm.min"
            autocomplete="off"
            clearable
            style="width: 210px"
          ></el-input>
        </el-form-item>
        <el-form-item label="最大值" prop="max">
          <el-input
            v-model="saveForm.max"
            clearable
            autocomplete="off"
            style="width: 210px"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelSave">取 消</el-button>
        <el-button type="primary" @click="saveOne('saveForm')">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改维护 -->
    <el-dialog :visible.sync="dialogFormVisible" title="数据维护" width="35%">
      <el-form
        :model="form"
        label-width="120px"
        size="small"
        :rules="rules"
        ref="form"
      >
        <el-form-item label="类型">
          <el-select v-model="form.type" style="width: 210px">
            <el-option label="PP" value="1"></el-option>
            <el-option label="料号" value="2"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="PP型号/料号" prop="ppType">
          <el-input
            v-model="form.ppType"
            autocomplete="off"
            style="width: 210px"
          ></el-input>
        </el-form-item>
        <el-form-item label="尺寸（经*纬）">
          <el-input
            v-model="form.size"
            autocomplete="off"
            style="width: 210px"
          ></el-input>
        </el-form-item>
        <el-form-item label="含胶量">
          <el-input
            v-model="form.rc"
            autocomplete="off"
            style="width: 210px"
          ></el-input>
        </el-form-item>
        <el-form-item label="最小值" prop="min">
          <el-input
            v-model="form.min"
            autocomplete="off"
            style="width: 210px"
          ></el-input>
        </el-form-item>
        <el-form-item label="最大值" prop="max">
          <el-input
            v-model="form.max"
            autocomplete="off"
            style="width: 210px"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save('form')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Maintain",
  data() {
    var minRule = (rule, value, callback) => {
      const reg = /^([0-9]|[1-9]\d+$)/;
      if (!value) {
        callback(new Error("数值不能为空"));
      }
      if (!reg.test(value)) {
        callback(new Error("请输入正整数"));
      } else {
        callback();
      }
    };
    var maxRule = (rule, value, callback) => {
      const reg = /^([0-9]|[1-9]\d+$)/;
      if (!value) {
        callback(new Error("数值不能为空"));
      }
      if (!reg.test(value)) {
        callback(new Error("请输入正整数"));
      }
      if (
        Number(this.saveForm.min) > Number(value) ||
        Number(this.form.min > Number(value))
      ) {
        callback(new Error("最小值不能大于最大值"));
      } else {
        callback();
      }
    };
    return {
      tableData: [{}],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      ppType: "",
      dialogFormVisible: false,
      dialogFormSave: false,
      form: {},
      saveForm: {},
      loading: false,
      uploadUrl: process.env.VUE_APP_BASE_API + "/msg/importPP",

      rules: {
        type: [{ required: true, message: "请选择", trigger: "blur" }],
        ppType: [
          { required: true, message: "请输入PP或料号", trigger: "blur" },
        ],
        min: [{ required: true, validator: minRule, trigger: "blur" }],
        max: [{ required: true, validator: maxRule, trigger: "blur" }],
      },
    };
  },
  created() {
    this.load();
  },
  methods: {
    //加载查询数据
    load() {
      this.loading = true;
      this.request
        .get("/msg/list", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            ppType: this.ppType,
          },
        })
        .then((res) => {
          this.tableData = res.data.records;
          this.total = res.data.total;
          this.loading = false;
        });
    },
    // 查询数据
    selectPage() {
      this.pageNum = 1;
      this.load();
    },
    //修改数据
    save(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          this.request.post("/msg/save", this.form).then((res) => {
            if (res) {
              this.$notify({
                title: "成功",
                message: "成功",
                type: "success",
              });
              this.load();
              this.dialogFormVisible = false;
            } else {
              this.$notify.error({
                title: "错误",
                message: "保存失败",
              });
            }
          });
        }
      });
    },
    //校验新增口令
    handleSaveOne() {
      this.$prompt("请输入密码口令", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputType: "password",
        inputErrorMessage: "密码不正确",
        inputValidator: (value) => {
          if (!value) {
            return "输入不能为空";
          }
          if (value !== "123456") {
            return "密码不正确";
          }
        },
      })
        .then(({ value }) => {
          this.$message({
            type: "success",
            message: "验证成功",
          });
          this.dialogFormSave = true;
        })
        .catch(() => {
          // 取消
        //  console.log(e)
        });
    },
    // 新增一条数据
    saveOne(saveForm) {
      this.$refs[saveForm].validate((valid) => {
        console.log(this.saveForm);
        if (valid) {
          if (
            this.saveForm.type == "1" &&
            this.saveForm.rc == "" &&
            this.saveForm.rc == null
          ) {
            this.$message({
              type: "warning",
              message: "PP含胶量不能为空",
            });
          } else {
            this.request.post("/msg/saveOne", this.saveForm).then((res) => {
              if (res.data == 1) {
                this.$notify({
                  title: "成功",
                  message: "成功",
                  type: "success",
                });
                this.cancelSave();
                this.load();
              } else {
                this.$notify.error({
                  title: "错误",
                  message: "数据库中已存在当前数据",
                });
              }
            });
          }
        }
      });
    },
    //新增窗口关闭
    cancelSave() {
      this.dialogFormSave = false;
      this.saveForm = {};
    },
    // 清空查询输入框
    reset() {
      this.ppType = "";
    },

    handleEdit(row) {
      this.$prompt("请输入密码口令", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputType: "password",
        inputErrorMessage: "密码不正确",
        inputValidator: (value) => {
          if (!value) {
            return "输入不能为空";
          }
          if (value !== "123456") {
            return "密码不正确";
          }
        },
      })
        .then(({ value }) => {
          this.$message({
            type: "success",
            message: "验证成功",
          });
          this.form = JSON.parse(JSON.stringify(row));
          this.dialogFormVisible = true;
        })

        .catch(() => {
     
        });
    },
    //删除数据
    handleDelete(id) {
      this.$prompt("请输入密码口令", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputType: "password",
        inputErrorMessage: "密码不正确",
        inputValidator: (value) => {
          if (!value) {
            return "输入不能为空";
          }
          if (value !== "123456") {
            return "密码不正确";
          }
        },
      })
        .then(({ value }) => {
          this.$message({
            type: "success",
            message: "验证成功",
          });
          this.request.delete("/msg/deleteById/" + id).then((res) => {
            if (res) {
              this.$notify({
                title: "成功",
                message: "删除成功",
                type: "success",
              });
              this.load();
            } else {
              this.$notify.error({
                title: "错误",
                message: "删除失败",
              });
            }
          });
        })
        .catch(() => {
  
        });
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
      // console.log(`每页 ${pageSize} 条`);
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
      // console.log(`当前页: ${pageNum}`);
    },
    change(e) {
      this.$forceUpdate(e);
    },
    // 导出数据
    expMsg() {
      var production = "http://172.16.18.63:9090/api"; // 线上 (生成环境)
      var development = "http://localhost:9090/api"; // 本地 (开发环境)
      var url =
        process.env.NODE_ENV === "production" ? production : development;
      window.open(url + "/msg/exMsg");
    },
    // 导入
    handleExcelImportSuccess() {
      this.$notify({
        title: "成功",
        message: "导入成功",
        type: "success",
      });
      this.load();
    },
    handleExcelImportError() {
      this.$notify({
        title: "失败",
        message: "导入失败",
        type: "error",
      });
    },
  },
};
</script>

<style>
</style>