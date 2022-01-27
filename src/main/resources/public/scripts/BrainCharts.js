export class BrainCharts {
    brainReadings;
    legend;
    ticks;
    xAxes;
    activityXAxes;

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

    renderDonutChart(chartId, title, showLegend, datasource) {
        return new Chart(document.getElementById(chartId).getContext('2d'), {
            type: 'doughnut',
            options: {
                plugins: {
                    legend: {display: showLegend},
                    title: {text: title, display: true},
                },
            },
            data: datasource
        });
    }

    renderLineChart(chartId, title, showLegend, xAxes, datasource) {
        return new Chart(document.getElementById(chartId).getContext('2d'), {
            type: 'line',
            options: {
                plugins: {
                    annotation: {
                        annotations: {}
                    },
                    legend: {display: showLegend},
                    title: {text: title, display: true},
                },
                radius: 3,
                scales: {
                    x: xAxes,
                },
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