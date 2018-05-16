/*
日期控件
*/
(function ($) {
    Zepto.fn.CusDate = function (opt) {
        var days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

        var innerHtml = "";
        innerHtml += '<div class="ui-select">';
        innerHtml += '<select name="date-year">';
        innerHtml += '</select>';
        innerHtml += '</div>';

        innerHtml += '<div class="ui-select">';
        innerHtml += '<select name="date-month">';
        innerHtml += '</select>';
        innerHtml += '</div>';

        innerHtml += '<div class="ui-select">';
        innerHtml += '<select name="date-day">';
        innerHtml += '</select>';
        innerHtml += '</div>';

        this.html(innerHtml);
        
        var sYear = this.find("select[name=date-year]");
        var sMonth = this.find("select[name=date-month]");
        var sDay = this.find("select[name=date-day]");

        for (var i = 2010; i <= 2020; i++) {
            sYear.append('<option value="' + i + '">' + i + '</option>');
        }
        for (var i = 1; i <= 12; i++) {
            sMonth.append('<option value="' + i.format(2) + '">' + i.format(2) + '</option>');
        }
        for (var i = 1; i <= 31; i++) {
            sDay.append('<option value="' + i.format(2) + '">' + i.format(2) + '</option>');
        }

        sYear.on('change', function () {
            var maxValue = getDays(sYear.val(), sMonth.val());
            bindDays(maxValue);
        });
        sMonth.on('change', function (a) {
            var maxValue = getDays(sYear.val(), sMonth.val());
            bindDays(maxValue);
        });

        //给日下拉框绑定数据
        function bindDays(maxValue) {
            var sel = sDay.val();
            sDay.children().remove();
            for (var i = 1; i <= maxValue; i++) {
                sDay.append('<option value="' + i.format(2) + '">' + i.format(2) + '</option>');
            }
            var lastV = sDay.find("option:last").val();
            if (sel > lastV) {
                sDay.val(lastV);
            } else {
                sDay.val(sel);
            }
        }

        //根据年.月获取天数
        function getDays(year, month) {
            var maxValue;
            if (parseInt(month) == 2) {
                var d = new Date(year + "-3-1");
                var times = d.getTime();
                d = new Date(times - 24 * 60 * 60 * 1000);
                maxValue = d.getDate();
            } else {
                maxValue = days[parseInt(month) - 1];
            }
            return maxValue;
        }

        //返回时间格式
        this.getTime = function () {
            return sYear.val() + "-" + sMonth.val() + "-" + sDay.val();
        }
        //设置时间
        this.setTime = function (d) {
            var date;
            if (typeof (d) == "string") {
                date = new Date(d);
            } else {
                date = d;
            }

            sYear.val(date.getFullYear());
            sMonth.val((date.getMonth() + 1).format(2));
            sDay.val(date.getDate().format(2));

            var maxValue = getDays(sYear.val(), sMonth.val());
            bindDays(maxValue);
        }

        //查看并绑定初始值
        var dateS = this.data("value");
        if (dateS) {
            //赋值
            var date = new Date(dateS);
            this.setTime(date);
        }

        return this;
    };
})();