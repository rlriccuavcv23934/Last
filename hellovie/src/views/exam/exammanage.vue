<template>
  <div class="box">
    <el-button style="margin-bottom: 10px;" type="primary" @click="handleAdd">添加</el-button>
    <el-input v-model="findid" clearable maxlength="10" placeholder="输入考试编号进行搜索" style="padding-bottom: 20px"
              v-on:input="handleSelect"/>
    {{ findid }}
    <el-table v-loading="loading" :data="tableData" border style="width: 100%">

      <el-table-column align="center" label="课程号">

        <!--  TODO      label考试编号-->
        <template #default="scope">
          {{ scope.row.course_id }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="考试名称">
        <template #default="scope">
          {{ scope.row.exam_name }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="老师编号">
        <template #default="scope">
          {{ scope.row.teacher_id }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="课室">
        <template #default="scope">
          {{ scope.row.room }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="开始时间">
        <template #default="scope">
          {{ scope.row.start_time }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="结束时间">
        <template #default="scope">
          {{ scope.row.end_time }}
        </template>
      </el-table-column>


      <el-table-column align="center" label="操作">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.course_id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <el-dialog v-model="dialogExamDataVisible" title="考试信息">
    {{ examData }}
    <el-form :model="examData">

      <el-form-item label="考试名称">
        <el-input v-model="examData.exam_name" autocomplete="off"/>
      </el-form-item>

      <el-form-item label="课室">
        <el-input v-model="examData.room" autocomplete="off"/>
      </el-form-item>

      <el-form-item label="考试时间设定">
        <el-date-picker
            v-model="examData.time"
            end-placeholder="End date"
            format="YYYY/MM/DD hh:mm:ss"
            range-separator="To"
            start-placeholder="Start date"

            type="datetimerange"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogExamDataVisible = false">取消</el-button>
        <el-button type="primary" @click="save">提交</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.box {
  padding: 10px;
}
</style>
<script>
import axios from "axios"
import qs from "qs"

export default {
  name: "List",
  data() {
    return {
      findid: null,
      loading: true,
      //TODO:
      dialogExamDataVisible: false,
      tableData: [],
      examData: {
        course_id: null, exam_name: null, teacher_id: null, room: null, time: null,
      }

    }
  },
  methods: {
    loadExamData() {
      axios.get('/edu/schedules')
          .then((response) => {
            this.tableData = response.data.data;
            this.loading = false;
          }).catch((error) => {
      });
    },
    handleAdd() {

      this.examData = {
        course_id: null, exam_name: null, teacher_id: '13', room: null, time: null,
      },

          this.dialogExamDataVisible = true;
    },


    handleEdit(row) {
      this.examData = JSON.parse(JSON.stringify(row));
      this.dialogExamDataVisible = true;
    },

    save() {

      if (this.examData.course_id) {
        //编辑操作
        axios.post('/edu/schedules/' +
            this.examData.course_id, "exam_name=" + this.examData.exam_name + "&teacher_id=" + this.examData.teacher_id + "&room=" + this.examData.room + "&start_time=" + this.examData.time[0] + "&end_time=" + this.examData.time[1] + "&_method=put").then((response) => {
          if (response.data.code == 200) {
            this.dialogExamDataVisible = false;
            //重新加载学科信息
            this.loadExamData();
          }
        }).catch((error) => {
          console.log(error);
        });
      } else {
        let param = qs.stringify(this.examData);
        axios.post('/edu/schedules/', param).then((response) => {
          if (response.data.code == 200) {
            this.dialogExamDataVisible = false;
            //重新加载学科信息
            this.loadExamData();
          }
        }).catch((error) => {
          console.log(error);
        });
        this.tableData.push(this.examData)
      }
    },

    handleDelete(course_id) {
      //发送delete
      axios.delete('/edu/schedules/' + course_id).then((response) => {
        if (response.data.code == 200) {
          //重新加载学科信息
          this.loadExamData();
        }
      }).catch((error) => {
        console.log(error);
      });
    },
    handleSelect() {//题目号搜索
      console.log(this.findid);
      axios.post('/edu/schedules/serech', "id=" + this.findid).then((response) => {
        if (response.data.code == 200) {
          //重新加载信息
          axios.get('/edu/schedules/serech')
              .then((response) => {
                this.tableData = response.data.data;
                this.loading = false;
              }).catch((error) => {
          });

        }
      }).catch((error) => {
        console.log(error);
      });
    },
  },


  created() {
    this.loadExamData();
  }
}
</script>