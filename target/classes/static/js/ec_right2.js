
var ec_right2 = echarts.init(document.querySelector("#r2"), "dark");

    var ddd = [
        { 'name': '肺炎', 'value': '12734670' }, { 'name': '实时', 'value': '12734670' },
        { 'name': '新型', 'value': '12734670' }
    ]

    var ec_right2_option = {
        title: {
            text: "今日疫情热搜",
            textStyle: {
                color: 'white',
            },
            left: 'left'
        },
        tooltip: {
            show: false
        },
        series: [{
            type: "wordCloud",
            gridSize: 1,
            sizeRange: [12, 55],
            rotationRange: [-45, 0, 45, 90],
            textStyle: {
                normal: {
                    color: function() {
                        return 'rgb(' +
                            Math.round(Math.random() * 255) +
                            ',' + Math.round(Math.random() * 255) +
                            ',' + Math.round(Math.random() * 255) + ')'
                    }
                }

            },
            right: null,
            bottom: null,
            data: ddd
        }]
    }

    ec_right2.setOption(ec_right2_option);
