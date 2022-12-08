<template>
  <div style="display: flex">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px"  >
      <el-row>
        <el-col :span="8">
          <el-form-item label="工单：" prop="gd"  >
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
        <el-col  :span="8">
          <el-form-item label="经*纬：" label-width="100px">
            <el-input
              v-model="form.size"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <el-form-item label="称重类型：">
            <el-input
            v-if="(handleTypeLable ==1)"
              type="text"
              size="small"
              @change="handlePPType"
              @click.native="changeLabel"
              value="PP"
            >
            </el-input>
            <el-input
              type="text"
              size="small"
              @change="handlePPType"
              @click.native="changeLabel"
              v-else
              value="料号"
            >
            </el-input>
            
          </el-form-item>
        </el-col>

        <el-col :span="8" >
          <el-form-item label="PP型号：" label-width="100px">
            <el-select
              v-model="form.ppType"
              :disabled="dis"
              clearable
              filterable
              placeholder="请选择"
              size="small"
              @focus="getPPOptions"
            >
              <el-option
                v-for=" item in ppOptions"
                :key="item.id"
                ref="mySelected"
                :label="item.label"
                :value="item.label"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      
        <el-col :span="8" >
          <el-form-item label="PP含胶量：" label-width="100px">
            <el-select
              v-model="form.rc"
              :disabled="dis"
              clearable
              filterable
              placeholder="请选择"
              size="small"
              style="width: 180px"
              @change="selectOne"
              @focus="getRcOptions"
            >
              <el-option
                v-for="item in rcOptions"
                :key="item.id"
                ref="mySelected"
                :label="item.label"
                :value="item.label"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="标准重量区间：" prop="min">
            <el-input v-model="form.min" disabled style="width: 150px;">
              <template slot="append">G</template>
            </el-input>
            <span class="line" style="margin-left: 20px;">-</span>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="1" class="line">-</el-col> -->
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
          <el-button
        style="margin-left: 200px; width: 100px"
        type="primary"
        @click="save('form')"
        >保存
      </el-button>
        </el-col>
        <el-col :span="18" :offset="18">
          <audio controls="controls" hidden src="../assets/error.mp3" ref="audio"></audio>
          <span
            type="text"
            title="form.checkFlag"
            :style="changeBut(form.checkFlag)"
            style="font-size: 100px "
            size="1000px"
            :v-model="textVu"
            >{{ textVu }}</span
          >
        </el-col>
      </el-row>
      
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
        type: "1",
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
      rcOptions: [],
      pin: "",
      handleTypeLable:1,
      xz: 0,
      dis: false,
      textVu: "",
    };
  },
  created() {
    //每隔500毫秒获取一次重量数据
    setInterval(this.getAcWT, 500);
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
              this.textVu = "重量符合";
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
              this.$refs.audio.currentTime = 0; //从头开始播放提示音
              this.$refs.audio.play(); //播放
            } else {
              this.form.checkFlag = "NG";
              this.textVu = "超上限";
              this.$message({
                message: "重量不符要求",
                type: "warning",
              });
              this.$refs.audio.currentTime = 0; //从头开始播放提示音
              this.$refs.audio.play(); //播放
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
           //查看从sap获取的数据
          // console.log(res);
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
    //下拉框点击切换  1 PP 2料号
    changeLabel(){
      if( this.form.type === "1"){
        this.form.type = "2"
        this.form.ppType = ""
        this.form.rc = ""
        this.form.min= ""
        this.form.max= ""
        this.handlePPType()
        this.handleTypeLable = 2
      }else{
        this.form.type = "1"
        this.handlePPType()
        this.handleTypeLable = 1
      }
      
    },
    //查询对应料号的重量区间
    handlePPType() {
      if (this.form.type === "2") {
        this.dis = true;
        this.request
          .get("/msg/getBzWt", {
            params: {
              ppType: this.form.pin,
              rc:"",
              size:""
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
                duration: 3000,
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
        .get("/msg/getPPTypes", {
          params: {
            size: this.form.size,
          },
        })
        .then((res) => {
          this.ppOptions.length = 0;
          var result = res.data;
          for(var i in result){
            this.ppOptions.push({id:i, label:result[i]})
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    //获取数据库对应PP含胶量rc值
    getRcOptions(){
      this.request.get("msg/getRcs",{
        params: {
          ppType:this.form.ppType,
          size : this.form.size
        }
      }).then(res =>{
        this.rcOptions.length = 0;
        var result = res.data;
          for(var i in result){
            this.rcOptions.push({id:i,label:result[i]})
          }
      })
    },
    //选择PP下拉框数据后将数据放入对应输入框
    selectOne() {
      this.request
        .get("/msg/getBzWt", {
          params: {
            ppType: this.form.ppType,
            rc:this.form.rc,
            size:this.form.size
          },
        })
        .then((res) => {
          // console.log(res)
          if(res.data.length == 0){
            this.$message({
                message: "没有当前数据",
                type: "warning",
                duration: 3000,
              });
          }else{
          this.form.min = res.data[0].min;
          this.form.max = res.data[0].max;
          }
        });
    },
    getAcWT() {
      //从缓存取出称重值
      var res = JSON.parse(localStorage.getItem("acwt"));
        //判断传入值是否一致
        var newN = parseInt(res);
        // console.log("xz：", this.xz, newN, this.cTime);
        const min = parseInt(this.xz) - 1;
        const max = parseInt(this.xz) + 1;
        if (newN >= min && newN <= max) {
          this.form.acWt = newN
        } else {
          this.xz = newN;
        }
        //清除acwt缓存
      localStorage.removeItem("acwt");
    },
    //后端
    // getAcWT() {
    //   this.request.get("/msg/getAcWt").then((res) => {
    //     //判断传入值是否与后台值一致
    //     var newN = parseInt( res.data);
    //     // console.log("xz：", this.xz, newN, this.cTime);
    //     const min = parseInt(this.xz) - 1;
    //     const max = parseInt(this.xz) + 1;
    //     if (newN >= min && newN <= max) {
    //       this.form.acWt = newN
    //     } else {
    //       this.xz = newN;
    //     }
    //   });
    // },
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
.item .el-form-item__label{
    font-size: 20px;
    font-family: "MicrosoftYaHeiLight", "微软雅黑 Light", "微软雅黑", sans-serif;
}
</style>