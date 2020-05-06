var ec_right1 = echarts.init(document.querySelector("#r1"), "dark");
    var ec_right1_option = {
        title: {
            text: "非湖北地区城市确诊TOP5",
            textStyle: {
                color: "white",
            },
            left: "left"
        },
        color: ["#3398DB"],
        tooltip: {
            trigger: "axis",
            axisPointer: {
                type: "shadow"
            }
        },
        xAxis: [{
            data: [],
            type: 'category',
        }],
        yAxis: {
            type: "value"
        },
        series: [{
            data: [525, 490, 391, 366, 327],
            type: 'bar',
            barMaxWidth: "50%"
        }]
    };

    ec_right1.setOption(ec_right1_option);
