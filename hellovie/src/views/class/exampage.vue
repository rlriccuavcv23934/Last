<template>
  <div class="box">

    <el-button type="primary" style="margin-bottom: 10px;" @click="handleAdd">添加</el-button>
    <el-input v-model="findid" v-on:input="handleSelect" placeholder="输入题号进行搜索" maxlength="10" clearable
              style="padding-bottom: 20px"/>
    {{ findid }}
    <el-table :data="tableData" v-loading="loading" border style="width: 100%">

      <el-table-column label="题目编号" align="center">
        <template #default="scope">
          {{ scope.row.question_id }}
        </template>
      </el-table-column>

      <el-table-column label="课程名" align="center">
        <template #default="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>

      <el-table-column label="题目类型" align="center">
        <template #default="scope">
          <template v-if="scope.row.type==0">选择题</template>
          <template v-else-if="scope.row.type==1">判断题</template>
          <template v-else-if="scope.row.type==2">填空题</template>
          <template v-else>其他题</template>
        </template>
      </el-table-column>

      <el-table-column label="试题" align="center">
        <template #default="scope">
          {{ scope.row.title }}
        </template>
      </el-table-column>

      <el-table-column label="分值" align="center">
        <template #default="scope">
          {{ scope.row.score }}
        </template>
      </el-table-column>

      <el-table-column label="选项" align="center">
        <template #default="scope">
          <template v-for="option in scope.row.options">
            <el-radio v-model="scope.row.answer" disabled :label="option.value">{{ option.value }}</el-radio>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="参考答案" align="center">
        <template #default="scope">
          {{ scope.row.answer }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.question_id)">删除</el-button>
        </template>
      </el-table-column>

    </el-table>
  </div>
  <el-dialog v-model="dialogFormVisible" title="保存题库">
    <el-form :model="examData">
      {{ examData }}
      <el-form-item label="课程名">

        <el-select v-model="examData.name" placeholder="请选择学科">
          <el-option
              v-for="item in subjectList"
              :key="item.name"
              :label="item.name"
              :value="item.name"
          />
        </el-select>
      </el-form-item>


      <el-form-item label="题目类型">
        <el-select v-model="examData.type" placeholder="Select">
          <el-option
              v-for="item in typeList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <div v-if="examData.type==0">
        <el-form-item label="　　">
          <el-input v-model="optionItem" style="width: 60px" size="small" placeholder="选项"/>
          <el-button size="small" style="margin: 0px 20px;" v-on:click="handleSaveOption">保存</el-button>
        </el-form-item>

        <el-form-item label="选项">
          <el-radio-group v-model="examData.answer">
            <el-radio v-for="item in examData.options" :label="item.value">{{ item.value }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>
      <el-form-item label="试题">
        <el-input v-model="examData.title" autocomplete="off"/>
      </el-form-item>

      <el-form-item label="分数">
        <el-input v-model="examData.score" autocomplete="off"/>
      </el-form-item>

      <el-form-item label="参考答案">
        <el-input type="textarea" v-model="examData.answer" autocomplete="off"/>
      </el-form-item>


    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">关闭</el-button>
        <el-button type="primary" @click="save">保存</el-button>
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
      loading: false,
      dialogFormVisible: false,
      typeList: [{
        label: "选择题",
        value: 0
      }, {
        label: "判断题",
        value: 1
      }, {
        label: "填空题",
        value: 2
      }],
      optionItem: null,
      subjectList: [],
      tableData: [{
        question_id: 11,
        title: "如下哪个是java关键字?",
        score: 10,
        type: 0,
        answer: "class",
        name: "python",
        options: [{"value": "class"}, {"value": "String"}]
      }],
      examData: {question_id: null, title: null, score: null, type: null, answer: null, name: null, options: []}
    }
  },
  methods: {

    //加载学科
    loadSubject() {
      axios.get('/edu/subjects')
          .then((response) => {
            this.subjectList = response.data.data;
          }).catch((error) => {
      });
    },

    loadexam() {
      axios.get('/edu/exams')
          .then((response) => {
            this.tableData = response.data.data
            this.loading = false;
            //将options属性的值转成json对象
            this.tableData.map((item) => {
              item.options = JSON.parse(item.options);
            });
          }).catch((error) => {
      });
    },

    handleAdd() {
      this.examData = {
        question_id: null, title: null, score: null, type: null, answer: null, name: null, options: []
      };
      this.dialogFormVisible = true;
    },

    handleEdit(row) {
      this.examData = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true;
    },

    //监听点击保存选项
    handleSaveOption() {
      if (this.optionItem) {
        //判断是否存在
        let result = this.examData.options.find((item) => {
          return item.value == this.optionItem;
        });
        if (result) {
          alert("选项已经存在");
          return;
        }
        this.examData.options.push({value: this.optionItem});
        this.optionItem = null;
      }
    },

    save() {
      if (this.examData.question_id) {
        axios.post('/edu/exampage/' + this.examData.question_id, "title=" + this.examData.title +
            "&score=" + this.examData.score + "&type=" + this.examData.type + "&answer=" + this.examData.answer +
            "&name=" + this.examData.name + "&options=" + this.examData.options + "&_method=put").then((response) => {
          if (response.data.code == 200) {
            this.dialogSubjectVisible = false;
            this.loadexam();
          }
        }).catch((error) => {
          console.log(error);
        });
      } else {
        //深度复制
        let param = JSON.parse(JSON.stringify(this.examData));
        if (param.options && param.options.length > 0) {
          param.options = JSON.stringify(param.options);
        }
        param = qs.stringify(param);
        axios.post('/edu/examDatas/', param).then((response) => {
          if (response.data.code == 200) {
            this.dialogFormVisible = false;
            this.loadexam();
          }
        }).catch((error) => {
          console.log(error);
        });
      }
    },
    handleDelete(id) {
      //发送delete
      axios.delete('/edu/electives/' + id).then((response) => {
        if (response.data.code == 200) {
          //重新加载学科信息
          this.loadexam();
        }
      }).catch((error) => {
        console.log(error);
      });
    },
    handleSelect() {//题目号搜索
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
    this.loadexam();
    this.loadSubject();
  }
}
</script>