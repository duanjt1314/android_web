function request(methodName) {
    if (methodName == 'diction_list') {
        return {
            success: true,
            msg: "",
            data: [{
                id: 1,
                name: "生活费"
            }, {
                id: 2,
                name: "医药费"
            }, {
                id: 3,
                name: "车费"
            }]
        };
    } else if (methodName == "lifing_save") {
        return {
            success: true,
            msg: "成功"
        };
    }
}

var contact = {
    alertMsg: function (msg) {
        alert(msg);
    },
    showMsg: function (msg) {
        alert(msg);
    }
}