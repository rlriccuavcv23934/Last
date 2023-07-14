<template>
  <div class="box">
    <el-button type="primary" style="margin-bottom: 10px;" @click="handleAdd">添加</el-button>
    <el-input v-model="findid" v-on:input="handleSelect" placeholder="输入课程号进行搜索" maxlength="10" clearable
              style="padding-bottom: 20px"/>
    {{ findid }}
    <el-table :data="tableData" v-loading="loading" border style="width: 100%">

      <el-table-column label="课程号" align="center">
        <template #default="scope">
          {{ scope.row.subject_id }}
        </template>
      </el-table-column>

      <el-table-column label="课程名" align="center">
        <template #default="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>

      <el-table-column label="老师编号" align="center">
        <template #default="scope">
          {{ scope.row.teacher_id }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.subject_id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <el-dialog v-model="dialogSubjectVisible" title="课程录入">
    {{ subject }}
    <el-form :model="subject">
      <el-form-item label="课程名">
        <el-input v-model="subject.name" autocomplete="off"/>
      </el-form-item>
    </el-form>

    <el-form :model="subject">
      <el-form-item label="教师编号">
        <el-input v-model="subject.teacher_id" autocomplete="off"/>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogSubjectVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
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
      dialogSubjectVisible: false,
      tableData: [],
      subject: {
        subject_id: null, name: null, teacher_id: null
      }

    }
  },
  methods: {
    loadSubject() {
      axios.get('/edu/subjects')
          .then((response) => {
            this.tableData = response.data.data;
            this.loading = false;
          }).catch((error) => {
      });
    },

    handleAdd() {

      this.subject = {
        subject_id: null, name: null, teacher_id: null
      },

          this.dialogSubjectVisible = true;
    },


    handleEdit(row) {
      this.subject = JSON.parse(JSON.stringify(row));
      this.dialogSubjectVisible = true;
    },

    save() {

      if (this.subject.subject_id) {
        //编辑操作
        axios.post('/edu/subjects/' + this.subject.subject_id, "&name=" + this.subject.name + "&teacher_id=" + this.subject.teacher_id + "&_method=put").then((response) => {
          if (response.data.code == 200) {
            this.dialogSubjectVisible = false;
            //重新加载学科信息
            this.loadSubject();
          }
        }).catch((error) => {
          console.log(error);
        });
      } else {
        let param = qs.stringify(this.subject);
        axios.post('/edu/subjects/', param).then((response) => {
          if (response.data.code == 200) {
            this.dialogSubjectVisible = false;
            //重新加载学科信息
            this.loadSubject();
          }
        }).catch((error) => {
          console.log(error);
        });
      }
    },

    handleDelete(id) {

      //发送delete
      axios.delete('/edu/subjects/' + id).then((response) => {
        if (response.data.code == 200) {
          //重新加载学科信息

          this.loadSubject();
        }
        if (response.data.code == 500) {
          this.$message({
            //TODO:
            message: response.data.msg,
            type: 'warning'
          });
        }
      }).catch((error) => {
        console.log(error);
      });
    },
    handleSelect() {//课程号搜索
      console.log(this.findid);
          //重新加载信息
          axios.get('/edu/subjects/'+ this.findid)
              .then((response) => {
                this.tableData = response.data.data;
                this.loading = false;
              }).catch((error) => {
          });
    },
  },

  created() {
    this.loadSubject();
  }
}
</script>