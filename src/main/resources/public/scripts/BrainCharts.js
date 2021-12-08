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
          second: 'HH:mm:ss A'
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
          second: 'HH:mm:ss A'
        }
      }
    };
  }

  renderLineChart(chartId, title, showLegend, xAxes, datasource) {
    return new Chart(document.getElementById(chartId).getContext('2d'), {
      type: 'line',
      options: {
        annotation: {
          annotations: []
        },
        plugins: {
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
}