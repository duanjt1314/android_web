function alertMsg(msg) {
    //$.tips({
    //    content: msg,
    //    stayTime: 2000,
    //    type: "info"
    //});
    contact.showMsg(msg);
}

function alertMsgCenter(msg, ok) {
    //$.dialog({
    //    content: msg,
    //    title: 'alert',
    //    ok: ok
    //});
    contact.alertMsg(msg);
}

function close() {
    //系统退出
    //contact.exit();
    history.back();
}

/**
 * 请求服务器数据
 * @param {*} methodName  方法名称
 * @param {*} params 参数，json对象
 */
function request(methodName, params) {
    //因为数据交互都是字符串，所以需要进行转换
    params.methodName = methodName;
    var result = contact.request(JSON.stringify(params));
    var rObj = JSON.parse(result);
    return rObj;
}

/**
 * 请求服务器数据(异步)
 * @param {*} methodName 方法名称
 * @param {*} params 参数
 * @param {*} callback 回调函数名
 */
function requestAsyn(methodName,params,callback) {
    //因为数据交互都是字符串，所以需要进行转换
    params.methodName = methodName;
    params.callback=callback;
    var result = contact.requestAsyn(JSON.stringify(params));
    var rObj = JSON.parse(result);
    return rObj;
}