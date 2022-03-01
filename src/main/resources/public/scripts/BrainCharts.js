export class BrainCharts {
    brainReadings;
    legend;
    ticks;
    xAxes;
    activityXAxes;
    defaultYAxes;

    constructor() {
        this.brainReadings = [];
        this.meditationHigh = 0;
        this.meditationLow = 100;
        this.attentionHigh = 0;
        this.attentionLow = 100;
        this.legend = {display: false};
        this.ticks = {
            callback: function(tick, index, array) {
                return (index % 3) ? "" : tick;
            },
            maxTicksLimit: 10,
        };
        this.defaultYAxes = {
            ticks: {
                callback : function(value, index, values){
                    if(!this.chart._yAxisValues) this.chart._yAxisValues = values;
                    return value;
                }
            },
            position: 'left',
        };
        this.xAxes = {
            legend: {display: false},
            ticks: this.ticks,
            type: 'time',
            time: {
                unit: 'second',
                displayFormats: {
                    second: 'h:mm:ss A'
                }
            }
        };
        this.activityXAxes = {
            legend: {display: true},
            ticks: this.ticks,
            type: 'time',
            time: {
                unit: 'second',
                displayFormats: {
                    second: 'h:mm:ss A'
                }
            }
        };
        this.defaultXAxes = {
            legend: {display: true},
            type: 'category',
        };
    }

    static getSecondaryYAxis() {
        return {
            ticks: {
                callback : function(value, index, values){
                    if(!this.chart._yAxisValues) this.chart._yAxisValues = values;
                    return value;
                }
            },
            position: 'right',
            grid: {
                drawOnChartArea: false, // only want the grid lines for one axis to show up
            }
        }
    }

    static defaultActivityDatasource() {
        return [
            {
                label: 'Delta',
                borderColor: "#9400D3",
                backgroundColor: "#9400D3",
                pointBackgroundColor: "#9400D3",
                fill: false,
                data: []
            },
            {
                label: 'Theta',
                borderColor: "#4B0082",
                backgroundColor: "#4B0082",
                pointBackgroundColor: "#4B0082",
                fill: false,
                data: []
            },
            {
                label: 'Low Alpha',
                borderColor: "#0000FF",
                backgroundColor: "#0000FF",
                pointBackgroundColor: "#0000FF",
                fill: false,
                data: []
            },
            {
                label: 'High Alpha',
                borderColor: "#00FF00",
                backgroundColor: "#00FF00",
                pointBackgroundColor: "#00FF00",
                fill: false,
                data: []
            },
            {
                label: 'Low Beta',
                borderColor: "#FFFF00",
                backgroundColor: "#FFFF00",
                pointBackgroundColor: "#FFFF00",
                fill: false,
                data: []
            },
            {
                label: 'High Beta',
                borderColor: "#FF7F00",
                backgroundColor: "#FF7F00",
                pointBackgroundColor: "#FF7F00",
                fill: false,
                data: []
            },
            {
                label: 'Low Gamma',
                borderColor: "#fa61bd",
                backgroundColor: "#fa61bd",
                pointBackgroundColor: "#fa61bd",
                fill: false,
                data: []
            },
            {
                label: 'High Gamma',
                borderColor: "#FF0000",
                backgroundColor: "#FF0000",
                pointBackgroundColor: "#FF0000",
                fill: false,
                data: []
            },
        ]
    }

    renderDonutPieChart(chartId, title, showLegend, datasource, type) {
        return new Chart(document.getElementById(chartId).getContext('2d'), {
            type: type,
            options: {
                plugins: {
                    legend: {display: showLegend},
                    title: {text: title, display: true},
                },
            },
            data: datasource
        });
    }

    renderLineChart(chartId, title, showLegend, scales, datasource) {
        return new Chart(document.getElementById(chartId).getContext('2d'), {
            type: 'line',
            options: {
                animation: {
                    onComplete: function(data) {
                        const chart = data.chart;
                        if(chart._hasCustomClickListener) return;
                        if(Boolean(chart.canvas.getAttribute('data-has-listener')) === true) return;
                        const mm = chart.canvas.closest('div').querySelector('.min-max-container');
                        if(mm) mm.remove();
                        if(!chart._yAxisValues) return;
                        const minY = chart._yAxisValues[0].value;
                        const maxY = chart._yAxisValues[data.chart._yAxisValues.length-1].value;
                        chart._yAxisMin = minY;
                        chart._yAxisMax = maxY;
                        const step = chart._yAxisValues.length > 2 ? chart._yAxisValues[1].value - chart._yAxisValues[0].value : 100;
                        const chartId = chart.canvas.getAttribute('id');
                        const minId = `${chartId}MinY`;
                        const maxId = `${chartId}MaxY`;
                        const dblClickHandler = function(evt) {
                            const canvas = evt.currentTarget;
                            const div = canvas.closest('div');
                            div.querySelector('.min-max-container').classList.toggle('d-none');
                            evt.stopPropagation();
                            evt.preventDefault();
                        };
                        document.getElementById(chartId).removeEventListener('dblclick', dblClickHandler);
                        document.getElementById(chartId).addEventListener('dblclick', dblClickHandler);
                        chart.canvas.setAttribute('data-has-listener', true);
                        chart._hasCustomClickListener = true;
                        let container = `<div class="mt-2 mt-2 border col-12 d-none p-1 rounded shadow min-max-container"><div class="justify-content-center row"><div class="col-4 pb-1 pt-1"><div class="input-group"><span class="input-group-text">Min Y</span><input class="col-1 form-control chart-min-y"id="${minId}"max="${maxY}"min="${minY}"value="${minY}"placeholder="Min"step="${step}"type="number" /><button id="${minId}_Reset"class="btn btn-outline-secondary"type="button">Reset</button></div></div><div class="col-4 pb-1 pt-1"><div class="input-group"><span class="input-group-text">Max Y</span><input class="col-1 form-control chart-max-y"id="${maxId}"max="${maxY}"value="${maxY}"min="${minY}"placeholder="Max"step="${step}"type="number" /><button id="${maxId}_Reset"class="btn btn-outline-secondary"type="button">Reset</button></div></div></div></div>`;
                        let temp = document.createElement('div')
                        temp.innerHTML = container;
                        chart.canvas.closest('div').append(temp.firstChild);
                        document.querySelector(`#${minId}`).addEventListener('change', function(evt){
                            const el = evt.currentTarget;
                            const chart = Chart.getChart(el.closest('.min-max-container').previousElementSibling.getAttribute('id'));
                            chart.options.scales.yAxes.min = el.value;
                            chart.update();
                        });
                        document.querySelector(`#${maxId}`).addEventListener('change', function(evt){
                            const el = evt.currentTarget;
                            const chart = Chart.getChart(el.closest('.min-max-container').previousElementSibling.getAttribute('id'));
                            chart.options.scales.yAxes.max = el.value;
                            chart.update();
                        });
                        document.querySelector(`#${minId}_Reset`).addEventListener('click', function(evt){
                            const chart = Chart.getChart(evt.currentTarget.closest('.min-max-container').previousElementSibling.getAttribute('id'));
                            evt.currentTarget.previousElementSibling.value = chart._yAxisMin;
                            chart.options.scales.yAxes.min = chart._yAxisMin;
                            chart.update();
                        });
                        document.querySelector(`#${maxId}_Reset`).addEventListener('click', function(evt){
                            const chart = Chart.getChart(evt.currentTarget.closest('.min-max-container').previousElementSibling.getAttribute('id'));
                            evt.currentTarget.previousElementSibling.value = chart._yAxisMax;
                            chart.options.scales.yAxes.max = chart._yAxisMax;
                            chart.update();
                        })
                    }
                },
                plugins: {
                    annotation: {
                        annotations: {}
                    },
                    legend: {
                        display: showLegend,
                    },
                    title: {text: title, display: true},
                },
                radius: 3,
                scales: scales,
            },
            data: {
                datasets: datasource
            }
        });
    }

    removeData(chart) {
        chart.data.labels.pop();
        chart.data.datasets.forEach((dataset) => {
            dataset.data.pop();
        });
        chart.update();
    }

    getAnnotation(label, value, mode, color="red") {
        return {
            drawTime: "afterDatasetsDraw",
            type: "line",
            yMin: value,
            yMax: value,
            borderWidth: 2,
            borderColor: color,
            label: {
                content: label,
                enabled: true,
                position: "top"
            }
        }
    }
}