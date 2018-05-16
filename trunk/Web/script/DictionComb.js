/*
字典下拉框,可以直接绑定字典信息
*/
(function ($) {
    Zepto.fn.DictionComb = function (opt) {
        var _default = {
            hiddenName: '_type',
            data: [{ name: 'a', id: '1' }, { name: 'b', id: '2' }]
        };

        _default = Zepto.extend(_default, opt);

        var innerHtml = "";
        innerHtml += "<select name='" + _default.hiddenName + "'>";
        for (var i = 0; i < _default.data.length; i++) {
            innerHtml += '<option value="' + _default.data[i].id + '">' + _default.data[i].name + '</option>';
        }
        innerHtml += "</select>";

        this.html(innerHtml);

        return this;
    };
})();