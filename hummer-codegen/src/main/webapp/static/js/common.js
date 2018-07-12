//获取头部边栏底部公共数据
var commonJavaPath = $('.main-header').attr('data-global');
var commonUserId = $('.main-header').attr('data-userdata');

function judgeUrl() {
    var path = window.location.pathname;
    $($('.sidebar-menu a')).each(function (i) {
        var href = $(this).attr('href');
        if (href === path) {
            $(this).closest('treeview-menu').addClass('menu-open');
            $(this).closest('treeview-menu').parent('li').attr('class', 'active')
        }
    })

}

Vue.prototype.clickModal = false;
//修改密码全局组件
Vue.prototype.openModifyPwd = function () {
    vm.$refs.modifypwd.modifyPwdDialog = true;
    vm.$refs.modifypwd.modifyPwdForm = {
        oldpwd: "",
        newpwd: "",
        checkPass: ""
    }
    // vm.$set(vm.$refs.modifypwd.modifyPwdForm,'oldpwd','');
    // vm.$set(vm.$refs.modifypwd.modifyPwdForm,'newpwd','');
    // vm.$set(vm.$refs.modifypwd.modifyPwdForm,'checkPass','');
    vm.$refs.modifypwd.$refs.modifyPwdForm ? vm.$refs.modifypwd.$refs.modifyPwdForm.resetFields() : '';

};
Vue.prototype.pickerOption = {
    disabledDate: function (time) {
        return time.getTime() > dateDeline;
    }
}
Vue.prototype.pickerOptions = {
    disabledDate: function (time) {
        return time.getTime() > dateDeline;
    },
    shortcuts: [
        {
            text: '昨日',
            onClick: function (picker) {
                var start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24);
                picker.$emit('pick', [start, start]);
            }
        },
        {
            text: '今日',
            onClick: function (picker) {
                var start = new Date();
                start.setTime(start.getTime());
                picker.$emit('pick', [start, start]);
            }
        }, {
            text: '最近一周',
            onClick: function (picker) {
                var end = new Date();
                var start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                picker.$emit('pick', [start, end]);
            }
        }, {
            text: '最近一个月',
            onClick: function (picker) {
                var end = new Date();
                var start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                picker.$emit('pick', [start, end]);
            }
        }, {
            text: '最近三个月',
            onClick: function (picker) {
                var end = new Date();
                var start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                picker.$emit('pick', [start, end]);
            }
        }]
}

function getQueryObj(queryObj) {
    var obj = {};
    for (var p in queryObj) {
        if (p == 'date') {
            obj.startTime = getDate(queryObj.date[0]);
            obj.endTime = getDate(queryObj.date[1]);
        } else {
            obj[p] = queryObj[p];
        }
    }
    return obj;
}

function getQueryObj2(queryObj) {
    var obj = {};
    for (var p in queryObj) {
        if (p == 'date') {
            obj.startTime = getDate(queryObj.date);
            obj.endTime = getDate(queryObj.date);
        } else {
            obj[p] = queryObj[p];
        }
    }
    return obj;
}

