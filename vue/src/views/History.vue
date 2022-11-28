<template>
  <div>
    <div style="margin: 10px 0">
      <el-input
          v-model="gd"
          clearable
          placeholder="请输入工单号"
          style="width: 200px"
          @input="change($event)"
      >
      </el-input>
      <el-input
          v-model="pin"
          class="ml-5"
          clearable
          placeholder="请输入料号/PP"
          style="width: 200px"
          @input="change($event)"
      ></el-input>
      <el-date-picker
          v-model="ctime"
          class="ml-5"
          end-placeholder="结束日期"
          range-separator="至"
          start-placeholder="开始日期"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
          @change="saveTime"
      >
      </el-date-picker>
      <el-button
          icon="el-icon-search"
          style="margin-left: 5px"
          type="primary"
          @click="selectPage"
      >搜索
      </el-button
      >
      <el-button
          icon="el-icon-refresh"
          style="margin-left: 5px"
          type="primary"
          @click="reset"
      >重置
      </el-button
      >
    </div>

    <div style="margin: 10px 0">
      <el-button icon="el-icon-top" type="primary" @click="exportMsg">导出</el-button>
    </div>
    <el-table v-loading="loading"
              :data="tableData"
              :row-class-name="tableRowClassName"
              border
              style="width: 100%">
      <!-- <el-skeleton :rows="6"  animated /> -->
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
      <el-table-column align="center" label="类型" prop="type" width="60">
        <template slot-scope="scope">
          <span v-if="scope.row.type === '1'">料号</span>
          <span v-if="scope.row.type === '2'">PP</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="工单号" prop="gd">
      </el-table-column>
      <el-table-column align="center" label="料号" prop="pin">

      </el-table-column>
      <el-table-column
          align="center"
          label="PP型号"
          prop="ppType"
      >
        <template slot-scope="scope">
          <el-popover placement="top" trigger="hover">
            <span v-if="scope.row.type == '2'"
            >含胶量：{{ scope.row.rc }}</span
            >
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.ppType }}</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          label="尺寸（经*纬）"
          prop="size"
      >
      </el-table-column>
      <el-table-column
          align="center"
          label="实际重量"
          prop="acWt"
          width="70"
      ></el-table-column>
      <el-table-column align="center" label="最小值" prop="min" width="70">
      </el-table-column>
      <el-table-column align="center" label="最大值" prop="max" width="70">
      </el-table-column>

      <el-table-column
          :formatter="dateFormat"
          align="center"
          label="创建时间"
          prop="createTime"
      ></el-table-column>
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
  </div>
</template>

<script>
export default {
  name: "History",
  data() {
    return {
      tableData: [{}],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      gd: "",
      pin: "",
      ctime: [],
      startTime: "",
      endTime: "",
      loading: false
    };
  },
  methods: {
    dateFormat(row) {
      var t = new Date(row.createTime); //row 表示一行数据, updateTime 表示要格式化的字段名称
      return (
          t.getFullYear() +
          "-" +
          (t.getMonth() + 1) +
          "-" +
          t.getDate() +
          " " +
          t.getHours() +
          ":" +
          t.getMinutes() +
          ":" +
          t.getSeconds()
      );
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
    //加载数据
    load() {
      this.loading = true
      this.request
          .get("/home/findPage", {
            params: {
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              gd: this.gd,
              pin: this.pin,
              startTime: this.startTime,
              endTime: this.endTime,
            },
          })
          .then((res) => {
            this.tableData = res.data.records;
            this.total = res.data.total;
            this.loading = false
          });
    },
    // 查询数据
    selectPage(){
      this.pageNum = 1;
      this.load();
    },
    // 清空搜索框
    reset() {
      this.gd = "";
      this.pin = "";
      this.startTime = "";
      this.endTime = "";
    },
    change(e) {
      this.$forceUpdate(e);
    },
    //保存时间数据
    saveTime(ctime) {
      this.ctime = ctime;
      this.startTime = ctime[0];
      this.endTime = ctime[1];
    },
    // 导出
    exportMsg() {
      console.log(process.env.VUE_APP_BASE_API)
      window.open( process.env.VUE_APP_BASE_API + "/home/exHistory")
      // window.open("http://localhost:9090/api/home/exHistory")
    },
    // 设置数据为NG的显示状态
    tableRowClassName(row) {
      if (row.row.checkFlag === "NG") {
        return 'warning-row';
      }
    }
  },
  created() {
    //请求分页查询数据
    this.load();

  },
};
</script>

<style>
.el-table .warning-row {
  background: rgb(247, 236, 217);
}
</style>