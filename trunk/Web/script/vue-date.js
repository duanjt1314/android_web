/**
* vue实现的一个日期组件,就是三个下拉框
**/
Vue.component('my-vue-date', {
    props: ['value'],//提供给外部绑定的值,格式为2019-01-20
    data: function () {
        return {
            year: [],
            month: [],
            day: [],
            currYear: 2015,
            currMonth: 1,
            currDay: 1
        };
    },
    methods: {
        refreshDay: function () {
            var maxMonth = [1, 3, 5, 7, 8, 10, 12];
            var monthLength = 31;
            if (maxMonth.indexOf(this.currMonth) != -1) {
                monthLength = 31;
            } else if (this.currMonth == 2) {
                if (this.isLeapYear(this.currYear)) {
                    //闰年,29天
                    monthLength = 29;
                } else {
                    monthLength = 28;
                }
            } else {
                monthLength = 30;
            }

            this.day.splice(0, this.day.length);//清空数组
            for (var i = 1; i <= monthLength; i++) {
                this.day.push(i);
            }

            //当前日赋值
            if (this.currDay > monthLength) {
                this.currDay = monthLength;
            }

            var val = this.currYear + "-" + this.currMonth.format(2) + "-" + this.currDay.format(2);
            this.$emit('input', val);//必须是input
        },
        //判断某一年是否为闰年
        isLeapYear: function (year) {
            return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
        }
    },
    template: '<div class="ui-select-group"> <div class="ui-select"> <select v-on:change="refreshDay()" v-model="currYear"> <template v-for="d in year"> <option :value="d">{{d}}</option> </template> </select> </div> <div class="ui-select"> <select v-on:change="refreshDay()" v-model="currMonth"> <template v-for="d in month"> <option :value="d">{{d.format(2)}}</option> </template> </select> </div> <div class="ui-select"> <select v-on:change="refreshDay()" v-model="currDay"> <template v-for="d in day"> <option :value="d">{{d.format(2)}}</option> </template> </select> </div> </div> </div>',
    created: function () {
        for (var i = 2015; i < 2023; i++) {
            this.year.push(i);
        }
        for (var i = 1; i <= 12; i++) {
            this.month.push(i);
        }
        for (var i = 1; i <= 31; i++) {
            this.day.push(i);
        }

        if (this.value) {            
            this.currYear = parseInt(this.value.substr(0, 4));
            this.currMonth = parseInt(this.value.substr(5, 2));
            this.currDay = parseInt(this.value.substr(8, 2));
        }
        this.refreshDay();
    }
})