//Establish the WebSocket connection and set up event handlers
let brainReadings = [];
let attentionReadings = [];
let meditationReadings = [];
let meditationHigh = 0;
let meditationLow = 100;
let attentionHigh = 0;
let attentionLow = 100;
let deltaReadings = [];
let thetaReadings = [];
let lowAlphaReadings = [];
let highAlphaReadings = [];
let lowBetaReadings = [];
let highBetaReadings = [];
let lowGammaReadings = [];
let highGammaReadings = [];
let attentionChart, meditationChart, activityChart;
let chartsInit = false;
let maxPoints = 25;
let ticks = {
  callback: function(tick, index, array) {
    return (index % 3) ? "" : tick;
  },
  maxTicksLimit: 10,
};
let legend = {display: false};

let xAxes = [{
  legend: {display: false},
  ticks: ticks,
  type: 'time',
  time: {
    unit: 'second',
    displayFormats: {
      second: 'HH:mm:ss A'
    }
  }
}];

let activityXAxes = [{
  legend: {display: true},
  ticks: ticks,
  type: 'time',
  time: {
    unit: 'second',
    displayFormats: {
      second: 'HH:mm:ss A'
    }
  }
}];

const initCharts = () => {
  attentionChart = new Chart(document.getElementById('attentionChart').getContext('2d'), {
    type: 'line',
    options: {
      annotation: {
        annotations: []
      },
      legend: legend,
      title: {text: "Attention", display: true},
      scales: {
        xAxes: xAxes,
      },
    },
    data: {
      datasets: [{
        pointBackgroundColor: "#f14343",
        backgroundColor: "#ee8e8e",
        lineColor: "#CC0000",
        data: attentionReadings,
      }]
    }
  });

  meditationChart = new Chart(document.getElementById('meditationChart').getContext('2d'), {
    type: 'line',
    options: {
      annotation: {
        annotations: []
      },
      legend: legend,
      title: {text: "Meditation", display: true},
      scales: {
        xAxes: xAxes,
      },
    },
    data: {
      datasets: [{
        pointBackgroundColor: "#2126c0",
        backgroundColor: "#2178c0",
        lineColor: "#216bc0",
        data: meditationReadings,
      }]
    }
  });

  activityChart = new Chart(document.getElementById('activityChart').getContext('2d'), {
    type: 'line',
    options: {
      annotation: {
        annotations: []
      },
      legend: {display: true},
      title: {text: "Brain Activity", display: true},
      radius: 10,
      scales: {
        xAxes: activityXAxes,
      },
    },
    data: {
      datasets: [
        {
          label: 'Delta',
          borderColor: "#9400D3",
          backgroundColor: "#9400D3",
          pointBackgroundColor: "#9400D3",
          fill: false,
          data: deltaReadings
        },
        {
          label: 'Theta',
          borderColor: "#4B0082",
          backgroundColor: "#4B0082",
          pointBackgroundColor: "#4B0082",
          fill: false,
          data: thetaReadings
        },
        {
          label: 'Low Alpha',
          borderColor: "#0000FF",
          backgroundColor: "#0000FF",
          pointBackgroundColor: "#0000FF",
          fill: false,
          data: lowAlphaReadings
        },
        {
          label: 'High Alpha',
          borderColor: "#00FF00",
          backgroundColor: "#00FF00",
          pointBackgroundColor: "#00FF00",
          fill: false,
          data: highAlphaReadings
        },
        {
          label: 'Low Beta',
          borderColor: "#FFFF00",
          backgroundColor: "#FFFF00",
          pointBackgroundColor: "#FFFF00",
          fill: false,
          data: lowBetaReadings
        },
        {
          label: 'High Beta',
          borderColor: "#FF7F00",
          backgroundColor: "#FF7F00",
          pointBackgroundColor: "#FF7F00",
          fill: false,
          data: highBetaReadings
        },
        {
          label: 'Low Gamma',
          borderColor: "#fa61bd",
          backgroundColor: "#fa61bd",
          pointBackgroundColor: "#fa61bd",
          fill: false,
          data: lowGammaReadings
        },
        {
          label: 'High Gamma',
          borderColor: "#FF0000",
          backgroundColor: "#FF0000",
          pointBackgroundColor: "#FF0000",
          fill: false,
          data: highGammaReadings
        },
      ]
    }
  });

  chartsInit = true;
}

const init = () => {

  initCharts();

  document.querySelector('#connectBtn').addEventListener('click', () => {
    if(typeof ws === 'undefined' || ws.readyState === WebSocket.CLOSED) {
      connect();
    }
    else {
      ws.close();
      ws = undefined;
      brainReadings = [];
      attentionReadings = [];
      meditationReadings = [];
      meditationHigh = 0;
      meditationLow = 100;
      attentionHigh = 0;
      attentionLow = 100;
      deltaReadings = [];
      thetaReadings = [];
      lowAlphaReadings = [];
      highAlphaReadings = [];
      lowBetaReadings = [];
      highBetaReadings = [];
      lowGammaReadings = [];
      highGammaReadings = [];
      document.querySelector('#meditationHigh').innerHTML = "N/A";
      document.querySelector('#meditationLow').innerHTML = "N/A";
      document.querySelector('#attentionHigh').innerHTML = "N/A";
      document.querySelector('#attentionLow').innerHTML = "N/A";

      document.querySelector('#currentSignalStrength').innerHTML = 0;
      document.querySelector('#currentAttention').innerHTML = "N/A";
      document.querySelector('#currentMeditation').innerHTML = "N/A";
      if (chartsInit) {
        attentionChart.destroy();
        meditationChart.destroy();
        activityChart.destroy();
        chartsInit = false;
        initCharts();
      }
    }
  });
};

