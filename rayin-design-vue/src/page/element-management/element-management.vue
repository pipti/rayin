<template>
  <div >
    <el-row>
      <el-col :span="4">
        <el-button type="primary" plain size="medium" style="border-radius: 0px" @click="elAdd">
          <i class="el-icon-document-add"></i>{{$i18n.t('elementDesign.new')}}</el-button>
        <!--<el-button type="primary" plain size="medium" style="border-radius: 0px">
        <i class="el-icon-upload2"></i>构件上载</el-button>-->
        <!--<el-button plain size="medium" style="border-radius: 0px">
        <i class="el-icon-delete"></i>构件删除</el-button>-->
        <el-tooltip class="item" effect="dark" placement="right-start" style="border-radius: 0px">
          <div slot="content">用于管理构件，构件是模板的基础组成部分，通过html进行样式的设计和数据绑定</div>
          <el-button size="medium"><i class="el-icon-help"></i>{{$i18n.t('tips')}}</el-button>
        </el-tooltip>

      </el-col>
      <el-col :span="4">
        <el-radio-group v-model="viewSwitch" size="mini">
          <el-radio-button label="thumViewData"><i class="el-icon-menu"></i></el-radio-button>
          <el-radio-button label="tableViewData"><i class="el-icon-s-operation"></i></el-radio-button>
        </el-radio-group>
      </el-col>
      <el-col :span="4">
        <el-radio-group v-model="modelSwitch" size="mini">
          <el-radio-button label="normalData"><i class="el-icon-set-up"></i></el-radio-button>
          <el-radio-button label="recycleData"><i class="el-icon-delete"></i>{{ $i18n.t('recycle') }}</el-radio-button>
        </el-radio-group>
      </el-col>
      <el-col :span="12">
        <el-input :placeholder="$i18n.t('elementDesign.placeholder.search')"
                  v-model="searchKey" class="input-with-select"
                  @keyup.enter.native="elSearchClick">
          <el-button slot="append" icon="el-icon-search" @click="elSearchClick"></el-button>
        </el-input>
      </el-col>
    </el-row>
    <el-row>

      <el-row style="margin-top:30px" v-if="viewSwitch==='thumViewData'">
        <el-col :xs="11" :sm="5" :md="5" :lg="3" :xl="2" v-for="(o,index) in elData"
                :key="o.elementId" :offset="2" style="margin-top: 10px">
          <el-card :body-style="{ padding: '0px' }" shadow="hover" style="width:230px;height:280px">
            <div style="width:100%;text-align: center;cursor:pointer;margin-top: 10px;height:200px;overflow-y: auto"
                 @click="elViewerClick(o)">
              <!--<html-capture :htmlCode="o.content" style="width:100%"></html-capture>-->
              <img style="width:100%" :src="o.elementThum" />
            </div>
            <div style="padding: 10px;height:50px;background: rgb(102, 177, 255);">
              <div class="bottom clearfix" style="color:white">
                <div v-if="o.name !== null">{{ o.name.substring(0,13) }}
                  <span v-if="o.name.length > 13">...</span>
                </div>
                <!--<div @click="orgSetViewClick(o)" style="float:right">
                <i class="el-icon-setting" style="font-size: 22px;color: white"></i></div>-->
                <div style="float:right">
                  <template v-if="dataModelSwitch==='normalData'">
                    <el-popover
                      placement="bottom"
                      width="30"
                      trigger="hover">
                      <div>
                        <el-button @click="elEditorClick(o)" type="text" size="small">
                          {{$i18n.t('edit')}}</el-button><br>
                        <el-button @click="elVersionClick(o)" type="text" size="small">
                          {{$i18n.t('versionHistory')}}</el-button><br>
                        <el-button @click="elTlRelsViewerClick(o,1)" type="text" size="small">
                          {{ $i18n.t('elementDesign.TemplateRelationship') }}</el-button><br>
                        <el-button @click="elTlSyncLogViewerClick(o,1)" type="text" size="small">
                          {{ $i18n.t('elementDesign.simultaneousRecording') }}</el-button><br>
                        <el-button @click="elCollClick(index, o)" type="text" size="small">
                          {{ $i18n.t('elementDesign.collect') }}</el-button><br>
