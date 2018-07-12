var vm = new Vue({
    el: "#app",
    data: function () {
        return {
            empCount: '',
            iosCount: '',
            andCount: "",
            appCount: '',
            functionCount: '',
            bsAppCount: ''
        }
    },
    methods: {
        getIndexData: function () {
            var _this = this;
            $.ajax({
                url: commonJavaPath + '/home/resources',
                type: "post",
                async: true,
                success: function (res) {
                    var res = JSON.parse(res);
                    _this.empCount = res.empCount;
                    _this.iosCount = res.iosCount;
                    _this.andCount = res.andCount;
                    _this.appCount = res.appCount;
                    _this.functionCount = res.functionCount;
                    _this.bsAppCount = res.bsAppCount;
                }
            })
        }

    },
    mounted: function () {
        var _this = this;
        document.getElementById('app').style.display = 'block';
        this.getIndexData();

        var menuList = JSON.parse($('.main-sidebar').attr('data-menu'));


        var employee = false;
        var device = false;
        var ret1 = false;
        var ret2 = false;
        var ret3 = false;
        menuList.forEach(function (v) {
            if (v.code == 'user') {
                v.list.forEach(function (sv) {
                    if (sv.code == 'employee') {
                        employee = true;
                    }
                })
            }

            if (v.code == 'device') {
                v.list.forEach(function (sv) {
                    if (sv.code == 'device_info') {
                        device = true;
                    }
                })
            }

            if (v.code == 'application') {
                v.list.forEach(function (sv) {
                    if (sv.code == 'appInfo') {
                        ret1 = true;
                    }
                    if (sv.code == 'appstore-function') {
                        ret2 = true;
                    }
                    if (sv.code == 'appstore-application') {
                        ret3 = true;
                    }

                })
            }

        });


        if (!employee) {
            $('#user').removeAttr('href').find('.icon-yonghu').css('color', '#ccc');
        }

        if (!device) {
            $('#deviceInfo1').removeAttr('href').find('.icon-apple').css('color', '#ccc');
            $('#deviceInfo2').removeAttr('href').find('.icon-android').css('color', '#ccc');
        }

        if (!ret1) {
            $('#appInfo').removeAttr('href').find('.icon-yingyong').css('color', '#ccc');
        }
        if (!ret2) {
            $('#lightApp').removeAttr('href').find('.icon-qingyingyong').css('color', '#ccc');
        }
        if (!ret3) {
            $('#thirdApp').removeAttr('href').find('.icon-disanfangyingyong').css('color', '#ccc');
        }


    }
})
