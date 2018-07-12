new Vue({
    el: '#app',
    data: function () {
        return {
            queryObj: {
                name: ''
            },
            appList: [{
                name: '移动门户',
                org: '前海圆舟',
                status: '启用',
                appKey: 'f83aaeec-971f-4cf9-91c1-de02a88e40b2',
                qrCode: './static/images/eqcode.png',
            }, {
                name: '移动门户',
                org: '前海圆舟',
                status: '启用',
                appKey: 'f83aaeec-971f-4cf9-91c1-de02a88e40b2',
                qrCode: './static/images/eqcode.png',
            }],
            pagination: {
                totalPage: 0,//总页数
                pageSize: 5,//每页显示条数
                currPage: 1, //当前页
                totalCount: 2
            },
            appVisible: false,
            appAction: '',
            appForm: {
                name: '',
                enName: '',
                company: '',
                org: '',
                remark: ''
            },
            appRule: {
                name: [{
                    required: true,
                    message: '请输入应用名称',
                    trigger: 'blur'
                }],
                org: [{
                    required: true,
                    message: '请选择所属机构',
                }]
            },

            packListVisible: false,
            packList: [{
                name: 'crm-public公共包',
                useRange: '移动门户'
            }, {
                name: 'crm-public公共包',
                useRange: '移动门户'
            }, {
                name: 'crm-public公共包',
                useRange: '移动门户'
            }, {
                name: 'crm-public公共包',
                useRange: '移动门户'
            }, {
                name: 'crm-public公共包',
                useRange: '移动门户'
            }, {
                name: 'crm-public公共包',
                useRange: '移动门户'
            }],
            packVisible: false,
            packAction: '',
            packForm: {
                name: '',
                file: {}
            }
        }
    },
    methods: {
        handleSizeChange: function (val) {
            Vue.set(this.pagination, 'pageSize', val);
        },
        handleCurrentChange: function (val) {//改变当前显示的页数
            Vue.set(this.pagination, 'currPage', val);
        },


        addApp: function () {
            this.appVisible = true;
            this.appAction = 'add';
        },
        updateApp: function () {
            this.appVisible = true;
            this.appAction = 'update';
        },
        addPack: function () {

        }
    },
    mounted: function () {
        document.getElementById('app').style.display = 'block'
    }
})