//修改密码
Vue.component('modifypwd', {
    props: ['modifyPwdDialog'],
    template: '<el-dialog title="修改密码"  :visible.sync="modifyPwdDialog"><el-form label-position="right" label-width="100px" :model="modifyPwdForm" ref="modifyPwdForm" :rules="modifyPwdFormRule"><el-form-item label="原密码" prop="oldpwd"><el-input style="width:360px;" type="password" v-model="modifyPwdForm.oldpwd" auto-complete="off"></el-input></el-form-item><el-form-item label="新密码" prop="newpwd"><el-input style="width:360px;" type="password" v-model="modifyPwdForm.newpwd" auto-complete="off"></el-input></el-form-item><el-form-item label="确认密码" prop="checkPass"><el-input style="width:360px;" type="password" v-model="modifyPwdForm.checkPass" auto-complete="off"></el-input></el-form-item></el-form><div slot="footer"><div class="btn btn-group"><button type="button" class="btn btn-danger" @click="modifyPwd">确定</button><button type="button" class="btn btn-default" @click="cancelModifyPwd">取消</button></div></div></el-dialog>',
    data: function () {
        var validatePass = function (rule, value, callback) {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else {
                if (vm.$refs.modifypwd.checkPass !== '') {
                    vm.$refs.modifypwd.$refs.modifyPwdForm.validateField('checkPass');
                }
                callback();
            }
        };
        var validatePass2 = function (rule, value, callback) {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== vm.$refs.modifypwd.modifyPwdForm.newpwd) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };

        return {
            modifyPwdDialog: false,
            modifyPwdForm: {
                oldpwd: '',
                newpwd: '',
                checkPass: ''
            },
            modifyPwdFormRule: {
                oldpwd: [
                    {required: true, trigger: 'blur', message: '密码不能为空'}
                ],
                newpwd: [
                    {required: true, validator: validatePass, trigger: 'blur'}
                ],
                checkPass: [
                    {required: true, validator: validatePass2, trigger: 'blur'}
                ]
            }
        }
    },
    methods: {
        modifyPwd: function () {
            var _this = this;
            this.$refs.modifyPwdForm.validate(function (valid) {
                if (valid) {
                    $.ajax({
                        url: commonJavaPath + "/employee/passwordLevel",
                        type: "post",
                        contentType: "application/json",
                        data: null,
                        dataType: "json",
                        success: function (res) {
                            if (res.code === 0) {
                                var reg1 = /^[A-Za-z0-9]{6,}$/;
                                var reg2 = /^(?!([a-zA-Z]+|\d+)$)[a-zA-Z\d]{8,}$/;
                                var reg3 = /(?=.*[a-z])(?=.*\d)(?=.*[`\.#_\-\$@!~%^*\+\|\(\)'";:,])[a-z\d`\.#_\-\$@!~%^*\+\|\(\)'";:,]{8,}/i;
                                var rule = res.passwordLevel;
                                var pwd = _this.modifyPwdForm.checkPass;
                                if (rule == 'one') {
                                    if (!reg1.test(pwd) && !reg2.test(pwd) && !reg3.test(pwd)) {
                                        _this.$message({
                                            type: "info",
                                            message: '密码至少为6位数字或字母，例如123456',
                                            duration: 1500
                                        });
                                        return false;
                                    }
                                }
                                if (rule == 'two') {
                                    if (!reg2.test(pwd) && !reg3.test(pwd)) {
                                        _this.$message({
                                            type: "info",
                                            message: '密码至少为8位以上（含8位）的数字+字母组合，例如12345678a',
                                            duration: 1500
                                        });
                                        return false;
                                    }
                                }
                                if (rule == 'third') {
                                    if (!reg3.test(pwd)) {
                                        _this.$message({
                                            type: "info",
                                            message: '密码至少为8位以上（含8位）的数字+字母+特殊字符组合，例如12345678a+',
                                            duration: 1500
                                        });
                                        return false;
                                    }
                                }
                                $.ajax({
                                    url: commonJavaPath + "/employee/modifyPassword",
                                    type: "post",
                                    contentType: "application/json",
                                    data: JSON.stringify({
                                        id: commonUserId,
                                        password: _this.modifyPwdForm.oldpwd,
                                        newPassword: _this.modifyPwdForm.checkPass
                                    }),
                                    dataType: "json",
                                    success: function (res) {
                                        console.log(res);
                                        if (res.code === 0) {
                                            _this.$message({
                                                type: "success",
                                                message: "修改成功",
                                                duration: 1500
                                            });
                                            _this.modifyPwdDialog = false;
                                        } else {
                                            _this.$message({
                                                type: "error",
                                                message: res.msg,
                                                duration: 1500
                                            })
                                        }
                                    }
                                })
                            } else {
                                _this.$message({
                                    type: "info",
                                    message: res.msg
                                });
                            }
                        }
                    })

                } else {
                    return false;
                }
            });
        },
        cancelModifyPwd: function () {
            this.$refs.modifyPwdForm.resetFields();
            this.modifyPwdDialog = false;
        }
    }
})


//设置请求头
Vue.http.interceptors.push(function (request, next) {
    request.headers.set('Content-type', 'application/json');
    next(function (response) {
        return response
    })
});

/************全局属性和方法************/

//获取host+port
Vue.prototype.getURL = function (projoName) {
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var finalName;
    if (projoName) {
        finalName = localhostPaht + projoName;
    } else {
        finalName = localhostPaht;
    }
    return finalName;
}
// 上传文件 的处理
Vue.prototype.handleFileChange = function (fileData, suffix) {
    return function (file, fileList) {
        if (!checksuffix(file.name, suffix)) {
            fileData = [];
        }
    }
}
Vue.prototype.handleFileSuccess = function (fileData, fileType) {
    return function (res, file) {
        if (res.code == 0) {
            if (fileType == 'pic') {
                fileData[0].fileId = res.fileId;
            }
            if (fileType == 'file') {
                fileData[0].fileId = res.fileId;
                fileData[0].fileName = file.name;
                fileData[0].fileSize = file.size;

            }
            if (fileType == 'versionFile' || fileType == 'updateVersionFile') {

                fileData[0].fileName = file.name;
                fileData[0].fileSize = file.size;
                fileData[0].adress = res.knApplicationVersionUploadVo;

            }
            if (fileType == 'pics') {
                fileData.push({
                    url: commonJavaPath + '/api/v4/attachment/id/' + res.fileId + '.' + getsuffix(file.name)
                })
            }
        } else {
            vm.$message({
                type: 'error',
                message: res.msg
            })
        }
        if (fileData[0].uploadStatus) {
            fileData[0].uploadStatus = false;
        }

    }
}

Vue.prototype.beforeFileUpload = function (suffix, errMsg, fileData) {
    return function (file) {
        if (!checksuffix(file.name, suffix)) {
            this.$message({
                type: 'error',
                message: errMsg
            });
            return false;
        }
        if (fileData) {
            fileData[0].uploadStatus = true;
        }
        return true;

    }

}
Vue.prototype.handleFileRemove = function (fileData) {
    return function (file) {
        var imgList = fileData.filter(function (v) {
            return file.url != v.url;
        });
        vm.appForm.imgList = imgList;
    }
}

//组织树节点的加载方法
Vue.prototype.loadOrgNode = function (node, resolve) {
    var _this = this;
    this.treeNodes = node;
    var param = {};
    if (node.level === 0) {
        param = {
            supId: '',
            depth: 1
        };
    } else {
        param = {
            supId: node.data.id,
            depth: node.data.depth + 1
        };
    }
    $.ajax({
        url: commonJavaPath + '/organization/org_tree',
        type: "post",
        data: param,
        dataType: "json",
        success: function (res) {
            var childrenData = res;
            childrenData.forEach(function (item) {
                item.disabled = false;
                //根据后台给的字段值(待定)判断是否为叶子节点(没有子节点)
                if (item.childCount == 0) {

                    item.leaf = true;
                } else {
                    item.leaf = false;
                }
            });

            setTimeout(function () {
                resolve(childrenData);
            }, 300);
        }
    })
},
//组织树的属性
    Vue.prototype.orgProps = {
        label: 'name',
        children: 'children',
        isLeaf: 'leaf'
    };


Array.prototype.diff = function (a) {
    return this.filter(function (i) {
        return a.indexOf(i) < 0;
    });
};

//ajax封装
function comonAjax(url, method, isAsync, data, successCallback, errorCallback, contentType) {
    $.ajax({
        url: commonJavaPath + url,
        data: data,
        type: method,
        dataType: 'json',
        contentType: contentType ? contentType : 'application/json',
        async: isAsync,
        success: function (res) {
            successCallback(res);
        },
        error: function (res) {
            errorCallback ? errorCallback(res) : console.log(res);
        }
    })
}

//	组织树默认展开三级
Vue.prototype.diffOpened = function (arr) {
    var _this = this;
    arr.forEach(function (i) {
        if (i.depth < 3) {
            i.state.opened = true;
        } else {
            i.state.opened = false;
        }
        if (i.children && i.children.length > 0) {
            _this.diffOpened(i.children)
        }
    })
}
Vue.prototype.diffOpened2 = function (arr) {
    var _this = this;
    arr.forEach(function (i) {
        if (i.depth < 3) {
            i.state = {
                opened: true
            }
        } else {
            i.state = {
                opened: false
            }
        }
        if (i.children && i.children.length > 0) {
            _this.diffOpened2(i.children)
        }
    })
}
/****自定义过滤器 */
//时间戳格式化
Vue.filter("formatDate", function (value, str) {
    if (value != '') {
        if (str == undefined) {
            return new Date(value).format();
        }
        return new Date(value).format(str);
    }
    return '';
});

Vue.filter("formatDateObj", function (value) {
    return getDate(value[0]) + '~' + getDate(value[1]);
});
//截取文件名
Vue.filter("basename", function (value) {
    if (typeof(value) == 'string' && value != '') {
        var filearr = value.split('/');
        return filearr[filearr.length - 1];
    }
    return '';
});
//过滤员工状态
Vue.filter("filterEmpStatus", function (value) {
    return value === 'ENABLE' ? '启用' : '禁用';
});
Vue.filter("filterUserStatus", function (value) {
    return value === 'enable' ? '启用' : '禁用';
});
//过滤用户类型
Vue.filter("filterUserType", function (value) {
    return value == 'ADMIN' ? '管理员' : '普通用户';
});

//过滤是否
Vue.filter("filterYesOrNo", function (value) {
    return value == 'Y' ? '是' : '否';
});

//验证手机号码
var checkTel = function (rule, value, callback, labelName) {
    if (!value) {
        callback(new Error('请输入' + labelName));
    } else {
        var reg = /^(0|86|17951)?(13[0-9]|15[012356789]|166|17[3678]|18[0-9]|14[57])[0-9]{8}$/;
        if (!reg.test(value)) {
            callback(new Error('请输入正确的' + labelName));
        }
    }
    callback();
}


//验证传真号码
var checkFax = function (rule, value, callback, labelName) {
    if (!value) {
        callback(new Error('请输入' + labelName));
    } else {
        var reg = /^(\d{3,4}-)?\d{7,8}$/;
        if (!reg.test(value)) {
            callback(new Error('请输入正确的' + labelName));
        }
    }
    callback();
}


//验证电子邮箱
var checkEmail = function (rule, value, callback, labelName) {
    if (!value) {
        callback(new Error('请输入' + labelName));
    } else {
        var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
        if (!reg.test(value)) {
            callback(new Error('请输入正确的' + labelName));
        }
    }
    callback();
}

//验证邮政编码
var checkPostcode = function (rule, value, callback, labelName) {
    if (!value) {
        callback(new Error('请输入' + labelName));
    } else {
        var reg = /^[a-zA-Z0-9 ]{3,12}$/;
        if (!reg.test(value)) {
            callback(new Error('请输入正确的' + labelName));
        }
    }
    callback();
}

//验证非负整数
var checkNum = function (rule, value, callback, labelName) {
    if (!value) {
        callback(new Error('请输入' + labelName));
    } else {
        var reg = /^[0-9]\d*$/;
        if (!reg.test(value)) {
            callback(new Error(labelName + '只能输入整数'));
        }
    }
    callback();
}
//验证正整数
var checkNumber1 = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[1-9]\d*$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为正整数'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[1-9]\d*$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为正整数'));
                }
            }
        }

        callback();
    }
}
//验证数字
var checkNumber = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[0-9]\d*$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为非负整数'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[0-9]\d*$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为非负整数'));
                }
            }
        }

        callback();
    }
}
var checkNumberSix = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[1-9]\d*$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为1-999999的整数'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[1-9]\d*$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为1-999999的整数'));
                }
            }
        }

        callback();
    }
}
//验证正整数
var checkInteger = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[0-9]\d*$/;
                if (!reg.test(value) || parseInt(value) <= 0) {
                    callback(new Error(labelName + '只能为正整数'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[0-9]\d*$/;
                if (!reg.test(value) || parseInt(value) <= 0) {
                    callback(new Error(labelName + '只能为正整数'));
                }
            }
        }

        callback();
    }
}
//同时验证手机号码和固化
var checkMobile = function (rule, value, callback, labelName) {
    if (!value) {
        callback(new Error('请输入' + labelName));
    } else {
        var ph = /^(0|86|17951)?(13[0-9]|15[012356789]|166|17[3678]|18[0-9]|14[57])[0-9]{8}$/;
        var mb = /^(0[0-9]{2,3}\-)([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
        if (!ph.test(value) && !mb.test(value)) {
            callback(new Error('请输入正确的' + labelName));
        }
    }
    callback();
}

//验证身份证
var checkIdCard = function (rule, value, callback, labelName) {
    if (!value) {
        callback(new Error('请输入' + labelName));
    } else {
        var reg = /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
        if (!reg.test(value)) {
            callback(new Error('请输入正确的' + labelName));
        }
    }
    callback();
}
//验证邮箱
var validEmail = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '要为正确的格式'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '要为正确的格式'));
                }
            }
        }

        callback();
    }
}
//验证手机号
var validTel = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^(0|86|17951)?(13[0-9]|15[012356789]|166|17[3678]|18[0-9]|14[57])[0-9]{8}$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '要为正确的格式'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^(0|86|17951)?(13[0-9]|15[012356789]|166|17[3678]|18[0-9]|14[57])[0-9]{8}$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '要为正确的格式'));
                }
            }
        }

        callback();
    }
}
//验证固话
var validPhone = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^(0[0-9]{2,3}\-)([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '要为正确的格式'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^(0[0-9]{2,3}\-)([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '要为正确的格式'));
                }
            }
        }

        callback();
    }
}
//验证汉字字母数字下划线
var checkNameRule1 = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[\w\u4e00-\u9fa5]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为汉字、字母、数字、_'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[\w\u4e00-\u9fa5]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为汉字、字母、数字、_'));
                }
            }
        }

        callback();
    }
}
//验证汉字字母数字
var checkNameRule2 = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为汉字、字母、数字'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为汉字、字母、数字'));
                }
            }
        }

        callback();
    }
}