<!--                        <el-button @click="elVersionClick(o)" type="text" size="small" disabled>-->
<!--                          共享至其他项目</el-button><br>-->
                        <el-button @click="elLogicalDelClick(index, o)" type="text" size="small">
                          {{$i18n.t('del')}}</el-button>
                      </div>
                      <el-button type="text" slot="reference" class="el-icon-setting"
                                 style="color: #ffffff;font-size:20px"></el-button>
                      </el-popover>
                    </template>

                  <template  v-if="dataModelSwitch==='recycleData'">
                    <el-popover
                      placement="bottom"
                      width="30"
                      trigger="hover">
                      <div>
                      <!--<el-button @click="elViewerClick(scope.row)" type="text" size="small" >查看</el-button>-->
                      <el-button @click="elResumeClick(index, o)" type="text" size="small">恢复</el-button><br>
                      <el-button @click="elDelClick(index, o)" type="text" size="small">彻底删除</el-button>
                      </div>
                      <el-button type="text" slot="reference" class="el-icon-setting"
                                 style="color: #ffffff;font-size:20px"></el-button>
                    </el-popover>

                  </template>
                </div>
              </div>
            </div>
            <!--<div style="padding: 10px;text-align: center">-->
              <!--<el-row>-->
                <!--<el-col :span="1">-->
                  <!--&nbsp;-->
                <!--</el-col>-->
                <!--<el-col :span="22">-->
                  <!--<span>{{o.organizationName}}</span>-->
                  <!--<div v-if="o.name !== null">{{ o.name.substring(0,8) }}-->
                    <!--<span v-if="o.name.length > 8">...</span>-->
                  <!--</div>-->
                <!--</el-col>-->
                <!--<el-col :span="1" >-->
                  <!--&lt;!&ndash;<div @click="orgSetViewClick(o)" v-if="o.owner===true" style="float:right">
                  <i class="el-icon-setting" style="font-size: 25px;color: #909399"></i></div>&ndash;&gt;-->
                  <!--&lt;!&ndash;<div @click="orgSetViewClick(o)" v-if="o.owner===true" style="float:right">
                  <i class="el-icon-setting" style="font-size: 25px;color: #909399"></i></div>&ndash;&gt;-->
                <!--</el-col>-->
              <!--</el-row>-->
              <!--<el-row>-->
                <!--<el-col :span="18">-->
                  <!--&nbsp;-->
                <!--</el-col>-->
                <!--<el-col :span="6" >-->
                  <!--<div @click="orgMemberSetClick(o)" v-if="o.owner===true" style="float:right;margin-left:5px">-->
                    <!--<i class="el-icon-user" style="font-size: 22px;color: #909399"></i>-->
                  <!--</div>-->
                  <!--<div @click="orgSetViewClick(o)" v-if="o.owner===true" style="float:right">
                  <i class="el-icon-setting" style="font-size: 22px;color: #909399"></i></div>-->
                <!--</el-col>-->
              <!--</el-row>-->
            <!--</div>-->
          </el-card>
        </el-col>
      </el-row>

      <!--<el-row v-if="viewSwitch==='thumViewData'">-->
        <!--&lt;!&ndash; 缩略图展示 &ndash;&gt;-->
          <!--&lt;!&ndash;构件&ndash;&gt;-->
            <!--<el-col v-for="(el,index) in elData" v-key="index" ref="elCaptures">-->
              <!--<el-card class="box-card"  shadow="hover">-->
                <!--<html-capture :htmlCode="el.content" :htmlUrl="el.url" style="width:100%"></html-capture>-->
              <!--</el-card>-->
              <!--&lt;!&ndash;<div style="margin-top: 0px;text-align: right">&ndash;&gt;-->
                <!--&lt;!&ndash;<div class="bottom clearfix">&ndash;&gt;-->
                  <!--&lt;!&ndash;<el-button type="text" class="button" size="small"
                  @click="elFavoritesDel(index,el)">删除</el-button>&ndash;&gt;-->
                  <!--&lt;!&ndash;<el-button type="text" class="button" size="small"
                  @click="elLoad(el)">载入构件</el-button>&ndash;&gt;-->
                <!--&lt;!&ndash;</div>&ndash;&gt;-->
              <!--&lt;!&ndash;</div>&ndash;&gt;-->
            <!--</el-col>-->
      <!--</el-row>-->

      <!-- 数据列表展示 -->
      <template v-if="viewSwitch==='tableViewData'">
        <el-table
          :data="elData"
          style="width: 100%"
          :height="this.$store.state.tableHeight">
          <el-table-column
            label="构件编号"
            width="120"
          fixed>
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                <el-tag size="small">{{ scope.row.elementId }}</el-tag>
                <div slot="reference" class="name-wrapper">
                  <el-tag size="small">{{ scope.row.elementId.substring(0,10) }}..</el-tag>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            prop="name"
            label="构件名称"
            width="300">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.name }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.name !== null">{{ scope.row.name.substring(0,10) }}
                    <span v-if="scope.row.name.length > 10">...</span>
                  </div>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            prop="elementVersion"
            label="版本号"
            width="70">
          </el-table-column>
          <el-table-column
            prop="createTimeStr"
            label="创建日期"
            width="100">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.createTimeStr }}
                <div slot="reference" class="name-wrapper">
                  {{ scope.row.createTimeStr.substring(0,10) }}
                </div>
              </el-popover>
            </template>
          </el-table-column>

          <el-table-column
            prop="updateTimeStr"
            label="更新日期"
            width="100">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.updateTimeStr }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.updateTimeStr !== null">{{ scope.row.updateTimeStr.substring(0,10) }}
                  </div>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            prop="userName"
            label="创建者"
            width="150">

          </el-table-column>
          <el-table-column
            label="备注"
            min-width="300">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.memo }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.memo !== null">{{ scope.row.memo.substring(0,10) }}
                    <span v-if="scope.row.memo.length > 10">...</span>
                  </div>
                </div>
              </el-popover>
            </template>
          </el-table-column>

          <el-table-column
            fixed="right"
            label="操作"
            width="130" v-if="dataModelSwitch==='normalData'">
            <template slot-scope="scope">
              <el-button @click="elViewerClick(scope.row)" type="text" size="small" >查看</el-button>
              <el-button @click="elEditorClick(scope.row)" type="text" size="small">编辑</el-button>
              <!--<el-button @click="elSyncQueryClick(scope.$index, scope.row)" type="text" size="small" disabled>
              更多</el-button>-->
              <el-popover
                placement="bottom"
                width="430"
                trigger="hover">
                <div>
                  <el-button @click="elVersionClick(scope.row)" type="text" size="small">版本记录</el-button>
                  <el-button @click="elTlRelsViewerClick(scope.row,1)" type="text" size="small">关联的模板</el-button>
                  <el-button @click="elTlSyncLogViewerClick(scope.row,1)" type="text" size="small">同步记录</el-button>
                  <el-button @click="elCollClick(scope.$index, scope.row)" type="text" size="small">收藏</el-button>
                  <el-button @click="elVersionClick(scope.row)" type="text" size="small" disabled>共享至其他项目</el-button>
                  <el-button @click="elLogicalDelClick(scope.$index, scope.row)" type="text" size="small">删除</el-button>
                </div>
                <el-button type="text" size="medium" slot="reference" class="el-icon-more"></el-button>
              </el-popover>
            </template>
          </el-table-column>

          <el-table-column
            fixed="right"
            label="操作"
            width="200" v-if="dataModelSwitch==='recycleData'">
            <template slot-scope="scope">
              <el-button @click="elViewerClick(scope.row)" type="text" size="small" >查看</el-button>
              <el-button @click="elResumeClick(scope.$index, scope.row)" type="text" size="small">恢复</el-button>
              <el-button @click="elDelClick(scope.$index, scope.row)" type="text" size="small">彻底删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="width:100%;text-align: right">
          <el-pagination
          @size-change="this.handlePageSizeChange"
          :page-sizes="[5,10, 20, 50, 100]"
          @current-change="handleCurrentChange"
          layout="total,prev, pager, next,sizes"
          :total="elDataTotal"
          :current-page="elCurrentPage"
          :page-size="this.$store.state.pageSize">
        </el-pagination>
        </div>
      </template>
    </el-row>

    <el-drawer
      title="版本历史"
      :visible.sync="drawerVersion"
      :with-header="true">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <el-card shadow="hover" style="margin-top:20px">
            <el-timeline :reverse="reverse">
              <el-timeline-item
                v-for="(activity, index) in activities"
                :key="index"
                :timestamp="activity.createTimeStr">
                构件名称：{{activity.name}}<br>
                构件备注：{{activity.memo}}<br>
                构件版本：{{activity.elementVersion}}
              </el-timeline-item>
            </el-timeline>
          </el-card>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
      <el-row>
        <div style="width:100%;text-align: right">
          <el-pagination
            @size-change="this.handlePageSizeChange"
            :page-sizes="[5,10, 20, 50, 100]"
            @current-change="handleVersionCurrentChange"
            layout="prev, pager, next,sizes"
            :total="elVersionDataTotal"
            :current-page="elVersionCurrentPage"
            :page-size="this.$store.state.pageSize">
          </el-pagination>
        </div>
      </el-row>
    </el-drawer>
    <!--模板预览视图-->
    <el-drawer
      title="模板预览"
      :visible.sync="drawerElViewer"
      :with-header="true"
      size="50%">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <el-card :style="{marginTop:'20px',overflow: 'auto',height:this.$store.state.elViewerBoxHeight}" >
            <html-panel :htmlCode.sync="elCode" ></html-panel>
          </el-card>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>

    <!--构件模板关联视图-->
    <el-drawer
      title="构件模板关联"
      :visible.sync="drawerElTlRelsViewer"
      :with-header="true"
      size="40%">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <template>
            <el-table
              :data="elTlRelsData"
              style="width: 100%"
              :height="this.$store.state.tableHeight">
              <el-table-column
                fixed
                prop="elementVersion"
                label="构件版本号"
                width="100">
              </el-table-column>
              <el-table-column
                fixed
                prop="templateVersion"
                label="模板版本号"
                width="100">
              </el-table-column>
              <el-table-column
                label="模板编号"
                width="120">
                <template slot-scope="scope">
                  <el-popover trigger="hover" placement="top">
                    <el-tag size="small">{{ scope.row.templateId }}</el-tag>
                    <div slot="reference" class="name-wrapper">
                      <el-tag size="small">{{ scope.row.templateId.substring(0,10) }}..</el-tag>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column
                prop="name"
                label="模板名称"
                width="300">
                <template slot-scope="scope">
                  <el-popover trigger="hover" placement="top">
                    {{ scope.row.name }}
                    <div slot="reference" class="name-wrapper">
                      <div v-if="scope.row.name !== null">{{ scope.row.name.substring(0,10) }}
                        <span v-if="scope.row.name.length > 10">...</span>
                      </div>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column
                fixed="right"
                prop="address"
                label="操作"
                width="100">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="syncElTl(scope.index, scope.row)">同步至模板</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div style="width:100%;text-align: right">
              <el-pagination
                @size-change="this.handlePageSizeChange"
                :page-sizes="[5, 10, 20, 50, 100]"
                @current-change="elTlRelsHandleCurrentChange"
                layout="total,prev, pager, next,sizes"
                :total="elTlRelsDataTotal"
                :current-page="elTlRelsCurrentPage"
                :page-size="this.$store.state.pageSize">
              </el-pagination>
            </div>
          </template>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>

    <!--构件同步日志-->
    <el-drawer
      title="构件同步日志"
      :visible.sync="drawerElTlSyncLogViewer"
      :with-header="true"
      size="40%">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <template>
            <el-table
              :data="elTlSyncLogData"
              style="width: 100%"
              :height="this.$store.state.tableHeight">
              <el-table-column
                prop="syncTimeStr"
                label="同步日期"
                width="100">
                <template slot-scope="scope">
                  <el-popover trigger="hover" placement="top">
                    {{ scope.row.syncTimeStr }}
                    <div slot="reference" class="name-wrapper">
                      {{ scope.row.syncTimeStr.substring(0,10) }}
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column
                prop="elementVersion"
                label="构件版本号"
                width="100">
              </el-table-column>
              <el-table-column
                prop="templateVersion"
                label="模板版本号"
                width="100">
              </el-table-column>
              <el-table-column
                label="模板编号"
                width="120">
                <template slot-scope="scope">
                  <el-popover trigger="hover" placement="top">
                    <el-tag size="small">{{ scope.row.templateId }}</el-tag>
                    <div slot="reference" class="name-wrapper">
                      <el-tag size="small">{{ scope.row.templateId.substring(0,10) }}..</el-tag>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column
                prop="name"
                label="模板名称"
                width="300">
                <template slot-scope="scope">
                  <el-popover trigger="hover" placement="top">
                    {{ scope.row.templateName }}
                    <div slot="reference" class="name-wrapper">
                      <div v-if="scope.row.templateName !== null">{{ scope.row.templateName.substring(0,10) }}
                        <span v-if="scope.row.templateName.length > 10">...</span>
                      </div>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>

            </el-table>
            <div style="width:100%;text-align: right">
              <el-pagination
                @size-change="this.handlePageSizeChange"
                :page-sizes="[5, 10, 20, 50, 100]"
                @current-change="elTlSyncLogDataHandleCurrentChange"
                layout="total,prev, pager, next,sizes"
                :total="elTlSyncLogDataTotal"
                :current-page="elTlSyncLogDataCurrentPage"
                :page-size="this.$store.state.pageSize">
              </el-pagination>
            </div>
          </template>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>
  </div>

