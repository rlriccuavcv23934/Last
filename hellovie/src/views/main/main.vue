<template>
  <el-container class="layout-container-demo" style="height: 100%">
    <el-aside width="200px">
      <el-scrollbar>
        <router-link to="/main">
          <div class="logo">
            考试管理系统
          </div>
        </router-link>
        <el-menu
            :router="true"
            active-text-color="#ffd04b"
            background-color="#545c64"
            class="el-menu-vertical-demo"
            default-active="2"
            text-color="#fff"
            @close="handleClose"
            @open="handleOpen"
        >
          <el-sub-menu index="1">
            <template #title>
              <el-icon>
                <location/>
              </el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/menber/student">学生管理</el-menu-item>
            <!--              <el-menu-item index="1-2">老师管理</el-menu-item>-->
          </el-sub-menu>

          <el-sub-menu index="2">
            <template #title>
              <el-icon>
                <location/>
              </el-icon>
              <span>题海</span>
            </template>
            <el-menu-item index="/class/classlist">科目管理</el-menu-item>
            <el-menu-item index="/exam/exampage">题海管理</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="3">
            <template #title>
              <el-icon>
                <location/>
              </el-icon>
              <span>考试中心</span>
            </template>
            <el-menu-item index="/exam/exammanage">考试管理</el-menu-item>
            <el-menu-item index="/exam/elective">成绩管理</el-menu-item>
          </el-sub-menu>

          <el-menu-item index="4">
            <el-icon>
              <setting/>
            </el-icon>
            <span>Navigator Four</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header height="56px" style="text-align: right; font-size: 12px;line-height: 56px;">
        <div class="toolbar">
          <el-dropdown>
            <el-icon style="margin-right: 8px;">
              <setting
              />
            </el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>View</el-dropdown-item>
                <el-dropdown-item>Add</el-dropdown-item>
                <el-dropdown-item>Delete</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <span>Tom</span>
        </div>
      </el-header>

      <el-main>
        <el-scrollbar>
          <router-view></router-view>
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>

</template>


<script>
import {Location, Menu as IconMenu, Message, Setting} from '@element-plus/icons-vue'

export default {
  name: "main",
  components: {
    IconMenu,
    Message,
    Setting,
    Location
  },
  data() {
    return {
      user: "xx",
      nvi: "notviso",
      vi: "viso",//控制显示隐藏
    }
  },
  methods: {
    loding1() {//控制显示隐藏
      if (this.user == "xx") {
        this.vi = "notviso"
      }
    },
    loding2() {//控制显示隐藏
      if (this.user == "xx") {
        this.nvi = "viso"
      }
    }
  },
  created() {
    this.loding1();
    this.loding2();
  },
}

</script>

<style scoped>

.layout-container-demo .el-header {
  position: relative;
  background-color: #3ecde1;
  color: #fff;
}

.layout-container-demo .el-aside {
  color: var(--el-text-color-primary);
  background: #69e3cd;
  height: 100%;
}

.layout-container-demo .el-aside .logo {
  height: 56px;
  line-height: 56px;
  text-align: center;
  color: #fff;
}

.layout-container-demo .el-aside .logo:hover {
  background-color: #e0a421;
}


.layout-container-demo .el-menu {
  border-right: none;
  background-color: #3ecde1;
}

.layout-container-demo .el-menu-item {
  border-right: none;
  background-color: #3ecde1;
}

.layout-container-demo .el-main {
  background-color: #ccfcf8;
  padding: 0;
}

.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
  background-color: #3ecde1;
}

.viso {
  display: block;
}

.notviso {
  display: none;
}

.box .left {
  font-size: 100px;
  text-align: center;
  writing-mode: vertical-rl;
  text-orientation: mixed;
  padding-top: 80px;
  padding-right: 90px;
}
</style>
