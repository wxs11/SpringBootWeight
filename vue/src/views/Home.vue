<template>
  <div style="display: flex">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-row>
        <el-col :span="8">
          <el-form-item label="工单：" prop="gd">
            <el-input
              v-model="form.gd"
              clearable
              style="width: 200px"
              @keyup.enter.native="enterClick"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="料号：">
            <el-input
              v-model="form.pin"
              disabled
              style="width: 200px"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :offset="2" :span="8">
          <el-form-item label="经*纬：">
            <el-input
              v-model="form.size"
              disabled
              style="width: 200px"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <el-form-item label="称重类型：">
            <el-select
              v-model="form.type"
              clearable
              filterable
              placeholder="请选择"
              size="small"
              style="width: 180px"
              @change="handlePPType"
            >
              <el-option
                v-for="item in typeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item label="PP型号：" label-width="180px">
            <el-select
              v-model="form.ppType"
              :disabled="dis"
              clearable
              filterable
              placeholder="请选择"
              size="small"
              style="width: 180px"
              @change="selectOne"
              @focus="getPPOptions"
            >
              <el-option
                v-for="item in ppOptions"
                :key="item[1]"
                ref="mySelected"
                :label="item[0]"
                :value="item[1]"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="标准重量区间：" prop="min">
            <el-input v-model="form.min" disabled style="width: 150px">
              <template slot="append">G</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :offset="2" :span="1" class="line">-</el-col>
        <el-col :span="8">
          <el-input v-model="form.max" disabled style="width: 150px">
            <template slot="append">G</template>
          </el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="实际重量：">
            <el-input v-model="form.acWt" disabled style="width: 200px">
              <template slot="append">G</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="8">
          <span
            type="text"
            title="form.checkFlag"
            :style="changeBut(form.checkFlag)"
            style="font-size: 50px"
            size="200px"
            :v-model="textVu"
            >{{ textVu }}</span
          >
        </el-col>
      </el-row>
      <el-button
        style="margin-left: 200px; width: 100px"
        type="primary"
        @click="save('form')"
        >保存
      </el-button>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      form: {
        gd: "",
        size: "",
        pin: " ",
        ppType: "",
        rc: "",
        type: "",
        min: "",
        max: "",
        acWt: "",
        checkFlag: "", //验证是否符合标准 OK符合 NG不符合
      },
      rules: {
        gd: [{ required: true, message: "请扫描工单", trigger: "blur" }],
        min: [{ required: true, message: "请选择数据", trigger: "blur" }],
      },
      typeOptions: [
        {
          value: "1",
          label: "PP",
        },
        {
          value: "2",
          label: "料号",
        },
      ],
      ppOptions: [],
      pin: "",
      xz: 0,
      dis: false,
      textVu: "",
    };
  },
  created() {
    //将料号类型设定为默认PP
    this.form.type = this.typeOptions[0].value;
    //每隔1500毫秒获取一次重量数据
    setInterval(this.getAcWT, 1000);
  },

  //对acWt 实际重量进行实时监听 如果变化则判断
  watch: {
    "form.acWt": {
      handler(val, oldval) {
        if (this.form.min != null && this.form.min.length != 0) {
          var min = parseInt(this.form.min);
          var max = parseInt(this.form.max);
          var acWt = parseInt(val);
          if (acWt > 5) {
            if (acWt >= min && acWt <= max) {
              this.form.checkFlag = "OK";
              this.$message({
                message: "重量符合要求",
                type: "success",
                
              });
            } else if (acWt < min) {
              this.form.checkFlag = "NG";
              this.textVu = "超下限";
              this.$message({
                message: "重量不符要求",
                type: "warning",
              });
            } else {
              this.form.checkFlag = "NG";
              this.textVu = "超上限";
              this.$message({
                message: "重量不符要求",
                type: "warning",
              });
            }
              this.save('form')
          }
        }
      },
      deep: true,
      immediate: false,
    },
  },
  methods: {
    save(form) {
      //保存数据
      this.$refs[form].validate((valid) => {
        if (valid) {
            this.request.post("/home/save", this.form).then((res) => {
              if (res.code != 201) {
                this.$notify({
                  title: "成功",
                  message: "保存成功",
                  type: "success",
                  duration: 3000,
                });
              } else {
                this.$notify.error({
                  title: "错误",
                  message: "保存失败",
                  duration: 3000,
                });
              }
            });
        }
      });
    },
    //获取sap对应工单数据
    enterClick() {
      this.request
        .get("/home/sap", {
          params: {
            gd: this.form.gd,
          },
        })
        .then((res) => {
          console.log(res); //查看从sap获取的数据
          if (res.code === 201) {
            this.$notify({
              title: "警告",
              message: "没有该工单数据",
              type: "warning",
            });
          } else {
            this.form.pin = res.data.MATNR;
            this.form.size = res.data.PNLSIZE;
          }
        });
    },
    //查询对应料号的重量区间
    handlePPType() {
      if (this.form.type === "2") {
        this.dis = true;
        this.request
          .get("/msg/getBzWt", {
            params: {
              id: "",
              pin: this.form.pin,
            },
          })
          .then((res) => {
            if (res.data.length === 0) {
              this.form.min = "";
              this.form.max = "";
              this.form.rc = "";
              this.form.ppType = "";
              this.$message({
                message: "没有当前数据",
                type: "warning",
                duration: 3500,
              });
            } else {
              this.form.min = res.data[0].min;
              this.form.max = res.data[0].max;
              this.form.rc = res.data[0].rc;
              this.form.ppType = res.data[0].ppType;
            }
          });
      } else {
        this.dis = !this.dis;
      }
    },
    //获取数据库对应PP下拉框数据
    getPPOptions() {
      this.request
        .get("/msg/getPPMaps", {
          params: {
            size: this.form.size,
          },
        })
        .then((res) => {
          // console.log('总条'+res.data)
          this.ppOptions.length = 0;
          var result = res.data;
          for (var i in result) {
            var pp = result[i].split("+");
            this.ppOptions.push(pp);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    //选择PP下拉框数据后将数据放入对应输入框
    selectOne() {
      this.request
        .get("/msg/getBzWt", {
          params: {
            id: this.form.ppType,
          },
        })
        .then((res) => {
          this.form.min = res.data[0].min;
          this.form.max = res.data[0].max;
          this.form.rc = res.data[0].rc;
          this.form.ppType = res.data[0].ppType;
        });
    },
    getAcWT() {
      this.request.get("/msg/getAcWt").then((res) => {
        //判断传入值是否与后台值一致
        var newN = parseInt( res.data);
        // console.log("xz：", this.xz, newN, this.cTime);
        const min = parseInt(this.xz) - 1;
        const max = parseInt(this.xz) + 1;
        if (newN >= min && newN <= max) {
          this.form.acWt = newN
        } else {
          this.xz = newN;
        }
      });
    },
    changeBut() {
      if (this.form.checkFlag == "NG") {
        return "color:red";
      } else {
        return "color:green";
      }
    },
  },
};
</script>

<style>
</style>