</template>

<script>
// eslint-disable-next-line
import HtmlPanel from '@/components/html-panel'
import axios from 'axios';
export default {
  name: 'ElementManagement',
  components: {
    HtmlPanel,
  },
  data() {
    return {
      modelSwitch: 'normalData',
      viewSwitch: 'thumViewData',
      delFlag: false,
      elData: [],
      elTlRelsData: [],
      elTlSyncLogData: [],
      searchKey: '',
      reverse: true,
      activities: [{
        name: '无',
        memo: '无版本历史记录',
        elementVersion: '无',
        createTimeStr: '',
      }],
      drawerVersion: false,
      drawerElViewer: false,
      drawerElTlSyncLogViewer: false,
      elCode: '',
      elViewerBoxHeight: '',
      elDataTotal: 1,
      elCurrentPage: 1,
      elVersionDataTotal: 1,
      elVersionCurrentPage: 1,
      currentSelectRow: [],
      elTlRelsDataTotal: 1,
      elTlRelsCurrentPage: 1,
      drawerElTlRelsViewer: false,
      elTlSyncLogDataTotal: 1,
      elTlSyncLogDataCurrentPage: 1,

    };
  },
  methods: {
    elTemplateRelationQuery(row, val) {
      axios.post(
        `${this.GLOBAL.webappApiConfig.ELementManagement.UserElementTemplateRelations.url}?pageCurrent=${val}&pageSize=${this.$store.state.pageSize}`,
        row,
        {},
      )
        .then((res) => {
          this.elTlRelsData = res.data.content.records;
          this.elTlRelsDataTotal = res.data.content.total;
          this.elTlRelsCurrentPage = res.data.content.current;
        })
        .catch((error) => {
          console.log(error);
          // alert(error)
        });
    },
    elElTlSyncLogQuery(row, val) {
      axios.get(
        `${this.GLOBAL.webappApiConfig.ELementManagement.UserElementSyncElTlLogQuery.url}/${row.elementId}?pageCurrent=${val}&pageSize=${this.$store.state.pageSize}`,
        {},
        {},
      )
        .then((res) => {
          this.elTlSyncLogData = res.data.content.records;
          this.elTlSyncLogDataTotal = res.data.content.total;
          this.elTlSyncLogDataCurrentPage = res.data.content.current;
        })
        .catch((error) => {
          console.log(error);
          // alert(error)
        });
    },
    elAdd() {
      this.$router.push({ name: 'ElementDesign', params: {} });
    },
    elVersionClick(row) {
      this.currentSelectRow = row;
      axios.get(
        `${this.GLOBAL.webappApiConfig.ELementManagement.UserElementVersions.url}/${row.elementId}?pageCurrent=1` + `&pageSize=${this.$store.state.pageSize}`,
        {},
        {},
      )
        .then((res) => {
          this.activities = [{
            name: row.name,
            memo: '无版本历史记录',
            elementVersion: '无',
            createTimeStr: '',
          }];
          if (res.data.content.records.length !== 0) {
            this.activities = res.data.content.records;
            this.elVersionDataTotal = res.data.content.total;
            this.elVersionCurrentPage = res.data.content.current;
          }
          // console.log(res)
        })
        .catch((error) => {
          console.log(error);
          // alert(error)
        });

      this.drawerVersion = true;
    },
    elEditorClick(row) {
      console.log(row);
      this.$router.push({ name: 'ElementDesign', params: { element: row } });
    },
    elViewerClick(row) {
      // console.log(row.content)
      this.elCode = row.content;
      this.drawerElViewer = true;
    },
    elTlRelsViewerClick(row, val) {
      this.currentSelectRow = row;
      this.elTemplateRelationQuery(row, val);
      this.drawerElTlRelsViewer = true;
    },
    elTlSyncLogViewerClick(row, val) {
      this.currentSelectRow = row;
      this.elElTlSyncLogQuery(row, val);
      this.drawerElTlSyncLogViewer = true;
    },
    elLogicalDelClick(index, row) {
      // 删除构件
      this.$confirm('是否确定删除该构件至回收站, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        // 删除构件
        axios.post(
          this.GLOBAL.webappApiConfig.ELementManagement.UserElementLogicalDel.url,
          row,
          {},
        )
          .then(() => {
            this.elData.splice(index, 1);
          })
          .catch((error) => {
            console.log(error);
          });
      });
    },
    elDelClick(index, row) {
    // 删除构件
      this.$confirm('此操作将永久删除该构件, 有关该构件的所有版本都将被删除，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        axios.post(
          this.GLOBAL.webappApiConfig.ELementManagement.UserElementDel.url,
          row,
          {},
        )
          .then(() => {
            this.elData.splice(index, 1);
          })
          .catch((error) => {
            console.log(error);
          });
      })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除',
          });
        });
    },
    elResumeClick(index, row) {
    // 删除构件
      axios.post(
        this.GLOBAL.webappApiConfig.ELementManagement.UserElementResume.url,
        row,
        {},
      )
        .then(() => {
          this.elData.splice(index, 1);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    htmlLoadCompleted() {
      // console.log('load complete:' + val)
    },
    handleCurrentChange(val) {
      axios.get(
        `${this.GLOBAL.webappApiConfig.ELementManagement.UserElementQuery.url
        + (this.searchKey === '' ? '' : `/${this.searchKey}`)}?pageCurrent=${val}&pageSize=${this.$store.state.pageSize}&delFlag=${this.delFlag}`,
        {},
        {},
      )
        .then((res) => {
          this.elData = res.data.content.records;
          this.elDataTotal = res.data.content.total;
          this.elCurrentPage = res.data.content.current;
          // console.log(res)
          // 数据渲染完成后再渲染组件样式，否则样式会错乱
          this.dataModelSwitch = this.modelSwitch;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    elTlRelsHandleCurrentChange(val) {
      this.elTemplateRelationQuery(this.currentSelectRow, val);
    },
    elTlSyncLogDataHandleCurrentChange(val) {
      this.elElTlSyncLogQuery(this.currentSelectRow, val);
    },
    elSearchClick() {
      this.handleCurrentChange(1);
    },
    handleVersionCurrentChange() {
      this.elVersionClick(this.currentSelectRow);
    },
    syncElTl(index, row) {
      // 同步构件至模板
      const tplElPar = this.currentSelectRow;
      tplElPar.templateId = row.templateId;
      tplElPar.templateVersion = row.templateVersion;
      axios.post(
        this.GLOBAL.webappApiConfig.ELementManagement.UserElementSyncElTl.url,
        tplElPar,
        {},
      )
        .then(() => {

        })
        .catch((error) => {
          console.log(error);
        });
    },
    elSyncQueryClick() {
    // 同步构件日志查询

    },
    elCollClick(index, row) {
      // 收藏构件
      axios.post(
        this.GLOBAL.webappApiConfig.ELementManagement.UserElementColle.url,
        row,
        {},
      )
        .then(() => {

        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  mounted() {
    this.elSearchClick();
  },
  watch: {
    modelSwitch(val) {
      if (val === 'normalData') {
        this.delFlag = false;
      } else {
        this.delFlag = true;
      }
      this.handleCurrentChange(1);
    },
  },
};
</script>
<style>
  .elViewerBox{

  }
</style>
