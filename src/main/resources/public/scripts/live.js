'use strict';
import Brain from './Brain.js';
import {BrainCharts} from "./BrainCharts.js";

window.chartsInit = false;
window.maxPoints = 25;

const initCharts = () => {
  window.brainCharts = new BrainCharts()

  let attentionDatasource = [{
    pointBackgroundColor: "#f14343",
    backgroundColor: "#ee8e8e",
    borderColor: "#CC0000",
    data: [],
  }];
  let meditationDatasource = [{
    pointBackgroundColor: "#2126c0",
    backgroundColor: "#2178c0",
    borderColor: "#216bc0",
    data: [],
  }];
  let activityDatasource = [
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
  ];

  window.attentionChart = window.brainCharts.renderLineChart('attentionChart', 'Attention', false, window.brainCharts.xAxes, attentionDatasource);
  window.meditationChart = window.brainCharts.renderLineChart('meditationChart', 'Meditation', false, window.brainCharts.xAxes, meditationDatasource);
  window.activityChart = window.brainCharts.renderLineChart('activityChart', 'Activity', false, window.brainCharts.activityXAxes, activityDatasource);
}
const init = () => {
  initCharts();
  window.chartsInit = true;

  document.querySelector('#connectBtn').addEventListener('click', () => {
    if(typeof window.ws === 'undefined' || window.ws.readyState === WebSocket.CLOSED) {
      connect();
    }
    else {
      window.ws.close();
      window.ws = undefined;
      window.brainCharts.brainReadings = [];
      window.brainCharts.meditationHigh = 0;
      window.brainCharts.meditationLow = 100;
      window.brainCharts.attentionHigh = 0;
      window.brainCharts.attentionLow = 100;
      window.attentionChart.data.datasets[0].data = [];
      window.meditationChart.data.datasets[0].data = [];
      window.activityChart.data.datasets[0].data = [];
      window.activityChart.data.datasets[1].data = [];
      window.activityChart.data.datasets[2].data = [];
      window.activityChart.data.datasets[3].data = [];
      window.activityChart.data.datasets[4].data = [];
      window.activityChart.data.datasets[5].data = [];
      window.activityChart.data.datasets[6].data = [];
      window.activityChart.data.datasets[7].data = [];
      document.querySelector('#meditationHigh').innerHTML = "N/A";
      document.querySelector('#meditationLow').innerHTML = "N/A";
      document.querySelector('#attentionHigh').innerHTML = "N/A";
      document.querySelector('#attentionLow').innerHTML = "N/A";

      document.querySelector('#currentSignalStrength').innerHTML = 0;
      document.querySelector('#currentAttention').innerHTML = "N/A";
      document.querySelector('#currentMeditation').innerHTML = "N/A";
      if (window.chartsInit) {
        brainCharts.removeData(window.attentionChart);
        brainCharts.removeData(window.meditationChart);
        brainCharts.removeData(window.activityChart);
      }
    }
  });
};

