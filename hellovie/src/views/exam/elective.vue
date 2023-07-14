<template>
  <div class="box">

    <el-button type="primary" style="margin-bottom: 10px;" @click="handleAdd">添加</el-button>
    <el-input v-model="findid" v-on:input="handleSelect" placeholder="输入课程号进行搜索" maxlength="10" clearable
              style="padding-bottom: 20px"/>
    {{ findid }}
    <el-table :data="tableData" v-loading="loading" border style="width: 100%">

      <el-table-column label="学号" align="center">
        <template #default="scope">
          {{ scope.row.student_id }}
        </template>
      </el-table-column>

      <el-table-column label="分数" align="center">
        <template #default="scope">
          {{ scope.row.score }}
        </template>
      </el-table-column>

      <el-table-column label="科目" align="center">
        <template #default="scope">
          {{ scope.row.subject_id }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center">
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

  <el-dialog v-model="dialogElectiveVisible" title="成绩录入">
    {{ elective }}
    <el-form :model="elective">
      <el-form-item label="学号">
        <el-input v-model="elective.student_id" autocomplete="off"/>
      </el-form-item>

      <el-form-item label="分数">
        <el-input v-model="elective.score" autocomplete="off"/>
      </el-form-item>

      <el-form-item label="科目">
        <el-input v-model="elective.subject_id" autocomplete="off"/>
      </el-form-item>


    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogElectiveVisible = false">取消</el-button>
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
      dialogElectiveVisible: false,
      tableData: [{student_id: 1, score: 1, subject_id: 1}],
      elective: {
        student_id: null,
        score: null,
        subject_id: null
      }
    }
  },
  methods: {
    loadElective() {
      axios.get('/edu/electives')
          .then((response) => {
            this.tableData = response.data.data;
            this.loading = false;
          }).catch((error) => {
      });
    },

    handleAdd() {

      this.elective = {
        student_id: null,
        score: null,
        subject_id: null
      },
          this.dialogElectiveVisible = true;
      this.temp = true;
    },


    handleEdit(row) {
      this.elective = JSON.parse(JSON.stringify(row));
      this.dialogElectiveVisible = true;
      this.temp = false;
    },

    save() {
      this.tableData.push(this.elective)
      this.dialogElectiveVisible = false;
      if (this.temp == false) {
        //编辑操作
        axios.post('/edu/electives/' + this.elective.student_id, "&score=" + this.elective.score + "&subject_id=" + this.elective.subject_id + "&_method=put").then((response) => {
          if (response.data.code == 200) {
            this.dialogElectiveVisible = false;
            //重新加载学科信息
            this.loadElective();
          }
        }).catch((error) => {
          console.log(error);
        });
      } else {
        let param = qs.stringify(this.elective);
        axios.post('/edu/electives/', param).then((response) => {
          if (response.data.code == 200) {
            this.dialogElectiveVisible = false;
            // 重新加载学科信息
            this.loadElective();
          }
        })
            .catch((error) => {
              console.log(error);
            });
      }
    },

    handleDelete(id) {
      //发送delete
      axios.delete('/edu/electives/' + id).then((response) => {
        if (response.data.code == 200) {
          //重新加载学科信息
          this.loadElective();
        }
      }).catch((error) => {
        console.log(error);
      });
    },
    handleSelect() {//课程号搜索
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
    },
  },

  created() {
    this.loadElective();
  }
}
</script>