class Brain {
  signalStrength;
  attention;
  meditation;
  delta;
  theta;
  lowAlpha;
  highAlpha;
  lowBeta;
  highBeta;
  lowGamma;
  highGamma;
  createdOn;

  constructor(brain) {
    this.signalStrength = +parseInt(brain.signalStrength);
    this.attention = +parseInt(brain.attention);
    this.meditation = +parseInt(brain.meditation);
    this.delta = +parseInt(brain.delta);
    this.theta = +parseInt(brain.theta);
    this.lowAlpha = +parseInt(brain.lowAlpha);
    this.highAlpha = +parseInt(brain.highAlpha);
    this.lowBeta = +parseInt(brain.lowBeta);
    this.highBeta = +parseInt(brain.highBeta);
    this.lowGamma = +parseInt(brain.lowGamma);
    this.highGamma = +parseInt(brain.highGamma);
    this.createdOn = new Date();
  }

}

const connect = () => {
  console.log('Connecting to WebSocket...')
  const protocol = location.protocol.indexOf("https") > -1 ? "wss" : "ws";
  ws = new WebSocket(`${protocol}://${location.hostname}:${location.port}/ws/demo`);
  ws.onopen = (msg) => {
    console.log('Connected!')
    document.querySelector('#connectBtnLbl').innerHTML = 'Disconnect';
    document.querySelector('#connectBtn').classList.add('btn-reset')
    document.querySelector('#connectBtn').classList.remove('btn-primary')
  };
  ws.onmessage = (msg) => {
    const parsedMsg = JSON.parse(msg.data);
    if( parsedMsg.hasOwnProperty("joined") || parsedMsg.hasOwnProperty("closed") ) {
      return
    }
    const brain = new Brain(parsedMsg);
    brainReadings.push(brain);
    attentionReadings.push({y: brain.attention, x: brain.createdOn});
    meditationReadings.push({y: brain.meditation, x: brain.createdOn});
    deltaReadings.push({y: brain.delta, x: brain.createdOn});
    thetaReadings.push({y: brain.theta, x: brain.createdOn});
    lowAlphaReadings.push({y: brain.lowAlpha, x: brain.createdOn});
    highAlphaReadings.push({y: brain.highAlpha, x: brain.createdOn});
    lowBetaReadings.push({y: brain.lowBeta, x: brain.createdOn});
    highBetaReadings.push({y: brain.highBeta, x: brain.createdOn});
    lowGammaReadings.push({y: brain.lowGamma, x: brain.createdOn});
    highGammaReadings.push({y: brain.highGamma, x: brain.createdOn});
    // keep the latest `maxPoints`
    if( brainReadings.length > maxPoints ) brainReadings.shift();
    if( attentionReadings.length > maxPoints ) attentionReadings.shift();
    if( meditationReadings.length > maxPoints ) meditationReadings.shift();
    if( deltaReadings.length > maxPoints ) deltaReadings.shift();
    if( thetaReadings.length > maxPoints ) thetaReadings.shift();
    if( lowAlphaReadings.length > maxPoints ) lowAlphaReadings.shift();
    if( highAlphaReadings.length > maxPoints ) highAlphaReadings.shift();
    if( lowBetaReadings.length > maxPoints ) lowBetaReadings.shift();
    if( highBetaReadings.length > maxPoints ) highBetaReadings.shift();
    if( lowGammaReadings.length > maxPoints ) lowGammaReadings.shift();
    if( highGammaReadings.length > maxPoints ) highGammaReadings.shift();

    if (chartsInit) {
      attentionChart.update();
      meditationChart.update();
      activityChart.update();
    }
    if(brain.meditation > meditationHigh) meditationHigh = brain.meditation;
    if(brain.meditation < meditationLow) meditationLow = brain.meditation;
    if(brain.attention > attentionHigh) attentionHigh = brain.attention;
    if(brain.attention < attentionLow) attentionLow = brain.attention;

    document.querySelector('#meditationHigh').innerHTML = meditationHigh;
    document.querySelector('#meditationLow').innerHTML = meditationLow;
    document.querySelector('#attentionHigh').innerHTML = attentionHigh;
    document.querySelector('#attentionLow').innerHTML = attentionLow;

    document.querySelector('#currentSignalStrength').innerHTML = brain.signalStrength;
    document.querySelector('#currentAttention').innerHTML = brain.attention;
    document.querySelector('#currentMeditation').innerHTML = brain.meditation;
  };
  ws.onclose = (e) => {
    /*
    console.log('Socket is closed. Reconnect will be attempted in 1 second.', e.reason);
    setTimeout(function() {
      connect();
    }, 1000);
     */
    document.querySelector('#connectBtnLbl').innerHTML = 'Connect';
    document.querySelector('#connectBtn').classList.add('btn-primary')
    document.querySelector('#connectBtn').classList.remove('btn-reset')
  };
  ws.onerror = function(err) {
    console.error('Socket encountered error: ', err.message, 'Closing socket');
    ws.close();
  };
};

const getAnnotation = (label, value) => {
  return {
    drawTime: "afterDatasetsDraw",
    type: "line",
    mode: "vertical",
    scaleID: "x-axis-0",
    value: value,
    borderWidth: 5,
    borderColor: "red",
    label: {
      content: label,
      enabled: true,
      position: "top"
    }
  }
}

document.addEventListener('DOMContentLoaded', init);