
    var ec_left2 = echarts.init(document.querySelector("#l2"), "dark");
    var ec_left2_option = {
        title: {
            text: "全国新增趋势",
            textStyle: {

            },
            left: "left",
        },
        tooltip: {
            trigger: "axis",
            //指示器
            axisPointer: {
                type: "line",
                lineStyle: {
                    color: "#7171C6"
                }
            },
        },
        legend: {
            data: ["新增确诊", "新增疑似","新增死亡","新增治愈"],
            left: "right"
        },
        //图形位置
        grid: {
            left: '4%',
            right: '6%',
            bottom: '4%',
            top: 50,
            containLabel: true
        },
        xAxis: [{
            type: "category",
            //x轴坐标点开始与结束点位置都不在最边缘
            data: ['01.20', '01.21', '01.22']
        }],
        yAxis: [{
            type: 'value',
            //y轴字体设置
            axisLable: {
                show: true,
                color: "white"
            },
            //与x轴平行的线样式
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#17273B',
                    width: 1,
                    type: 'solid',
                }
            }
        }],
        series: [{
            name: "新增确诊",
            type: "line",
            smooth: true,
            data: [260, 406, 529]
        }, {
            name: "新增疑似",
            type: 'line',
            smooth: true,
            data: [54, 37, 3935]
        },
            {
                name: "新增治愈",
                type: "line",
                smooth: true,
                data: [260, 406, 529]
        }, {
                name: "新增死亡",
                type: 'line',
                smooth: true,
                data: [54, 37, 3935]

        }]
    };

    ec_left2.setOption(ec_left2_option);
