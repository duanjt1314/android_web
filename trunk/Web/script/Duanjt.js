/**
* 自定义的JavaScript类库，方便调用和查询
*/
var Duanjt = {
    /**
    * 日期类，对日期的处理
    */
    Date: {
        /**
        * 将一个毫秒数转换为日期
        */
        NumToDate: function (num) {
            if (typeof (num) == "number") {
                var time = new Date(num); //里面的毫秒数不能加引号
                return time.getFullYear() + "-" + Duanjt.String.InsertSpace((time.getMonth() + 1), 2) + "-" + Duanjt.String.InsertSpace(time.getDate(), 2); //月份为0-11
            } else if (typeof (num) == "string") {
                if (num.indexOf("(") > 0) {
                    var a = num.indexOf("(");
                    var b = num.indexOf(")");
                    if (num.indexOf("+") > 0)
                        b = num.indexOf("+");
                    var str = num.substr(a + 1, b - a - 1);
                    var time = new Date();
                    time.setTime(str);
                    return time.getFullYear() + "-" + Duanjt.String.InsertSpace((time.getMonth() + 1), 2) + "-" + Duanjt.String.InsertSpace(time.getDate(), 2); //月份为0-11
                } else {
                    var time = new Date();
                    time.setTime(num); //里面的毫秒数可以加引号，也可以不加引号
                    return time.getFullYear() + "-" + Duanjt.String.InsertSpace((time.getMonth() + 1), 2) + "-" + Duanjt.String.InsertSpace(time.getDate(), 2); //月份为0-11
                }
            }
        },
        /**
        * 将一个毫秒数转换为时间
        */
        NumToDateTime: function (num) {
            if (typeof (num) == "number") {
                var time = new Date(num); //里面的毫秒数不能加引号
                return time.getFullYear() + "-" + Duanjt.String.InsertSpace((time.getMonth() + 1), 2) + "-" + Duanjt.String.InsertSpace(time.getDate(), 2) + " " + Duanjt.String.InsertSpace(time.getHours(), 2) + ":" + Duanjt.String.InsertSpace(time.getMinutes(), 2) + ":" + Duanjt.String.InsertSpace(time.getSeconds(), 2); //月份为0-11
            } else if (typeof (num) == "string") {
                if (num.indexOf("(") > 0) {
                    var a = num.indexOf("(");
                    var b = num.indexOf(")");
                    if (num.indexOf("+") > 0)
                        b = num.indexOf("+");
                    var str = num.substr(a + 1, b - a - 1);
                    var time = new Date();
                    time.setTime(str);
                    return time.getFullYear() + "-" + Duanjt.String.InsertSpace((time.getMonth() + 1), 2) + "-" + Duanjt.String.InsertSpace(time.getDate(), 2) + " " + Duanjt.String.InsertSpace(time.getHours(), 2) + ":" + Duanjt.String.InsertSpace(time.getMinutes(), 2) + ":" + Duanjt.String.InsertSpace(time.getSeconds(), 2); //月份为0-11
                } else {
                    var time = new Date();
                    time.setTime(num); //里面的毫秒数可以加引号，也可以不加引号
                    return time.getFullYear() + "-" + Duanjt.String.InsertSpace((time.getMonth() + 1), 2) + "-" + Duanjt.String.InsertSpace(time.getDate(), 2) + " " + Duanjt.String.InsertSpace(time.getHours(), 2) + ":" + Duanjt.String.InsertSpace(time.getMinutes(), 2) + ":" + Duanjt.String.InsertSpace(time.getSeconds(), 2); //月份为0-11
                }
            }
        },
        /**
        * 将时间转换为毫秒数,date格式为：2014/11/05 12:05:05
        */
        DateToNum: function (date) {
            var time = new Date(date.replace(/-/g, "/"));
            return time.getTime();
        },
        //Microsoft AJAX serialized dates 
        MsAjaxTime: function (val) {
            var re = /-?\d+/;
            var m = re.exec(val);
            var d = new Date(parseInt(m[0]));
            return d;
        },
        /**
        * 将格式为2012-1-7的日期转换为格式为2012-01-07的日期格式
        */
        DateFormat: function (dateStr) {
            if (dateStr.indexOf('.') > 0)
                dateStr = dateStr.replace(/\./g, "-"); //正则表达式匹配替换，\.表示一个点，而不是任意通配符
            if (dateStr.indexOf('/') > 0)
                dateStr = dateStr.replace(/\//g, "-");
            var array = dateStr.split('-');
            return array[0] + "-" + Duanjt.String.InsertSpace(array[1], 2) + "-" + Duanjt.String.InsertSpace(array[2], 2); //月份为0-11
        },
        //判断字符串 是否符合时期类型格式 如 (2008-07-22 13:04:06)
        IsDateTime: function (str) {
            var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
            var r = str.match(reg);
            if (r != null)
                return true;
            else
                return false;
        },
        //判断两个时间之间相差的天数，格式为2014-11-02
        DiffDay: function (beginTime, endTime) {
            return (new Date(endTime) - new Date(beginTime)) / 1000 / 60 / 60 / 24;
        }
    },
    /**
    *地址，对Url地址的处理
    */
    Url: {
        /*
        *截取Url中传入的参数，如aa.html?id=5,
        *调用方法,var parms=GetPara("aa.html?id=5")
        *alert(parms['id']);
        *返回5
        */
        GetPara: function (url) {
            var pattern = /([A-Za-z0-9_-]+)=([A-Za-z0-9_-]+)/ig; //定义正则表达式 
            var parames = {}; //定义数组 
            url.replace(pattern, function (a, b, c) { parames[b] = c; });
            /* 这是最关键的.当replace匹配到classid=9时.那么就用执行function(a,b,c);其中a的值为:classid=9,b的值为 classid,c的值为9;(这是反向引用.因为在定义 正则表达式的时候有两个子匹配.)然后将数组的key为classid的值赋为9;然后完成.再继续匹配到id=2;此时执行 function(a,b,c);其中a的值为:id=2,b的值为id,c的值为2;然后将数组的key为id的值赋为2.*/
            return parames; //返回这个数组
        }
    },
    /**
    * 字符串类，对字符串的处理
    */
    String: {
        /**
        * 判断字符串的位数是否满足指定位数，如果不满足就在前面加0
        */
        InsertSpace: function (str, num) {
            var result = str.toString();
            while (result.length < num) {
                result = "0" + result;
            }
            return result;
        },
        /**
        * 获得Guid
        */
        GetGuid: function () {
            var guid = "";
            for (var i = 1; i <= 32; i++) {
                var n = Math.floor(Math.random() * 16.0).toString(16);
                guid += n;
                if ((i == 8) || (i == 12) || (i == 16) || (i == 20))
                    guid += "-";
            }
            return guid;
        }
    },
    Float: {
        /**
        * 将指定小数保留len位小数，不足位数用0补足
        */
        ToFloat: function (val, len) {
            var f = parseFloat(val);
            if (isNaN(f)) {
                return false;
            }
            var f = Math.round(val * Math.pow(10, len)) / Math.pow(10, len);
            var s = f.toString();
            var rs = s.indexOf('.');
            if (rs < 0) {
                rs = s.length;
                s += '.';
            }
            while (s.length <= rs + len) {
                s += '0';
            }
            return s;
        }
    },
    JSON: {
        /**
        * 将Json转换为Model实体（Ext专用）
        */
        GetModel: function (json) {

        }
    },
    /**
    * Cookie操作类
    */
    Cookie: {
        /**
        * 设置Cookie的值
        * name(名称)
        * value(Cookie值)
        * hours(过期时间，不设置就表示关闭浏览器就过期)
        * path 路径，如:/
        */
        SetCookie: function (name, value, hours, path) {
            var expire = "";
            if (hours != null) {
                expire = new Date((new Date()).getTime() + hours * 3600000);
                expire = "; expires=" + expire.toGMTString();
            }
            if (path && path != null) {
                expire += ";path=" + path;
            }
            document.cookie = escape(name) + "=" + escape(value) + expire;
        },
        /**
        * 根据名称获取cookie的值
        */
        GetCookie: function (name) {
            var cookieValue = "";
            var search = escape(name) + "=";
            if (document.cookie.length > 0) {
                offset = document.cookie.indexOf(search);
                if (offset != -1) {
                    offset += search.length;
                    end = document.cookie.indexOf(";", offset);
                    if (end == -1) end = document.cookie.length;
                    cookieValue = unescape(document.cookie.substring(offset, end))
                }
            }
            return cookieValue;
        },
        /**
        * 移除cookie
        */
        Remove: function (name, path) {
            var expire = new Date((new Date()).getTime() - 100);
            expire = "; expires=" + expire.toGMTString();

            if (path && path != null) {
                expire += ";path=" + path;
            }
            document.cookie = escape(name) + "=" + escape("") + expire;
        }
    }
}

/**
* 日期格式化，date：需要格式化的日期 format：格式化字符串，如yyyy-MM-dd
*/
Date.prototype.format = function (format) //author: meizz   //辅助函数
{
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(),    //day
        "h+": this.getHours(),   //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S": this.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
    (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
      RegExp.$1.length == 1 ? o[k] :
        ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}

//将数字转换为指定长度，长度不够在前面加0
Number.prototype.format = function (l) {
    var s = this.toString();
    while (s.length < l) {
        s = "0" + s;
    }
    return s;
}