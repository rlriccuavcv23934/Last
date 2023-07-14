<template>
  <div class="box">
    <el-button style="margin-bottom: 10px;" type="primary" @click="handleAdd">添加</el-button>

    <el-input v-model="findid" clearable maxlength="10" placeholder="输入学号进行搜索" style="padding-bottom: 20px"
              v-on:input="handleSelect"/>
    {{ findid }}
    <el-table v-loading="loading" :data="tableData" border style="width: 100%">

      <el-table-column align="center" label="学号">
        <template #default="scope">
          {{ scope.row.student_id }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="姓名">
        <template #default="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="密码">
        <template #default="scope">
          {{ scope.row.password }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="手机号码">
        <template #default="scope">
          {{ scope.row.phone }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="邮箱">
        <template #default="scope">
          {{ scope.row.mail }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.student_id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <el-dialog v-model="dialogStudentVisible" title="学生信息">
    {{ student }}
    <el-form :model="student">
      <el-form-item label="学号">
        <el-input v-model="student.student_id" autocomplete="off"/>
      </el-form-item>

      <el-form-item label="姓名">
        <el-input v-model="student.name" autocomplete="off"/>
      </el-form-item>

      <el-form-item label="密码">
        <el-input v-model="student.password" autocomplete="off"/>
      </el-form-item>

      <el-form-item label="手机号码">
        <el-input v-model="student.phone" autocomplete="off"/>
      </el-form-item>

      <el-form-item label="邮箱">
        <el-input v-model="student.mail" autocomplete="off"/>
      </el-form-item>

    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogStudentVisible = false">取消</el-button>
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
      temp: true,
      loading: false,
      dialogStudentVisible: false,
      tableData: [{student_id: 1999010101, name: "test", password: "test", phone: "test", mail: "test"}],
      student: {
        student_id: null, name: null, password: null, phone: null, mail: null
      }

    }
  },
  methods: {
    loadStudent() {
      axios.get('/edu/students')
          .then((response) => {
            this.tableData = response.data.data;
            this.loading = false;
          }).catch((error) => {
      });
    },

    handleAdd() {

      this.student = {
        student_id: null, name: null, password: null, phone: null, mail: null
      },
          this.dialogStudentVisible = true;
      this.temp = true;
    },


    handleEdit(row) {
      this.student = JSON.parse(JSON.stringify(row));
      this.dialogStudentVisible = true;
      this.temp = false;
    },

    save() {

      if (this.temp == false) {
        //编辑操作
        axios.post('/edu/students/' + this.student.student_id, "name=" + this.student.name
            + "&password=" + this.student.password + "&phone=" + this.student.phone
            + "&mail=" + this.student.mail + "&_method=put").then((response) => {
          if (response.data.code == 200) {
            this.dialogStudentVisible = false;
            //重新加载学科信息
            this.loadStudent();
          }
        }).catch((error) => {
          console.log(error);
        });
      } else {
        let param = qs.stringify(this.student);
        axios.post('/edu/students/', param).then((response) => {
          if (response.data.code == 200) {
            this.dialogStudentVisible = false;

            //重新加载学科信息
            this.loadStudent();
          }
        }).catch((error) => {
          console.log(error);
        });
      }
    },

    handleDelete(student_id) {
      //发送delete
      axios.delete('/edu/students/' + student_id).then((response) => {
        if (response.data.code == 200) {
          //重新加载学科信息
          this.loadStudent();
        }
      }).catch((error) => {
        console.log(error);
      });
    },
    handleSelect() {
      console.log(this.findid);
      axios.post('/edu/students/serech', "id=" + this.findid).then((response) => {
        if (response.data.code == 200) {
          //重新加载信息
          axios.get('/edu/students/serech')
              .then((response) => {
                this.tableData = response.data.data;
                this.loading = false;
              }).catch((error) => {
          });

        }
      }).catch((error) => {
        console.log(error);
      });
    }
  },
  created() {
    this.loadStudent();
  }
}
</script>