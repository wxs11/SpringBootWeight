<template>
  <div>
    <el-row
      type="flex"
      class="row-bg"
      justify="center"
      v-show="portsList.length == 0"
    >
      <el-col :span="7"
        ><div style="margin-top: 400px">
          <span style="display: block">
            仅支持Chrome 89+或者Edge 89+浏览器(安全上下文（HTTPS）中可用)
          </span>
          <el-button type="primary" @click="obtainAuthorization"
            >授权</el-button
          >
        </div></el-col
      >
    </el-row>
    <el-form
      v-show="portsList.length > 0"
      ref="form"
      :model="form"
      label-width="100px"
    >
      <el-row>
        <el-col :span="6"
          ><div>
            <el-form-item label="串口">
              <el-select
                v-model="form.port"
                filterable
                placeholder="请选择串口"
                :disabled="isDisable"
              >
                <el-option
                  v-for="item in portsList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="波特率">
              <el-autocomplete
                popper-class="my-autocomplete"
                v-model="form.baudRate"
                :fetch-suggestions="querySearch"
                placeholder="请输入波特率"
                :disabled="isDisable"
              >
                <i class="el-icon-edit el-input__icon" slot="suffix"> </i>
                <template slot-scope="{ item }">
                  <div class="name">{{ item.value }}</div>
                  <span class="addr">{{ item.address }}</span>
                </template>
              </el-autocomplete>
            </el-form-item>
            <el-form-item label="数据位">
              <el-select
                v-model="form.dataBits"
                placeholder="请选择数据位"
                :disabled="isDisable"
              >
                <el-option label="7" value="7"></el-option>
                <el-option label="8" value="8"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="停止位">
              <el-select
                v-model="form.stopBits"
                placeholder="请选择停止位"
                :disabled="isDisable"
              >
                <el-option label="1" value="1"></el-option>
                <el-option label="2" value="2"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="校验位">
              <el-select
                v-model="form.parity"
                placeholder="请选择校验位"
                :disabled="isDisable"
              >
                <el-option label="None" value="none"></el-option>
                <el-option label="Even" value="even"></el-option>
                <el-option label="Odd" value="odd"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="流控制">
              <el-select
                v-model="form.flowControl"
                placeholder="请选择流控制"
                :disabled="isDisable"
              >
                <el-option label="None" value="none"></el-option>
                <el-option label="HardWare" value="hardware"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="显示历史">
              <el-switch
                v-model="form.isShowHistory"
                @change="loadHistory"
              ></el-switch>
              <el-button
                type="danger"
                icon="el-icon-delete"
                circle
                title="清空历史"
                @click="clearHistory"
              ></el-button>
            </el-form-item>
            <el-form-item label="发送区设置" v-show="isShowSendArea">
              <el-form-item label="发送格式">
                <el-radio-group v-model="form.type">
                  <el-radio label="1">ASCII</el-radio>
                  <el-radio label="2">HEX</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="发送信息">
                <el-input type="textarea" v-model="form.sendMsg"></el-input>
              </el-form-item>
              <el-button type="primary" @click="sendCommon">发送</el-button>
            </el-form-item>

            <el-form-item>
              <el-button :type="btnType" @click="connectBtn">{{
                btnText
              }}</el-button>
              <el-button type="info" @click="obtainAuthorization"
                >新增授权</el-button
              >
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="10"
          ><div>
            <el-form-item label="电子秤信息">
              <el-input
                type="textarea"
                v-model="form.desc"
                disabled
                :autosize="{ minRows: 21, maxRows: 25 }"
              ></el-input>
            </el-form-item>
            <el-form-item label="转换后信息">
              <el-input type="text" v-model="form.acwt" disabled></el-input>
            </el-form-item>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>
   
  <script>