//验证字母数字
var checkNameRule3 = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[ A-Za-z0-9]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为字母、数字'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[ A-Za-z0-9]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为字母、数字'));
                }
            }
        }
        callback();
    }
}
//验证字母下划线
var checkNameRule4 = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[A-Za-z_]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为字母、_'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[A-Za-z_]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为字母、_'));
                }
            }
        }
        callback();
    }
}
//验证字母
var checkNameRule5 = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[A-Za-z]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为字母'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[A-Za-z]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为字母'));
                }
            }
        }
        callback();
    }
}
//非中文
var checkNameRule6 = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[\u4e00-\u9fa5]+/g;
                if (reg.test(value)) {
                    callback(new Error(labelName + '不能包含汉字'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[\u4e00-\u9fa5]+/g;
                if (reg.test(value)) {
                    callback(new Error(labelName + '不能包含汉字'));
                }
            }
        }
        callback();
    }
}

//中文和字母
var checkNameRule9 = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[A-Za-z\u4e00-\u9fa5]+/g;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能包含汉字、字母'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[A-Za-z\u4e00-\u9fa5]+/g;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能包含汉字、字母'));
                }
            }
        }
        callback();
    }
}
//验证汉字字母数字下划线-
var checkNameRule7 = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[-_A-Za-z0-9\u4e00-\u9fa5]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为汉字、字母、数字、_、-'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[-_A-Za-z0-9\u4e00-\u9fa5]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为汉字、字母、数字、_、-'));
                }
            }
        }

        callback();
    }
}

