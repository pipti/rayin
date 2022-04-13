<script>
/* eslint-disable */
// 全局表格高度
const tableHeight = window.innerHeight - 200 + 'px'
const boxHeight = window.innerHeight - 250 + 'px'
const iframeHeight = window.innerHeight - 230 + 'px'
const windowHeight = window.innerHeight
const editorHeight = window.innerHeight - 200 + 'px'
// 全局构件查看高度
const elViewerBoxHeight = window.innerHeight - 170 + 'px'

const webappApiConfig = {
  ELementManagement:{
    UserElementQuery:{
      url: "/api/element/query",
      method: "get"
    },
    UserElementLogicalDel:{
      url: "/api/element/logicalDel",
      method: "post"
    },
    UserElementDel:{
      url: "/api/element/del",
      method: "post"
    },
    UserElementResume:{
      url: "/api/element/resume",
      method: "post"
    },
    UserElementVersions:{
      url: "/api/element/versions",
      method: "get"
    },
    UserElementTemplateRelations:{
      url: "/api/element/template/relations",
      method: "get"
    },
    UserElementColle:{
      url: "/api/element/colle",
      method: "post"
    },
    UserElementSyncElTl:{
      url: "/api/element/syncElTl",
      method: "post"
    },
    UserElementSyncElTlLogQuery:{
      url: "/api/element/syncElTlLog/query",
      method: "get"
    },
    UserElementElModifyHistoryQuery:{
      url: "/api/element/modifyHistory/query",
      method: "get"
    }
  },
  ElementDesign:{
    UserElementPdfView:{
      url: "/api/element/pdfView",
      method: "post"
    },
    UserElementSave:{
      url: "/api/element/save",
      method: "post"
    },UserElementFavoritesQuery:{
      url: "/api/element/favorites/query",
      method: "get"
    },UserElementFavoritesDel:{
      url: "/api/element/favorites/del",
      method: "post"
    },
    UserElementFonts:{
      url: "/api/element/fonts",
      method: "get"
    }
  },
  TemplateManagement:{
    UserTemplateQuery:{
      url: "/api/template/query",
      method: "get"
    },
    UserTemplateSave:{
      url: "/api/template/save",
      method: "post"
    },
    UserTemplateLogicalDel:{
      url: "/api/template/logicalDel",
      method: "post"
    },
    UserTemplateGenerate:{
      url: "/api/template/tplGenerate",
      method: "post"
    },
    UserTemplateDel:{
      url: "/api/template/del",
      method: "post"
    },
    UserTemplateTest:{
      url: "/api/template/test",
      method: "post"
    },
    UserTemplateTestDataSave:{
      url: "/api/template/testData/save",
      method: "post"
    },UserTemplateResume:{
      url: "/api/template/resume",
      method: "post"
    }
  },
  OrganizationManager:{
    OrganizationMemberQuery:{
      url: "/api/organization/member/query",
      method: "get"
    },
    OrganizationMemberAdd:{
      url: "/api/organization/member/add",
      method: "post"
    },
    OrganizationMemberDel:{
      url: "/api/organization/member/del",
      method: "post"
    },
    OrganizationAddMemberQuery:{
      url: "/api/organization/addmember/query",
      method: "get"
    },
    OrganizationQuery:{
      url: "/api/organization/query",
      method: "get"
    },OrganizationSave:{
      url: "/api/organization/save",
      method: "post"
    },
    OrganizationDel:{
      url: "/api/organization/del",
      method: "post"
    },
    OrganizationThirdStorageTest:{
      url: "/api/organization/third/storage/test",
      method: "post"
    }
  },
  OrganizationPDFManagement:{
    OrganizationPDFQuery:{
      url: "/api/organization/pdf/query",
      method: "get"
    },
    OrganizationPDFView:{
      url: "/api/organization/pdf/view",
      method: "get"
    }
  },
  OrganizationIndex: {
    OrganizationIndexSave:{
      url: "/api/organization/index/save",
      method: "post"
    },
    OrganizationIndexDel:{
      url: "/api/organization/index/del",
      method: "post"
    },
    OrganizationIndexQuery:{
      url: "/api/organization/index/query",
      method: "get"
    }
  },
  OrganizationResource: {
    OrganizationDataSave:{
      url: "/api/resource/data/save",
      method: "post"
    },
    OrganizationDataDel:{
      url: "/api/resource/data/del",
      method: "post"
    },
    OrganizationDataQuery:{
      url: "/api/resource/data/query",
      method: "get"
    },
    organizationFileQuery:{
      url: "/api/resource/file/query",
      method: "get"
    },
    organizationCreateFolder:{
      url: "/api/resource/file/createFolder",
      method: "get"
    },
    organizationFileUpload:{
      url: "/api/resource/file/upload",
      method: "get"
    },
    organizationFileAllPath:{
      url: "/api/resource/file/allPath",
      method: "get"
    },
    organizationFileView:{
      url: "/api/resource/file/view",
      method: "get"
    },
    organizationFileDel:{
      url: "/api/resource/file/del",
      method: "get"
    },
    organizationFileUrl:{
      url: "/api/resource/file/url",
      method: "get"
    }
  },
  UserIndex: {
    UserIndexSave: {
      url: "/api/user/index/save",
      method: "post"
    },
    UserIndexQuery: {
      url: "/api/user/index/query",
      method: "get"
    }
  },
  BusinessParamManagement:{
    BusinessParamQuery:{
      url: "/api/businessParam/query",
      method: "get"
    },
    BusinessParamSave:{
      url: "/api/businessParam/save",
      method: "post"
    }
  }
}

export default {
    tableHeight,
    elViewerBoxHeight,
    boxHeight,
    iframeHeight,
    windowHeight,
    editorHeight,
    webappApiConfig
}


</script>