const connect = () => {
  console.log('Connecting to WebSocket...')
  const protocol = location.protocol.indexOf("https") > -1 ? "wss" : "ws";
  window.ws = new WebSocket(`${protocol}://${location.hostname}:${location.port}/ws/demo`);
  window.ws.onopen = (msg) => {
    console.log('Connected!')
    document.querySelector('#connectBtnLbl').innerHTML = 'Disconnect';
    document.querySelector('#connectBtn').classList.add('btn-danger')
    document.querySelector('#connectBtn').classList.remove('btn-primary')
  };
  window.ws.onmessage = (msg) => {
    const parsedMsg = JSON.parse(msg.data);
    if( parsedMsg.hasOwnProperty("joined") || parsedMsg.hasOwnProperty("closed") ) {
      return
    }
    const brain = new Brain(parsedMsg);
    window.brainCharts.brainReadings.push(brain);
    window.attentionChart.data.datasets[0].data.push({y: brain.attention, x: brain.createdOn});
    window.meditationChart.data.datasets[0].data.push({y: brain.meditation, x: brain.createdOn});
    window.activityChart.data.datasets[0].data.push({y: brain.delta, x: brain.createdOn});
    window.activityChart.data.datasets[1].data.push({y: brain.theta, x: brain.createdOn});
    window.activityChart.data.datasets[2].data.push({y: brain.lowAlpha, x: brain.createdOn});
    window.activityChart.data.datasets[3].data.push({y: brain.highAlpha, x: brain.createdOn});
    window.activityChart.data.datasets[4].data.push({y: brain.lowBeta, x: brain.createdOn});
    window.activityChart.data.datasets[5].data.push({y: brain.highBeta, x: brain.createdOn});
    window.activityChart.data.datasets[6].data.push({y: brain.lowGamma, x: brain.createdOn});
    window.activityChart.data.datasets[7].data.push({y: brain.highGamma, x: brain.createdOn});
    // keep the latest `maxPoints`
    if( window.brainCharts.brainReadings.length > window.maxPoints ) window.brainCharts.brainReadings.shift();
    if( window.attentionChart.data.datasets[0].data.length > window.maxPoints ) window.attentionChart.data.datasets[0].data.shift();
    if( window.meditationChart.data.datasets[0].data.length > window.maxPoints ) window.meditationChart.data.datasets[0].data.shift();
    if( window.activityChart.data.datasets[0].data.length > window.maxPoints ) window.activityChart.data.datasets[0].data.shift();
    if( window.activityChart.data.datasets[1].data.length > window.maxPoints ) window.activityChart.data.datasets[1].data.shift();
    if( window.activityChart.data.datasets[2].data.length > window.maxPoints ) window.activityChart.data.datasets[2].data.shift();
    if( window.activityChart.data.datasets[3].data.length > window.maxPoints ) window.activityChart.data.datasets[3].data.shift();
    if( window.activityChart.data.datasets[4].data.length > window.maxPoints ) window.activityChart.data.datasets[4].data.shift();
    if( window.activityChart.data.datasets[5].data.length > window.maxPoints ) window.activityChart.data.datasets[5].data.shift();
    if( window.activityChart.data.datasets[6].data.length > window.maxPoints ) window.activityChart.data.datasets[6].data.shift();
    if( window.activityChart.data.datasets[7].data.length > window.maxPoints ) window.activityChart.data.datasets[7].data.shift();

    if (window.chartsInit) {
      window.attentionChart.update();
      window.meditationChart.update();
      window.activityChart.update();
    }
    if(brain.meditation > window.brainCharts.meditationHigh) window.brainCharts.meditationHigh = brain.meditation;
    if(brain.meditation < window.brainCharts.meditationLow) window.brainCharts.meditationLow = brain.meditation;
    if(brain.attention > window.brainCharts.attentionHigh) window.brainCharts.attentionHigh = brain.attention;
    if(brain.attention < window.brainCharts.attentionLow) window.brainCharts.attentionLow = brain.attention;

    document.querySelector('#meditationHigh').innerHTML = window.brainCharts.meditationHigh;
    document.querySelector('#meditationLow').innerHTML = window.brainCharts.meditationLow;
    document.querySelector('#attentionHigh').innerHTML = window.brainCharts.attentionHigh;
    document.querySelector('#attentionLow').innerHTML = window.brainCharts.attentionLow;

    document.querySelector('#currentSignalStrength').innerHTML = brain.signalStrength;
    document.querySelector('#currentAttention').innerHTML = brain.attention;
    document.querySelector('#currentMeditation').innerHTML = brain.meditation;
  };
  window.ws.onclose = (e) => {
    /*
    console.log('Socket is closed. Reconnect will be attempted in 1 second.', e.reason);
    setTimeout(function() {
      connect();
    }, 1000);
     */
    document.querySelector('#connectBtnLbl').innerHTML = 'Connect';
    document.querySelector('#connectBtn').classList.add('btn-primary')
    document.querySelector('#connectBtn').classList.remove('btn-danger')
  };
  window.ws.onerror = function(err) {
    console.error('Socket encountered error: ', err.message, 'Closing socket');
    window.ws.close();
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