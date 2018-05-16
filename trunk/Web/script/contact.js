contact = {
    login: function (loginId, loginPwd) {
        if (loginId == "admin" && loginPwd == "admin") {
            var obj = { success: true, data: {} };
            return JSON.stringify(obj);
        } else {
            return "{\"success\":false,\"msg\":\"用户名或密码错误\"}";
        }
    },
    lifing_getpage: function (pageIndex) {
        console.log(pageIndex);
        var result = {
            data: [{
                "costTypeId": "1000000001",
                "costTypeName": "生活费",
                "createBy": "-1",
                "createTime": "2016-11-12 11:11:11",
                "cusGroup": "",
                "familyPay": false,
                "id": "1000000",
                "imgUrl": "",
                "isMark": false,
                "isUpload": false,
                "notes": "我似乎测试的",
                "price": 22,
                "reason": "买菜",
                "time": "2016-11-11",
                "updateBy": "",
                "updateTime": ""
            }, {
                "costTypeId": "1000000001",
                "costTypeName": "生活费",
                "createBy": "-1",
                "createTime": "2016-11-12 11:11:11",
                "cusGroup": "",
                "familyPay": false,
                "id": "1000000",
                "imgUrl": "",
                "isMark": false,
                "isUpload": false,
                "notes": "我似乎测试的",
                "price": 22,
                "reason": "买菜",
                "time": "2016-11-11",
                "updateBy": "",
                "updateTime": ""
            }, {
                "costTypeId": "1000000001",
                "costTypeName": "生活费",
                "createBy": "-1",
                "createTime": "2016-11-12 11:11:11",
                "cusGroup": "",
                "familyPay": false,
                "id": "1000000",
                "imgUrl": "",
                "isMark": false,
                "isUpload": false,
                "notes": "我似乎测试的",
                "price": 22,
                "reason": "买菜",
                "time": "2016-11-11",
                "updateBy": "",
                "updateTime": ""
            }, {
                "costTypeId": "1000000001",
                "costTypeName": "生活费",
                "createBy": "-1",
                "createTime": "2016-11-12 11:11:11",
                "cusGroup": "",
                "familyPay": false,
                "id": "1000000",
                "imgUrl": "",
                "isMark": false,
                "isUpload": false,
                "notes": "我似乎测试的",
                "price": 22,
                "reason": "买菜",
                "time": "2016-11-11",
                "updateBy": "",
                "updateTime": ""
            }], success: true
        };
        if (pageIndex > 2) {
            result = { success: true, data: [] };
        }
        return JSON.stringify(result);
    },
    lifing_delete: function (id) {
        var obj = { success: true, data: {} };
        return JSON.stringify(obj);
    },
    lifing_save: function (lifestr) {
        var obj = { success: true, data: {} };
        return JSON.stringify(obj);
    },
    diction_list: function (parentId) {
        var result = {
            data: [{
                id: '1000000001',
                name: '生活费'
            }, {
                id: '1000000002',
                name: '水电费'
            }, {
                id: '1000000003',
                name: '车费'
            }, {
                id: '1000000004',
                name: '水果零食'
            }, {
                id: '1000000005',
                name: '房屋消费'
            }],
            success: true
        };
        return JSON.stringify(result);
    },
    income_getpage: function (pageIndex) {
        var result = {
            data: [{
                "createBy": "-1",
                "createTime": "2016-11-12 11:11:11",
                "cusGroup": "",
                "familyIncome": false,
                "id": "1000000",
                "isMark": false,
                "isUpload": "1",
                "note": "我似乎测试的",
                "price": "22",
                "time": "2016-11-11",
                "updateBy": "",
                "updateTime": ""
            }], success: true
        };
        return JSON.stringify(result);
    },
    lifing_GetCollectByDay: function () {
        var result = {
            data: [{
                "Time": "2017-04-01",
                "Price": "20.233223",
            }, {
                "Time": "2017-04-02",
                "Price": "30",
            }, {
                "Time": "2017-04-03",
                "Price": "40",
            }, {
                "Time": "2017-04-01",
                "Price": "20.233223",
            }, {
                "Time": "2017-04-02",
                "Price": "30",
            }, {
                "Time": "2017-04-03",
                "Price": "40",
            }, {
                "Time": "2017-04-01",
                "Price": "20.233223",
            }, {
                "Time": "2017-04-02",
                "Price": "30",
            }, {
                "Time": "2017-04-03",
                "Price": "40",
            }, {
                "Time": "2017-04-01",
                "Price": "20.233223",
            }, {
                "Time": "2017-04-02",
                "Price": "30",
            }, {
                "Time": "2017-04-03",
                "Price": "40",
            }, {
                "Time": "2017-04-01",
                "Price": "20.233223",
            }, {
                "Time": "2017-04-02",
                "Price": "30",
            }, {
                "Time": "2017-04-03",
                "Price": "40",
            }, {
                "Time": "2017-04-01",
                "Price": "20.233223",
            }, {
                "Time": "2017-04-02",
                "Price": "30",
            }, {
                "Time": "2017-04-03",
                "Price": "40",
            }], success: true
        };
        return JSON.stringify(result);
    },
    lifing_GetCollectByMonth: function () {
        var result = {
            data: [{
                "Time": "2017-04",
                "Price": "20",
            }, {
                "Time": "2017-05",
                "Price": "30",
            }, {
                "Time": "2017-06",
                "Price": "40",
            }], success: true
        };
        return JSON.stringify(result);
    },
    bank_getpage: function (pageIndex) {
        console.log(pageIndex);
        var result = {
            data: [{
                "Balance": "4000",
                "BankType": "1000100004",
                "BankTypeName": "余额宝",
                "CreateBy": "a23ded0e-9034-4985-a02a-85d4386d1a74",
                "CreateTime": "2014-06-18 22:14:04",
                "Id": "1844d2fb-332d-4423-9a76-e09f7c9e8273",
                "ImgUrl": "",
                "IsUpload": "1",
                "Note": "从中国建设银行转入到余额宝",
                "Price": "4000",
                "SaveType": "1000200001",
                "Time": "2014-06-18",
                "UpdateBy": "a23ded0e-9034-4985-a02a-85d4386d1a74",
                "UpdateTime": "2014-06-18 22:14:04"
            }, {
                "Balance": "50998.71",
                "BankType": "1000100001",
                "BankTypeName": "中国建设银行",
                "CreateBy": "a23ded0e-9034-4985-a02a-85d4386d1a74",
                "CreateTime": "2014-06-18 22:14:04",
                "Id": "b3d92c70-cf9e-4c71-ac19-9023f5fd4cf7",
                "ImgUrl": "",
                "IsUpload": "0",
                "Note": "从中国建设银行转入到余额宝",
                "Price": "4000",
                "SaveType": "1000200002",
                "Time": "2014-06-18",
                "UpdateBy": "a23ded0e-9034-4985-a02a-85d4386d1a74",
                "UpdateTime": "2014-06-18 22:14:04"
            }], success: true
        };
        if (pageIndex > 2) {
            result = { success: true, data: [] };
        }
        return JSON.stringify(result);
    },
    bank_delete: function () {
        return JSON.stringify({ success: true, data: [] });
    },
    bank_getbyid: function () {
        var result = {
            data: {
                "Balance": "4000",
                "BankType": "1000100004",
                "BankTypeName": "余额宝",
                "CreateBy": "a23ded0e-9034-4985-a02a-85d4386d1a74",
                "CreateTime": "2014-06-18 22:14:04",
                "Id": "1844d2fb-332d-4423-9a76-e09f7c9e8273",
                "ImgUrl": "",
                "IsUpload": "1",
                "Note": "从中国建设银行转入到余额宝",
                "Price": "4000",
                "SaveType": "1000200001",
                "Time": "2014-06-18",
                "UpdateBy": "a23ded0e-9034-4985-a02a-85d4386d1a74",
                "UpdateTime": "2014-06-18 22:14:04"
            }, success: true
        };
        return JSON.stringify(result);
    },
    bank_save: function (lifestr) {
        var obj = { success: true, data: {} };
        return JSON.stringify(obj);
    }
}