'use strict';
import Brain from './Brain.js';
import {BrainCharts} from "./BrainCharts.js";
import {mean, median, mode} from "./utils.js";

window.chartsInit = false;
window.maxPoints = 24;
window.sessionStart = null;
window.sessionEnd = null;

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
    let activityDatasource = BrainCharts.defaultActivityDatasource();

    window.attentionChart = window.brainCharts.renderLineChart('attentionChart', 'Attention', false, window.brainCharts.xAxes, window.brainCharts.defaultYAxes, attentionDatasource);
    window.meditationChart = window.brainCharts.renderLineChart('meditationChart', 'Meditation', false, window.brainCharts.xAxes, window.brainCharts.defaultYAxes, meditationDatasource);
    window.activityChart = window.brainCharts.renderLineChart('activityChart', 'Activity (Hz)', true, window.brainCharts.activityXAxes, window.brainCharts.defaultYAxes,activityDatasource);
}

const init = () => {
    let tooltipTriggers = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    let tooltips = tooltipTriggers.map(function (el) {
        return new bootstrap.Tooltip(el)
    });
    initCharts();
    window.chartsInit = true;

    document.querySelector('#createSessionModalBtn').addEventListener('click', () => {
        let modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('sessionModal'));
        let sessionBtn =  document.querySelector('#createSessionModalBtn');
        let nameEl = document.querySelector('#sessionName');
        let notesEl = document.querySelector('#sessionNotes');

        if(window.sessionStart == null) {
            document.querySelector('#sessionForm').classList.remove('was-validated')
            nameEl.value = '';
            notesEl.value = '';
            modal.show();
            return
        }
        else {
            document.querySelector('#connectBtn').classList.remove('d-none');
            window.ws.close();
            window.ws = undefined;
            window.sessionEnd = new Date();
            let name = nameEl.value;
            let notes = notesEl.value;

            fetch(`/api/brainSession`,
                {
                    method: 'POST',
                    headers: new Headers({'content-type': 'application/json'}),
                    body: JSON.stringify({
                        name: name,
                        notes: notes,
                        startedAt: window.sessionStart,
                        endedAt: window.sessionEnd,
                    }),
                }
            )
                .then(response => {
                    nameEl.value = '';
                    notesEl.value = '';
                    window.sessionName = null;
                    window.sessionNotes = null;
                    sessionBtn.classList.add('btn-success');
                    sessionBtn.classList.remove('btn-danger');
                    sessionBtn.innerHTML = 'Start Session';
                    window.sessionStart = null;
                    modal.hide();
                });
        }
    });

    document.querySelector('#createSessionBtn').addEventListener('click', () => {
        let nameEl = document.querySelector('#sessionName');
        let notesEl = document.querySelector('#sessionNotes');
        let sessionBtn =  document.querySelector('#createSessionModalBtn');
        let modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('sessionModal'));
        let form = document.querySelector('#sessionForm');
        form.classList.add('was-validated')
        if (!form.checkValidity()) {
            return;
        }
        if(window.sessionStart == null) {
            document.querySelector('#connectBtn').classList.add('d-none');
            if(typeof window.ws !== "undefined") {
                window.ws.close();
                window.ws = undefined;
            }
            clearCharts();
            connect();
            window.sessionStart = new Date();
            sessionBtn.classList.remove('btn-success');
            sessionBtn.classList.add('btn-danger');
            sessionBtn.innerHTML = 'Stop Session';
            modal.hide();
        }
    });

    document.querySelector('#connectBtn').addEventListener('click', () => {
        if(typeof window.ws === 'undefined' || window.ws.readyState === WebSocket.CLOSED) {
            clearCharts();
            connect();
        }
        else {
            window.ws.close();
            window.ws = undefined;
            document.querySelector('#currentSignalStrength').innerHTML = 0;
        }
    });
};

const clearCharts = () => {
    if (window.chartsInit) {
        brainCharts.removeData(window.attentionChart);
        brainCharts.removeData(window.meditationChart);
        brainCharts.removeData(window.activityChart);
    }

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
    document.querySelector('#meditationMean').innerHTML = "N/A";
    document.querySelector('#meditationMedian').innerHTML = "N/A";
    document.querySelector('#meditationMode').innerHTML = "N/A";
    document.querySelector('#attentionHigh').innerHTML = "N/A";
    document.querySelector('#attentionLow').innerHTML = "N/A";
    document.querySelector('#attentionMean').innerHTML = "N/A";
    document.querySelector('#attentionMedian').innerHTML = "N/A";
    document.querySelector('#attentionMode').innerHTML = "N/A";

    document.querySelector('#currentSignalStrength').innerHTML = 0;
    document.querySelector('#currentAttention').innerHTML = "N/A";
    document.querySelector('#currentMeditation').innerHTML = "N/A";
}

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

        // keep the latest `maxPoints`
        //if( window.brainCharts.brainReadings.length > window.maxPoints ) window.brainCharts.brainReadings.shift();
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

        window.brainCharts.brainReadings.push(brain);
        window.attentionChart.data.datasets[0].data.push({y: brain.attention, x: brain.createdOn});
        window.meditationChart.data.datasets[0].data.push({y: brain.meditation, x: brain.createdOn});
        window.activityChart.data.datasets[0].data.push({y: brain.delta / 1000, x: brain.createdOn});
        window.activityChart.data.datasets[1].data.push({y: brain.theta / 1000, x: brain.createdOn});
        window.activityChart.data.datasets[2].data.push({y: brain.lowAlpha / 1000, x: brain.createdOn});
        window.activityChart.data.datasets[3].data.push({y: brain.highAlpha / 1000, x: brain.createdOn});
        window.activityChart.data.datasets[4].data.push({y: brain.lowBeta / 1000, x: brain.createdOn});
        window.activityChart.data.datasets[5].data.push({y: brain.highBeta / 1000, x: brain.createdOn});
        window.activityChart.data.datasets[6].data.push({y: brain.lowGamma / 1000, x: brain.createdOn});
        window.activityChart.data.datasets[7].data.push({y: brain.highGamma / 1000, x: brain.createdOn});

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
        let meditationVals = window.brainCharts.brainReadings.map(b => b.meditation);
        document.querySelector('#meditationMean').innerHTML = mean(meditationVals);
        document.querySelector('#meditationMedian').innerHTML = median(meditationVals);
        document.querySelector('#meditationMode').innerHTML = mode(meditationVals);
        document.querySelector('#attentionHigh').innerHTML = window.brainCharts.attentionHigh;
        document.querySelector('#attentionLow').innerHTML = window.brainCharts.attentionLow;
        let attentionVals = window.brainCharts.brainReadings.map(b => b.attention);
        document.querySelector('#attentionMean').innerHTML = mean(attentionVals);
        document.querySelector('#attentionMedian').innerHTML = median(attentionVals);
        document.querySelector('#attentionMode').innerHTML = mode(attentionVals);

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

document.addEventListener('DOMContentLoaded', init);