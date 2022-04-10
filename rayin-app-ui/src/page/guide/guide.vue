<template>
  <!--<div style="height: 700px;">-->
    <!--<el-button style="margin-top: 12px;border-radius: 0px" @click="next1" type="primary" plain size="medium">-->
      <!--模板制作步骤<i class="el-icon-d-arrow-right"></i>-->
      <!--</el-button>-->
    <!--<el-steps  :active="active1" style="margin-top:30px" >-->
      <!--<el-step title="步骤 1" description="在资源管理中添加构件设计所需要的资源，例如图片"></el-step>-->
      <!--<el-step title="步骤 2" description="在构件设置中添加构件，并对每个构件进行设计，绑定数据，设置相关参数"></el-step>-->
      <!--<el-step title="步骤 3" description="在模板配置中通过选择构件进行组合并排序，设置构件绑定模板的相关参数，模板整体配置参数"></el-step>-->
      <!--<el-step title="步骤 4" description="通过模板测试功能进行测试模板的生成效果"></el-step>-->
    <!--</el-steps>-->

    <!--<el-button style="margin-top: 12px;border-radius: 0px" @click="next2" type="primary" plain size="medium">-->
      <!--模板测试步骤<i class="el-icon-d-arrow-right"></i>-->
    <!--</el-button>-->
    <!--<el-steps  :active="active2" style="margin-top:30px" >-->
      <!--<el-step title="步骤 1" description="通过模板测试功能进行测试模板的生成效果"></el-step>-->
      <!--<el-step title="步骤 2" description="也可以通过接口调用进行模板生成的批量测试"></el-step>-->
      <!--<el-step title="步骤 3" description="通过凭证管理中的文件查询下载，对生成的电子凭证进行管理"></el-step>-->
    <!--</el-steps>-->

    <!--<el-button style="margin-top: 12px;border-radius: 0px" @click="next3" type="primary" plain size="medium">-->
      <!--环境发布步骤<i class="el-icon-d-arrow-right"></i>-->
    <!--</el-button>-->
    <!--<el-steps  :active="active3" style="margin-top:30px" >-->
      <!--<el-step title="步骤 1" description="创建组织"></el-step>-->
      <!--<el-step title="步骤 2" description="发布相关模板至组织中"></el-step>-->
      <!--<el-step title="步骤 3" description="调用组织对应的接口"></el-step>-->
      <!--<el-step title="步骤 4" description="在组织对应的空间中管理电子凭证"></el-step>-->
    <!--</el-steps>-->
  <!--</div>-->
  <el-carousel :interval="5000" arrow="always" height="692px" style="margin-top:150px">
    <el-carousel-item v-for="item in imgList" :key="item">
      <img :src="item" style="width:100%"/>
    </el-carousel-item>
  </el-carousel>
</template>

<script>
export default {
  name: 'guide',
  data () {
    return {
      //      active1: 0,
      //      active2: 0,
      //      active3: 0,
      imgList: [
        '/static/help/images/guide_1.png',
        '/static/help/images/guide_2.png',
        '/static/help/images/guide_3.png',
        '/static/help/images/guide_4.png',
        '/static/help/images/guide_5.png',
        '/static/help/images/guide_6.png',
        '/static/help/images/guide_7.png',
        '/static/help/images/guide_8.png'
      ]
    }
  },
  methods: {
    //    next1 () {
    //      if (this.active1++ > 3) this.active1 = 0
    //    },
    //    next2 () {
    //      if (this.active2++ > 2) this.active2 = 0
    //    },
    //    next3 () {
    //      if (this.active3++ > 3) this.active3 = 0
    //    }
  }
}
</script>

<style>
  .el-carousel__item h3 {
    color: #475669;
    font-size: 18px;
    opacity: 0.75;
    line-height: 300px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
</style>