//验证字母数字下划线-
var checkNameRule8 = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^[A-Za-z0-9_-]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为字母、数字、_、-'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^[A-Za-z0-9_-]+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为字母、数字、_、-'));
                }
            }
        }
        callback();
    }
}
var checkAllEmail = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '格式要正确'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '格式要正确'));
                }
            }
        }
        callback();
    }
}
//验证手机号码固话
var checkAllPhone = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var ph = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
                var mb = /^(0[0-9]{2,3}\-)([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
                if (!ph.test(value) && !mb.test(value)) {
                    callback(new Error(labelName + '格式要正确'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var ph = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
                var mb = /^(0[0-9]{2,3}\-)([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
                if (!ph.test(value) && !mb.test(value)) {
                    callback(new Error(labelName + '格式要正确'));
                }
            }
        }
        callback();
    }
}
//验证域名
var checkDomain = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^((25[0-5])|(2[0-4]\d)|(1\d\d)|([1-9]\d)|\d)(\.((25[0-5])|(2[0-4]\d)|(1\d\d)|([1-9]\d)|\d)){3}$|^([a-zA-Z0-9]([a-zA-Z0-9\-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,6}$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为正确格式的IP或域名'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^((25[0-5])|(2[0-4]\d)|(1\d\d)|([1-9]\d)|\d)(\.((25[0-5])|(2[0-4]\d)|(1\d\d)|([1-9]\d)|\d)){3}$|^([a-zA-Z0-9]([a-zA-Z0-9\-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,6}$/;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为正确格式的IP或域名'));
                }
            }
        }
        callback();
    }
}
//验证URL
var checkUrl = function (labelName, required) {
    return function (rule, value, callback) {
        if (required === false) {
            if (value) {
                var reg = /^https?:\/\/(([a-zA-Z0-9_-])+(\.)?)*(:\d+)?(\/((\.)?(\?)?=?&?[a-zA-Z0-9_-](\?)?)*)*$/i;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为http或https开头的地址'));
                }
            }
        } else {
            if (!value) {
                callback(new Error('请输入' + labelName));
            } else {
                var reg = /^https?:\/\/(([a-zA-Z0-9_-])+(\.)?)*(:\d+)?(\/((\.)?(\?)?=?&?[a-zA-Z0-9_-](\?)?)*)*$/i;
                if (!reg.test(value)) {
                    callback(new Error(labelName + '只能为http或https开头的地址'));
                }
            }
        }
        callback();
    }
}
Date.prototype.format = function (format) {
    //eg:format="yyyy-MM-dd hh:mm:ss";

    if (!format) {
        format = "yyyy-MM-dd hh:mm";
    }

    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "H+": this.getHours() < 10 ? '0' + this.getHours() : this.getHours(), // hour
        "h+": this.getHours() < 10 ? '0' + this.getHours() : this.getHours(), // hour
        "m+": this.getMinutes() < 10 ? '0' + this.getMinutes() : this.getMinutes(), // minute
        "s+": this.getSeconds() < 10 ? '0' + this.getSeconds() : this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds()
    };

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

function checksuffix(name, isAllow) {
    var fileType = getsuffix(name);
    if (isAllow.indexOf(fileType.toLowerCase()) == -1) {
        return false;
    }
    return true;
}

function getsuffix(name) {
    var splitArr = name.split('.');
    return splitArr[splitArr.length - 1];
}

//生成uuid
function createUuid() {
    function S4() {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    }

    return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
}

function getDate(timeStmp) {
    var date = new Date(timeStmp);
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + '-' + month + '-' + strDate;
    return currentdate;
}

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return '';
}

function cloneQueryObj(queryObj) {
    var obj = {};
    for (var p in queryObj) {
        obj[p] = queryObj[p];
    }
    return obj;
}

var dateDeline = Date.now();