import MySerialPort from "../utils/MySerialPort";
import USBDevice from "../utils/usb.json";
export default {
  data() {
    return {
      input: "",
      keepReading: true,
      form: {
        baudRate: "9600",
        dataBits: "8",
        stopBits: "1",
        parity: "none",
        flowControl: "none",
        desc: "",
        type: "1",
        isShowHistory: false,
        acwt: "",
      },
      btnType: "primary",
      btnText: "连接串口",
      restaurants: [],
      portsList: [],
      isShowSendArea: false,
      readType: 1,
      times: 0,
      temp: 0,
    };
  },
  mounted() {
    if ("serial" in navigator) {
      this.myserialport = new MySerialPort();
      this.getPorts();
      navigator.serial.addEventListener("connect", (e) => {
        this.$message.success("设备已连接");
        this.getPorts();
      });
      navigator.serial.addEventListener("disconnect", (e) => {
        this.$message.error("设备已断开");
      });
      this.restaurants = this.loadAll();
    } else {
      this.$message.error(
        "当前为HTTP模式或者浏览器版本过低，不支持网页连接串口"
      );
    }
  },
  computed: {
    isDisable() {
      return this.btnType == "danger";
    },
  },
  created() {},
  methods: {
    //接受数据的回调
    callBack(value) {
      if (this.form.isShowHistory) {
        this.form.desc = this.getArray();
        this.getAcwt();
        // this.form.acwt =
      } else {
        if (value.length > 0)
          this.form.desc = this.myserialport.hex2atostr(value);
      }
    },
    clearHistory() {
      this.form.desc = "";
      this.form.acwt = "";
      this.myserialport.state.readValue = [];
    },
    loadHistory() {
      if (this.form.isShowHistory) {
        this.form.desc = this.getArray().concat("");
      } else {
        let temp = this.getArray();
        if (temp.length > 0) this.form.desc = temp[1].concat("");
      }
    },
    readLi() {
      let readType = this.readType;
      return this.myserialport.state.readValue.map((items, index) => {
        const item = items.value;
        const type = items.type; // 1接收，2发送
        let body = [];
        if (item !== undefined) {
          let strArr = [];
          for (let hex of Array.from(item)) {
            // strArr.push(hex.toString(16).toLocaleUpperCase());
            strArr.push(hex);
          }
          // if (strArr.includes("D") && strArr.includes("A")) {
          //   if (strArr.indexOf("A") - strArr.indexOf("D") === 1) {
          //     strArr.splice(strArr.indexOf("D"), 1);
          //     strArr.splice(strArr.indexOf("A"), 1, <br key={0} />);
          //   }
          // }
          // strArr = strArr.map((item) => {
          //   if (typeof item === "string") {
          //     if (readType === 1) {
          //       return this.myserialport.hex2a(parseInt(item, 16));
          //     } else if (readType === 2) {
          //       return item + " ";
          //     }
          //   }
          //   return item;
          // });
          if (typeof strArr[strArr.length - 1] === "string") {
            strArr.push("\r\n");
          }
          body.push(strArr.join(""));
        }
        return body;
      });
    },
    //获取原始流
    getArray() {
      let res = this.myserialport.state.readValue;
      let body = [];
      if (res !== undefined) {
        for (let hex of Array.from(res)) {
          // debugger;
          // strArr.push(hex.toString(16).toLocaleUpperCase());
          body.push(hex);
        }
        // if (typeof body[body.length - 1] === "string") {
        //   body.push("\r\n");
        //   }
      }
      return body.join("");
    },
    //转换获取称重数据
    getAcwt() {
      let s = this.getArray();
      let list = s.split("+");
      //记录截取的字符串信息
      var kong = list[5].substring(0, 6);
      // console.log(kong)
      //判断是否包含空格 包含则不进行操作
      if (kong.includes(" ") == false) {
        let res = parseInt(list[5].substring(0, 6));
        // console.log("res:",res)
        //判断 如果传入值与上一次值相等 次数加一
        if (this.temp == res) {
          this.times++;
          // console.log("times:",this.times)
        } else {
          this.temp = res;
        }
        //如果相等的次数大于2则确认值并传给acwt
        if (this.times > 2) {
          this.form.acwt = this.temp;
          this.times = 0;
          //将称重值传入到缓存
          localStorage.setItem("acwt", JSON.stringify(this.form.acwt));
        }
      }
    },

    //连接
    async connectBtn() {
      if (this.btnType == "primary") {
        try {
          this.myserialport.state.baudRate = this.form.baudRate;
          this.myserialport.state.dataBits = this.form.dataBits;
          this.myserialport.state.stopBits = this.form.stopBits;
          this.myserialport.state.parity = this.form.parity;
          this.myserialport.state.flowControl = this.form.flowControl;
          await this.myserialport.openPort(this.form.port, true, this.callBack);
        } catch (error) {
          this.$message.error("串口连接失败！请检查串口是否已被占用");
        }
        if (this.myserialport.state.isOpen) {
          this.$message.success("串口连接成功");
          this.btnType = "danger";
          this.btnText = "关闭串口";
        }
      } else {
        this.myserialport.openPort(this.form.port, false, this.callBack);
        this.$message.success("串口关闭成功");
        this.btnType = "primary";
        this.btnText = "连接串口";
      }
    },
    //授权
    async obtainAuthorization() {
      if ("serial" in navigator) {
        console.log("The Web Serial API is supported.");
        if (!this.myserialport) this.myserialport = new MySerialPort();
        try {
          await this.myserialport.handleRequestPort();
          this.$message.success("串口授权成功");
          this.getPortInfo(this.myserialport.state.ports);
        } catch (error) {
          this.$message.warning("未选择新串口授权！");
        }
      } else {
        this.$message.error(
          "当前为HTTP模式或者浏览器版本过低，不支持网页连接串口"
        );
      }
    },
    //串口列表初始化
    getPortInfo(portList) {
      this.portsList = [];
      portList.map((port, index) => {
        const { usbProductId, usbVendorId } = port.getInfo();
        if (usbProductId === undefined || usbVendorId === undefined) {
          this.portsList.push({ label: "未知设备" + index, value: index });
        } else {
          const usbVendor = USBDevice.filter(
            (item) => parseInt(item.vendor, 16) === usbVendorId
          );
          let usbProduct = [];
          if (usbVendor.length === 1) {
            usbProduct = usbVendor[0].devices.filter(
              (item) => parseInt(item.devid, 16) === usbProductId
            );
          }
          this.portsList.push({ label: usbProduct[0].devname, value: index });
        }
      });
    },
    // 发送
    async sendCommon() {
      if (this.myserialport.state.isOpen) {
        if (this.form.sendMsg.length !== 0) {
          const writeType = this.form.type;
          let value = this.form.sendMsg;
          let arr = [];
          if (writeType == 1) {
            // ASCII
            for (let i = 0; i < value.length; i++) {
              arr.push(this.myserialport.a2hex(value[i]));
            }
          } else if (writeType == 2) {
            // HEX
            if (/^[0-9A-Fa-f]+$/.test(value) && value.length % 2 === 0) {
              for (let i = 0; i < value.length; i = i + 2) {
                arr.push(parseInt(value.substring(i, i + 2), 16));
              }
            } else {
              this.$message.error("格式错误");
              return;
            }
          }
          this.myserialport.writeText(arr);
        } else {
          this.$message.warning("请输入发送的信息");
        }
      } else {
        this.$message.warning("串口处于关闭状态，请连接串口");
      }
    },
    async getPorts() {
      await this.myserialport.getPorts();
      this.getPortInfo(this.myserialport.state.ports);
    },
    querySearch(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString
        ? restaurants.filter(this.createFilter(queryString))
        : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (
          restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) ===
          0
        );
      };
    },
    loadAll() {
      return [
        { value: "110" },
        { value: "300" },
        { value: "600" },
        { value: "1200" },
        { value: "2400" },
        { value: "4800" },
        { value: "7200" },
        { value: "9600" },
        { value: "14400" },
        { value: "19200" },
        { value: "28800" },
        { value: "38400" },
        { value: "56000" },
        { value: "57600" },
        { value: "76800" },
        { value: "115200" },
        { value: "230400" },
        { value: "460800" },
      ];
    },
  },
};
</script>
   
  <style scoped>
/* ::v-deep .el-textarea__inner {
    height: 320px !important;
    width: 80% !important;
  } */
</style>